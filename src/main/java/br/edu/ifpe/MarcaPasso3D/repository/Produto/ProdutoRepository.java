package br.edu.ifpe.MarcaPasso3D.repository.Produto;

import br.edu.ifpe.MarcaPasso3D.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Page<Produto> findAll(Pageable pageable);

    List<Produto> findAllByOrderByNomeAsc();

    List<Produto> findAllByOrderByPrecoAsc();

    List<Produto> findByCategoria(String categoria);

    List<Produto> findTop8ByOrderByTotalVendasDesc();

    @Query("""
        SELECT p FROM Produto p
        WHERE LOWER(p.nome)      LIKE LOWER(CONCAT('%', :termo, '%'))
           OR LOWER(p.categoria) LIKE LOWER(CONCAT('%', :termo, '%'))
        ORDER BY p.totalVendas DESC
    """)
    List<Produto> findByTermoParaCarrossel(@Param("termo") String termo, Pageable pageable);
}