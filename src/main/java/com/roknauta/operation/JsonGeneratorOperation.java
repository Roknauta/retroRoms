package com.roknauta.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.roknauta.RetroRomsException;
import com.roknauta.datasource.noIntro.db.Datafile;
import com.roknauta.datasource.noIntro.db.NoIntroGame;
import com.roknauta.datasource.retroachivments.RetroAchivment;
import com.roknauta.domain.OperationOptions;
import com.roknauta.domain.Sistema;
import com.roknauta.domain.game.Game;
import com.roknauta.domain.game.Rom;
import com.roknauta.domain.game.factory.GameFactory;
import com.roknauta.domain.game.factory.RomFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JsonGeneratorOperation extends OperationBase implements Operation {

    private static final String XML_FOLDER = "xml";
    private static final String ACHIVMENTS_FOLDER = "retroachviments";

    public JsonGeneratorOperation(Sistema sistema, OperationOptions options) {
        super(sistema, options);
    }

    @Override
    public void process() {
        File folderSistema = new File(diretorioOrigem, XML_FOLDER.concat(File.separator).concat(sistema.getName()));
        List<String> hashes = getHashesWithRetroAchivments();
        if (folderSistema.exists()) {
            System.out.println("Processando o sistema: " + sistema.getName());
            List<Game> games = new ArrayList<>();
            for (File xml : folderSistema.listFiles()) {
                getNoIntroGamesFromXml(xml).forEach(gameXml -> {
                    if (gameXml.isValidGame()) {
                        Rom rom = RomFactory.fromNoIntroDB(gameXml);
                        Game game = GameFactory.fromNoIntroDB(gameXml, hashes.contains(rom.getMd5()), rom);
                        games.add(game);
                    }
                });
            }
            generanteJsonFromNoIntroGames(games);
        }
    }

    private void generanteJsonFromNoIntroGames(List<Game> games) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(new File(systemDirectory, sistema.getName().concat(".json")), games);
        } catch (Exception e) {
            throw new RetroRomsException(e);
        }
    }

    private List<String> getHashesWithRetroAchivments() {
        try {
            File json = new File(diretorioOrigem,
                ACHIVMENTS_FOLDER.concat(File.separator).concat(sistema.getName()).concat(".json"));
            List<RetroAchivment> achivments = new ArrayList<>();
            if (json.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                achivments = Arrays.asList(objectMapper.readValue(json, RetroAchivment[].class));
            }
            return achivments.stream().map(RetroAchivment::getHashes).flatMap(Collection::stream)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RetroRomsException(e);
        }
    }

    private List<NoIntroGame> getNoIntroGamesFromXml(File xml) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            Datafile dataFile = xmlMapper.readValue(xml, Datafile.class);
            return dataFile.getGames();
        } catch (Exception e) {
            throw new RetroRomsException(e);
        }
    }
}
