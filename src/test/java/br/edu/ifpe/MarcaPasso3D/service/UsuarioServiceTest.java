package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.AlterarEmailDTO;
import br.edu.ifpe.MarcaPasso3D.dto.AlterarSenhaDTO;
import br.edu.ifpe.MarcaPasso3D.dto.AtualizarPerfilDTO;
import br.edu.ifpe.MarcaPasso3D.dto.UsuarioRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.UsuarioResponseDTO;
import br.edu.ifpe.MarcaPasso3D.model.Usuario;
import br.edu.ifpe.MarcaPasso3D.repository.Usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * TC-USUARIO-* — Testes unitários do UsuarioService.
 *
 * Cobertura:
 *   TC-USUARIO-001  cadastro com dados válidos
 *   TC-USUARIO-002  cadastro com e-mail duplicado → IllegalArgumentException
 *   TC-USUARIO-003  cadastro com CPF duplicado    → IllegalArgumentException
 *   TC-USUARIO-004  busca por e-mail existente
 *   TC-USUARIO-005  busca por e-mail inexistente  → RuntimeException
 *   TC-USUARIO-006  senhaCorreta retorna true para senha válida
 *   TC-USUARIO-007  senhaCorreta retorna false para senha inválida
 *   TC-USUARIO-008  atualizarPerfil salva nome e telefone
 *   TC-USUARIO-009  alterarSenha com senha atual correta
 *   TC-USUARIO-010  alterarSenha com senha atual incorreta → IllegalArgumentException
 *   TC-USUARIO-011  alterarEmail com senha correta e e-mail novo único
 *   TC-USUARIO-012  alterarEmail com e-mail já em uso → IllegalArgumentException
 *   TC-USUARIO-013  alterarEmail com senha incorreta  → IllegalArgumentException
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TC-USUARIO — UsuarioService")
class UsuarioServiceTest {

    @Mock  UsuarioRepository repository;
    @Mock  PasswordEncoder   passwordEncoder;

    @InjectMocks UsuarioService service;

    private Usuario usuarioBase;

    @BeforeEach
    void setUp() {
        usuarioBase = new Usuario();
        usuarioBase.setId(1L);
        usuarioBase.setNome("João Teste");
        usuarioBase.setEmail("joao@teste.com");
        usuarioBase.setSenha("$2a$10$hashFake");
        usuarioBase.setTelefone("81999990000");
        usuarioBase.setCpf("123.456.789-00");
    }

    // ─────────────────────────────────────────────
    // TC-USUARIO-001 a 003 — Cadastro
    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Cadastro de usuário")
    class CadastroTest {

        @Test
        @DisplayName("TC-USUARIO-001 — criar() com dados válidos retorna DTO com id")
        void criar_comDadosValidos_retornaDTO() {
            when(repository.existsByEmail("joao@teste.com")).thenReturn(false);
            when(repository.existsByCpf("12345678900")).thenReturn(false);
            when(passwordEncoder.encode("senha123")).thenReturn("$2a$10$hashFake");
            when(repository.save(any(Usuario.class))).thenReturn(usuarioBase);

            UsuarioRequestDTO dto = buildRequest("João Teste", "joao@teste.com", "senha123", "12345678900");
            UsuarioResponseDTO resultado = service.criar(dto);

            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getEmail()).isEqualTo("joao@teste.com");
            verify(passwordEncoder).encode("senha123");
            verify(repository).save(any(Usuario.class));
        }

