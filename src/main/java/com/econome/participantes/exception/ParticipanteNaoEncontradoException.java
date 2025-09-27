package com.econome.participantes.exception;

/**
 * Exceção lançada quando um Participante não é encontrado.
 */
public class ParticipanteNaoEncontradoException extends RuntimeException {
    public ParticipanteNaoEncontradoException(Long id) {
        super("Participante não encontrado. ID=" + id);
    }
}
