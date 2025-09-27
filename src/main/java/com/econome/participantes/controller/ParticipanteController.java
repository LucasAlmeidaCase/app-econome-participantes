package com.econome.participantes.controller;

import com.econome.participantes.dto.ParticipanteRequest;
import com.econome.participantes.dto.ParticipanteResponse;
import com.econome.participantes.service.ParticipanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/participantes")
@Validated
@RequiredArgsConstructor
public class ParticipanteController {

    private final ParticipanteService participanteService;

    @GetMapping
    public List<ParticipanteResponse> listar() {
        return participanteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ParticipanteResponse buscarPorId(@PathVariable Long id) {
        return participanteService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ParticipanteResponse> criar(@Valid @RequestBody ParticipanteRequest request) {
        ParticipanteResponse criado = participanteService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public ParticipanteResponse atualizar(@PathVariable Long id, @Valid @RequestBody ParticipanteRequest request) {
        return participanteService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        participanteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
