package com.roknauta.operation;

import com.roknauta.RetroRomsException;
import com.roknauta.domain.Sistema;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Enumeration;

public class ExtractOperation extends OperationBase implements Operation {

    private static final String ZIP = "zip";
    private static final String SEVEN_Z = "7z";
    private static final String OPERATION_DIRECTORY = "extracao";
    private static final String TEMP_DIRECTORY = "temp";

    private File diretorioSistema;
    private File tempDirectory;

    public ExtractOperation(Sistema sistema, OperationOptions options) {
        super(sistema, options);
    }

    @Override
    public void process() {
        criarDiretorios();
        for (File arquivo : FileUtils.listFiles(diretorioOrigem, null, true)) {
            processarArquivo(arquivo);
        }
        tempDirectory.delete();
    }

    private void criarDiretorios() {
        this.diretorioSistema = criarDiretorioSeNaoExistir(diretorioDestino, sistema.getName());
        this.tempDirectory = criarDiretorioSeNaoExistir(diretorioDestino, TEMP_DIRECTORY);
    }

    private void processarArquivo(File arquivo) {
        if (arquivoValido(arquivo)) {
            if (isZipOr7z(arquivo)) {
                descompactarArquivo(arquivo);
            } else {
                copiarArquivoParaPastaOperacao(arquivo);
            }
        }
    }

    private void copiarArquivoParaPastaOperacao(File origem) {
        try {
            String extension = FilenameUtils.getExtension(origem.getName());
            File destino =
                new File(diretorioSistema, DigestUtils.md5Hex(new FileInputStream(origem)) + "." + extension);
            if (!FileUtils.directoryContains(diretorioSistema, destino))
                FileUtils.copyFile(origem, destino);
        } catch (IOException e) {
            throw new RetroRomsException(
                "Erro ao copiar o arquivo: " + origem.getPath() + ". Detalhes: " + e.getMessage());
        }
    }

    private boolean arquivoValido(File file) {
        return isZipOr7z(file) || sistema.getExtensions().stream().anyMatch(
            extension -> extension.toLowerCase().equals(FilenameUtils.getExtension(file.getName().toLowerCase())));
    }

    public void descompactarArquivo(File arquivoCompactado) {
        if (isZip(arquivoCompactado)) {
            extractFromZip(arquivoCompactado);
        } else if (is7z(arquivoCompactado)) {
            extractFrom7z(arquivoCompactado);
        }
    }

    private void salvarEntry(ArchiveEntry entry, InputStream inputStream) throws IOException {
        if (!entry.isDirectory()) {
            File arquivoDescompactado = salvarArchiveEntry(entry, tempDirectory, inputStream);
            processarArquivo(arquivoDescompactado);
            arquivoDescompactado.delete();
        }
    }

    private void extractFromZip(File arquivoCompactado) {
        try (ZipFile zipFile = ZipFile.builder().setFile(arquivoCompactado).get()) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                salvarEntry(entry, zipFile.getInputStream(entry));
            }
        } catch (Exception e) {
            arquivosComErro.add(arquivoCompactado.getName());
        }
    }

    private void extractFrom7z(File arquivoCompactado) {
        try (SevenZFile sevenZFile = SevenZFile.builder().setFile(arquivoCompactado).get()) {
            for (SevenZArchiveEntry entry : sevenZFile.getEntries()) {
                salvarEntry(entry, sevenZFile.getInputStream(entry));
            }
        } catch (Exception e) {
            arquivosComErro.add(arquivoCompactado.getName());
        }
    }

    private File salvarArchiveEntry(ArchiveEntry entry, File diretorioDestino, InputStream inputStream)
        throws IOException {
        File arquivo = new File(diretorioDestino, FilenameUtils.getName(entry.getName()));
        try (OutputStream os = new FileOutputStream(arquivo)) {
            IOUtils.copy(inputStream, os);
        }
        return arquivo;
    }

    private boolean isZipOr7z(File arquivo) {
        return isZip(arquivo) || is7z(arquivo);
    }

    public boolean isZip(File file) {
        return FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(ZIP);
    }

    public boolean is7z(File file) {
        return FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(SEVEN_Z);
    }
}
