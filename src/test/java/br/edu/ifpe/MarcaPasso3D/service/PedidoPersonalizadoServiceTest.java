package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.Personalizado.AtualizarStatusPersonalizadoDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Personalizado.PedidoPersonalizadoRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Personalizado.PedidoPersonalizadoResponseDTO;
import br.edu.ifpe.MarcaPasso3D.model.Personalizado.PedidoPersonalizado;
import br.edu.ifpe.MarcaPasso3D.model.Personalizado.StatusPedidoPersonalizado;
import br.edu.ifpe.MarcaPasso3D.repository.Personalizado.PedidoPersonalizadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * TC-PERS-* — Testes unitários do PedidoPersonalizadoService.
 *
 * Erros corrigidos em relação à versão anterior:
 *   1. Status inicial é AGUARDANDO_ORCAMENTO 
 *   2. Enum real: AGUARDANDO_ORCAMENTO | ORCAMENTO_ENVIADO | APROVADO | EM_PRODUCAO | CONCLUIDO | CANCELADO
 *   3. TelegramService tem dois métodos: enviarMensagem(texto) e enviarMensagemComFoto(url, caption)
 *      → sem fotos  → chama enviarMensagem()
 *      → com fotos  → chama enviarMensagemComFoto() para a 1ª foto (e demais sem caption)
 *   4. RequestDTO não tem campo "material" — removido do helper
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TC-PERS — PedidoPersonalizadoService")
class PedidoPersonalizadoServiceTest {

    @Mock PedidoPersonalizadoRepository repository;
    @Mock TelegramService telegramService;

    @InjectMocks PedidoPersonalizadoService service;

    private PedidoPersonalizado pedidoBase;

    @BeforeEach
    void setUp() {
        pedidoBase = new PedidoPersonalizado();
        pedidoBase.setIdUsuario(10L);
        pedidoBase.setNomePedido("Troféu FIFA");
        pedidoBase.setDescricao("Réplica em escala 1:1 do troféu da Copa do Mundo");
        pedidoBase.setQuantidade(1);
        pedidoBase.setNomeCliente("Cliente Teste");
        pedidoBase.setWhatsapp("81999990000");
        // status default definido no próprio campo da entidade = AGUARDANDO_ORCAMENTO
    }

    // ─────────────────────────────────────────────
    // TC-PERS-001 a 003 — Criação
    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Criação de pedido")
    class CriacaoTest {

        @Test
        @DisplayName("TC-PERS-001 — criar() persiste pedido e retorna DTO com status AGUARDANDO_ORCAMENTO")
        void criar_persistePedidoComStatusAguardandoOrcamento() {
            when(repository.save(any(PedidoPersonalizado.class))).thenReturn(pedidoBase);
            doNothing().when(telegramService).enviarMensagem(anyString());

            PedidoPersonalizadoResponseDTO resultado = service.criar(10L, buildRequest("Troféu FIFA", 1, null));

            assertThat(resultado.getStatus()).isEqualTo(StatusPedidoPersonalizado.AGUARDANDO_ORCAMENTO);
            verify(repository).save(any(PedidoPersonalizado.class));
        }

        @Test
        @DisplayName("TC-PERS-002 — criar() sem fotos dispara enviarMensagem() (texto puro)")
        void criar_semFotos_disparaEnviarMensagemTexto() {
            when(repository.save(any(PedidoPersonalizado.class))).thenReturn(pedidoBase);
            doNothing().when(telegramService).enviarMensagem(anyString());

            service.criar(10L, buildRequest("Troféu FIFA", 1, null));

            verify(telegramService, times(1)).enviarMensagem(anyString());
            verify(telegramService, never()).enviarMensagemComFoto(anyString(), any());
        }

