package com.roknauta;

import com.roknauta.domain.Sistema;
import com.roknauta.operation.Operation;
import com.roknauta.operation.OperationOptions;
import com.roknauta.operation.factory.OperationFactory;

public class App {

    public static void main(String[] args) {
        String operationMethod = "selection";
        String sourceDirectory = "/home/douglas/workspace/xd/extraction";
        String targetDirectory = "/home/douglas/workspace/xd/selection";
        Operation operation = OperationFactory.getOperationFromMothod(operationMethod);
        OperationOptions options = new OperationOptions(sourceDirectory, targetDirectory);
        for (Sistema sistema : Sistema.values()) {
            System.out.println("Processando o sistema: " + sistema.getName());
            operation.process(sistema, options);
        }
    }
}
