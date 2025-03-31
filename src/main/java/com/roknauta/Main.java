package com.roknauta;

import com.roknauta.domain.Sistema;
import com.roknauta.operation.ExtractOperation;
import com.roknauta.operation.OperationOptions;
import com.roknauta.operation.SelectOperation;
import org.apache.commons.cli.Option;

public class Main {

    private static final String SOURCE_PACKS = "/run/media/douglas/Games/Emulation/Packs/Prontos/";
    private static final String EXTRACT_DESTINATION = "/home/douglas/workspace/retro-roms2/extracao";
    private static final String SELECTED_DESTINATION = "/home/douglas/workspace/retro-roms2/escolhidos";

    public static void main(String[] args) {
        extrairRoms();
        //gerarJson();
        //escolherRoms();
    }

    private static void escolherRoms() {
        OperationOptions options = new OperationOptions(EXTRACT_DESTINATION, SELECTED_DESTINATION);
        //new SelectOperation(Sistema.ATARI_7800, options).process();
        for (Sistema sistema : Sistema.values()) {
            System.out.println("Processando o sistema: " + sistema.getName());
            new SelectOperation(sistema, options).process();
        }
    }

    private static Option createOption(String shortName, String longName, String description, boolean required) {
        return Option.builder().option(shortName).longOpt(longName).desc(description).hasArg().required(required)
            .build();
    }

    private static void extrairRoms() {
        /*OperationOptions options = new OperationOptions(SOURCE_PACKS + Sistema.ATARI_7800.getName(), EXTRACT_DESTINATION);
        new ExtractOperation(Sistema.ATARI_7800, options).process();*/
        for (Sistema sistema : Sistema.values()) {
            System.out.println("Processando o sistema: " + sistema.getName());
            OperationOptions options = new OperationOptions(SOURCE_PACKS + sistema.getName(), EXTRACT_DESTINATION);
            new ExtractOperation(sistema, options).process();
        }
    }
}
