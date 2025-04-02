package com.roknauta.operation.factory;

import com.roknauta.operation.ExtractionOperation;
import com.roknauta.operation.Operation;
import com.roknauta.operation.SelectionOperation;

public class OperationFactory {

    private static final String EXTRACTION_CODE = "extraction";
    private static final String SELECTION_CODE = "selection";

    public static Operation getOperationFromMothod(String codigo) {
        switch (codigo) {
            case EXTRACTION_CODE -> {
                return new ExtractionOperation();
            }
            case SELECTION_CODE -> {
                return new SelectionOperation();
            }
        }
        throw new UnsupportedOperationException("Operation Method not supported: "+codigo);
    }

}
