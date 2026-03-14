package br.edu.ifpe.MarcaPasso3D.dto;

import java.math.BigDecimal;

public class CarrinhoItemDTO {

    private Long idItem;
    private Long idProduto;
    private String nomeProduto;
    private String imagemPrincipal;
    private Integer quantidade;
    private BigDecimal precoUnitario;

    public CarrinhoItemDTO(Long idItem, Long idProduto, String nomeProduto,
                           String imagemPrincipal, Integer quantidade, BigDecimal precoUnitario) {
        this.idItem = idItem;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.imagemPrincipal = imagemPrincipal;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Long getIdItem() { return idItem; }
    public Long getIdProduto() { return idProduto; }
    public String getNomeProduto() { return nomeProduto; }
    public String getImagemPrincipal() { return imagemPrincipal; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
}