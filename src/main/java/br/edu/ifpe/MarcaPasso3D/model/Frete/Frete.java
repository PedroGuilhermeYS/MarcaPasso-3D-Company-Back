package br.edu.ifpe.MarcaPasso3D.model.Frete;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ceps")
public class Frete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cep")
    private Long id;

    @Column(name = "cep", nullable = false, unique = true, length = 9)
    private String cep;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "prazo_entrega_dias", nullable = false)
    private Integer prazoEntregaDias;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getPrazoEntregaDias() {
        return prazoEntregaDias;
    }

    public void setPrazoEntregaDias(Integer prazoEntregaDias) {
        this.prazoEntregaDias = prazoEntregaDias;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}