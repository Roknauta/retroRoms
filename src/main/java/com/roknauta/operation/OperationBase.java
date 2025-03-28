package com.roknauta.operation;

import com.roknauta.domain.OperationOptions;
import com.roknauta.domain.Sistema;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public abstract class OperationBase {

    private static final String TEMP_DIRECTORY = "temp";

    protected Sistema sistema;
    protected OperationOptions options;
    protected File systemRootDirectory;
    protected File diretorioOrigem;
    protected File operationFolder;
    protected File tempDirectory;
    protected Set<String> arquivosComErro;

    protected OperationBase(Sistema sistema, OperationOptions options) {
        this.sistema = sistema;
        this.options = options;
        criarDiretoriosBase();
        this.arquivosComErro = new HashSet<>();
    }

    private void criarDiretoriosBase() {
        this.diretorioOrigem = new File(options.getDiretorioOrigem());
        File diretorioDestino = criarDiretorioSeNaoExistir(options.getDiretorioDestino());
        this.systemRootDirectory = criarDiretorioSeNaoExistir(diretorioDestino, sistema.getName());
        this.operationFolder = criarDiretorioSeNaoExistir(systemRootDirectory, getOperationFolder());
        tempDirectory = criarDiretorioSeNaoExistir(systemRootDirectory, TEMP_DIRECTORY);
    }

    protected File criarDiretorioSeNaoExistir(File parent, String nomeDiretorio) {
        File diretorio = new File(parent, nomeDiretorio);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
        return diretorio;
    }

    protected File criarDiretorioSeNaoExistir(String fullPath) {
        File diretorio = new File(fullPath);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
        return diretorio;
    }

    protected abstract String getOperationFolder();


}
