package com.roknauta.datasource.noIntro.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Details {

    private String mediaTitle;
    private String link1;
    private String originalformat;
    @JsonProperty("d_date")
    private String dDate;
    @JsonProperty("r_date")
    private String rDate;
    private String dumper;
    private String project;
    private String section;
    private String id;
    private String region;
    private String origin;
    private String comment1;
    @JsonProperty("r_date_info")
    private String rDateInfo;
    private String tool;
    private String dirname;
    private String rominfo;
    private String nfoname;
    private String nfosize;
    private String nfocrc;
    private String archivename;
    private String date;
    private String group;
    private String comment;
    private String link2;
    private String category;
    private String d_date_info;
    private String media_title;
    private String comment2;
    private String nodump;
    private String link3;

}
