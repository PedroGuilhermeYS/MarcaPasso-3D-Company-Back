package br.edu.ifpe.MarcaPasso3D.dto.IA;

public class ChatIAFiltrosDTO {

    private String categoria;
    private String personalizavel;
    private String preco;
    private String termo;
    private String ordenacao;

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPersonalizavel() { return personalizavel; }
    public void setPersonalizavel(String personalizavel) { this.personalizavel = personalizavel; }

    public String getPreco() { return preco; }
    public void setPreco(String preco) { this.preco = preco; }

    public String getTermo() { return termo; }
    public void setTermo(String termo) { this.termo = termo; }

    public String getOrdenacao() { return ordenacao; }
    public void setOrdenacao(String ordenacao) { this.ordenacao = ordenacao; }
}