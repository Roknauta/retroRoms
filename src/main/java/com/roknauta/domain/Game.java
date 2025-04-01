package com.roknauta.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    private String name;
    private String gameId;
    private String gameParent;
    private Integer revision;
    private List<String> regions;
    private boolean pirate;
    private boolean unlicensed;
    private boolean bios;
    private boolean demo;
    private boolean proto;
    private boolean beta;
    private boolean afterMarket;
    private boolean sample;
    private boolean hasStatus;
    private List<Rom> roms;
}
