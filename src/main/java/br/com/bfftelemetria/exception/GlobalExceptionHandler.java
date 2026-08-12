package br.com.bfftelemetria.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VeiculoNaoEncontradoException.class)
    public ResponseEntity<Object> handleVeiculoNaoEncontrado(VeiculoNaoEncontradoException ex) {
        Map<String, Object> corpoErro = new HashMap<>();
        corpoErro.put("timestamp", LocalDateTime.now());
        corpoErro.put("status", HttpStatus.NOT_FOUND.value());
        corpoErro.put("erro", "Não Encontrado");
        corpoErro.put("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(corpoErro);
    }

}
