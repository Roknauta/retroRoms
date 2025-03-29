package com.roknauta.datasource.noIntro.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Source {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("file")
    private List<File> files;
    private Serials serials;
    private Details details;
}
