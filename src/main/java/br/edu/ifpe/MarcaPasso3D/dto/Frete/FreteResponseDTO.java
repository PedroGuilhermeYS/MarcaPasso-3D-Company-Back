package br.edu.ifpe.MarcaPasso3D.dto.Frete;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class FreteResponseDTO {

    private Long id;

    private String cidade;

    // 55535000
    @JsonProperty("cep_destino")
    private String cepDestino;

    // 55535-000 -> vai mostrar assim
    @JsonProperty("cep_entrega")
    private String cepEntrega;

    @JsonProperty("valor_frete")
    private BigDecimal valorFrete;

    @JsonProperty("prazo_entrega_dias")
    private Integer prazoEntregaDias;

    public FreteResponseDTO(Long id, String cidade, String cepDestino, String cepEntrega, BigDecimal valorFrete, Integer prazoEntregaDias) {
        this.id = id;
        this.cidade = cidade;
        this.cepDestino = cepDestino;
        this.cepEntrega = cepEntrega;
        this.valorFrete = valorFrete;
        this.prazoEntregaDias = prazoEntregaDias;
    }

    public Long getId() {
        return id;
    }

    public String getCidade() {
        return cidade;
    }

    public String getCepDestino() {
        return cepDestino;
    }

    public String getCepEntrega() {
        return cepEntrega;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public Integer getPrazoEntregaDias() {
        return prazoEntregaDias;
    }
}