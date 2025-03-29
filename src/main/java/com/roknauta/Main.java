package com.roknauta;

import com.roknauta.domain.OperationOptions;
import com.roknauta.domain.Sistema;
import com.roknauta.operation.ExtractOperation;
import com.roknauta.operation.JsonGeneratorOperation;
import com.roknauta.operation.Operation;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

public class Main {

    private static final String SOURCE_PACKS = "/run/media/douglas/Games/Emulation/Packs/Prontos/";
    private static final String EXTRACT_DESTINATION = "/home/douglas/workspace/retro-roms/extracao";

    public static void main(String[] args) {
        gerarJson();
        //extrairRoms();
    }

    private static Option createOption(String shortName, String longName, String description, boolean required) {
        return Option.builder().option(shortName).longOpt(longName).desc(description).hasArg().required(required)
            .build();
    }

    private static void extrairRoms() {
        for (Sistema sistema : Sistema.values()) {
            System.out.println("Processando o sistema: " + sistema.getName());
            OperationOptions options = new OperationOptions(SOURCE_PACKS + sistema.getName(), EXTRACT_DESTINATION);
            new ExtractOperation(sistema, options).process();
        }
    }

    private static void gerarJson() {
        OperationOptions options =
            new OperationOptions("/home/douglas/Documents/dat", "/home/douglas/workspace/retro-roms");
        //new JsonGeneratorOperation(Sistema.SNES, options).process();
        for (Sistema sistema : Sistema.values()) {
            new JsonGeneratorOperation(sistema, options).process();
        }
    }

    private static void processarSistema(Operation operation) {
        operation.process();
    }
}
