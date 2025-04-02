package com.roknauta;

import org.apache.commons.cli.*;

public class App {

    private final static String OPTION_OPERATION = "operation";
    private final static String OPTION_SISTEMA = "system";
    private final static String OPTION_SOURCE_DIRECTORY = "sourceDirectory";
    private final static String OPTION_TARGET_DIRECTORY = "targetDirectory";

    private static String operationMethod;
    private static String system;
    private static String sourceDirectory;
    private static String targetDirectory;


    public static void main(String[] args) {
        String[] commandWithIncorrectOption = new String[] {"-o", "extraction", "-y", "snes", "-s", "ho"};
        processArgs(commandWithIncorrectOption);
        /*
        Operation operation = OperationFactory.getOperationFromMothod(operationMethod);
        OperationOptions options = new OperationOptions(sourceDirectory, targetDirectory);
        for (Sistema sistema : Sistema.values()) {
            System.out.println("Processando o sistema: " + sistema.getName());
            operation.process(sistema, options);
        }*/
    }

    private static void processArgs(String[] args) {
        Options options = new Options();
        try {
            Option operationOption =
                createOption("o", "operation", OPTION_OPERATION, "Operação a ser processada", true);
            Option systemOption = createOption("y", "system", OPTION_SISTEMA, "Sistema que será processado", true);
            Option sourceDirectoryOption =
                createOption("s", "source", OPTION_SOURCE_DIRECTORY, "Diretório de origem do processamento", true);
            Option targetDirectoryOption =
                createOption("t", "target", OPTION_TARGET_DIRECTORY, "Diretório de destino do processamento", true);
            options.addOption("h", "help", false, "Exibe informações de ajuda");
            options.addOption(operationOption).addOption(systemOption).addOption(sourceDirectoryOption)
                .addOption(targetDirectoryOption);
            CommandLineParser commandLineParser = new DefaultParser();
            CommandLine commandLine = commandLineParser.parse(options, args);
            System.out.println();
        } catch (ParseException e) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("java -jar -o <operation> -y <system> -s <sourceDirectory> -t <targetDirectory>",
                "Parâmetros", options, "");
        }

    }

    private static Option createOption(String shortName, String longName, String argName, String description,
        boolean required) {
        return Option.builder(shortName).longOpt(longName).argName(argName).desc(description).hasArg()
            .required(required).build();
    }
}
