package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.model.Produto;
import br.edu.ifpe.MarcaPasso3D.repository.Produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifpe.MarcaPasso3D.dto.IA.ResumoProdutoRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.IA.ResumoProdutoResponseDTO;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private ChatIAService chatIAService;

    public Page<Produto> consultarHome(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Produto> consultarTodos() {
        return repository.findAll();
    }

    public Produto consultarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado: " + id));
    }

    public List<Produto> ordenarPorNome() {
        return repository.findAllByOrderByNomeAsc();
    }

    public List<Produto> ordenarPorMenorPreco() {
        return repository.findAllByOrderByPrecoAsc();
    }

    public List<Produto> consultarCarrossel(String termoPesquisa) {
        if (termoPesquisa != null && !termoPesquisa.isBlank()) {
            Pageable top8 = PageRequest.of(0, 8);
            List<Produto> porTermo = repository.findByTermoParaCarrossel(termoPesquisa.trim(), top8);

            if (!porTermo.isEmpty()) {
                return porTermo;
            }
        }
        return repository.findTop8ByOrderByTotalVendasDesc();
    }

    public List<Produto> consultarNovaTela(String categoria) {
        return repository.findByCategoria(categoria);
    }

    public Produto cadastrar(Produto produto) {
        if (produto.getResumoIA() == null || produto.getResumoIA().isBlank()) {
            ResumoProdutoRequestDTO dto = new ResumoProdutoRequestDTO();
            dto.setNome(produto.getNome());
            dto.setCategoria(produto.getCategoria());
            dto.setMaterial(produto.getMaterial());
            dto.setPreco(produto.getPreco());
            dto.setDescricao(produto.getDescricao());

            ResumoProdutoResponseDTO resumo = chatIAService.gerarResumoProduto(dto);
            produto.setResumoIA(resumo.getResumo());
        }
        return repository.save(produto);
    }

    public Produto atualizar(Long id, Produto dados) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado: " + id));

        produto.setNome(dados.getNome());
        produto.setDescricao(dados.getDescricao());
        produto.setPreco(dados.getPreco());
        produto.setImagemPrincipal(dados.getImagemPrincipal());
        produto.setPersonalizavel(dados.getPersonalizavel());
        produto.setCategoria(dados.getCategoria());
        produto.setMaterial(dados.getMaterial());
        produto.setEstoque(dados.getEstoque());

        if (dados.getResumoIA() != null && !dados.getResumoIA().isBlank()) {
            produto.setResumoIA(dados.getResumoIA());
        }

        return repository.save(produto);
    }

    public void deletar(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado: " + id));
        repository.delete(produto);
    }

    public void incrementarVendas(Long produtoId, int quantidade) {
        Produto produto = repository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado: " + produtoId));
        int atual = produto.getTotalVendas() == null ? 0 : produto.getTotalVendas();
        produto.setTotalVendas(atual + quantidade);
        repository.save(produto);
    }
}