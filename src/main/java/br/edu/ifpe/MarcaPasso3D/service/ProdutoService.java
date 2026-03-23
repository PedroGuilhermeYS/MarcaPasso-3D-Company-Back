package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.model.Produto;
import br.edu.ifpe.MarcaPasso3D.repository.Produto.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

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

    public List<Produto> consultarCarrossel() {
        return repository.findTop5ByOrderByIdDesc();
    }

    public List<Produto> consultarNovaTela(String categoria) {
        return repository.findByCategoria(categoria);
    }

    public Produto cadastrar(Produto produto) {
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

        return repository.save(produto);
    }

    public void deletar(Long id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado: " + id));
        repository.delete(produto);
    }
}