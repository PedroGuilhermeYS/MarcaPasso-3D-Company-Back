package br.edu.ifpe.MarcaPasso3D.repository.Cupom;

import br.edu.ifpe.MarcaPasso3D.model.Cupom.Cupom;
import br.edu.ifpe.MarcaPasso3D.model.Cupom.TipoValidadeCupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {

    List<Cupom> findAllByOrderByIdDesc();

    Optional<Cupom> findByNomeCupomIgnoreCase(String nomeCupom);

    boolean existsByNomeCupomIgnoreCase(String nomeCupom);

    List<Cupom> findByTipoValidadeAndDataExpiracaoLessThanEqual(TipoValidadeCupom tipoValidade, LocalDate dataExpiracao);
}
