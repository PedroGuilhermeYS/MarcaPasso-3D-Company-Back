package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.model.Produto;
import br.edu.ifpe.MarcaPasso3D.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping("/home")
    public ResponseEntity<Page<Produto>> consultarHome(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(service.consultarHome(pageable));
    }

    @GetMapping
    public ResponseEntity<List<Produto>> consultarTodos() {
        return ResponseEntity.ok(service.consultarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> consultarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.consultarPorId(id));
    }

    @GetMapping("/ordenar-nome")
    public ResponseEntity<List<Produto>> ordenarPorNome() {
        return ResponseEntity.ok(service.ordenarPorNome());
    }

    @GetMapping("/menor-preco")
    public ResponseEntity<List<Produto>> ordenarPorMenorPreco() {
        return ResponseEntity.ok(service.ordenarPorMenorPreco());
    }

    @GetMapping("/carrossel")
    public ResponseEntity<List<Produto>> consultarCarrossel() {
        return ResponseEntity.ok(service.consultarCarrossel());
    }

    @GetMapping("/nova-tela/{categoria}")
    public ResponseEntity<List<Produto>> consultarNovaTela(@PathVariable String categoria) {
        return ResponseEntity.ok(service.consultarNovaTela(categoria));
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto) {
        return ResponseEntity.status(201).body(service.cadastrar(produto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        return ResponseEntity.ok(service.atualizar(id, produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}