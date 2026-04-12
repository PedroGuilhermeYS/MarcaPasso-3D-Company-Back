package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.dto.FavoritoProdutoDTO;
import br.edu.ifpe.MarcaPasso3D.service.FavoritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    // GET — buscar favoritos do usuário
    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<FavoritoProdutoDTO>> listar(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(favoritoService.listar(idUsuario));
    }

    // POST — adicionar produto ao favorito
    @PostMapping("/{idUsuario}/produto/{idProduto}")
    public ResponseEntity<List<FavoritoProdutoDTO>> adicionar(
            @PathVariable Long idUsuario,
            @PathVariable Long idProduto) {
        return ResponseEntity.ok(favoritoService.adicionar(idUsuario, idProduto));
    }

    // DELETE — remover produto do favorito
    @DeleteMapping("/{idUsuario}/produto/{idProduto}")
    public ResponseEntity<List<FavoritoProdutoDTO>> remover(
            @PathVariable Long idUsuario,
            @PathVariable Long idProduto) {
        return ResponseEntity.ok(favoritoService.remover(idUsuario, idProduto));
    }

    // DELETE — limpar todos os favoritos do usuário
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> limpar(@PathVariable Long idUsuario) {
        favoritoService.limpar(idUsuario);
        return ResponseEntity.noContent().build();
    }
}