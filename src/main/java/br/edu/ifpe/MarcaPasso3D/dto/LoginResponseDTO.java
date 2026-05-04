package br.edu.ifpe.MarcaPasso3D.dto;

public class LoginResponseDTO {

    private boolean ok;
    private String token;
    private Long id;
    private String email;
    private String role;
    private String nome;
    private String cpf;
    private String telefone;

    public LoginResponseDTO(String token, Long id, String email, String role, String nome, String cpf, String telefone) {
        this.ok = true;
        this.token = token;
        this.id = id;
        this.email = email;
        this.role = role;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public boolean isOk() { return ok; }
    public String getToken() { return token; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
}