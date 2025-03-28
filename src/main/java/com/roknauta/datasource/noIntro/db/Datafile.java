package com.roknauta.datasource.noIntro.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JacksonXmlRootElement(localName = "datafile")
@Getter
@Setter
public class Datafile {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("game")
    protected List<NoIntroGame> games;
}
