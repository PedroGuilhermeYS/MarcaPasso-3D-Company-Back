package br.edu.ifpe.MarcaPasso3D;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Validação da migração Flyway")
class FlywayMigrationValidationTest {

    @Autowired
    private JdbcTemplate jdbc;

    // =========================================================
    // 1. HISTÓRICO DO FLYWAY (via SQL direto)
    // =========================================================

    @Test
    @DisplayName("Tabela flyway_schema_history deve existir")
    void flywaySchemaHistoryExiste() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables " +
                "WHERE table_schema = 'public' AND table_name = 'flyway_schema_history'",
                Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Flyway deve ter ao menos 2 migrations aplicadas")
    void flywayTemDuasMigrationsAplicadas() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history WHERE success = true",
                Integer.class);
        assertThat(count).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("V1__init_schema deve constar no histórico como sucesso")
    void v1InitSchemaNoHistorico() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history " +
                "WHERE version = '1' AND success = true AND script LIKE '%init_schema%'",
                Integer.class);
        assertThat(count).as("V1__init_schema não encontrada no histórico do Flyway").isEqualTo(1);
    }

    @Test
    @DisplayName("V2__seed_produtos deve constar no histórico como sucesso")
    void v2SeedProdutosNoHistorico() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history " +
                "WHERE version = '2' AND success = true AND script LIKE '%seed_produtos%'",
                Integer.class);
        assertThat(count).as("V2__seed_produtos não encontrada no histórico do Flyway").isEqualTo(1);
    }

    @Test
    @DisplayName("Não deve existir migrations com falha no histórico")
    void semMigrationsComFalha() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM flyway_schema_history WHERE success = false",
                Integer.class);
        assertThat(count).as("Existem migrations com falha no histórico").isEqualTo(0);
    }

    // =========================================================
    // 2. ESTRUTURA DE TABELAS
    // =========================================================

    @Test
    @DisplayName("Tabela 'produtos' deve existir")
    void tabelaProdutosExiste() { assertTabelaExiste("produtos"); }

    @Test
    @DisplayName("Tabela 'usuarios' deve existir")
    void tabelaUsuariosExiste() { assertTabelaExiste("usuarios"); }

    @Test
    @DisplayName("Tabela 'favoritos' deve existir")
    void tabelaFavoritosExiste() { assertTabelaExiste("favoritos"); }

    @Test
    @DisplayName("Tabela 'favorito_produtos' deve existir")
    void tabelaFavoritoProdutosExiste() { assertTabelaExiste("favorito_produtos"); }

    @Test
    @DisplayName("Tabela 'carrinhos' deve existir")
    void tabelaCarrinhosExiste() { assertTabelaExiste("carrinhos"); }

    @Test
    @DisplayName("Tabela 'carrinho_itens' deve existir")
    void tabelaCarrinhoItensExiste() { assertTabelaExiste("carrinho_itens"); }

    @Test
    @DisplayName("Tabela 'enderecos' deve existir")
    void tabelaEnderecosExiste() { assertTabelaExiste("enderecos"); }

    @Test
    @DisplayName("Tabela 'cupons' deve existir")
    void tabelaCuponsExiste() { assertTabelaExiste("cupons"); }

    @Test
    @DisplayName("Tabela 'encomendas' deve existir")
    void tabelaEncomendasExiste() { assertTabelaExiste("encomendas"); }

    @Test
    @DisplayName("Tabela 'encomenda_itens' deve existir")
    void tabelaEncomendaItensExiste() { assertTabelaExiste("encomenda_itens"); }

    // =========================================================
    // 3. COLUNAS CRÍTICAS
    // =========================================================

    @Test
    @DisplayName("Tabela 'produtos' deve ter colunas obrigatórias")
    void produtosTemColunasObrigatorias() {
        assertThat(listarColunas("produtos"))
                .contains("id_produto", "nome", "preco", "categoria", "estoque", "total_vendas");
    }

    @Test
    @DisplayName("Tabela 'usuarios' deve ter colunas obrigatórias")
    void usuariosTemColunasObrigatorias() {
        assertThat(listarColunas("usuarios"))
                .contains("id_usuario", "nome", "email", "senha", "role", "ativo");
    }

    @Test
    @DisplayName("Tabela 'carrinho_itens' deve ter colunas obrigatórias")
    void carrinhoItensTemColunasObrigatorias() {
        assertThat(listarColunas("carrinho_itens"))
                .contains("id_item", "id_carrinho", "id_produto", "quantidade", "preco_unitario");
    }

    @Test
    @DisplayName("Tabela 'encomendas' deve ter colunas obrigatórias")
    void encomendasTemColunasObrigatorias() {
        assertThat(listarColunas("encomendas"))
                .contains("id_encomenda", "numero_pedido", "status", "total", "forma_pagamento");
    }

    @Test
    @DisplayName("Tabela 'cupons' deve ter colunas obrigatórias")
    void cuponsTemColunasObrigatorias() {
        assertThat(listarColunas("cupons"))
                .contains("id_cupom", "nome_cupom", "valor_desconto", "tipo_validade");
    }

    // =========================================================
    // 4. CONSTRAINTS DE UNICIDADE
    // =========================================================

    @Test
    @DisplayName("Email de usuário deve ter constraint UNIQUE")
    void emailUsuarioUnico() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.table_constraints tc " +
                "JOIN information_schema.constraint_column_usage ccu " +
                "  ON tc.constraint_name = ccu.constraint_name " +
                "WHERE tc.table_name = 'usuarios' " +
                "  AND tc.constraint_type = 'UNIQUE' " +
                "  AND ccu.column_name = 'email'",
                Integer.class);
        assertThat(count).as("Constraint UNIQUE em usuarios.email não encontrada").isGreaterThan(0);
    }

    @Test
    @DisplayName("Nome de cupom deve ter constraint UNIQUE")
    void nomeCupomUnico() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.table_constraints tc " +
                "JOIN information_schema.constraint_column_usage ccu " +
                "  ON tc.constraint_name = ccu.constraint_name " +
                "WHERE tc.table_name = 'cupons' " +
                "  AND tc.constraint_type = 'UNIQUE' " +
                "  AND ccu.column_name = 'nome_cupom'",
                Integer.class);
        assertThat(count).as("Constraint UNIQUE em cupons.nome_cupom não encontrada").isGreaterThan(0);
    }

    // =========================================================
    // 5. SEED DE DADOS (V2)
    // =========================================================

    @Test
    @DisplayName("V2 seed deve ter inserido produtos na tabela")
    void seedProdutosInserido() {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM produtos", Integer.class);
        assertThat(count).as("V2__seed_produtos deve ter inserido ao menos 1 produto").isGreaterThan(0);
    }

    // =========================================================
    // 6. FOREIGN KEYS (via information_schema)
    // =========================================================

    @Test
    @DisplayName("FK de carrinho_itens para carrinhos deve existir")
    void fkCarrinhoItensCarrinho() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.referential_constraints rc " +
                "JOIN information_schema.table_constraints tc " +
                "  ON rc.constraint_name = tc.constraint_name " +
                "WHERE tc.table_name = 'carrinho_itens'",
                Integer.class);
        assertThat(count).as("FK em carrinho_itens não encontrada").isGreaterThan(0);
    }

    @Test
    @DisplayName("FK de encomenda_itens para encomendas deve existir")
    void fkEncomendaItensEncomenda() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.referential_constraints rc " +
                "JOIN information_schema.table_constraints tc " +
                "  ON rc.constraint_name = tc.constraint_name " +
                "WHERE tc.table_name = 'encomenda_itens'",
                Integer.class);
        assertThat(count).as("FK em encomenda_itens não encontrada").isGreaterThan(0);
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private void assertTabelaExiste(String tabela) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables " +
                "WHERE table_schema = 'public' AND table_name = ?",
                Integer.class, tabela);
        assertThat(count).as("Tabela '%s' não encontrada no banco", tabela).isEqualTo(1);
    }

    private List<String> listarColunas(String tabela) {
        return jdbc.queryForList(
                "SELECT column_name FROM information_schema.columns " +
                "WHERE table_schema = 'public' AND table_name = ?",
                String.class, tabela);
    }
}