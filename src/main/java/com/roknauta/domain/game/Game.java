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
    @JsonIgnore
    private String licensed;
    private Rom rom;

    public boolean isPirate(){
        return "2".equals(licensed);
    }

    public boolean isUnlicensed(){
        return "0".equals(licensed);
    }
}
