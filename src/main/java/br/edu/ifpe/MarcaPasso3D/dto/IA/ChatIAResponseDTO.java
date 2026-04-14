package br.edu.ifpe.MarcaPasso3D.dto.IA;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatIAResponseDTO {

    private String acao;
    private String mensagem;
    private String id;
    private ChatIAFiltrosDTO filtros;

    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public ChatIAFiltrosDTO getFiltros() { return filtros; }
    public void setFiltros(ChatIAFiltrosDTO filtros) { this.filtros = filtros; }
}