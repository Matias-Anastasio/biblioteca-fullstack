package com.matiasanastasio.biblioteca.dto.exception;

public record ApiError(
        int status,
        String error,
        String message,
        String path
) {}
