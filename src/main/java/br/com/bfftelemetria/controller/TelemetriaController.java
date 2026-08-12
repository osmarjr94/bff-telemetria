package br.com.bfftelemetria.controller; // Ajuste o pacote se necessário

import br.com.bfftelemetria.dto.TelemetriaResponseDTO;
import br.com.bfftelemetria.service.IdempotencyService;
import br.com.bfftelemetria.service.TelemetriaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

        if (idempotencyService.existe(idempotencyKey, placa)) {
            return ResponseEntity.ok(idempotencyService.recuperar(idempotencyKey, placa));
        }

        TelemetriaResponseDTO response = service.getStatusVeiculo(placa);

        if (idempotencyKey != null) {
            idempotencyService.salvar(idempotencyKey, placa, response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{placa}/historico")
    public ResponseEntity<List<TelemetriaResponseDTO>> getHistoricoPorData(
            @PathVariable String placa,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<TelemetriaResponseDTO> historico = service.buscarHistoricoPorPlacaEData(placa, data);
        return ResponseEntity.ok(historico);
    }

}