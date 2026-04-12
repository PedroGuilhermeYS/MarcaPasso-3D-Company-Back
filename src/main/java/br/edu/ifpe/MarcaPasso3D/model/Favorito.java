package br.edu.ifpe.MarcaPasso3D.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "favoritos")
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorito")
    private Long id;

    @Column(name = "id_usuario", nullable = false, unique = true)
    private Long idUsuario;

    @ManyToMany
    @JoinTable(
        name = "favorito_produtos",
        joinColumns = @JoinColumn(name = "id_favorito"),
        inverseJoinColumns = @JoinColumn(name = "id_produto")
    )
    private List<Produto> produtos = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
}