package br.edu.ifpe.MarcaPasso3D.dto.MercadoPago;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO com os dados necessários para criar uma Preference no Mercado Pago.
 * O frontend envia este objeto para o backend, que chama a API do MP.
 */
public class MercadoPagoPreferenceRequestDTO {

    private List<ItemDTO> itens;
    private BigDecimal frete;
    private BigDecimal descontoCupom;
    private String formaPagamento;  // "pix" | "cartao"
    private String clienteNome;
    private String clienteEmail;
    private String clienteCpf;

    // ── ItemDTO aninhado ──────────────────────────────────────
    public static class ItemDTO {
        private String nome;
        private Integer quantidade;
        private BigDecimal precoUnitario;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }

        public Integer getQuantidade() { return quantidade; }
        public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

        public BigDecimal getPrecoUnitario() { return precoUnitario; }
        public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
    }

    // ── Getters & Setters ─────────────────────────────────────
    public List<ItemDTO> getItens() { return itens; }
    public void setItens(List<ItemDTO> itens) { this.itens = itens; }

    public BigDecimal getFrete() { return frete; }
    public void setFrete(BigDecimal frete) { this.frete = frete; }

    public BigDecimal getDescontoCupom() { return descontoCupom; }
    public void setDescontoCupom(BigDecimal descontoCupom) { this.descontoCupom = descontoCupom; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }

    public String getClienteEmail() { return clienteEmail; }
    public void setClienteEmail(String clienteEmail) { this.clienteEmail = clienteEmail; }

    public String getClienteCpf() { return clienteCpf; }
    public void setClienteCpf(String clienteCpf) { this.clienteCpf = clienteCpf; }
}
