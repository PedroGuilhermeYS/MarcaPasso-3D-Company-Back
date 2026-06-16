package br.edu.ifpe.MarcaPasso3D.repository.Frete;

import br.edu.ifpe.MarcaPasso3D.model.Frete.Frete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FreteRepository extends JpaRepository<Frete, Long> {

    List<Frete> findAllByOrderByIdAsc();

    Optional<Frete> findByCep(String cep);

    boolean existsByCep(String cep);
}