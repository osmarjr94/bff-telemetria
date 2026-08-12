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

    public void salvar(String chave, String placa, TelemetriaResponseDTO response) {
        // Usamos uma combinação dos dois para evitar colisão
        cache.put(chave + "-" + placa, response);
    }

    public TelemetriaResponseDTO recuperar(String chave, String placa) {
        return cache.get(chave + "-" + placa);
    }

    public boolean existe(String chave, String placa) {
        return cache.containsKey(chave + "-" + placa);
    }
}