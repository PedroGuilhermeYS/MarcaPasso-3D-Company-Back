package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.dto.Cupom.CupomRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Cupom.CupomResponseDTO;
import br.edu.ifpe.MarcaPasso3D.service.CupomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cupons")
public class CupomController {

    private final CupomService service;

    public CupomController(CupomService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CupomResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CupomResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CupomResponseDTO> criar(@Valid @RequestBody CupomRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CupomResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CupomRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
