package com.roknauta.domain.game.factory;

import com.roknauta.datasource.noIntro.db.Archive;
import com.roknauta.datasource.noIntro.db.NoIntroGame;
import com.roknauta.domain.game.Game;
import com.roknauta.domain.game.Rom;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class GameFactory {

    public static Game fromNoIntroDB(NoIntroGame game, boolean hasAchivement, List<Rom> roms) {
        Archive archive = game.getArchive();
        return new Game(game.getName(), archive.getNumber(), archive.getClone(), toRevision(archive.getVersion1()),
            toList(archive.getRegion()), toList(archive.getLanguages()), hasAchivement, game.getArchive().getLicensed(),
            roms);
    }

    private static List<String> toList(String str) {
        return Arrays.stream(str.split(",")).map(String::trim).toList();
    }

    public static Integer toRevision(String str) {
        if (StringUtils.isNotEmpty(str) && str.toLowerCase().matches("rev[0-9 ]+"))
            return Integer.valueOf(StringUtils.substringAfter(str.toLowerCase(), "rev").trim());
        return 0;
    }

}
