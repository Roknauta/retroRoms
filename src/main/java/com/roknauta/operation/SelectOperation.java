package com.roknauta.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roknauta.RetroRomsException;
import com.roknauta.domain.Game;
import com.roknauta.domain.Rom;
import com.roknauta.domain.Sistema;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SelectOperation extends OperationBase implements Operation {

    public static final String ACCEPTED_REGIONS =
        "USA,Brazil,Europe,World,Portugal,Canada,Australia,United Kingdom,New Zealand,Mexico,Argentina,Latin America,Spain,France,Italy,Germany,Greece,Sweden,Austria,Romania,Netherlands,Finland,Denmark,Hungary,Scandinavia,Japan,Hong Kong,Asia,China,Korea,Taiwan,Russia,Unknown";


    public SelectOperation(Sistema sistema, OperationOptions options) {
        super(sistema, options);
    }

    @Override
    public void process() {
        List<Game> gamesEscolhidos = getEscolhidos();
        List<File> roms = loadRoms();
        Set<String> processedGames = new HashSet<>();
        for (File rom : roms) {
            String md5 = getMd5Hex(rom);
            gamesEscolhidos.stream().filter(
                game -> !processedGames.contains(game.getGameId()) && game.getRoms().stream()
                    .anyMatch(gameRom -> gameRom.getMd5().equals(md5))).findFirst().ifPresent(
                game -> game.getRoms().stream().filter(gameRom -> gameRom.getMd5().equals(md5)).findFirst()
                    .ifPresent(gameRom -> {
                        copiarArquivo(rom, game, gameRom);
                        processedGames.add(game.getGameId());
                    }));
        }
    }

    private void copiarArquivo(File origem, Game gameData, Rom rom) {
        try {
            File destino = new File(systemDirectory, gameData.getName() + "." + rom.getExtension());
            if (!FileUtils.directoryContains(systemDirectory, destino))
                FileUtils.copyFile(origem, destino);
        } catch (IOException e) {
            throw new RetroRomsException(
                "Erro ao copiar o arquivo: " + origem.getPath() + ". Detalhes: " + e.getMessage());
        }
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
        return Arrays.asList(ACCEPTED_REGIONS.split(","));
    }

    private List<File> loadRoms() {
        return Arrays.asList(Objects.requireNonNull(new File(diretorioOrigem, sistema.getName()).listFiles()));
    }

    private List<Game> loadGameData() throws IOException {
        File arquivo = new File(getDatasourcesFolder(), sistema.getName() + ".json");
        ObjectMapper objectMapper = new ObjectMapper();
        return Arrays.stream(objectMapper.readValue(arquivo, Game[].class)).filter(this::isValidGame).toList();
    }
}
