package br.edu.ifpe.MarcaPasso3D.dto.IA;

import java.math.BigDecimal;

public class ResumoProdutoRequestDTO {

    private String nome;
    private String descricao;
    private String categoria;
    private String material;
    private BigDecimal preco;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
}