package br.edu.ifpe.MarcaPasso3D.dto;

import br.edu.ifpe.MarcaPasso3D.model.Usuario;
import java.time.LocalDateTime;

public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private Usuario.Role role;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public UsuarioResponseDTO(Usuario u) {
        this.id = u.getId();
        this.nome = u.getNome();
        this.email = u.getEmail();
        this.telefone = u.getTelefone();
        this.cpf = u.getCpf();
        this.role = u.getRole();
        this.ativo = u.getAtivo();
        this.criadoEm = u.getCriadoEm();
        this.atualizadoEm = u.getAtualizadoEm();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getCpf() { return cpf; }
    public Usuario.Role getRole() { return role; }
    public Boolean getAtivo() { return ativo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}