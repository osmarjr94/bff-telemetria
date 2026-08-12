package br.com.bfftelemetria.repository;

import br.com.bfftelemetria.dto.TelemetriaResponseDTO;
import br.com.bfftelemetria.model.EventoTelemetria;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TelemetriaRepository {

    public List<EventoTelemetria> buscarEventosTelemetria() {
        LocalDateTime agora = LocalDateTime.now();

        return Arrays.asList(
                new EventoTelemetria("ABC1234", 80.5, agora.minusMinutes(5)),
                new EventoTelemetria("XYZ5678", 60.2, agora.minusMinutes(10)),
                new EventoTelemetria("LMN9012", 90.0, agora.minusMinutes(15))
        );
    }

    public List<TelemetriaResponseDTO> buscarPorPlacaEData(String placa, LocalDate data) {
        List<EventoTelemetria> eventos = buscarEventosTelemetria();

        return eventos.stream()
                .filter(e -> e.placa().equalsIgnoreCase(placa))
                // Filtra comparando a data do evento (exemplo considerando que EventoTelemetria tenha LocalDateTime ou LocalDate)
                .filter(e -> e.dataHora().toLocalDate().isEqual(data))
                .map(e -> {
                    String status = e.velocidade() > 80.0 ? "ALERTA: Velocidade excedida!" : "REGULAR: Dentro da velocidade.";
                    return new TelemetriaResponseDTO(e.placa(), status, e.dataHora());
                })
                .collect(Collectors.toList());
    }
}
