package br.edu.ifpe.MarcaPasso3D.model.Personalizado;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos_personalizados")
public class PedidoPersonalizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ── Vínculo com o usuário ────────────────────────────────

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    // ── Sobre o pedido ───────────────────────────────────────

    @Column(name = "nome_pedido", nullable = false)
    private String nomePedido;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "finalidade", columnDefinition = "TEXT")
    private String finalidade;

    @Column(name = "tamanho")
    private String tamanho;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade = 1;

    @Column(name = "cores")
    private String cores;

    // ── Fotos de referência (URLs do Supabase) ───────────────

    @ElementCollection
    @CollectionTable(
        name = "pedido_personalizado_fotos",
        joinColumns = @JoinColumn(name = "id_pedido")
    )
    @Column(name = "url_foto")
    private List<String> fotosReferencia = new ArrayList<>();

    // ── Contato e prazo ──────────────────────────────────────

    @Column(name = "nome_cliente", nullable = false)
    private String nomeCliente;

    @Column(name = "whatsapp", nullable = false)
    private String whatsapp;

    @Column(name = "prazo_desejado_dias")
    private Integer prazoDesejadoDias;

    // ── Controle ─────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusPedidoPersonalizado status = StatusPedidoPersonalizado.AGUARDANDO_ORCAMENTO;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    // ── Getters & Setters ────────────────────────────────────

    public Long getId() { return id; }

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
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}