        @Test
        @DisplayName("TC-PERS-002b — criar() com fotos dispara enviarMensagemComFoto() para a primeira")
        void criar_comFotos_disparaEnviarMensagemComFoto() {
            PedidoPersonalizado pedidoComFotos = pedidoComFotos("https://url1.jpg", "https://url2.jpg");
            when(repository.save(any(PedidoPersonalizado.class))).thenReturn(pedidoComFotos);
            doNothing().when(telegramService).enviarMensagemComFoto(anyString(), any());

            service.criar(10L, buildRequest("Com fotos", 1, List.of("https://url1.jpg", "https://url2.jpg")));

            // primeira foto com caption, segunda sem (null)
            verify(telegramService).enviarMensagemComFoto(eq("https://url1.jpg"), anyString());
            verify(telegramService).enviarMensagemComFoto(eq("https://url2.jpg"), isNull());
            verify(telegramService, never()).enviarMensagem(anyString());
        }

        @Test
        @DisplayName("TC-PERS-003 — criar() define quantidade = 1 quando DTO informa null")
        void criar_defineQuantidadeUm_quandoNulo() {
            // Cria DTO com quantidade null — service deve defaultar para 1
            PedidoPersonalizadoRequestDTO dto = buildRequest("Item X", null, null);

            ArgumentCaptor<PedidoPersonalizado> captor = ArgumentCaptor.forClass(PedidoPersonalizado.class);
            when(repository.save(captor.capture())).thenReturn(pedidoBase);
            doNothing().when(telegramService).enviarMensagem(anyString());

            service.criar(10L, dto);

            assertThat(captor.getValue().getQuantidade()).isEqualTo(1);
        }
    }

    // ─────────────────────────────────────────────
    // TC-PERS-004 a 007 — Listagem e busca
    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Listagem e busca")
    class ListagemTest {

        @Test
        @DisplayName("TC-PERS-004 — listarPorUsuario() retorna apenas pedidos do usuário informado")
        void listarPorUsuario_retornaListaDoUsuario() {
            when(repository.findByIdUsuarioOrderByCriadoEmDesc(10L)).thenReturn(List.of(pedidoBase));

            List<PedidoPersonalizadoResponseDTO> resultado = service.listarPorUsuario(10L);

            assertThat(resultado).hasSize(1);
        }

        @Test
        @DisplayName("TC-PERS-005 — buscarPorIdEUsuario() retorna pedido quando id e usuário conferem")
        void buscarPorIdEUsuario_retornaQuandoEncontrado() {
            when(repository.findByIdAndIdUsuario(1L, 10L)).thenReturn(Optional.of(pedidoBase));

            PedidoPersonalizadoResponseDTO resultado = service.buscarPorIdEUsuario(10L, 1L);

            assertThat(resultado.getNomePedido()).isEqualTo("Troféu FIFA");
        }

