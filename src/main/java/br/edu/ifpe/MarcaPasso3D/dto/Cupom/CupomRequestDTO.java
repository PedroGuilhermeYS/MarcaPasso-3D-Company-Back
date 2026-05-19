package br.edu.ifpe.MarcaPasso3D.dto.Cupom;

import br.edu.ifpe.MarcaPasso3D.model.Cupom.TipoValidadeCupom;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CupomRequestDTO {

    @NotBlank(message = "O nome do cupom é obrigatório")
    private String nomeCupom;

    @NotNull(message = "O valor de desconto é obrigatório")
    @Positive(message = "O valor de desconto deve ser maior que zero")
    private BigDecimal valorDesconto;

    @NotNull(message = "O tipo de validade é obrigatório")
    private TipoValidadeCupom tipoValidade;

    private LocalDate dataExpiracao;

    public String getNomeCupom() {
        return nomeCupom;
    }

    public void setNomeCupom(String nomeCupom) {
        this.nomeCupom = nomeCupom;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public TipoValidadeCupom getTipoValidade() {
        return tipoValidade;
    }

    public void setTipoValidade(TipoValidadeCupom tipoValidade) {
        this.tipoValidade = tipoValidade;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDate dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
}
