package com.roknauta.operation.factory;

import com.roknauta.RetroRomsException;
import com.roknauta.operation.ExtractOperation;
import com.roknauta.operation.Operation;
import com.roknauta.operation.SelectOperation;

public class OperationFactory {

    private static final String EXTRACTION_CODE = "extraction";
    private static final String SELECTION_CODE = "selection";

    public static Operation getOperationFromMothod(String codigo) {
        switch (codigo) {
            case EXTRACTION_CODE -> {
                return new ExtractOperation();
            }
            case SELECTION_CODE -> {
                return new SelectOperation();
            }
        }
        throw new RetroRomsException("");
    }

}
