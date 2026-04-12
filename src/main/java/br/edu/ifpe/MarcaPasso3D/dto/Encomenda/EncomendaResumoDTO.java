package br.edu.ifpe.MarcaPasso3D.dto.Encomenda;

import br.edu.ifpe.MarcaPasso3D.model.Encomenda.StatusEncomenda;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EncomendaResumoDTO {

    private Long id;
    private String numeroPedido;
    private LocalDateTime dataHora;
    private String formaPagamento;
    private StatusEncomenda status;
    private BigDecimal total;

    public EncomendaResumoDTO(Long id, String numeroPedido, LocalDateTime dataHora,
                               String formaPagamento, StatusEncomenda status, BigDecimal total) {
        this.id = id;
        this.numeroPedido = numeroPedido;
        this.dataHora = dataHora;
        this.formaPagamento = formaPagamento;
        this.status = status;
        this.total = total;
    }

    public Long getId() { return id; }
    public String getNumeroPedido() { return numeroPedido; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getFormaPagamento() { return formaPagamento; }
    public StatusEncomenda getStatus() { return status; }
    public BigDecimal getTotal() { return total; }
}
