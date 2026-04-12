package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.dto.Encomenda.CriarEncomendaDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Encomenda.EncomendaDetalheDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Encomenda.EncomendaResumoDTO;
import br.edu.ifpe.MarcaPasso3D.service.EncomendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encomendas")
public class EncomendaController {

    private final EncomendaService encomendaService;

    public EncomendaController(EncomendaService encomendaService) {
        this.encomendaService = encomendaService;
    }

    /**
     * GET /api/encomendas/{idUsuario}
     * Lista todos os pedidos do usuário (resumo para a listagem).
     */
    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<EncomendaResumoDTO>> listar(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(encomendaService.listarPorUsuario(idUsuario));
    }

    /**
     * GET /api/encomendas/{idUsuario}/{idEncomenda}
     * Retorna o detalhe completo de um pedido do usuário.
     */
    @GetMapping("/{idUsuario}/{idEncomenda}")
    public ResponseEntity<EncomendaDetalheDTO> detalhe(
            @PathVariable Long idUsuario,
            @PathVariable Long idEncomenda) {
        return ResponseEntity.ok(encomendaService.buscarDetalhe(idUsuario, idEncomenda));
    }

    /**
     * POST /api/encomendas/{idUsuario}
     * Cria um novo pedido para o usuário.
     */
    @PostMapping("/{idUsuario}")
    public ResponseEntity<EncomendaDetalheDTO> criar(
            @PathVariable Long idUsuario,
            @RequestBody CriarEncomendaDTO dto) {
        EncomendaDetalheDTO criado = encomendaService.criarEncomenda(idUsuario, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
}
