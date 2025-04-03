package com.roknauta;

import com.roknauta.domain.Sistema;
import com.roknauta.operation.Operation;
import com.roknauta.operation.OperationOptions;
import com.roknauta.operation.factory.OperationFactory;
import com.roknauta.utils.AppUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class App {

    private final static String OPTION_CONFIG_FILE = "config_file";

    public static void main(String[] args) throws FileNotFoundException {
        String configPath = "/home/douglas/workspace/git/pessoal/retroRoms/config/config.properties";
        Properties properties = loadProperties(configPath);
        OperationOptions options = OperationFactory.buildOptionsFromProperties(properties);
        Operation operation = OperationFactory.getOperationFromMothod(properties.getProperty("geral.operacao"));
        for (String systemName : AppUtils.stringToList(properties.getProperty("geral.sistemas"))) {
            Sistema sistema = Sistema.fromName(systemName);
            System.out.println("Processando o sistema: " + sistema.getName());
            operation.process(sistema,options);
        }
    }

    private static Properties loadProperties(String path){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
