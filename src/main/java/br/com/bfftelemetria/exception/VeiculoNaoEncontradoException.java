package br.com.bfftelemetria.exception;

public class VeiculoNaoEncontradoException extends RuntimeException {

    public VeiculoNaoEncontradoException() {
        super();
    }

    public VeiculoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}