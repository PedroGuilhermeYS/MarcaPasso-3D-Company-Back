package br.edu.ifpe.MarcaPasso3D.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.HashMap;

@Service
public class TelegramService {

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.chat-id}")
    private String chatId;

    private final RestClient restClient = RestClient.create();

    /**
     * Envia apenas texto (HTML).
     */
    public void enviarMensagem(String texto) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";

        Map<String, String> body = Map.of(
            "chat_id",    chatId,
            "text",       texto,
            "parse_mode", "HTML"
        );

        try {
            restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();

            System.out.println(">>> [Telegram] Mensagem enviada com sucesso!");
        } catch (Exception e) {
            System.err.println(">>> [Telegram] Erro ao enviar mensagem: " + e.getMessage());
        }
    }

    /**
     * Envia uma foto (URL pública) com legenda em HTML.
     * Se a URL da foto for nula/vazia, cai para enviarMensagem normal.
     */
    public void enviarMensagemComFoto(String fotoUrl, String caption) {
        if (fotoUrl == null || fotoUrl.isBlank()) {
            enviarMensagem(caption);
            return;
        }

        String url = "https://api.telegram.org/bot" + botToken + "/sendPhoto";

        Map<String, String> body = new HashMap<>();
        body.put("chat_id",    chatId);
        body.put("photo",      fotoUrl);
        body.put("caption",    caption);
        body.put("parse_mode", "HTML");

        try {
            restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();

            System.out.println(">>> [Telegram] Mensagem com foto enviada com sucesso!");
        } catch (Exception e) {
            // Se a foto falhar (URL inválida, etc), tenta mandar só o texto
            System.err.println(">>> [Telegram] Erro ao enviar foto, tentando só texto: " + e.getMessage());
            enviarMensagem(caption);
        }
    }
}