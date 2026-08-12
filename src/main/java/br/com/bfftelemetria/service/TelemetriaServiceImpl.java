package br.com.bfftelemetria.service;

import br.com.bfftelemetria.dto.TelemetriaResponseDTO;
import br.com.bfftelemetria.exception.VeiculoNaoEncontradoException;
import br.com.bfftelemetria.model.EventoTelemetria;
import br.com.bfftelemetria.repository.TelemetriaRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TelemetriaServiceImpl implements TelemetriaService {

    private final TelemetriaRepository repository;

    public TelemetriaServiceImpl(TelemetriaRepository repository) {
        this.repository = repository;
    }

    @Override
    @Retry(name = "telemetriaService", fallbackMethod = "fallbackStatusVeiculo")
    public TelemetriaResponseDTO getStatusVeiculo(String placa) {
        List<EventoTelemetria> eventos = repository.buscarEventosTelemetria();

        // Verifica se o veículo existe na lista (opcional, mas recomendado)
        boolean existe = eventos.stream().anyMatch(e -> e.placa().equalsIgnoreCase(placa));
        if (!existe) {
            throw new VeiculoNaoEncontradoException("Veículo não encontrado para a placa: " + placa);
        }

        boolean acimaDaVelocidade = eventos.stream()
                .filter(e -> e.placa().equalsIgnoreCase(placa))
                .anyMatch(e -> e.velocidade() > 80.0);

        String status = acimaDaVelocidade ? "ALERTA: Velocidade excedida!" : "REGULAR: Dentro da velocidade.";

        // Retornando o DTO em vez da String
        return new TelemetriaResponseDTO(placa, status, LocalDateTime.now());
    }

    @Override
    public TelemetriaResponseDTO getStatusVeiculoByPlaca(String placa) {
        return getStatusVeiculo(placa);
    }
}