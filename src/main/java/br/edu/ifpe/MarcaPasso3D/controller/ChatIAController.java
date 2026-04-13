package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.dto.IA.ChatIARequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.IA.ChatIAResponseDTO;
import br.edu.ifpe.MarcaPasso3D.service.ChatIAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController → essa classe é um controlador REST (responde com JSON)
// @RequestMapping → define que todas as rotas aqui começam com /chat-ia
@RestController
@RequestMapping("/chat-ia")
public class ChatIAController {

    // Injeta o serviço que contém toda a lógica de negócio
    @Autowired
    private ChatIAService chatIAService;

    // Endpoint: POST /chat-ia
    // O frontend chama esse endpoint enviando a mensagem do usuário
    // e recebe de volta a ação que deve executar (filtrar, abrir produto, etc.)
    @PostMapping
    public ResponseEntity<ChatIAResponseDTO> chat(@RequestBody ChatIARequestDTO request) {

        // Delega o processamento para o serviço
        ChatIAResponseDTO resposta = chatIAService.processar(request.getMensagem());

        // Retorna HTTP 200 com o JSON da resposta
        return ResponseEntity.ok(resposta);
    }
}