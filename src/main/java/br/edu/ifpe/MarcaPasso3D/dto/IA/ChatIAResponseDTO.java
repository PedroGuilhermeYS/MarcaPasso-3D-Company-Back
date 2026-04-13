package br.edu.ifpe.MarcaPasso3D.dto.IA;

import com.fasterxml.jackson.annotation.JsonInclude;

// Essa classe representa o que o backend devolve para o frontend após processar a IA.
// @JsonInclude(NON_NULL) garante que campos nulos não apareçam no JSON da resposta,
// deixando a resposta mais limpa.
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatIAResponseDTO {

    // A ação que o frontend deve executar:
    // "filtrar"      → aplicar filtros e ir para a home de produtos
    // "produto"      → abrir a página de um produto específico
    // "favoritar"    → favoritar um produto
    // "sem_resultado"→ nenhum produto encontrado, sugerir WhatsApp
    // "chat"         → resposta de conversa geral, sem navegação
    private String acao;

    // Mensagem amigável que aparece no balão do chat para o usuário
    private String mensagem;

    // ID do produto (só presente nas ações "produto" e "favoritar")
    private String id;

    // Filtros a aplicar (só presente na ação "filtrar")
    private ChatIAFiltrosDTO filtros;

    // --- Getters e Setters ---

    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public ChatIAFiltrosDTO getFiltros() { return filtros; }
    public void setFiltros(ChatIAFiltrosDTO filtros) { this.filtros = filtros; }
}