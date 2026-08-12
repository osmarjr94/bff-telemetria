package br.com.bfftelemetria.repository;

import br.com.bfftelemetria.model.EventoTelemetria;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
}
