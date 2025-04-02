package com.roknauta.operation;

import com.roknauta.RetroRomsException;
import com.roknauta.domain.Sistema;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class OperationBase {

    protected Sistema sistema;
    protected OperationOptions options;
    protected File systemDirectory;
    protected File diretorioOrigem;
    protected File diretorioDestino;
    protected Set<String> arquivosComErro;

    protected void init(Sistema sistema, OperationOptions options) {
        this.sistema = sistema;
        this.options = options;
        this.arquivosComErro = new HashSet<>();
        criarDiretoriosBase();
    }

    private void criarDiretoriosBase() {
        this.diretorioOrigem = new File(options.getDiretorioOrigem());
        this.diretorioDestino = criarDiretorioSeNaoExistir(options.getDiretorioDestino());
        this.systemDirectory = criarDiretorioSeNaoExistir(diretorioDestino, sistema.getName());
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

    protected String getMd5Hex(File file) {
        try {
            return DigestUtils.md5Hex(new FileInputStream(file));
        } catch (IOException e) {
            throw new RetroRomsException(e);
        }
    }

    protected File getDatasourcesFolder() {
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource("datasources")).getFile());
    }

    protected String toFileName(String name, String extension) {
        return name.concat(FilenameUtils.EXTENSION_SEPARATOR_STR).concat(extension);
    }

    protected void copiarArquivo(File origem, File diretorioDestino, String newFileName) {
        try {
            File newFile = new File(diretorioDestino, newFileName);
            if (!FileUtils.directoryContains(diretorioDestino, newFile))
                FileUtils.copyFile(origem, newFile);
        } catch (IOException e) {
            throw new RetroRomsException(
                "Erro ao copiar o arquivo: " + origem.getPath() + ". Detalhes: " + e.getMessage());
        }
    }


}
