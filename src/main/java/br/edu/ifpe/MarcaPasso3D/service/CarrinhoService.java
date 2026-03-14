package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.CarrinhoItemDTO;
import br.edu.ifpe.MarcaPasso3D.model.Carrinho.Carrinho;
import br.edu.ifpe.MarcaPasso3D.model.Carrinho.CarrinhoItem;
import br.edu.ifpe.MarcaPasso3D.repository.Carrinho.CarrinhoItemRepository;
import br.edu.ifpe.MarcaPasso3D.repository.Carrinho.CarrinhoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final CarrinhoItemRepository carrinhoItemRepository; 

    public CarrinhoService(CarrinhoRepository carrinhoRepository, CarrinhoItemRepository carrinhoItemRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.carrinhoItemRepository = carrinhoItemRepository;
    }

    public List<CarrinhoItemDTO> getItensByUsuario(Long idUsuario) {
        Carrinho carrinho = carrinhoRepository.findByIdUsuario(idUsuario).orElseThrow(() -> new RuntimeException("Carrinho não encontrado para o usuário: " + idUsuario));

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
    
    public void removerItem(Long idItem) {
        CarrinhoItem item = carrinhoItemRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item não encontrado: " + idItem));
        carrinhoItemRepository.delete(item);
    }
}