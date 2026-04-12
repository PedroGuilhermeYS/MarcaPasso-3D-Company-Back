package br.edu.ifpe.MarcaPasso3D.model.Encomenda;

import br.edu.ifpe.MarcaPasso3D.model.Produto;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "encomenda_itens")
public class EncomendaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encomenda", nullable = false)
    private Encomenda encomenda;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario", nullable = false)
    private BigDecimal precoUnitario;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Encomenda getEncomenda() { return encomenda; }
    public void setEncomenda(Encomenda encomenda) { this.encomenda = encomenda; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
}
