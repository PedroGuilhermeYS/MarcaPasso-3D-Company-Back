package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.FavoritoProdutoDTO;
import br.edu.ifpe.MarcaPasso3D.model.Favorito;
import br.edu.ifpe.MarcaPasso3D.model.Produto;
import br.edu.ifpe.MarcaPasso3D.repository.Favorito.FavoritoRepository;
import br.edu.ifpe.MarcaPasso3D.repository.Produto.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final ProdutoRepository produtoRepository;

    public FavoritoService(FavoritoRepository favoritoRepository, ProdutoRepository produtoRepository) {
        this.favoritoRepository = favoritoRepository;
        this.produtoRepository = produtoRepository;
    }

    // Busca ou cria o favorito do usuário
    private Favorito buscarOuCriar(Long idUsuario) {
        return favoritoRepository.findByIdUsuario(idUsuario)
                .orElseGet(() -> {
                    Favorito novo = new Favorito();
                    novo.setIdUsuario(idUsuario);
                    return favoritoRepository.save(novo);
                });
    }

    // GET — listar produtos favoritos do usuário
    public List<FavoritoProdutoDTO> listar(Long idUsuario) {
        Favorito favorito = buscarOuCriar(idUsuario);
        return favorito.getProdutos().stream()
                .map(p -> new FavoritoProdutoDTO(
                        p.getId(),
                        p.getNome(),
                        p.getPreco(),
                        p.getImagemPrincipal(),
                        p.getCategoria()
                ))
                .collect(Collectors.toList());
    }

    // POST — adicionar produto ao favorito
    public List<FavoritoProdutoDTO> adicionar(Long idUsuario, Long idProduto) {
        Favorito favorito = buscarOuCriar(idUsuario);

        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + idProduto));

        boolean jaExiste = favorito.getProdutos().stream()
                .anyMatch(p -> p.getId().equals(idProduto));

        if (!jaExiste) {
            favorito.getProdutos().add(produto);
            favoritoRepository.save(favorito);
        }

        return listar(idUsuario);
    }

    // DELETE — remover produto do favorito
    public List<FavoritoProdutoDTO> remover(Long idUsuario, Long idProduto) {
        Favorito favorito = buscarOuCriar(idUsuario);

        favorito.getProdutos().removeIf(p -> p.getId().equals(idProduto));
        favoritoRepository.save(favorito);

        return listar(idUsuario);
    }

    // DELETE — limpar todos os favoritos do usuário
    public void limpar(Long idUsuario) {
        Favorito favorito = buscarOuCriar(idUsuario);
        favorito.getProdutos().clear();
        favoritoRepository.save(favorito);
    }
}