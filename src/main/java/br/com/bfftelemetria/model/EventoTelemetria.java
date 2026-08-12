package br.com.bfftelemetria.model;

import lombok.Getter;

import java.time.LocalDateTime;


public record EventoTelemetria(

        String placa,
        double velocidade,
        LocalDateTime dataHora
) {
}

