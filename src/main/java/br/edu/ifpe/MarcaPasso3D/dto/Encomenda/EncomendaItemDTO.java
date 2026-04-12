package br.edu.ifpe.MarcaPasso3D.dto.Encomenda;

import java.math.BigDecimal;

public class EncomendaItemDTO {

    private Long idProduto;
    private String nomeProduto;
    private String imagemPrincipal;
    private Integer quantidade;
    private BigDecimal precoUnitario;

    public EncomendaItemDTO(Long idProduto, String nomeProduto, String imagemPrincipal,
                             Integer quantidade, BigDecimal precoUnitario) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.imagemPrincipal = imagemPrincipal;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Long getIdProduto() { return idProduto; }
    public String getNomeProduto() { return nomeProduto; }
    public String getImagemPrincipal() { return imagemPrincipal; }
    public Integer getQuantidade() { return quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
}
