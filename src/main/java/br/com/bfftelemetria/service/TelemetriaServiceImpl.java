package br.com.bfftelemetria.service;

import br.com.bfftelemetria.dto.TelemetriaResponseDTO;
import br.com.bfftelemetria.exception.VeiculoNaoEncontradoException;
import br.com.bfftelemetria.model.EventoTelemetria;
import br.com.bfftelemetria.repository.TelemetriaRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TelemetriaServiceImpl implements TelemetriaService {

    private final TelemetriaRepository repository;

    public TelemetriaServiceImpl(TelemetriaRepository repository) {
        this.repository = repository;
    }

    @Override
    @CircuitBreaker(name = "telemetriaService", fallbackMethod = "fallbackGetStatusVeiculo")
    @Retry(name = "telemetriaService")
    public TelemetriaResponseDTO getStatusVeiculo(String placa) {
        List<EventoTelemetria> eventos = repository.buscarEventosTelemetria();

        boolean existe = eventos.stream().anyMatch(e -> e.placa().equalsIgnoreCase(placa));
        if (!existe) {
            throw new VeiculoNaoEncontradoException("Veículo não encontrado para a placa: " + placa);
        }

        boolean acimaDaVelocidade = eventos.stream()
                .filter(e -> e.placa().equalsIgnoreCase(placa))
                .anyMatch(e -> e.velocidade() > 80.0);

        String status = acimaDaVelocidade ? "ALERTA: Velocidade excedida!" : "REGULAR: Dentro da velocidade.";

        return new TelemetriaResponseDTO(placa, status, LocalDateTime.now());
    }

    @Override
    public TelemetriaResponseDTO getStatusVeiculoByPlaca(String placa) {
        return getStatusVeiculo(placa);
    }

    // --- MÉTODO DE FALLBACK ---
    public TelemetriaResponseDTO fallbackGetStatusVeiculo(String placa, Throwable t) {

        if (t instanceof VeiculoNaoEncontradoException) {
            throw (VeiculoNaoEncontradoException) t;
        }

        return new TelemetriaResponseDTO(placa, "INDISPONÍVEL: Serviço temporariamente fora do ar.", LocalDateTime.now());
    }

    @Override
    @CircuitBreaker(name = "telemetriaService", fallbackMethod = "fallbackBuscarHistorico")
    @Retry(name = "telemetriaService")
    public List<TelemetriaResponseDTO> buscarHistoricoPorPlacaEData(String placa, LocalDate data) {
        // Usando o nome correto do atributo injetado: 'repository'
        return repository.buscarPorPlacaEData(placa, data);
    }

    // Fallback opcional para o histórico caso precise
    public List<TelemetriaResponseDTO> fallbackBuscarHistorico(String placa, LocalDate data, Throwable t) {
        return List.of();
    }
}