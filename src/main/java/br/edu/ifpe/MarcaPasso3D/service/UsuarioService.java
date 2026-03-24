package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.UsuarioRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.UsuarioResponseDTO;
import br.edu.ifpe.MarcaPasso3D.model.Usuario;
import br.edu.ifpe.MarcaPasso3D.repository.Usuario.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario buscarEntidadePorEmail(String email) {
        return repository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + email));
    }

    public boolean senhaCorreta(String senhaRaw, String senhaHash) {
        return passwordEncoder.matches(senhaRaw, senhaHash);
    }

    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("E-mail já cadastrado: " + dto.getEmail());
        if (dto.getCpf() != null && !dto.getCpf().isBlank() && repository.existsByCpf(dto.getCpf()))
            throw new IllegalArgumentException("CPF já cadastrado: " + dto.getCpf());

        Usuario u = new Usuario();
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        u.setSenha(passwordEncoder.encode(dto.getSenha()));
        u.setTelefone(dto.getTelefone());
        u.setCpf(dto.getCpf());
        if (dto.getRole() != null) u.setRole(dto.getRole());

        return new UsuarioResponseDTO(repository.save(u));
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(UsuarioResponseDTO::new).collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        return new UsuarioResponseDTO(buscarEntidade(id));
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario u = buscarEntidade(id);

        if (!u.getEmail().equals(dto.getEmail()) && repository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("E-mail já em uso: " + dto.getEmail());

        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        u.setTelefone(dto.getTelefone());
        u.setCpf(dto.getCpf());

        if (dto.getSenha() != null && !dto.getSenha().isBlank())
            u.setSenha(passwordEncoder.encode(dto.getSenha()));

        if (dto.getRole() != null)
            u.setRole(dto.getRole());

        return new UsuarioResponseDTO(repository.save(u));
    }

    public void desativar(Long id) {
        Usuario u = buscarEntidade(id);
        u.setAtivo(false);
        repository.save(u);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id))
            throw new RuntimeException("Usuário não encontrado: " + id);
        repository.deleteById(id);
    }

    private Usuario buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
    }
}