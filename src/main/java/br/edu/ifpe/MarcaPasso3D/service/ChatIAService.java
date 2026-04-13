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

@Service // Marca essa classe como um serviço do Spring (camada de negócio)
public class ChatIAService {

    // Injeta o repositório de produtos para buscar do banco de dados
    @Autowired
    private ProdutoRepository produtoRepository;

    // Lê a chave da API do arquivo application.properties
    // Assim a chave fica segura no servidor, longe do frontend
    @Value("${ia.api.key}")
    private String iaApiKey;

    // ObjectMapper é a classe do Jackson (biblioteca padrão do Spring Boot)
    // que converte objetos Java para JSON e JSON para objetos Java
    private final ObjectMapper objectMapper = new ObjectMapper();

    // RestTemplate é a classe do Spring para fazer requisições HTTP para outras APIs
    // (assim o backend chama a API da IA em nome do frontend)
    private final RestTemplate restTemplate = new RestTemplate();

    // Método principal: recebe a mensagem do usuário e retorna a ação processada
    public ChatIAResponseDTO processar(String mensagemUsuario) {
        try {
            // 1. Buscar todos os produtos do banco de dados
            List<Produto> produtos = produtoRepository.findAll();

            // 2. Converter os produtos para JSON (para incluir no prompt da IA)
            String produtosJson = objectMapper.writeValueAsString(
                produtos.stream().map(p -> {
                    // Cria um mapa simples com só os dados relevantes para a IA
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

            // 3. Montar o prompt completo que será enviado para a IA
            String prompt = montarPrompt(mensagemUsuario, produtosJson);

            // 4. Chamar a API da IA e obter a resposta bruta (texto JSON)
            String respostaIa = chamarApiIA(prompt);

            // 5. Converter o texto JSON da IA para o nosso DTO de resposta
            return interpretarResposta(respostaIa);

        } catch (Exception e) {
            // Se qualquer coisa falhar, retornar uma resposta de erro amigável
            ChatIAResponseDTO erro = new ChatIAResponseDTO();
            erro.setAcao("chat");
            erro.setMensagem("Desculpe, ocorreu um erro interno. Tente novamente em instantes.");
            return erro;
        }
    }

    // Monta o texto (prompt) que instrui a IA sobre o que fazer e como responder
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

    // Faz a chamada HTTP para a API da IA externa e retorna o texto da resposta
    private String chamarApiIA(String prompt) {
        // Define os cabeçalhos da requisição
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // A chave da API fica aqui no backend, nunca exposta ao frontend
        headers.setBearerAuth(iaApiKey);

        // Corpo da requisição: { "message": "prompt aqui" }
        Map<String, String> corpo = new HashMap<>();
        corpo.put("message", prompt);

        // Cria a requisição completa (cabeçalhos + corpo)
        HttpEntity<Map<String, String>> requisicao = new HttpEntity<>(corpo, headers);

        // Faz o POST para a API da IA e obtém a resposta
        ResponseEntity<String> resposta = restTemplate.exchange(
            "https://apifreellm.com/api/v1/chat", // URL da API da IA
            HttpMethod.POST,
            requisicao,
            String.class
        );

        // A resposta da API vem no formato:
        // { "success": true, "response": "{ json da IA aqui }" }
        // Precisamos extrair o campo "response" que contém o JSON da ação
        try {
            JsonNode json = objectMapper.readTree(resposta.getBody());
            return json.get("response").asText();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler resposta da API da IA");
        }
    }

    // Converte o texto JSON retornado pela IA para o nosso DTO ChatIAResponseDTO
    private ChatIAResponseDTO interpretarResposta(String respostaIaTexto) {
        try {
            // Remove possíveis blocos de markdown (```json ... ```) que a IA pode incluir
            String textoLimpo = respostaIaTexto
                .replaceAll("(?i)^```json\\s*", "")
                .replaceAll("```\\s*$", "")
                .trim();

            // Converte o JSON para o nosso DTO
            // O Jackson automaticamente preenche os campos (acao, mensagem, id, filtros)
            JsonNode node = objectMapper.readTree(textoLimpo);

            ChatIAResponseDTO dto = new ChatIAResponseDTO();
            dto.setAcao(node.path("acao").asText("chat"));
            dto.setMensagem(node.path("mensagem").asText("Como posso ajudar?"));

            // Preenche o ID do produto (só para ações "produto" e "favoritar")
            if (node.has("id")) {
                dto.setId(node.get("id").asText());
            }

            // Preenche os filtros (só para a ação "filtrar")
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
            // Se a IA retornar algo que não conseguimos parsear, exibimos erro amigável
            ChatIAResponseDTO fallback = new ChatIAResponseDTO();
            fallback.setAcao("chat");
            fallback.setMensagem("Não entendi bem sua solicitação. Pode reformular?");
            return fallback;
        }
    }
}