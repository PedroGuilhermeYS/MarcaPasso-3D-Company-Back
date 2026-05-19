package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.Cupom.CupomRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Cupom.CupomResponseDTO;
import br.edu.ifpe.MarcaPasso3D.model.Cupom.Cupom;
import br.edu.ifpe.MarcaPasso3D.model.Cupom.TipoValidadeCupom;
import br.edu.ifpe.MarcaPasso3D.repository.Cupom.CupomRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CupomService {

    private final CupomRepository repository;

    public CupomService(CupomRepository repository) {
        this.repository = repository;
    }

    public List<CupomResponseDTO> listarTodos() {
        removerCuponsExpirados();
        return repository.findAllByOrderByIdDesc()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CupomResponseDTO buscarPorId(Long id) {
        removerCuponsExpirados();
        return toResponseDTO(buscarEntidadePorId(id));
    }

    @Transactional
    public CupomResponseDTO criar(CupomRequestDTO dto) {
        removerCuponsExpirados();
        validarDTO(dto, null);

        Cupom cupom = new Cupom();
        cupom.setNomeCupom(dto.getNomeCupom().trim());
        cupom.setValorDesconto(dto.getValorDesconto());
        cupom.setTipoValidade(dto.getTipoValidade());
        cupom.setDataExpiracao(dto.getTipoValidade() == TipoValidadeCupom.TEMPORARIO ? dto.getDataExpiracao() : null);

        return toResponseDTO(repository.save(cupom));
    }

    @Transactional
    public CupomResponseDTO atualizar(Long id, CupomRequestDTO dto) {
        removerCuponsExpirados();
        Cupom cupom = buscarEntidadePorId(id);
        validarDTO(dto, id);

        cupom.setNomeCupom(dto.getNomeCupom().trim());
        cupom.setValorDesconto(dto.getValorDesconto());
        cupom.setTipoValidade(dto.getTipoValidade());
        cupom.setDataExpiracao(dto.getTipoValidade() == TipoValidadeCupom.TEMPORARIO ? dto.getDataExpiracao() : null);

        return toResponseDTO(repository.save(cupom));
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cupom não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void removerCuponsExpirados() {
        List<Cupom> expirados = repository
                .findByTipoValidadeAndDataExpiracaoLessThanEqual(TipoValidadeCupom.TEMPORARIO, LocalDate.now());

        if (!expirados.isEmpty()) {
            repository.deleteAll(expirados);
        }
    }

    private Cupom buscarEntidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado: " + id));
    }

    private void validarDTO(CupomRequestDTO dto, Long idAtualizando) {
        String nome = dto.getNomeCupom() == null ? null : dto.getNomeCupom().trim();

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do cupom é obrigatório");
        }

        boolean nomeEmUso = repository.findByNomeCupomIgnoreCase(nome)
                .map(cupom -> idAtualizando == null || !cupom.getId().equals(idAtualizando))
                .orElse(false);

        if (nomeEmUso) {
            throw new IllegalArgumentException("Já existe um cupom com esse nome: " + nome);
        }

        if (dto.getTipoValidade() == null) {
            throw new IllegalArgumentException("O tipo de validade é obrigatório");
        }

        if (dto.getTipoValidade() == TipoValidadeCupom.TEMPORARIO) {
            if (dto.getDataExpiracao() == null) {
                throw new IllegalArgumentException("Cupom temporário precisa de uma data de expiração");
            }
            if (dto.getDataExpiracao().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("A data de expiração não pode ser anterior a hoje");
            }
        }
    }

    private CupomResponseDTO toResponseDTO(Cupom cupom) {
        boolean expirado = cupom.getTipoValidade() == TipoValidadeCupom.TEMPORARIO
                && cupom.getDataExpiracao() != null
                && !cupom.getDataExpiracao().isAfter(LocalDate.now());

        return new CupomResponseDTO(
                cupom.getId(),
                cupom.getNomeCupom(),
                cupom.getValorDesconto(),
                cupom.getTipoValidade(),
                cupom.getDataExpiracao(),
                expirado,
                cupom.getCriadoEm(),
                cupom.getAtualizadoEm()
        );
    }
}
