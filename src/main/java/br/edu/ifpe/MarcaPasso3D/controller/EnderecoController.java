package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.model.Endereço.Endereco;
import br.edu.ifpe.MarcaPasso3D.service.EnderecoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Endereco>> getEnderecos(@PathVariable Long idUsuario) {
        List<Endereco> enderecos = enderecoService.getEnderecosByUsuario(idUsuario);
        return ResponseEntity.ok(enderecos);
    }

    @PostMapping
    public ResponseEntity<Endereco> adicionarEndereco(@RequestBody Endereco endereco) {
        Endereco novo = enderecoService.salvar(endereco);
        return ResponseEntity.status(201).body(novo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> editarEndereco(
            @PathVariable Long id,
            @RequestBody Endereco dadosAtualizados) {
        Endereco atualizado = enderecoService.atualizar(id, dadosAtualizados);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEndereco(@PathVariable Long id) {
        enderecoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}