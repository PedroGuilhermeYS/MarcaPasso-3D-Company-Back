package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.dto.CarrinhoItemDTO;
import br.edu.ifpe.MarcaPasso3D.service.CarrinhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<CarrinhoItemDTO>> getCarrinho(@PathVariable Long idUsuario) {
        List<CarrinhoItemDTO> itens = carrinhoService.getItensByUsuario(idUsuario);
        return ResponseEntity.ok(itens);
    }

    @DeleteMapping("/item/{idItem}")
    public ResponseEntity<Void> removerItem(@PathVariable Long idItem) {
        carrinhoService.removerItem(idItem);
        return ResponseEntity.noContent().build();
    }
}