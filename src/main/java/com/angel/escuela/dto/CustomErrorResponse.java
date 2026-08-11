package com.angel.escuela.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {
}
