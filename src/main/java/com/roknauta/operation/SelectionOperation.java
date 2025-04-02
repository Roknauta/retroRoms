package com.roknauta.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roknauta.RetroRomsException;
import com.roknauta.domain.Game;
import com.roknauta.domain.Rom;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Responsável por selecionar as roms já extraídas priorizando por região e que possuam retroAchievements
 */
public class SelectionOperation extends OperationBase implements Operation {

    public static final String PREFERED_REGIONS =
        "USA,Brazil,Europe,World,Portugal,Canada,Australia,United Kingdom,New Zealand,Mexico,Argentina,Latin America,Spain,France,Italy,Germany,Greece,Sweden,Austria,Romania,Netherlands,Finland,Denmark,Hungary,Scandinavia,Japan,Hong Kong,Asia,China,Korea,Taiwan,Russia,Unknown";

    @Override
    public void process(OperationOptions options) {
        init( options);
        List<Game> gamesEscolhidos = getEscolhidos();
        Map<String, File> roms = loadRoms();
        gamesEscolhidos.forEach(game -> getPreferedRom(game, roms).ifPresent(
            gameRom -> copiarArquivo(roms.get(gameRom.getMd5()), systemDirectory,
                toFileName(game.getName(), gameRom.getExtension()))));
    }

    /**
     * @param game Game from Datasource
     * @param roms Mapa de roms (arquivos) com seus hashs md5
     * @return Retorna o primeiro Rom (Datadource) que exista na lista de roms (arquivos) priorizando a que possua
     * retroAchievements
     */
    private Optional<Rom> getPreferedRom(Game game, Map<String, File> roms) {
        return game.getRoms().stream().filter(gameRom -> roms.containsKey(gameRom.getMd5()))
            .max(Comparator.comparing(Rom::isHasRetroAchievements));
    }

    private List<Game> getEscolhidos() {
        Map<Game, List<Game>> parentClones = mapParentWithClonesGames();
        List<Game> escolhidos = new ArrayList<>();
        parentClones.forEach((parent, clones) -> {
            Game game = selectPreferedGame(parent, clones);
            escolhidos.add(game);
        });
        return escolhidos;
    }

    private boolean isValidGame(Game game) {
        return !Arrays.asList(game.isBios(), game.isUnlicensed(), game.isBeta(), game.isDemo(), game.isProto(),
            game.isSample(), game.isAfterMarket(), game.isHasStatus(), game.isPirate()).contains(true);
    }

    private Game selectPreferedGame(Game parent, List<Game> clones) {
        if (CollectionUtils.isEmpty(clones) || clones.stream().allMatch(game -> game.equals(parent))) {
            return parent;
        } else {
            List<Game> parentWithClones = new ArrayList<>(clones);
            parentWithClones.add(parent);
            return getFirstGameByPreferedRegion(parentWithClones);
        }
    }

    private Map<Game, List<Game>> mapParentWithClonesGames() {
        try {
            List<Game> games = loadGameData();
            List<Game> parentGames = games.stream().filter(game -> "P".equals(game.getGameParent())).toList();
            List<Game> cloneGames = games.stream().filter(game -> !"P".equals(game.getGameParent())).toList();
            Map<Game, List<Game>> parentClones = new HashMap<>();
            parentGames.forEach(game -> parentClones.put(game, new ArrayList<>()));
            cloneGames.forEach(
                gameClone -> parentGames.stream().filter(game -> game.getGameId().equals(gameClone.getGameParent()))
                    .findFirst().ifPresentOrElse(parent -> parentClones.get(parent).add(gameClone),
                        () -> parentClones.put(gameClone, Collections.singletonList(gameClone))));
            return parentClones;
        } catch (IOException e) {
            throw new RetroRomsException(e);
        }
    }

    /**
     * @param games Lista de games a serem processadas
     * @return Retorna o primeiro game da lista quando bater com a região preferida.
     */
    private Game getFirstGameByPreferedRegion(List<Game> games) {
        if (CollectionUtils.isNotEmpty(games)) {
            for (String region : getPreferedRegionsInOrder()) {
                Game selectedGame = games.stream().filter(game -> game.getRegions().contains(region))
                    .max(Comparator.comparing(Game::getRevision)).orElse(null);
                if (selectedGame != null) {
                    return selectedGame;
                }
            }
        }
        return null;
    }

    public List<String> getPreferedRegionsInOrder() {
        return Arrays.asList(PREFERED_REGIONS.split(","));
    }

    private Map<String, File> loadRoms() {
        File roms = criarDiretorioSeNaoExistir(diretorioOrigem, sistema.getName());
        Map<String, File> romsMap = new HashMap<>();
        for (File rom : Objects.requireNonNull(roms.listFiles())) {
            String md5 = getMd5Hex(rom);
            romsMap.put(md5, rom);
        }
        return romsMap;
    }

    private List<Game> loadGameData() throws IOException {
        File arquivo = new File(getDatasourcesFolder(), sistema.getName() + ".json");
        ObjectMapper objectMapper = new ObjectMapper();
        return Arrays.stream(objectMapper.readValue(arquivo, Game[].class)).filter(this::isValidGame).toList();
    }
}
