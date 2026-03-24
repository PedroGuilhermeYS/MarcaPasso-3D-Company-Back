package br.edu.ifpe.MarcaPasso3D.dto;

public class LoginResponseDTO {

    private boolean ok;
    private String token;
    private UserPayload user;

    public LoginResponseDTO(String token, Long id, String email, String role, String nome) {
        this.ok = true;
        this.token = token;
        this.user = new UserPayload(id, email, role, nome);
    }

    public boolean isOk() { return ok; }
    public String getToken() { return token; }
    public UserPayload getUser() { return user; }

    public static class UserPayload {
        private Long id;
        private String email;
        private String role;
        private String nome;

        public UserPayload(Long id, String email, String role, String nome) {
            this.id = id;
            this.email = email;
            this.role = role;
            this.nome = nome;
        }

        public Long getId() { return id; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getNome() { return nome; }
    }
}