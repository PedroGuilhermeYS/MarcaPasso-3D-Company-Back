package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.dto.AlterarEmailDTO;
import br.edu.ifpe.MarcaPasso3D.dto.AlterarSenhaDTO;
import br.edu.ifpe.MarcaPasso3D.dto.AtualizarPerfilDTO;
import br.edu.ifpe.MarcaPasso3D.dto.LoginRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.LoginResponseDTO;
import br.edu.ifpe.MarcaPasso3D.dto.UsuarioRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.UsuarioResponseDTO;
import br.edu.ifpe.MarcaPasso3D.model.Usuario;
import br.edu.ifpe.MarcaPasso3D.security.JwtUtil;
import br.edu.ifpe.MarcaPasso3D.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
        try {
            Usuario usuario = usuarioService.buscarEntidadePorEmail(req.getEmail());

            if (!usuarioService.senhaCorreta(req.getPassword(), usuario.getSenha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Credenciais inválidas"));
            }

            String role = usuario.getRole().name().toLowerCase();
            String token = jwtUtil.gerarToken(usuario.getId(), usuario.getEmail(), role, usuario.getNome(), usuario.getCpf(), usuario.getTelefone());

            return ResponseEntity.ok(new LoginResponseDTO(token, usuario.getId(), usuario.getEmail(), role, usuario.getNome(), usuario.getCpf(), usuario.getTelefone()));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciais inválidas"));
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@Valid @RequestBody UsuarioRequestDTO dto) {
        try {
            UsuarioResponseDTO criado = usuarioService.criar(dto);

            String role = criado.getRole().name().toLowerCase();
            String token = jwtUtil.gerarToken(criado.getId(), criado.getEmail(), role, criado.getNome(), criado.getCpf(), criado.getTelefone());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new LoginResponseDTO(token, criado.getId(), criado.getEmail(), role, criado.getNome(), criado.getCpf(), criado.getTelefone()));

        } catch (IllegalArgumentException e) {
            int status = e.getMessage().contains("E-mail") ? 409 : 400;
            return ResponseEntity.status(status).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/verificar")
    public ResponseEntity<?> verificar(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        if (token == null || !jwtUtil.isValido(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token inválido ou expirado"));
        }

        var claims = jwtUtil.extrairClaims(token);
        Long id = Long.valueOf(claims.getSubject());
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);
        String nome = claims.get("nome", String.class);
        String cpf = claims.get("cpf", String.class);
        String telefone = claims.get("telefone", String.class);

        return ResponseEntity.ok(new LoginResponseDTO(token, id, email, role, nome, cpf, telefone));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("ok", true));
    }

    // Atualiza nome e telefone do próprio usuário
    @PatchMapping("/usuario/{id}")
    public ResponseEntity<?> atualizarPerfil(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarPerfilDTO dto) {
        try {
            UsuarioResponseDTO atualizado = usuarioService.atualizarPerfil(id, dto);
            return ResponseEntity.ok(Map.of(
                "ok", true,
                "nome", atualizado.getNome(),
                "telefone", atualizado.getTelefone() != null ? atualizado.getTelefone() : ""
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // Altera a senha do usuário (exige confirmação da senha atual)
    @PatchMapping("/usuario/{id}/senha")
    public ResponseEntity<?> alterarSenha(
            @PathVariable Long id,
            @Valid @RequestBody AlterarSenhaDTO dto) {
        try {
            usuarioService.alterarSenha(id, dto);
            return ResponseEntity.ok(Map.of("ok", true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // Altera o e-mail do usuário (exige confirmação de senha)
    @PatchMapping("/usuario/{id}/email")
    public ResponseEntity<?> alterarEmail(
            @PathVariable Long id,
            @Valid @RequestBody AlterarEmailDTO dto) {
        try {
            UsuarioResponseDTO atualizado = usuarioService.alterarEmail(id, dto);

            // Gera novo token com o e-mail atualizado
            String role = atualizado.getRole().name().toLowerCase();
            String novoToken = jwtUtil.gerarToken(
                atualizado.getId(), atualizado.getEmail(), role,
                atualizado.getNome(), atualizado.getCpf(), atualizado.getTelefone()
            );

            return ResponseEntity.ok(new LoginResponseDTO(
                novoToken, atualizado.getId(), atualizado.getEmail(), role,
                atualizado.getNome(), atualizado.getCpf(), atualizado.getTelefone()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}