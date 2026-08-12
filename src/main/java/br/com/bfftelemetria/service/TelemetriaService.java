package br.com.bfftelemetria.service;

import br.com.bfftelemetria.dto.TelemetriaResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface TelemetriaService {
    TelemetriaResponseDTO getStatusVeiculo(String placa);
    TelemetriaResponseDTO getStatusVeiculoByPlaca(String placa);
    List<TelemetriaResponseDTO> buscarHistoricoPorPlacaEData(String placa, LocalDate data);

}