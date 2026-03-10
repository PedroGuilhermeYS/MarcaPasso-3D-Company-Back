package br.edu.ifpe.MarcaPasso3D.repository.Produto;

import br.edu.ifpe.MarcaPasso3D.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Page<Produto> findAll(Pageable pageable);

    List<Produto> findAllByOrderByNomeAsc();

    List<Produto> findAllByOrderByPrecoAsc();

    List<Produto> findTop5ByOrderByIdDesc();

    List<Produto> findByCategoria(String categoria);
}