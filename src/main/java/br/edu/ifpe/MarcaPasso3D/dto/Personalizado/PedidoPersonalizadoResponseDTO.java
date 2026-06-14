package br.edu.ifpe.MarcaPasso3D.dto.Personalizado;

import br.edu.ifpe.MarcaPasso3D.model.Personalizado.StatusPedidoPersonalizado;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoPersonalizadoResponseDTO {

    private Long id;
    private Long idUsuario;

    // ── Sobre o pedido ───────────────────────────────────────
    private String nomePedido;
    private String descricao;
    private String finalidade;
    private String tamanho;
    private Integer quantidade;
    private String cores;
    private List<String> fotosReferencia;

    // ── Contato e prazo ──────────────────────────────────────
    private String nomeCliente;
    private String whatsapp;
    private Integer prazoDesejadoDias;

    // ── Controle ─────────────────────────────────────────────
    private StatusPedidoPersonalizado status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    // ── Getters & Setters ────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

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

    public StatusPedidoPersonalizado getStatus() { return status; }
    public void setStatus(StatusPedidoPersonalizado status) { this.status = status; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}