        @Test
        @DisplayName("TC-PERS-006 — buscarPorIdEUsuario() lança RuntimeException se não encontrado")
        void buscarPorIdEUsuario_lancaExcecao_quandoNaoEncontrado() {
            when(repository.findByIdAndIdUsuario(99L, 10L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.buscarPorIdEUsuario(10L, 99L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("não encontrado");
        }

        @Test
        @DisplayName("TC-PERS-007 — listarTodos() retorna todos os pedidos (admin)")
        void listarTodos_retornaTodosOsPedidos() {
            PedidoPersonalizado p2 = new PedidoPersonalizado();
            p2.setIdUsuario(20L);
            p2.setNomePedido("Outro pedido");
            p2.setStatus(StatusPedidoPersonalizado.ORCAMENTO_ENVIADO);

            when(repository.findAllByOrderByCriadoEmDesc()).thenReturn(List.of(pedidoBase, p2));

            assertThat(service.listarTodos()).hasSize(2);
        }
    }

    // ─────────────────────────────────────────────
    // TC-PERS-008 a 009 — Atualização de status
    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Atualização de status")
    class AtualizacaoStatusTest {

        @Test
        @DisplayName("TC-PERS-008 — atualizarStatus() muda status para EM_PRODUCAO corretamente")
        void atualizarStatus_mudaStatusParaEmProducao() {
            PedidoPersonalizado atualizado = new PedidoPersonalizado();
            atualizado.setStatus(StatusPedidoPersonalizado.EM_PRODUCAO);
            atualizado.setNomePedido("Troféu FIFA");

            when(repository.findById(1L)).thenReturn(Optional.of(pedidoBase));
            when(repository.save(any(PedidoPersonalizado.class))).thenReturn(atualizado);

            AtualizarStatusPersonalizadoDTO dto = new AtualizarStatusPersonalizadoDTO();
            dto.setStatus(StatusPedidoPersonalizado.EM_PRODUCAO);

            PedidoPersonalizadoResponseDTO resultado = service.atualizarStatus(1L, dto);

            assertThat(resultado.getStatus()).isEqualTo(StatusPedidoPersonalizado.EM_PRODUCAO);
        }

        @Test
        @DisplayName("TC-PERS-008b — atualizarStatus() aceita todos os valores válidos do enum")
        void atualizarStatus_aceitaTodosOsValoresDoEnum() {
            for (StatusPedidoPersonalizado status : StatusPedidoPersonalizado.values()) {
                PedidoPersonalizado retorno = new PedidoPersonalizado();
                retorno.setStatus(status);

                when(repository.findById(1L)).thenReturn(Optional.of(pedidoBase));
                when(repository.save(any(PedidoPersonalizado.class))).thenReturn(retorno);

                AtualizarStatusPersonalizadoDTO dto = new AtualizarStatusPersonalizadoDTO();
                dto.setStatus(status);

                PedidoPersonalizadoResponseDTO resultado = service.atualizarStatus(1L, dto);
                assertThat(resultado.getStatus()).isEqualTo(status);
            }
        }

        @Test
        @DisplayName("TC-PERS-009 — atualizarStatus() lança RuntimeException se pedido não existe")
        void atualizarStatus_lancaExcecao_quandoNaoExiste() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            AtualizarStatusPersonalizadoDTO dto = new AtualizarStatusPersonalizadoDTO();
            dto.setStatus(StatusPedidoPersonalizado.CANCELADO);

            assertThatThrownBy(() -> service.atualizarStatus(99L, dto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("não encontrado");
        }
    }

    // ─────────────────────────────────────────────
    // TC-PERS-010 a 011 — Deleção
    // ─────────────────────────────────────────────
    @Nested
    @DisplayName("Deleção de pedido")
    class DelecaoTest {

        @Test
        @DisplayName("TC-PERS-010 — deletar() chama deleteById para pedido existente")
        void deletar_removePedidoExistente() {
            when(repository.existsById(1L)).thenReturn(true);

            service.deletar(1L);

            verify(repository).deleteById(1L);
        }

        @Test
        @DisplayName("TC-PERS-011 — deletar() lança RuntimeException para id inexistente")
        void deletar_lancaExcecao_quandoNaoExiste() {
            when(repository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> service.deletar(99L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("não encontrado");
        }
    }

    // ─── helpers ────────────────────────────────
    private PedidoPersonalizadoRequestDTO buildRequest(String nome, Integer quantidade, List<String> fotos) {
        PedidoPersonalizadoRequestDTO dto = new PedidoPersonalizadoRequestDTO();
        dto.setNomePedido(nome);
        dto.setDescricao("Descrição de teste");
        dto.setQuantidade(quantidade);
        dto.setNomeCliente("Cliente Teste");
        dto.setWhatsapp("81999990000");
        dto.setFotosReferencia(fotos);
        return dto;
    }

    private PedidoPersonalizado pedidoComFotos(String... urls) {
        PedidoPersonalizado p = new PedidoPersonalizado();
        p.setIdUsuario(10L);
        p.setNomePedido("Com fotos");
        p.setDescricao("Descrição");
        p.setQuantidade(1);
        p.setNomeCliente("Cliente");
        p.setWhatsapp("81999990000");
        p.setFotosReferencia(List.of(urls));
        return p;
    }
}