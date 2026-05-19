package br.edu.ifpe.MarcaPasso3D.dto.Cupom;

import br.edu.ifpe.MarcaPasso3D.model.Cupom.TipoValidadeCupom;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CupomResponseDTO {

    private Long id;
    private String nomeCupom;
    private BigDecimal valorDesconto;
    private TipoValidadeCupom tipoValidade;
    private LocalDate dataExpiracao;
    private boolean expirado;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public CupomResponseDTO(Long id, String nomeCupom, BigDecimal valorDesconto, TipoValidadeCupom tipoValidade,
                            LocalDate dataExpiracao, boolean expirado, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nomeCupom = nomeCupom;
        this.valorDesconto = valorDesconto;
        this.tipoValidade = tipoValidade;
        this.dataExpiracao = dataExpiracao;
        this.expirado = expirado;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Long getId() {
        return id;
    }

    public String getNomeCupom() {
        return nomeCupom;
    }

    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }

    public TipoValidadeCupom getTipoValidade() {
        return tipoValidade;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public boolean isExpirado() {
        return expirado;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
