package br.com.bfftelemetria.controller; // Ajuste o pacote se necessário

import br.com.bfftelemetria.dto.TelemetriaResponseDTO;
import br.com.bfftelemetria.service.IdempotencyService;
import br.com.bfftelemetria.service.TelemetriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/telemetria")
public class TelemetriaController {

    private final TelemetriaService service;
    private final IdempotencyService idempotencyService;

    public TelemetriaController(TelemetriaService service, IdempotencyService idempotencyService) {
        this.service = service;
        this.idempotencyService = idempotencyService;
    }

    @GetMapping("/{placa}")
    public ResponseEntity<TelemetriaResponseDTO> consultarStatus(
            @PathVariable String placa,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {

        if (idempotencyService.existe(idempotencyKey)) {
            return ResponseEntity.ok(idempotencyService.recuperar(idempotencyKey));
        }

        TelemetriaResponseDTO response = service.getStatusVeiculo(placa);

        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            idempotencyService.salvar(idempotencyKey, response);
        }

        return ResponseEntity.ok(response);
    }
}