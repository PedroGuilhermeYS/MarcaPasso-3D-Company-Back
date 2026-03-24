package br.edu.ifpe.MarcaPasso3D.repository.Usuario;

import br.edu.ifpe.MarcaPasso3D.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}