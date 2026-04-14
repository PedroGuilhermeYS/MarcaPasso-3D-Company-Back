package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.IA.ChatIAFiltrosDTO;
import br.edu.ifpe.MarcaPasso3D.dto.IA.ChatIAResponseDTO;
import br.edu.ifpe.MarcaPasso3D.model.Produto;
import br.edu.ifpe.MarcaPasso3D.repository.Produto.ProdutoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatIAService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Value("${ia.api.key}")
    private String iaApiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate = new RestTemplate();

    public ChatIAResponseDTO processar(String mensagemUsuario) {
        try {

            List<Produto> produtos = produtoRepository.findAll();

            String produtosJson = objectMapper.writeValueAsString(
                produtos.stream().map(p -> {
                    // mapa com dados relevantes para a IA
                    Map<String, Object> dados = new HashMap<>();
                    dados.put("id", p.getId());
                    dados.put("nome", p.getNome());
                    dados.put("categoria", p.getCategoria());
                    dados.put("preco", p.getPreco());
                    dados.put("personalizavel", p.getPersonalizavel());
                    dados.put("descricao", p.getDescricao());
                    return dados;
                }).toList()
            );

            String prompt = montarPrompt(mensagemUsuario, produtosJson);

            // Chamar a API da IA
            String respostaIa = chamarApiIA(prompt);

            return interpretarResposta(respostaIa);

        } catch (Exception e) {
            ChatIAResponseDTO erro = new ChatIAResponseDTO();
            erro.setAcao("chat");
            erro.setMensagem("Desculpe, ocorreu um erro interno. Tente novamente em instantes.");
            return erro;
        }
    }

    // prompt
    private String montarPrompt(String mensagemUsuario, String produtosJson) {
        return """
            Você é um assistente virtual da loja MarcaPasso3D, especializada em produtos impressos em 3D.
            Responda SOMENTE com um objeto JSON válido. Sem texto fora do JSON, sem markdown, sem explicações.

            Produtos cadastrados na loja:
            """ + produtosJson + """

            Filtros disponíveis no sistema:
            - categoria: "Decoração" | "Colecionáveis" | "Acessórios" | "" (todos)
            - personalizavel: "true" | "false" | "" (todos)
            - preco: "1" (até R$50) | "2" (R$50–R$100) | "3" (acima R$100) | "" (todos)
            - termo: busca por nome do produto | ""
            - ordenacao: "az" | "menor" | "maior" | ""

            Formatos de resposta aceitos:

            Filtrar produtos:
            {"acao":"filtrar","filtros":{"categoria":"","personalizavel":"","preco":"","termo":"","ordenacao":""},"mensagem":"texto amigável"}

            Abrir produto específico:
            {"acao":"produto","id":"ID_DO_PRODUTO","mensagem":"texto amigável"}

            Favoritar produto:
            {"acao":"favoritar","id":"ID_DO_PRODUTO","mensagem":"texto amigável"}

            Nenhum resultado:
            {"acao":"sem_resultado","mensagem":"texto explicando e sugerindo contato pelo WhatsApp"}

            Conversa geral:
            {"acao":"chat","mensagem":"resposta ao usuário"}

            Regras:
            - Pedido de ver/mostrar/listar produtos → "filtrar"
            - Produto específico mencionado que existe na lista → "produto" com ID exato
            - Pedido de favoritar → "favoritar" com ID exato
            - Produto não encontrado → "sem_resultado"
            - Dúvidas gerais → "chat"
            - Responda sempre em português brasileiro

            Mensagem do cliente: """ + mensagemUsuario;
    }

    // chamada HTTP
    private String chamarApiIA(String prompt) {
       
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
       
        headers.setBearerAuth(iaApiKey);

        Map<String, String> corpo = new HashMap<>();
        corpo.put("message", prompt);

        HttpEntity<Map<String, String>> requisicao = new HttpEntity<>(corpo, headers);

        ResponseEntity<String> resposta = restTemplate.exchange(
            "https://apifreellm.com/api/v1/chat",
            HttpMethod.POST,
            requisicao,
            String.class
        );

        // A resposta da API vem no formato:
        // { "success": true, "response": "{ json da IA aqui }" }
        // extrair o campo "response" que contém o JSON da ação
        try {
            JsonNode json = objectMapper.readTree(resposta.getBody());
            return json.get("response").asText();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler resposta da API da IA");
        }
    }

    // Converte o texto JSON
    private ChatIAResponseDTO interpretarResposta(String respostaIaTexto) {
        try {
            String textoLimpo = respostaIaTexto
                .replaceAll("(?i)^```json\\s*", "")
                .replaceAll("```\\s*$", "")
                .trim();

            JsonNode node = objectMapper.readTree(textoLimpo);

            ChatIAResponseDTO dto = new ChatIAResponseDTO();
            dto.setAcao(node.path("acao").asText("chat"));
            dto.setMensagem(node.path("mensagem").asText("Como posso ajudar?"));

            // Preenche o ID do produto só para ações "produto" e "favoritar"
            if (node.has("id")) {
                dto.setId(node.get("id").asText());
            }

            // Preenche os filtros só para a ação "filtrar"
            if (node.has("filtros")) {
                JsonNode f = node.get("filtros");
                ChatIAFiltrosDTO filtros = new ChatIAFiltrosDTO();
                filtros.setCategoria(f.path("categoria").asText(""));
                filtros.setPersonalizavel(f.path("personalizavel").asText(""));
                filtros.setPreco(f.path("preco").asText(""));
                filtros.setTermo(f.path("termo").asText(""));
                filtros.setOrdenacao(f.path("ordenacao").asText(""));
                dto.setFiltros(filtros);
            }

            return dto;

        } catch (Exception e) {
            ChatIAResponseDTO fallback = new ChatIAResponseDTO();
            fallback.setAcao("chat");
            fallback.setMensagem("Não entendi bem sua solicitação. Pode reformular?");
            return fallback;
        }
    }
}