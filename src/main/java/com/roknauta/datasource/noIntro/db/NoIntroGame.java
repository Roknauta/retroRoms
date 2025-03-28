package com.roknauta.datasource.noIntro.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Getter
@Setter
public class NoIntroGame {

    private String name;
    private Archive archive;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("source")
    private List<Source> sources;
    private Release release;

    public boolean isValidGame() {
        return CollectionUtils.isNotEmpty(this.getSources()) && this.getArchive() != null && StringUtils.isEmpty(
            this.getArchive().getDevstatus()) && this.getSources().stream()
            .anyMatch(source -> source.getFile() != null);
    }
}
