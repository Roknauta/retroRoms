package com.roknauta.operation;

import com.roknauta.domain.Sistema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OperationOptions {

    private Sistema sistema;
    private String diretorioOrigem;
    private String diretorioDestino;

}
