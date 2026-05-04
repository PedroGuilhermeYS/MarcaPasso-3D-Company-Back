package br.edu.ifpe.MarcaPasso3D.dto;

import jakarta.validation.constraints.NotBlank;

public class AtualizarPerfilDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String telefone;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}