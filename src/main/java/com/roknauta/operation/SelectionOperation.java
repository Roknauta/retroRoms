package com.roknauta.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roknauta.domain.Game;
import com.roknauta.domain.Rom;
import com.roknauta.domain.Sistema;
import com.roknauta.utils.AppUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;

/**
 * Responsável por selecionar as roms já extraídas priorizando por região e que possuam retroAchievements
 */
public class SelectionOperation extends OperationBase implements Operation {

    @Override
    public void process(Sistema sistema, OperationOptions options) {
        init(sistema, options);
        List<Game> gamesEscolhidos = loadGames();
        Map<String, File> roms = loadRoms();
        gamesEscolhidos.forEach(game -> getPreferedRom(game, roms).ifPresent(
            gameRom -> copiarArquivo(roms.get(gameRom.getMd5()), targetSystemDirectory,
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

    private List<Game> loadGames() {
        List<Game> games = loadGameData();
        return options.isOneRomPerRegion() ? filterOneGamePerRegion(games) : games;
    }

    private List<Game> filterOneGamePerRegion(List<Game> games) {
        Map<Game, List<Game>> parentClones = mapParentWithClonesGames(games);
        List<Game> escolhidos = new ArrayList<>();
        parentClones.forEach((parent, clones) -> {
            Game game = selectPreferedGame(parent, clones);
            escolhidos.add(game);
        });
        return escolhidos;
    }

    private boolean isValidGame(Game game) {
        if (gameWithValidRegion(game)) {
            List<Boolean> flags =
                List.of(game.isPirate(), game.isSample(), game.isAfterMarket(), game.isBeta(), game.isBios(),
                    game.isDemo(), game.isProto(), game.isUnlicensed(), game.isHasStatus());
            return flags.stream().noneMatch(flag -> flag) || flagWithPermission(flags);
        }
        return false;
    }

    private boolean flagWithPermission(List<Boolean> flags) {
        List<Boolean> flagsOptions =
            List.of(options.isAllowPirateRom(), options.isAllowSampleRom(), options.isAllowAfterMarketRom(),
                options.isAllowBetaRom(), options.isAllowBiosRom(), options.isAllowDemoRom(), options.isAllowProtoRom(),
                options.isAllowUnlicensedRom(), options.isAllowRomWithStatus());
        for (int i = 0; i < flags.size(); i++) {
            if (flags.get(i) && flagsOptions.get(i)) {
                return true;
            }
        }
        return false;
    }

    private boolean gameWithValidRegion(Game game) {
        return game.getRegions().stream().allMatch(region -> options.getAcceptedRegions().contains(region));
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

    private Map<Game, List<Game>> mapParentWithClonesGames(List<Game> games) {
        List<Game> parentGames = games.stream().filter(game -> "P".equals(game.getGameParent())).toList();
        List<Game> cloneGames = games.stream().filter(game -> !"P".equals(game.getGameParent())).toList();
        Map<Game, List<Game>> parentClones = new HashMap<>();
        parentGames.forEach(game -> parentClones.put(game, new ArrayList<>()));
        cloneGames.forEach(
            gameClone -> parentGames.stream().filter(game -> game.getGameId().equals(gameClone.getGameParent()))
                .findFirst().ifPresentOrElse(parent -> parentClones.get(parent).add(gameClone),
                    () -> parentClones.put(gameClone, Collections.singletonList(gameClone))));
        return parentClones;
    }

    /**
     * @param games Lista de games a serem processadas
     * @return Retorna o primeiro game da lista quando bater com a região preferida.
     */
    private Game getFirstGameByPreferedRegion(List<Game> games) {
        if (CollectionUtils.isNotEmpty(games)) {
            for (String region : options.getAcceptedRegions()) {
                Game selectedGame = games.stream().filter(game -> game.getRegions().contains(region))
                    .max(Comparator.comparing(Game::getRevision)).orElse(null);
                if (selectedGame != null) {
                    return selectedGame;
                }
            }
        }
        return null;
    }

    private Map<String, File> loadRoms() {
        Map<String, File> romsMap = new HashMap<>();
        for (File rom : Objects.requireNonNull(sourceSystemDirectory.listFiles())) {
            String md5 = getMd5Hex(rom);
            romsMap.put(md5, rom);
        }
        return romsMap;
    }

    private List<Game> loadGameData() {
        File arquivo = getDatasource();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Arrays.stream(objectMapper.readValue(arquivo, Game[].class)).filter(this::isValidGame).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getDatasource() {
        try {
            String dataSourceName = toFileName(sistema.getName(), "json");
            final String path = "datasources/" + dataSourceName;
            final File jarFile = new File(AppUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            if (jarFile.isFile()) {
                final JarFile jar = new JarFile(jarFile);
                File dataSource = new File(System.getProperty("java.io.tmpdir"), dataSourceName);
                if (!dataSource.exists()) {
                    FileUtils.copyInputStreamToFile(jar.getInputStream(jar.getJarEntry(path)), dataSource);
                }
                jar.close();
                return dataSource;
            }
            return new File(getClass().getClassLoader().getResource(path).getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
