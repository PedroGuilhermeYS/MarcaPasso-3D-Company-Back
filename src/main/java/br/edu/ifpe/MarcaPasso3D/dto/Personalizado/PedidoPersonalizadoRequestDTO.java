package br.edu.ifpe.MarcaPasso3D.dto.Personalizado;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class PedidoPersonalizadoRequestDTO {

    // ── Sobre o pedido ───────────────────────────────────────

    @NotBlank(message = "Nome do pedido é obrigatório")
    private String nomePedido;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    private String finalidade;
    private String tamanho;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade mínima é 1")
    private Integer quantidade;

    private String cores;

    // ── Fotos (URLs já enviadas ao Supabase pelo frontend) ───

    @Size(max = 5, message = "Máximo de 5 fotos")
    private List<String> fotosReferencia;

    // ── Contato e prazo ──────────────────────────────────────

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCliente;

    @NotBlank(message = "WhatsApp é obrigatório")
    private String whatsapp;

    @Min(value = 10, message = "Prazo mínimo é de 10 dias")
    private Integer prazoDesejadoDias;

    // ── Getters & Setters ────────────────────────────────────

    public String getNomePedido() { return nomePedido; }
    public void setNomePedido(String nomePedido) { this.nomePedido = nomePedido; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getFinalidade() { return finalidade; }
    public void setFinalidade(String finalidade) { this.finalidade = finalidade; }

    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getCores() { return cores; }
    public void setCores(String cores) { this.cores = cores; }

    public List<String> getFotosReferencia() { return fotosReferencia; }
    public void setFotosReferencia(List<String> fotosReferencia) { this.fotosReferencia = fotosReferencia; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }

    public Integer getPrazoDesejadoDias() { return prazoDesejadoDias; }
    public void setPrazoDesejadoDias(Integer prazoDesejadoDias) { this.prazoDesejadoDias = prazoDesejadoDias; }
}
