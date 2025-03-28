package com.roknauta;

public class RetroRomsException extends RuntimeException {

    public RetroRomsException(String msg) {
       super(msg);
    }

    public RetroRomsException(Exception e) {
        super(e);
    }
}
