package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.dto.Frete.FreteRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Frete.FreteResponseDTO;
import br.edu.ifpe.MarcaPasso3D.service.FreteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fretes")
public class FreteController {

    private final FreteService service;

    public FreteController(FreteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<FreteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FreteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<FreteResponseDTO> buscarPorCep(@PathVariable String cep) {
        return ResponseEntity.ok(service.buscarPorCep(cep));
    }

    @PostMapping
    public ResponseEntity<FreteResponseDTO> criar(@Valid @RequestBody FreteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FreteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FreteRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}