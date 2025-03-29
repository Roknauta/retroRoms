package com.roknauta.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roknauta.RetroRomsException;
import com.roknauta.domain.OperationOptions;
import com.roknauta.domain.Sistema;
import com.roknauta.domain.game.Game;
import com.roknauta.domain.game.Rom;
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
        for (File rom : roms) {
            String md5 = getMd5Hex(rom);
            gamesEscolhidos.stream().filter(game -> !game.isUnlicensed() && game.getRoms().stream()
                .anyMatch(romData -> romData.getMd5().equals(md5))).findFirst().ifPresent(gameData -> {
                gameData.getRoms().stream().filter(romData -> romData.getMd5().equals(md5)).findFirst()
                    .ifPresent(romData -> {
                        copiarArquivo(rom, gameData, romData);
                    });
            });
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
        Map<Game, List<Game>> parentClones = mapGameData();
        List<Game> escolhidos = new ArrayList<>();
        parentClones.forEach((parent, clones) -> {
            if (CollectionUtils.isEmpty(clones) || clones.stream().allMatch(game -> game.equals(parent))) {
                escolhidos.add(parent);
            } else {
                List<Game> parentWithClones = new ArrayList<>(clones);
                parentWithClones.add(parent);
                List<Game> gamesWithRetroAchievements =
                    parentWithClones.stream().filter(Game::isHasRetroAchievements).toList();
                Game escolhido = preferedGame(gamesWithRetroAchievements);
                if (escolhido == null) {
                    escolhido = preferedGame(parentWithClones);
                }
                escolhidos.add(escolhido);
            }
        });
        return escolhidos;
    }



    private Map<Game, List<Game>> mapGameData() {
        try {
            List<Game> games = loadGameData();
            List<Game> gamesParent = games.stream().filter(Game::isParent).toList();
            List<Game> gamesClone = games.stream().filter(Game::isClone).toList();
            Map<Game, List<Game>> parentClones = new HashMap<>();
            gamesParent.forEach(game -> parentClones.put(game, new ArrayList<>()));
            gamesClone.forEach(gameClone -> {
                Game parent =
                    gamesParent.stream().filter(game -> game.getGameId().equals(gameClone.getGameParent())).findFirst()
                        .orElse(null);
                if (parent != null) {
                    parentClones.get(parent).add(gameClone);
                } else {
                    parentClones.put(gameClone, Collections.singletonList(gameClone));
                }
            });
            return parentClones;
        } catch (IOException e) {
            throw new RetroRomsException(e);
        }
    }

    private Game preferedGame(List<Game> games) {
        if (CollectionUtils.isNotEmpty(games)) {
            games = games.stream().sorted(Comparator.comparing(Game::getRevision).reversed()).toList();
            for (String region : getPreferedRegionsInOrder()) {
                Optional<Game> game = games.stream().filter(game1 -> game1.getRegions().contains(region)).findFirst();
                if (game.isPresent()) {
                    return game.get();
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
        List<Game> games;
        File arquivo = new File(getDatasourcesFolder(), "gameData.json");
        ObjectMapper objectMapper = new ObjectMapper();
        games = Arrays.asList(objectMapper.readValue(arquivo, Game[].class));
        return games;
    }
}
