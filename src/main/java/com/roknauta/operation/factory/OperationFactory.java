package com.roknauta.operation.factory;

import com.roknauta.operation.ExtractionOperation;
import com.roknauta.operation.Operation;
import com.roknauta.operation.OperationOptions;
import com.roknauta.operation.SelectionOperation;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class OperationFactory {

    private static final String EXTRACTION_CODE = "extracao";
    private static final String SELECTION_CODE = "selecao";

    private static final String PROP_SOURCE_DIRECTORY = "geral.diretorio.origem";
    private static final String PROP_TARGET_DIRECTORY = "geral.diretorio.destino";
    private static final String PROP_ACCEPTED_REGIONS = "selecao.regioes.permitidas";
    private static final String PROP_ONE_ROM_PER_REGION = "selecao.uma.rom.por.regiao";
    private static final String PROP_ALLOW_PIRATE_ROM = "selecao.considerar.pirate";
    private static final String PROP_ALLOW_DEMO_ROM = "selecao.considerar.demo";
    private static final String PROP_ALLOW_PROTO_ROM = "selecao.considerar.proto";
    private static final String PROP_ALLOW_BETA_ROM = "selecao.considerar.beta";
    private static final String PROP_ALLOW_SAMPLE_ROM = "selecao.considerar.sample";
    private static final String PROP_ALLOW_BIOS_ROM = "selecao.considerar.bios";
    private static final String PROP_ALLOW_UNLICENSED_ROM = "selecao.considerar.unlicensed";
    private static final String PROP_ALLOW_AFTERMARKET_ROM = "selecao.considerar.afterMarket";
    private static final String PROP_ROM_WITH_STATUS = "selecao.considerar.com.status";

    public static Operation getOperationFromMothod(String codigo) {
        switch (codigo) {
            case EXTRACTION_CODE -> {
                return new ExtractionOperation();
            }
            case SELECTION_CODE -> {
                return new SelectionOperation();
            }
        }
        throw new UnsupportedOperationException("Operation Method not supported: " + codigo);
    }

    public static OperationOptions buildOptionsFromProperties(Properties properties) {
        String sourceDirectory = properties.getProperty(PROP_SOURCE_DIRECTORY);
        String targetDirectory = properties.getProperty(PROP_TARGET_DIRECTORY);
        List<String> acceptedRegions = propertyToList(properties.getProperty(PROP_ACCEPTED_REGIONS));
        boolean oneRomPerRegion = propertyToBoolean(properties.getProperty(PROP_ONE_ROM_PER_REGION));
        boolean allowPirateRom = propertyToBoolean(properties.getProperty(PROP_ALLOW_PIRATE_ROM));
        boolean allowProtoRom = propertyToBoolean(properties.getProperty(PROP_ALLOW_PROTO_ROM));
        boolean allowBetaRom = propertyToBoolean(properties.getProperty(PROP_ALLOW_BETA_ROM));
        boolean allowDemoRom = propertyToBoolean(properties.getProperty(PROP_ALLOW_DEMO_ROM));
        boolean allowSampleRom = propertyToBoolean(properties.getProperty(PROP_ALLOW_SAMPLE_ROM));
        boolean allowBiosRom = propertyToBoolean(properties.getProperty(PROP_ALLOW_BIOS_ROM));
        boolean allowUnlicensedRom = propertyToBoolean(properties.getProperty(PROP_ALLOW_UNLICENSED_ROM));
        boolean allowAfterMarketRom = propertyToBoolean(properties.getProperty(PROP_ALLOW_AFTERMARKET_ROM));
        boolean allowRomWithStatus = propertyToBoolean(properties.getProperty(PROP_ROM_WITH_STATUS));
        return new OperationOptions( sourceDirectory, targetDirectory, acceptedRegions, oneRomPerRegion,
            allowPirateRom, allowProtoRom,allowBetaRom, allowDemoRom, allowSampleRom, allowBiosRom, allowUnlicensedRom,
            allowAfterMarketRom, allowRomWithStatus);
    }

    private static List<String> propertyToList(String value) {
        return Arrays.stream(value.split(",")).toList();
    }

    private static boolean propertyToBoolean(String value) {
        return "Y".equalsIgnoreCase(value);
    }

}
