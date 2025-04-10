package com.naczea.bankapp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUtil {
    private ResponseUtilStatus estado;
    private String mensaje;
    private String tipoClase;
    private Object payload;
}
