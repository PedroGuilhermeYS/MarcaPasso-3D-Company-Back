package br.edu.ifpe.MarcaPasso3D.repository.Endereço;

import br.edu.ifpe.MarcaPasso3D.model.Endereço.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    List<Endereco> findByIdUsuario(Long idUsuario);
}