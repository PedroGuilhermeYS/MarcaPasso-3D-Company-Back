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
}