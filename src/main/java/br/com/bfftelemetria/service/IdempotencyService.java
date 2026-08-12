package br.com.bfftelemetria.service;

import br.com.bfftelemetria.dto.TelemetriaResponseDTO;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class IdempotencyService {

    private final ConcurrentHashMap<String, TelemetriaResponseDTO> cache = new ConcurrentHashMap<>();

    public IdempotencyService() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(cache::clear, 10, 10, TimeUnit.MINUTES);
    }

    public boolean existe(String idempotencyKey) {
        return idempotencyKey != null && cache.containsKey(idempotencyKey);
    }

    public TelemetriaResponseDTO recuperar(String idempotencyKey) {
        return cache.get(idempotencyKey);
    }

    public void salvar(String idempotencyKey, TelemetriaResponseDTO response) {
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {
            cache.put(idempotencyKey, response);
        }
    }
}