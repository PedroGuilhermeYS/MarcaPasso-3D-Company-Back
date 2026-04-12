package br.edu.ifpe.MarcaPasso3D.model.Encomenda;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "encomendas")
public class Encomenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_encomenda")
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "numero_pedido", nullable = false, unique = true)
    private String numeroPedido;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEncomenda status = StatusEncomenda.PENDENTE;

    // ── Valores ──────────────────────────────────────────────
    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    @Column(name = "frete", nullable = false)
    private BigDecimal frete;

    @Column(name = "desconto", nullable = false)
    private BigDecimal desconto;

    @Column(name = "desconto_cupom", nullable = false)
    private BigDecimal descontoCupom;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    // ── Forma de Pagamento ────────────────────────────────────
    @Column(name = "forma_pagamento", nullable = false)
    private String formaPagamento;          // "pix" | "cartao"

    @Column(name = "tipo_pagamento")
    private String tipoPagamento;           // "débito" | "crédito"

    @Column(name = "bandeira_cartao")
    private String bandeiraCartao;

    @Column(name = "parcelamento")
    private String parcelamento;

    @Column(name = "titular_cartao")
    private String titularCartao;

    @Column(name = "cpf_titular")
    private String cpfTitular;

    @Column(name = "bin_cartao")
    private String binCartao;

    // ── Endereço (snapshot no momento do pedido) ──────────────
    @Column(name = "end_rua")
    private String endRua;

    @Column(name = "end_numero")
    private String endNumero;

    @Column(name = "end_complemento")
    private String endComplemento;

    @Column(name = "end_bairro")
    private String endBairro;

    @Column(name = "end_cidade")
    private String endCidade;

    @Column(name = "end_estado")
    private String endEstado;

    @Column(name = "end_cep")
    private String endCep;

    // ── Dados do cliente (snapshot) ───────────────────────────
    @Column(name = "cliente_nome")
    private String clienteNome;

    @Column(name = "cliente_email")
    private String clienteEmail;

    @Column(name = "cliente_cpf")
    private String clienteCpf;

    // ── Itens ─────────────────────────────────────────────────
    @OneToMany(mappedBy = "encomenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EncomendaItem> itens = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.dataHora = LocalDateTime.now();
    }

    // ── Getters & Setters ────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public StatusEncomenda getStatus() { return status; }
    public void setStatus(StatusEncomenda status) { this.status = status; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getFrete() { return frete; }
    public void setFrete(BigDecimal frete) { this.frete = frete; }

    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

    public BigDecimal getDescontoCupom() { return descontoCupom; }
    public void setDescontoCupom(BigDecimal descontoCupom) { this.descontoCupom = descontoCupom; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public String getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(String tipoPagamento) { this.tipoPagamento = tipoPagamento; }

    public String getBandeiraCartao() { return bandeiraCartao; }
    public void setBandeiraCartao(String bandeiraCartao) { this.bandeiraCartao = bandeiraCartao; }

    public String getParcelamento() { return parcelamento; }
    public void setParcelamento(String parcelamento) { this.parcelamento = parcelamento; }

    public String getTitularCartao() { return titularCartao; }
    public void setTitularCartao(String titularCartao) { this.titularCartao = titularCartao; }

    public String getCpfTitular() { return cpfTitular; }
    public void setCpfTitular(String cpfTitular) { this.cpfTitular = cpfTitular; }

    public String getBinCartao() { return binCartao; }
    public void setBinCartao(String binCartao) { this.binCartao = binCartao; }

    public String getEndRua() { return endRua; }
    public void setEndRua(String endRua) { this.endRua = endRua; }

    public String getEndNumero() { return endNumero; }
    public void setEndNumero(String endNumero) { this.endNumero = endNumero; }

    public String getEndComplemento() { return endComplemento; }
    public void setEndComplemento(String endComplemento) { this.endComplemento = endComplemento; }

    public String getEndBairro() { return endBairro; }
    public void setEndBairro(String endBairro) { this.endBairro = endBairro; }

    public String getEndCidade() { return endCidade; }
    public void setEndCidade(String endCidade) { this.endCidade = endCidade; }

    public String getEndEstado() { return endEstado; }
    public void setEndEstado(String endEstado) { this.endEstado = endEstado; }

    public String getEndCep() { return endCep; }
    public void setEndCep(String endCep) { this.endCep = endCep; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public String getClienteCpf() { return clienteCpf; }
    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }

    public List<EncomendaItem> getItens() { return itens; }
    public void setItens(List<EncomendaItem> itens) { this.itens = itens; }
}
