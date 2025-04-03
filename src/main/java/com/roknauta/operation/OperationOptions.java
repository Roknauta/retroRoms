package com.roknauta.operation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OperationOptions {

    private String sourceDirectory;
    private String targetDirectory;
    private List<String> acceptedRegions;
    private boolean oneRomPerRegion;
    private boolean allowPirateRom;
    private boolean allowProtoRom;
    private boolean allowBetaRom;
    private boolean allowDemoRom;
    private boolean allowSampleRom;
    private boolean allowBiosRom;
    private boolean allowUnlicensedRom;
    private boolean allowAfterMarketRom;
    private boolean allowRomWithStatus;

}
