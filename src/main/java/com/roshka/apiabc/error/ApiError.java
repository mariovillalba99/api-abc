package com.roshka.apiabc.error;

import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class ApiError {
    public String codigo;
    public String error;

    public final static ApiError INTERNAL_SERVER_ERROR =
            new ApiError("g100", "Error interno del servidor");

    public final static ApiError BAD_REQUEST = new ApiError("g268",
            "Parámetros inválidos");

    public ApiError(String codigo, String error) {
        this.codigo = codigo;
        this.error = error;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "codigo='" + codigo + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
