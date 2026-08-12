package br.com.bfftelemetria.controller;

import br.com.bfftelemetria.dto.TelemetriaResponseDTO;
import br.com.bfftelemetria.service.TelemetriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/telemetria")
public class TelemetriaController {

    private final TelemetriaService service;

    // Injeção de dependência via construtor (mesma boa prática do Service!)
    public TelemetriaController(TelemetriaService service) {
        this.service = service;
    }

    @GetMapping("/{placa}")
    public ResponseEntity<TelemetriaResponseDTO> consultarStatus(@PathVariable String placa) {
        TelemetriaResponseDTO response = service.getStatusVeiculo(placa);
        return ResponseEntity.ok(response);
    }
}