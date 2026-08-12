package br.com.bfftelemetria.dto;

import java.time.LocalDateTime;

public record TelemetriaResponseDTO(
        String placa,
        String status,
        LocalDateTime dataConsulta
) {}