package com.roknauta.domain.game.factory;


import com.roknauta.datasource.noIntro.db.File;
import com.roknauta.datasource.noIntro.db.NoIntroGame;
import com.roknauta.datasource.noIntro.db.Source;
import com.roknauta.domain.game.Rom;

import java.util.Objects;

public class RomFactory {

    public static Rom fromNoIntroDB(NoIntroGame game) {
        File file = game.getSources().stream().map(Source::getFile).filter(Objects::nonNull).findFirst().get();
        return new Rom(file.getExtension(), file.getCrc32(), file.getMd5(), file.getSha1(), file.getSha256());
    }

}
