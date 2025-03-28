package com.roknauta.datasource.noIntro.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@JsonIgnoreProperties(ignoreUnknown = true)
public class Archive {

    private String number;
    private String regparent;
    private String licensed;
    private String languages;
    private String langchecked;
    private String clone;
    private String name;
    private String categories;
    private String region;
    @JsonProperty("name_alt")
    private String nameAlt;
    private String version1;
    private String physical;
    private String aftermarket;
    private String devstatus;
    private String complete;
    private String special2;
    private String special1;
    private String additional;
    private String showlang;
    private String bios;
    @JsonProperty("sticky_note")
    private String stickyNote;
    private String version2;
    @JsonProperty("datter_note")
    private String datterNote;
    private String alt;
    private String dat;
    private String adult;
    private String gameid1;
    private String description;
    private String mergeof;
    private String listed;

}
