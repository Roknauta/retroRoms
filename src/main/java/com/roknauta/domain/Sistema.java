package com.roknauta.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Sistema {

    NES("nes","nes, unif, unf"),
   SNES("snes","smc, fig, sfc, gd3, gd7, dx2, bsx, swc"),
   GB("gb","gb"),
   GBC("gbc","gbc"),
   GBA("gba","gba"),
    N64("n64","z64,n64,v64"),
    //NDS("nds","nds,bin"),
    MASTER_SYSTEM("mastersystem","bin,sms"),
    MEGA_DRIVE("megadrive","bin,gen,md,sg,smd"),
    SEGA32X("sega32x","32x,smd,bin,md"),
    GAME_GEAR("gamegear","bin,gg"),
    ATARI_2600("atari2600","a26,bin"),
    ATARI_5200("atari5200","rom, xfd, atr, atx, cdm, cas, car, bin, a52, xex"),
    ATARI_7800("atari7800","a78,bin"),
    ATARI_LYNX("lynx","lnx,bll,lyx,o,bin"),
    ATARI_JAGUAR("jaguar","cue, j64, jag, cof, abs, cdi, rom"),
    //ATARI_8_BITS("atari800","rom, xfd, atr, atx, cdm, cas, car,bin,a52,xex"),
    //ATARI_ST("atarist","st, msa, stx, dim, ipf, m3u"),
    COLECO_VISION("colecovision","bin, col, rom"),
    COMODORE_64("c64","d64, d81, crt, prg, tap, t64, m3u"),
    CHANNEL_F("channelf","bin, chf, rom"),
    ODYSSEY2("o2em","bin"),
    INTELLIVISION("intellivision","int, bin,rom"),
    PC_ENGINE("pcengine","bin,pce");

    private final String name;
    private final String extensions;


    public List<String> getExtensions() {
        return Arrays.stream(this.extensions.split(",")).map(String::trim).toList();
    }
}
