package br.edu.ifpe.MarcaPasso3D.dto.Encomenda;

import java.math.BigDecimal;
import java.util.List;

public class CriarEncomendaDTO {

    // Itens do pedido
    private List<ItemDTO> itens;

    // Valores
    private BigDecimal subtotal;
    private BigDecimal frete;
    private BigDecimal desconto;
    private BigDecimal descontoCupom;
    private BigDecimal total;

    // Pagamento
    private String formaPagamento;       // "pix" | "cartao"
    private String tipoPagamento;        // "débito" | "crédito"
    private String bandeiraCartao;
    private String parcelamento;
    private String titularCartao;
    private String cpfTitular;
    private String binCartao;

    // Endereço de entrega (snapshot)
    private String endRua;
    private String endNumero;
    private String endComplemento;
    private String endBairro;
    private String endCidade;
    private String endEstado;
    private String endCep;

    // Dados do cliente (snapshot)
    private String clienteNome;
    private String clienteEmail;
    private String clienteCpf;

    // ── ItemDTO aninhado ─────────────────────────────────────
    public static class ItemDTO {
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

    // ── Getters & Setters ────────────────────────────────────
    public List<ItemDTO> getItens() { return itens; }
    public void setItens(List<ItemDTO> itens) { this.itens = itens; }

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
}