        @Test
        @DisplayName("TC-USUARIO-002 — criar() com e-mail duplicado lança IllegalArgumentException")
        void criar_comEmailDuplicado_lancaExcecao() {
            when(repository.existsByEmail("joao@teste.com")).thenReturn(true);

            UsuarioRequestDTO dto = buildRequest("Outro", "joao@teste.com", "senha123", null);

            assertThatThrownBy(() -> service.criar(dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("E-mail já cadastrado");
        }

        @Test
        @DisplayName("TC-USUARIO-003 — criar() com CPF duplicado lança IllegalArgumentException")
        void criar_comCpfDuplicado_lancaExcecao() {
            when(repository.existsByEmail("novo@teste.com")).thenReturn(false);
            when(repository.existsByCpf("12345678900")).thenReturn(true);

            UsuarioRequestDTO dto = buildRequest("Novo", "novo@teste.com", "senha123", "12345678900");

            assertThatThrownBy(() -> service.criar(dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("CPF já cadastrado");
        }
    }

    // ─────────────────────────────────────────────
    // TC-USUARIO-004 a 007 — Busca e validação de senha
    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Busca e validação de senha")
    class BuscaSenhaTest {

        @Test
        @DisplayName("TC-USUARIO-004 — buscarEntidadePorEmail() retorna usuário existente")
        void buscarPorEmail_existente_retornaUsuario() {
            when(repository.findByEmail("joao@teste.com")).thenReturn(Optional.of(usuarioBase));

            Usuario resultado = service.buscarEntidadePorEmail("joao@teste.com");

            assertThat(resultado.getEmail()).isEqualTo("joao@teste.com");
        }

        @Test
        @DisplayName("TC-USUARIO-005 — buscarEntidadePorEmail() com e-mail inexistente lança RuntimeException")
        void buscarPorEmail_inexistente_lancaExcecao() {
            when(repository.findByEmail("fantasma@teste.com")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.buscarEntidadePorEmail("fantasma@teste.com"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Usuário não encontrado");
        }

        @Test
        @DisplayName("TC-USUARIO-006 — senhaCorreta() retorna true para senha válida")
        void senhaCorreta_retornaTrue_paraSenhaValida() {
            when(passwordEncoder.matches("minhasenha", "$2a$10$hashFake")).thenReturn(true);

            assertThat(service.senhaCorreta("minhasenha", "$2a$10$hashFake")).isTrue();
        }

        @Test
        @DisplayName("TC-USUARIO-007 — senhaCorreta() retorna false para senha inválida")
        void senhaCorreta_retornaFalse_paraSenhaInvalida() {
            when(passwordEncoder.matches("senhaerrada", "$2a$10$hashFake")).thenReturn(false);

            assertThat(service.senhaCorreta("senhaerrada", "$2a$10$hashFake")).isFalse();
        }
    }

    // ─────────────────────────────────────────────
    // TC-USUARIO-008 — Atualizar perfil
    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Atualização de perfil")
    class AtualizarPerfilTest {

        @Test
        @DisplayName("TC-USUARIO-008 — atualizarPerfil() salva nome e telefone corretamente")
        void atualizarPerfil_salvaNomeETelefone() {
            Usuario atualizado = new Usuario();
            atualizado.setId(1L);
            atualizado.setNome("João Atualizado");
            atualizado.setEmail("joao@teste.com");
            atualizado.setTelefone("81988880000");

            when(repository.findById(1L)).thenReturn(Optional.of(usuarioBase));
            when(repository.save(any(Usuario.class))).thenReturn(atualizado);

            AtualizarPerfilDTO dto = new AtualizarPerfilDTO();
            dto.setNome("João Atualizado");
            dto.setTelefone("81988880000");

            UsuarioResponseDTO resultado = service.atualizarPerfil(1L, dto);

            assertThat(resultado.getNome()).isEqualTo("João Atualizado");
            assertThat(resultado.getTelefone()).isEqualTo("81988880000");
        }
    }

    // ─────────────────────────────────────────────
    // TC-USUARIO-009 a 010 — Alterar senha
    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Alteração de senha")
    class AlterarSenhaTest {

        @Test
        @DisplayName("TC-USUARIO-009 — alterarSenha() com senha atual correta salva novo hash")
        void alterarSenha_comSenhaCorreta_salvaNovaSenha() {
            when(repository.findById(1L)).thenReturn(Optional.of(usuarioBase));
            when(passwordEncoder.matches("senhaAtual", "$2a$10$hashFake")).thenReturn(true);
            when(passwordEncoder.encode("novaSenha")).thenReturn("$2a$10$novoHash");

            AlterarSenhaDTO dto = new AlterarSenhaDTO();
            dto.setSenhaAtual("senhaAtual");
            dto.setNovaSenha("novaSenha");

            assertThatNoException().isThrownBy(() -> service.alterarSenha(1L, dto));
            verify(repository).save(argThat(u -> "$2a$10$novoHash".equals(u.getSenha())));
        }

        @Test
        @DisplayName("TC-USUARIO-010 — alterarSenha() com senha atual incorreta lança IllegalArgumentException")
        void alterarSenha_comSenhaIncorreta_lancaExcecao() {
            when(repository.findById(1L)).thenReturn(Optional.of(usuarioBase));
            when(passwordEncoder.matches("senhaErrada", "$2a$10$hashFake")).thenReturn(false);

            AlterarSenhaDTO dto = new AlterarSenhaDTO();
            dto.setSenhaAtual("senhaErrada");
            dto.setNovaSenha("qualquer");

            assertThatThrownBy(() -> service.alterarSenha(1L, dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Senha atual incorreta");
        }
    }

    // ─────────────────────────────────────────────
    // TC-USUARIO-011 a 013 — Alterar e-mail
    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Alteração de e-mail")
    class AlterarEmailTest {

        @Test
        @DisplayName("TC-USUARIO-011 — alterarEmail() com dados válidos atualiza e-mail")
        void alterarEmail_comDadosValidos_atualizaEmail() {
            Usuario atualizado = new Usuario();
            atualizado.setId(1L);
            atualizado.setNome("João Teste");
            atualizado.setEmail("novoemail@teste.com");

            when(repository.findById(1L)).thenReturn(Optional.of(usuarioBase));
            when(passwordEncoder.matches("minhasenha", "$2a$10$hashFake")).thenReturn(true);
            when(repository.existsByEmail("novoemail@teste.com")).thenReturn(false);
            when(repository.save(any(Usuario.class))).thenReturn(atualizado);

            AlterarEmailDTO dto = new AlterarEmailDTO();
            dto.setSenha("minhasenha");
            dto.setNovoEmail("novoemail@teste.com");

            UsuarioResponseDTO resultado = service.alterarEmail(1L, dto);

            assertThat(resultado.getEmail()).isEqualTo("novoemail@teste.com");
        }

        @Test
        @DisplayName("TC-USUARIO-012 — alterarEmail() com e-mail já em uso lança IllegalArgumentException")
        void alterarEmail_comEmailEmUso_lancaExcecao() {
            when(repository.findById(1L)).thenReturn(Optional.of(usuarioBase));
            when(passwordEncoder.matches("minhasenha", "$2a$10$hashFake")).thenReturn(true);
            when(repository.existsByEmail("ocupado@teste.com")).thenReturn(true);

            AlterarEmailDTO dto = new AlterarEmailDTO();
            dto.setSenha("minhasenha");
            dto.setNovoEmail("ocupado@teste.com");

            assertThatThrownBy(() -> service.alterarEmail(1L, dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("E-mail já está em uso");
        }

        @Test
        @DisplayName("TC-USUARIO-013 — alterarEmail() com senha incorreta lança IllegalArgumentException")
        void alterarEmail_comSenhaIncorreta_lancaExcecao() {
            when(repository.findById(1L)).thenReturn(Optional.of(usuarioBase));
            when(passwordEncoder.matches("senhaErrada", "$2a$10$hashFake")).thenReturn(false);

            AlterarEmailDTO dto = new AlterarEmailDTO();
            dto.setSenha("senhaErrada");
            dto.setNovoEmail("qualquer@email.com");

            assertThatThrownBy(() -> service.alterarEmail(1L, dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Senha incorreta");
        }
    }

    // ─── helpers ────────────────────────────────
    private UsuarioRequestDTO buildRequest(String nome, String email, String senha, String cpf) {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNome(nome);
        dto.setEmail(email);
        dto.setSenha(senha);
        dto.setCpf(cpf);
        return dto;
    }
}