package br.edu.ifpe.MarcaPasso3D.repository.Carrinho;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.MarcaPasso3D.model.Carrinho.Carrinho;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Optional<Carrinho> findByIdUsuario(Long idUsuario);
} 