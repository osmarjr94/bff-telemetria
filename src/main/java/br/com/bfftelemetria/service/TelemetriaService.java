package br.com.bfftelemetria.service;

import br.com.bfftelemetria.dto.TelemetriaResponseDTO;

public interface TelemetriaService {
    TelemetriaResponseDTO getStatusVeiculo(String placa);
    TelemetriaResponseDTO getStatusVeiculoByPlaca(String placa);
}