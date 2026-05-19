package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.dto.IA.ChatIARequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.IA.ChatIAResponseDTO;
import br.edu.ifpe.MarcaPasso3D.dto.IA.ResumoProdutoRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.IA.ResumoProdutoResponseDTO;
import br.edu.ifpe.MarcaPasso3D.service.ChatIAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat-ia")
public class ChatIAController {

    @Autowired
    private ChatIAService chatIAService;

    @PostMapping
    public ResponseEntity<ChatIAResponseDTO> chat(@RequestBody ChatIARequestDTO request) {
        ChatIAResponseDTO resposta = chatIAService.processar(request.getMensagem());
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/resumo-produto")
    public ResponseEntity<ResumoProdutoResponseDTO> resumirProduto(
            @RequestBody ResumoProdutoRequestDTO request) {
        ResumoProdutoResponseDTO resposta = chatIAService.gerarResumoProduto(request);
        return ResponseEntity.ok(resposta);
    }
}