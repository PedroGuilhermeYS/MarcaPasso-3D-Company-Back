package br.edu.ifpe.MarcaPasso3D.repository.Encomenda;

import br.edu.ifpe.MarcaPasso3D.model.Encomenda.Encomenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EncomendaRepository extends JpaRepository<Encomenda, Long> {

    List<Encomenda> findByIdUsuarioOrderByDataHoraDesc(Long idUsuario);

    Optional<Encomenda> findByIdAndIdUsuario(Long id, Long idUsuario);

    boolean existsByNumeroPedido(String numeroPedido);
}
