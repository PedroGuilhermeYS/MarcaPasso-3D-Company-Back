package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.dto.Personalizado.AtualizarStatusPersonalizadoDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Personalizado.PedidoPersonalizadoRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Personalizado.PedidoPersonalizadoResponseDTO;
import br.edu.ifpe.MarcaPasso3D.model.Personalizado.StatusPedidoPersonalizado;
import br.edu.ifpe.MarcaPasso3D.service.PedidoPersonalizadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personalizados")
public class PedidoPersonalizadoController {

    private final PedidoPersonalizadoService service;

    public PedidoPersonalizadoController(PedidoPersonalizadoService service) {
        this.service = service;
    }

    // ── Rotas do usuário autenticado ─────────────────────────

    // POST /api/personalizados/{idUsuario}
    @PostMapping("/{idUsuario}")
    public ResponseEntity<PedidoPersonalizadoResponseDTO> criar(
            @PathVariable Long idUsuario,
            @Valid @RequestBody PedidoPersonalizadoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(idUsuario, dto));
    }

    // GET /api/personalizados/{idUsuario}
    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<PedidoPersonalizadoResponseDTO>> listarPorUsuario(
            @PathVariable Long idUsuario) {
        return ResponseEntity.ok(service.listarPorUsuario(idUsuario));
    }

    // GET /api/personalizados/{idUsuario}/{id}
    @GetMapping("/{idUsuario}/{id}")
    public ResponseEntity<PedidoPersonalizadoResponseDTO> buscarPorIdEUsuario(
            @PathVariable Long idUsuario,
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorIdEUsuario(idUsuario, id));
    }

    // ── Rotas do admin ───────────────────────────────────────

    // GET /api/personalizados
    @GetMapping
    public ResponseEntity<List<PedidoPersonalizadoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET /api/personalizados?status=AGUARDANDO_ORCAMENTO
    @GetMapping(params = "status")
    public ResponseEntity<List<PedidoPersonalizadoResponseDTO>> listarPorStatus(
            @RequestParam StatusPedidoPersonalizado status) {
        return ResponseEntity.ok(service.listarPorStatus(status));
    }

    // GET /api/personalizados/admin/{id}
    @GetMapping("/admin/{id}")
    public ResponseEntity<PedidoPersonalizadoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // PATCH /api/personalizados/admin/{id}/status
    @PatchMapping("/admin/{id}/status")
    public ResponseEntity<PedidoPersonalizadoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusPersonalizadoDTO dto) {
        return ResponseEntity.ok(service.atualizarStatus(id, dto));
    }

    // DELETE /api/personalizados/admin/{id}
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
