package br.edu.ifpe.MarcaPasso3D.repository.Favorito;

import br.edu.ifpe.MarcaPasso3D.model.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    Optional<Favorito> findByIdUsuario(Long idUsuario);
}