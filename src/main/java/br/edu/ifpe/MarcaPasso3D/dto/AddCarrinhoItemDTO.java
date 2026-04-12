package br.edu.ifpe.MarcaPasso3D.dto;

import java.math.BigDecimal;

public class AddCarrinhoItemDTO {

    private Long idProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;

    public Long getIdProduto() { return idProduto; }
    public void setIdProduto(Long idProduto) { this.idProduto = idProduto; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
}