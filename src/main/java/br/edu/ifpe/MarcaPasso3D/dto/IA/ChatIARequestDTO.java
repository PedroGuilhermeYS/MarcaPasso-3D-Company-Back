package br.edu.ifpe.MarcaPasso3D.dto.IA;

// DTO = Data Transfer Object
// Essa classe representa o corpo (body) da requisição que chega do frontend.
// O frontend envia: { "mensagem": "quero ver produtos de decoração" }
public class ChatIARequestDTO {

    // A mensagem digitada pelo usuário no chat
    private String mensagem;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}