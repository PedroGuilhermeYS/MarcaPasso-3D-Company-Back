package br.edu.ifpe.MarcaPasso3D.repository.Carrinho;

import br.edu.ifpe.MarcaPasso3D.model.Carrinho.CarrinhoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoItemRepository extends JpaRepository<CarrinhoItem, Long> {
}