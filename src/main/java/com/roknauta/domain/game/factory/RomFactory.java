package com.roknauta.domain.game.factory;


import com.roknauta.datasource.noIntro.db.File;
import com.roknauta.domain.game.Rom;

public class RomFactory {

    public static Rom fromNoIntroDB(File file) {
        return new Rom(file.getExtension(), file.getCrc32(), file.getMd5(), file.getSha1(), file.getSha256());
    }

}
