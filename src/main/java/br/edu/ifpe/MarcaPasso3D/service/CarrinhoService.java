package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.AddCarrinhoItemDTO;
import br.edu.ifpe.MarcaPasso3D.dto.CarrinhoItemDTO;
import br.edu.ifpe.MarcaPasso3D.model.Carrinho.Carrinho;
import br.edu.ifpe.MarcaPasso3D.model.Carrinho.CarrinhoItem;
import br.edu.ifpe.MarcaPasso3D.model.Produto;
import br.edu.ifpe.MarcaPasso3D.repository.Carrinho.CarrinhoItemRepository;
import br.edu.ifpe.MarcaPasso3D.repository.Carrinho.CarrinhoRepository;
import br.edu.ifpe.MarcaPasso3D.repository.Produto.ProdutoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final CarrinhoItemRepository carrinhoItemRepository;
    private final ProdutoRepository produtoRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository,
                           CarrinhoItemRepository carrinhoItemRepository,
                           ProdutoRepository produtoRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.carrinhoItemRepository = carrinhoItemRepository;
        this.produtoRepository = produtoRepository;
    }

    // GET — buscar itens do carrinho do usuário
    public List<CarrinhoItemDTO> getItensByUsuario(Long idUsuario) {
        Carrinho carrinho = carrinhoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado para o usuário: " + idUsuario));

        return carrinho.getItens().stream()
                .map(item -> new CarrinhoItemDTO(
                        item.getId(),
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getProduto().getImagemPrincipal(),
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                ))
                .collect(Collectors.toList());
    }

    // POST — adicionar item ao carrinho do usuário
    public List<CarrinhoItemDTO> adicionarItem(Long idUsuario, AddCarrinhoItemDTO dto) {
        // Busca ou cria o carrinho do usuário
        Carrinho carrinho = carrinhoRepository.findByIdUsuario(idUsuario)
                .orElseGet(() -> {
                    Carrinho novo = new Carrinho();
                    novo.setIdUsuario(idUsuario);
                    return carrinhoRepository.save(novo);
                });

        Produto produto = produtoRepository.findById(dto.getIdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + dto.getIdProduto()));

        // Verifica se o produto já está no carrinho
        CarrinhoItem itemExistente = carrinho.getItens().stream()
                .filter(i -> i.getProduto().getId().equals(dto.getIdProduto()))
                .findFirst()
                .orElse(null);

        if (itemExistente != null) {
            // Se já existe, soma a quantidade
            itemExistente.setQuantidade(itemExistente.getQuantidade() + dto.getQuantidade());
            carrinhoItemRepository.save(itemExistente);
        } else {
            // Se não existe, cria novo item
            CarrinhoItem novoItem = new CarrinhoItem();
            novoItem.setCarrinho(carrinho);
            novoItem.setProduto(produto);
            novoItem.setQuantidade(dto.getQuantidade());
            novoItem.setPrecoUnitario(dto.getPrecoUnitario() != null
                    ? dto.getPrecoUnitario()
                    : produto.getPreco());
            carrinhoItemRepository.save(novoItem);
        }

        return getItensByUsuario(idUsuario);
    }

    // DELETE — remover item do carrinho
    public void removerItem(Long idItem) {
        CarrinhoItem item = carrinhoItemRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item não encontrado: " + idItem));
        carrinhoItemRepository.delete(item);
    }
}