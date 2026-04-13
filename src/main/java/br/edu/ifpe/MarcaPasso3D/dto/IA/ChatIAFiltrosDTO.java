package br.edu.ifpe.MarcaPasso3D.dto.IA;

// Representa os filtros de produto que a IA decide aplicar.
// Exemplo: usuário pede "produtos de decoração até R$100"
// A IA retorna: { "categoria": "Decoração", "preco": "2", ... }
public class ChatIAFiltrosDTO {

    // Categoria do produto: "Decoração", "Colecionáveis", "Acessórios" ou "" (todos)
    private String categoria;

    // Se o produto é personalizável: "true", "false" ou "" (todos)
    private String personalizavel;

    // Faixa de preço: "1" (até R$50), "2" (R$50-100), "3" (acima R$100) ou ""
    private String preco;

    // Busca por nome: qualquer texto, ou "" para não filtrar
    private String termo;

    // Ordenação: "az" (A-Z), "menor" (menor preço), "maior" (maior preço) ou ""
    private String ordenacao;

    // --- Getters e Setters ---

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