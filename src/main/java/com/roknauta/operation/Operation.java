package com.roknauta.operation;

import com.roknauta.domain.Sistema;

public interface Operation {

    void process(Sistema sistema, OperationOptions options);

}
