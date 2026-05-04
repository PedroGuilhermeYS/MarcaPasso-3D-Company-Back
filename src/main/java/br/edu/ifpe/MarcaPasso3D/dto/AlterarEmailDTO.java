package br.edu.ifpe.MarcaPasso3D.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AlterarEmailDTO {

    @NotBlank(message = "Novo e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String novoEmail;

    @NotBlank(message = "Senha é obrigatória para confirmar a alteração")
    private String senha;

    public String getNovoEmail() { return novoEmail; }
    public void setNovoEmail(String novoEmail) { this.novoEmail = novoEmail; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}