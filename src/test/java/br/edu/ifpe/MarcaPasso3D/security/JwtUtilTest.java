package br.edu.ifpe.MarcaPasso3D.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

/**
 * TC-JWT Testes unitários do JwtUtil.
 *
 *   TC-JWT-001  gerarToken() produz token não nulo e com 3 partes
 *   TC-JWT-002  extrairId()  retorna o id correto do subject
 *   TC-JWT-003  extrairEmail() retorna o e-mail da claim
 *   TC-JWT-004  extrairRole()  retorna a role da claim
 *   TC-JWT-005  isValido() retorna true para token recém-gerado
 *   TC-JWT-006  isValido() retorna false para token forjado (assinatura errada)
 *   TC-JWT-007  isValido() retorna false para token expirado
 *   TC-JWT-008  extrairClaims() retorna todas as claims esperadas
 */
@DisplayName("TC-JWT — JwtUtil")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Chave >= 32 chars para HS256 (mesma do application.properties de dev)
    private static final String SECRET     = "marcapasso3d-chave-sade-likeatattoo";
    private static final long   EXPIRACAO  = 3_600_000L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", EXPIRACAO);
    }

    // ─────────────────────────────────────────────
    // TC-JWT-001 a 004 — Geração e extração de claims
    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Geração e extração de claims")
    class GeracaoTest {

        @Test
        @DisplayName("TC-JWT-001 — gerarToken() produz string JWT com 3 segmentos")
        void gerarToken_produzJwtValido() {
            String token = jwtUtil.gerarToken(1L, "joao@teste.com", "user", "João Teste", "12345678900", "81999990000");
            assertThat(token).isNotBlank();
            assertThat(token.split("\\.")).hasSize(3);
        }

        @Test
        @DisplayName("TC-JWT-002 — extrairId() retorna o id inserido como subject")
        void extrairId_retornaIdCorreto() {
            String token = gerarTokenPadrao();
            assertThat(jwtUtil.extrairId(token)).isEqualTo(42L);
        }

        @Test
        @DisplayName("TC-JWT-003 — extrairEmail() retorna o e-mail inserido na claim")
        void extrairEmail_retornaEmailCorreto() {
            String token = gerarTokenPadrao();
            assertThat(jwtUtil.extrairEmail(token)).isEqualTo("maria@teste.com");
        }

        @Test
        @DisplayName("TC-JWT-004 — extrairRole() retorna a role inserida na claim")
        void extrairRole_retornaRoleCorreta() {
            String token = gerarTokenPadrao();
            assertThat(jwtUtil.extrairRole(token)).isEqualTo("admin");
        }

        @Test
        @DisplayName("TC-JWT-008 — extrairClaims() retorna todas as claims esperadas")
        void extrairClaims_retornaTodosOsCampos() {
            String token = jwtUtil.gerarToken(99L, "all@claims.com", "user",
                                              "Fulano", "98765432100", "81911112222");
            Claims claims = jwtUtil.extrairClaims(token);

            assertThat(claims.getSubject()).isEqualTo("99");
            assertThat(claims.get("email",    String.class)).isEqualTo("all@claims.com");
            assertThat(claims.get("role",     String.class)).isEqualTo("user");
            assertThat(claims.get("nome",     String.class)).isEqualTo("Fulano");
            assertThat(claims.get("cpf",      String.class)).isEqualTo("98765432100");
            assertThat(claims.get("telefone", String.class)).isEqualTo("81911112222");
        }
    }

    // ─────────────────────────────────────────────
    // TC-JWT-005 a 007 — Validação
    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Validação de token")
    class ValidacaoTest {

        @Test
        @DisplayName("TC-JWT-005 — isValido() retorna true para token recém-gerado")
        void isValido_retornaTrue_paraTokenRecente() {
            String token = gerarTokenPadrao();
            assertThat(jwtUtil.isValido(token)).isTrue();
        }

        @Test
        @DisplayName("TC-JWT-006 — isValido() retorna false para token com assinatura forjada")
        void isValido_retornaFalse_paraTokenForjado() {
            String tokenForjado = "eyJhbGciOiJIUzI1NiJ9"
                    + ".eyJzdWIiOiI5OTkifQ"
                    + ".assinatura_invalida_aqui";
            assertThat(jwtUtil.isValido(tokenForjado)).isFalse();
        }

        @Test
        @DisplayName("TC-JWT-007 — isValido() retorna false para token expirado")
        void isValido_retornaFalse_paraTokenExpirado() {
            // Gera token com expiração de 1ms (já expirado no momento da validação)
            ReflectionTestUtils.setField(jwtUtil, "expirationMs", 1L);
            String tokenExpirado = jwtUtil.gerarToken(1L, "x@x.com", "user", "X", null, null);
            // Aguarda 10ms para garantir expiração
            try { Thread.sleep(10); } catch (InterruptedException ignored) {}

            assertThat(jwtUtil.isValido(tokenExpirado)).isFalse();
        }
    }

    // ─── helpers ────────────────────────────────
    private String gerarTokenPadrao() {
        return jwtUtil.gerarToken(42L, "maria@teste.com", "admin", "Maria Teste", "99988877766", "81944443333");
    }
}