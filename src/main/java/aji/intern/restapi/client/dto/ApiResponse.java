package aji.intern.restapi.client.dto;

public record ApiResponse<T>(
        ResponseHeader responseHeader,
        T data
) {}

