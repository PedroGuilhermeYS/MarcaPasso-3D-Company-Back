package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.model.Endereço.Endereco;
import br.edu.ifpe.MarcaPasso3D.repository.Endereço.EnderecoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public List<Endereco> getEnderecosByUsuario(Long idUsuario) {
        return enderecoRepository.findByIdUsuario(idUsuario);
    }

    public Endereco salvar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public Endereco atualizar(Long id, Endereco dadosAtualizados) {
        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado: " + id));

        endereco.setNome(dadosAtualizados.getNome());
        endereco.setRua(dadosAtualizados.getRua());
        endereco.setNumero(dadosAtualizados.getNumero());
        endereco.setComplemento(dadosAtualizados.getComplemento());
        endereco.setBairro(dadosAtualizados.getBairro());
        endereco.setCidade(dadosAtualizados.getCidade());
        endereco.setEstado(dadosAtualizados.getEstado());
        endereco.setCep(dadosAtualizados.getCep());

        return enderecoRepository.save(endereco);
    }

    public void excluir(Long id) {
        if (!enderecoRepository.existsById(id)) {
            throw new RuntimeException("Endereço não encontrado: " + id);
        }
        enderecoRepository.deleteById(id);
    }
}