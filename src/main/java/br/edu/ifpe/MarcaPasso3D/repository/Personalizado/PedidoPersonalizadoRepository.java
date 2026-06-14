package br.edu.ifpe.MarcaPasso3D.repository.Personalizado;

import br.edu.ifpe.MarcaPasso3D.model.Personalizado.PedidoPersonalizado;
import br.edu.ifpe.MarcaPasso3D.model.Personalizado.StatusPedidoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoPersonalizadoRepository extends JpaRepository<PedidoPersonalizado, Long> {

    // ── Queries do usuário ───────────────────────────────────

    List<PedidoPersonalizado> findByIdUsuarioOrderByCriadoEmDesc(Long idUsuario);

    Optional<PedidoPersonalizado> findByIdAndIdUsuario(Long id, Long idUsuario);

    // ── Queries do admin ─────────────────────────────────────

    List<PedidoPersonalizado> findAllByOrderByCriadoEmDesc();

    List<PedidoPersonalizado> findByStatusOrderByCriadoEmDesc(StatusPedidoPersonalizado status);
}
