package com.roknauta.domain.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private List<String> languages;
    private boolean hasRetroAchievements;
    private String licensed;
    private List<Rom> roms;

    @JsonIgnore
    public boolean isPirate() {
        return "2".equals(licensed);
    }

    @JsonIgnore
    public boolean isUnlicensed() {
        return "0".equals(licensed);
    }

    @JsonIgnore
    public boolean isParent() {
        return "P".equalsIgnoreCase(this.gameParent);
    }

    @JsonIgnore
    public boolean isClone() {
        return !isParent();
    }
}
