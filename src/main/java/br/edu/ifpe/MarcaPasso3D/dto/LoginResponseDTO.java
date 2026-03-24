package br.edu.ifpe.MarcaPasso3D.dto;

public class LoginResponseDTO {

    private boolean ok;
    private String token;
    private Long id;
    private String email;
    private String role;
    private String nome;

    public LoginResponseDTO(String token, Long id, String email, String role, String nome) {
        this.ok = true;
        this.token = token;
        this.id = id;
        this.email = email;
        this.role = role;
        this.nome = nome;
    }

    public boolean isOk() { return ok; }
    public String getToken() { return token; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getNome() { return nome; }
}