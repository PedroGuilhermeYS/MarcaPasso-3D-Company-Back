package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.Frete.FreteRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Frete.FreteResponseDTO;
import br.edu.ifpe.MarcaPasso3D.model.Frete.Frete;
import br.edu.ifpe.MarcaPasso3D.repository.Frete.FreteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FreteService {

    private final FreteRepository repository;

    public FreteService(FreteRepository repository) {
        this.repository = repository;
    }

    public List<FreteResponseDTO> listarTodos() {
        return repository.findAllByOrderByIdAsc()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public FreteResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    public FreteResponseDTO buscarPorCep(String cep) {
        String cepNormalizado = normalizarCep(cep);
        Frete frete = repository.findByCep(cepNormalizado)
                .orElseThrow(() -> new RuntimeException("CEP não encontrado: " + cep));
        return toResponseDTO(frete);
    }

    @Transactional
    public FreteResponseDTO criar(FreteRequestDTO dto) {
        String cepNormalizado = normalizarCep(dto.getCepEntrega());
        validarCepUnico(cepNormalizado, null);

        Frete frete = new Frete();
        frete.setCep(cepNormalizado);
        frete.setCidade(dto.getCidade().trim());
        frete.setPreco(dto.getValorFrete());
        frete.setPrazoEntregaDias(dto.getPrazoEntregaDias());

        return toResponseDTO(repository.save(frete));
    }

    @Transactional
    public FreteResponseDTO atualizar(Long id, FreteRequestDTO dto) {
        Frete frete = buscarEntidadePorId(id);
        String cepNormalizado = normalizarCep(dto.getCepEntrega());
        validarCepUnico(cepNormalizado, id);

        frete.setCep(cepNormalizado);
        frete.setCidade(dto.getCidade().trim());
        frete.setPreco(dto.getValorFrete());
        frete.setPrazoEntregaDias(dto.getPrazoEntregaDias());

        return toResponseDTO(repository.save(frete));
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Frete não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    private Frete buscarEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Frete não encontrado: " + id));
    }

    String normalizarCep(String cep) {
        if (cep == null) return null;
        return cep.replaceAll("[^0-9]", "");
    }

    private String formatarCep(String cepSemHifen) {
        if (cepSemHifen == null || cepSemHifen.length() != 8) return cepSemHifen;
        return cepSemHifen.substring(0, 5) + "-" + cepSemHifen.substring(5);
    }

    private void validarCepUnico(String cepNormalizado, Long idAtualizando) {
        repository.findByCep(cepNormalizado).ifPresent(existente -> {
            if (idAtualizando == null || !existente.getId().equals(idAtualizando)) {
                throw new IllegalArgumentException("Já existe um frete cadastrado para o CEP: " + cepNormalizado);
            }
        });
    }

    private FreteResponseDTO toResponseDTO(Frete frete) {
        return new FreteResponseDTO(
                frete.getId(),
                frete.getCidade(),
                frete.getCep(),
                formatarCep(frete.getCep()),
                frete.getPreco(),
                frete.getPrazoEntregaDias()
        );
    }
}