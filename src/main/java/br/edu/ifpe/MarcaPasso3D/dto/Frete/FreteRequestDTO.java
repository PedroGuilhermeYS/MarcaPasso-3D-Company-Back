package br.edu.ifpe.MarcaPasso3D.dto.Frete;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class FreteRequestDTO {

    @NotBlank(message = "O CEP é obrigatório")
    @JsonProperty("cep_entrega")
    private String cepEntrega;

    @NotBlank(message = "A cidade é obrigatória")
    private String cidade;

    @NotNull(message = "O preço é obrigatório")
    @PositiveOrZero(message = "O preço não pode ser negativo")
    @JsonProperty("valor_frete")
    private BigDecimal valorFrete;

    @NotNull(message = "O prazo de entrega é obrigatório")
    @Positive(message = "O prazo de entrega deve ser maior que zero")
    @JsonProperty("prazo_entrega_dias")
    private Integer prazoEntregaDias;

    public String getCepEntrega() {
        return cepEntrega;
    }

    public void setCepEntrega(String cepEntrega) {
        this.cepEntrega = cepEntrega;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public Integer getPrazoEntregaDias() {
        return prazoEntregaDias;
    }

    public void setPrazoEntregaDias(Integer prazoEntregaDias) {
        this.prazoEntregaDias = prazoEntregaDias;
    }
}