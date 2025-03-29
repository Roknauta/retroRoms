import com.fasterxml.jackson.databind.ObjectMapper;
import com.roknauta.RetroRomsException;
import com.roknauta.domain.Sistema;
import com.roknauta.domain.game.Game;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Test {

    private static final String EXTRACT_SOURCE = "/home/douglas/workspace/retro-roms/extracao/";
    private static final String DESTINATION_FOLDER = "/home/douglas/workspace/retro-roms/escolhidos/";

    public static final String ACCEPTED_REGIONS =
        "USA,Brazil,Europe,World,Portugal,Canada,Australia,United Kingdom,New Zealand,Mexico,Argentina,Latin America,Spain,France,Italy,Germany,Greece,Sweden,Austria,Romania,Netherlands,Finland,Denmark,Hungary,Scandinavia,Japan,Hong Kong,Asia,China,Korea,Taiwan,Russia";

    public static void main(String[] args) throws IOException {
        List<Game> gamesEscolhidos = getEscolhidos(Sistema.SNES);
        List<File> roms = loadRoms(Sistema.SNES);
        File destino = new File(DESTINATION_FOLDER);
        for (File rom : roms) {
            String md5 = DigestUtils.md5Hex(new FileInputStream(rom));
            gamesEscolhidos.stream().filter(game -> !game.isUnlicensed() && game.getRom().getMd5().equals(md5))
                .findFirst().ifPresent(gameData -> copiarArquivo(rom, destino, gameData));
        }
        System.out.println("");
    }

    private static void copiarArquivo(File origem, File pastaDestino, Game gameData) {
        try {
            File destino = new File(pastaDestino, gameData.getName() + "." + gameData.getRom().getExtension());
            if (!FileUtils.directoryContains(pastaDestino, destino))
                FileUtils.copyFile(origem, destino);
        } catch (IOException e) {
            throw new RetroRomsException(
                "Erro ao copiar o arquivo: " + origem.getPath() + ". Detalhes: " + e.getMessage());
        }
    }

    private static List<Game> getEscolhidos(Sistema sistema) throws IOException {
        Map<Game, List<Game>> parentClones = mapGameData(sistema);
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
                escolhidos.add(ObjectUtils.firstNonNull(escolhido, preferedGame(parentWithClones)));
            }
        });
        return escolhidos;
    }



    private static Map<Game, List<Game>> mapGameData(Sistema sistema) throws IOException {
        List<Game> games = loadGameData(sistema);
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
    }

    private static Game preferedGame(List<Game> games) {
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

    public static List<String> getPreferedRegionsInOrder() {
        return Arrays.asList(ACCEPTED_REGIONS.split(","));
    }

    private static List<File> loadRoms(Sistema sistema) {
        return Arrays.asList(Objects.requireNonNull(new File(EXTRACT_SOURCE, sistema.getName()).listFiles()));
    }

    private static List<Game> loadGameData(Sistema sistema) throws IOException {
        List<Game> games;
        String dataSourceLocation = "datasources/" + sistema.getName() + ".json";
        File arquivo = new File(Test.class.getResource(dataSourceLocation).getFile());
        ObjectMapper objectMapper = new ObjectMapper();
        games = Arrays.asList(objectMapper.readValue(arquivo, Game[].class));
        return games;
    }

}
