package br.edu.ifpe.MarcaPasso3D.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "resumo_ia", columnDefinition = "TEXT")
    private String resumoIA;

    @Column(name = "preco", nullable = false)
    private BigDecimal preco;

    @Column(name = "imagem_principal")
    private String imagemPrincipal;

    @Column(name = "personalizavel")
    private Boolean personalizavel;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "material")
    private String material;

    @Column(name = "estoque")
    private Integer estoque;

    @Column(name = "total_vendas")
    private Integer totalVendas = 0;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getResumoIA() { return resumoIA; }
    public void setResumoIA(String resumoIA) { this.resumoIA = resumoIA; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public String getImagemPrincipal() { return imagemPrincipal; }
    public void setImagemPrincipal(String imagemPrincipal) { this.imagemPrincipal = imagemPrincipal; }

    public Boolean getPersonalizavel() { return personalizavel; }
    public void setPersonalizavel(Boolean personalizavel) { this.personalizavel = personalizavel; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }

    public Integer getTotalVendas() { return totalVendas; }
    public void setTotalVendas(Integer totalVendas) { this.totalVendas = totalVendas; }
}