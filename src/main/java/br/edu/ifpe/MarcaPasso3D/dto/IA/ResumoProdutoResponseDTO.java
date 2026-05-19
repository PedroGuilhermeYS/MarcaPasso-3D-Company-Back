package br.edu.ifpe.MarcaPasso3D.dto.IA;

public class ResumoProdutoResponseDTO {

    private String resumo;

    public ResumoProdutoResponseDTO() {}

    public ResumoProdutoResponseDTO(String resumo) {
        this.resumo = resumo;
    }

    public String getResumo() { return resumo; }
    public void setResumo(String resumo) { this.resumo = resumo; }
}