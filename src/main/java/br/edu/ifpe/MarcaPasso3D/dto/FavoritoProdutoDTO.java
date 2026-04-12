package br.edu.ifpe.MarcaPasso3D.dto;

import java.math.BigDecimal;

public class FavoritoProdutoDTO {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private String imagemPrincipal;
    private String categoria;

    public FavoritoProdutoDTO(Long id, String nome, BigDecimal preco, String imagemPrincipal, String categoria) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.imagemPrincipal = imagemPrincipal;
        this.categoria = categoria;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public String getImagemPrincipal() { return imagemPrincipal; }
    public String getCategoria() { return categoria; }
}