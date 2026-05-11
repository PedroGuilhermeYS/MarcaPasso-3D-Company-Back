package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.IA.ChatIAFiltrosDTO;
import br.edu.ifpe.MarcaPasso3D.dto.IA.ChatIAResponseDTO;
import br.edu.ifpe.MarcaPasso3D.dto.IA.ResumoProdutoRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.IA.ResumoProdutoResponseDTO;
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

            String prompt = montarPromptChat(mensagemUsuario, produtosJson);
            String respostaIa = chamarApiIA(prompt);
            return interpretarRespostaChat(respostaIa);

        } catch (Exception e) {
            ChatIAResponseDTO erro = new ChatIAResponseDTO();
            erro.setAcao("chat");
            erro.setMensagem("Desculpe, ocorreu um erro interno. Tente novamente em instantes.");
            return erro;
        }
    }

    private String montarPromptChat(String mensagemUsuario, String produtosJson) {
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

    private ChatIAResponseDTO interpretarRespostaChat(String respostaIaTexto) {
        try {
            String textoLimpo = respostaIaTexto
                    .replaceAll("(?i)^```json\\s*", "")
                    .replaceAll("```\\s*$", "")
                    .trim();

            JsonNode node = objectMapper.readTree(textoLimpo);

            ChatIAResponseDTO dto = new ChatIAResponseDTO();
            dto.setAcao(node.path("acao").asText("chat"));
            dto.setMensagem(node.path("mensagem").asText("Como posso ajudar?"));

            if (node.has("id")) {
                dto.setId(node.get("id").asText());
            }

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

    public ResumoProdutoResponseDTO gerarResumoProduto(ResumoProdutoRequestDTO dados) {
        try {
            String prompt = montarPromptResumo(dados);
            String respostaIa = chamarApiIA(prompt);

            String resumo = respostaIa
                    .replaceAll("(?i)^```[a-z]*\\s*", "")
                    .replaceAll("```\\s*$", "")
                    .trim();

            return new ResumoProdutoResponseDTO(resumo);

        } catch (Exception e) {
            return new ResumoProdutoResponseDTO(
                    "Não foi possível gerar o resumo no momento. Tente novamente em instantes."
            );
        }
    }

    private String montarPromptResumo(ResumoProdutoRequestDTO d) {
        StringBuilder sb = new StringBuilder();
        sb.append("Você é um copywriter especialista em e-commerce de produtos impressos em 3D.\n");
        sb.append("Crie um resumo comercial CURTO (máximo 3 frases) e atraente para o produto abaixo.\n");
        sb.append("Use linguagem clara, destaque os diferenciais e finalize com um convite à ação.\n");
        sb.append("Responda APENAS com o texto do resumo, sem introdução, sem aspas, sem JSON.\n\n");
        sb.append("Dados do produto:\n");

        if (d.getNome() != null)      sb.append("- Nome: ").append(d.getNome()).append("\n");
        if (d.getCategoria() != null) sb.append("- Categoria: ").append(d.getCategoria()).append("\n");
        if (d.getMaterial() != null)  sb.append("- Material: ").append(d.getMaterial()).append("\n");
        if (d.getPreco() != null)     sb.append("- Preço: R$ ").append(d.getPreco()).append("\n");
        if (d.getDescricao() != null) sb.append("- Descrição técnica: ").append(d.getDescricao()).append("\n");

        return sb.toString();
    }

    // Chamada HTTP compartilhada para a API da IA

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

        try {
            JsonNode json = objectMapper.readTree(resposta.getBody());
            return json.get("response").asText();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler resposta da API da IA");
        }
    }
}