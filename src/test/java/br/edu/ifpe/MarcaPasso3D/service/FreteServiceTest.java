package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.Frete.FreteRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Frete.FreteResponseDTO;
import br.edu.ifpe.MarcaPasso3D.model.Frete.Frete;
import br.edu.ifpe.MarcaPasso3D.repository.Frete.FreteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreteServiceTest {

    @Mock
    FreteRepository repository;

    @InjectMocks
    FreteService service;

    private Frete freteBase;

    @BeforeEach
    void setUp() {
        freteBase = new Frete();
        freteBase.setId(1L);
        freteBase.setCep("55535000");
        freteBase.setCidade("Joaquim Nabuco - PE");
        freteBase.setPreco(new BigDecimal("25.00"));
        freteBase.setPrazoEntregaDias(5);
    }

    @Test
    void listarTodos_retornaListaMapeada() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(List.of(freteBase));

        List<FreteResponseDTO> resultado = service.listarTodos();

        assertThat(resultado).hasSize(1);
        FreteResponseDTO dto = resultado.get(0);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getCidade()).isEqualTo("Joaquim Nabuco - PE");
        assertThat(dto.getCepDestino()).isEqualTo("55535000");
        assertThat(dto.getCepEntrega()).isEqualTo("55535-000");
        assertThat(dto.getValorFrete()).isEqualByComparingTo("25.00");
        assertThat(dto.getPrazoEntregaDias()).isEqualTo(5);
    }

    @Test
    void listarTodos_retornaListaVaziaQuandoNaoHaDados() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(List.of());

        assertThat(service.listarTodos()).isEmpty();
    }

    @Test
    void buscarPorId_retornaDTO_quandoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.of(freteBase));

        FreteResponseDTO dto = service.buscarPorId(1L);

        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    void buscarPorId_lancaExcecao_quandoNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }


    @Test
    void buscarPorCep_normalizaHifen_eRetornaDTO() {
        when(repository.findByCep("55535000")).thenReturn(Optional.of(freteBase));

        FreteResponseDTO dto = service.buscarPorCep("55535-000");

        assertThat(dto.getCepDestino()).isEqualTo("55535000");
        assertThat(dto.getCepEntrega()).isEqualTo("55535-000");
    }

    @Test
    void buscarPorCep_lancaExcecao_quandoCepNaoExiste() {
        when(repository.findByCep(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorCep("00000000"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("00000000");
    }

    @Test
    void criar_salvaNoBancoCepSemHifen() {
        when(repository.findByCep("55535000")).thenReturn(Optional.empty());
        when(repository.save(any(Frete.class))).thenReturn(freteBase);

        FreteRequestDTO dto = buildRequest("55535-000", "Joaquim Nabuco - PE", "25.00", 5);
        FreteResponseDTO resultado = service.criar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(repository).save(argThat(f -> "55535000".equals(f.getCep())));
    }

    @Test
    void criar_lancaExcecao_quandoCepJaExiste() {
        when(repository.findByCep("55535000")).thenReturn(Optional.of(freteBase));

        FreteRequestDTO dto = buildRequest("55535-000", "Qualquer cidade", "10.00", 3);

        assertThatThrownBy(() -> service.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("55535000");
    }

    @Test
    void atualizar_alteraDadosCorretamente() {
        Frete atualizado = new Frete();
        atualizado.setId(1L);
        atualizado.setCep("51160220");
        atualizado.setCidade("Recife - PE");
        atualizado.setPreco(new BigDecimal("15.00"));
        atualizado.setPrazoEntregaDias(7);

        when(repository.findById(1L)).thenReturn(Optional.of(freteBase));
        when(repository.findByCep("51160220")).thenReturn(Optional.empty());
        when(repository.save(any(Frete.class))).thenReturn(atualizado);

        FreteRequestDTO dto = buildRequest("51160-220", "Recife - PE", "15.00", 7);
        FreteResponseDTO resultado = service.atualizar(1L, dto);

        assertThat(resultado.getCidade()).isEqualTo("Recife - PE");
        assertThat(resultado.getCepEntrega()).isEqualTo("51160-220");
    }

    @Test
    void atualizar_permiteManterMesmoCep_noMesmoRegistro() {
        when(repository.findById(1L)).thenReturn(Optional.of(freteBase));
        when(repository.findByCep("55535000")).thenReturn(Optional.of(freteBase)); // mesmo registro
        when(repository.save(any(Frete.class))).thenReturn(freteBase);

        FreteRequestDTO dto = buildRequest("55535-000", "Joaquim Nabuco - PE", "25.00", 5);

        assertThatNoException().isThrownBy(() -> service.atualizar(1L, dto));
    }

    @Test
    void deletar_removePorId() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deletar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deletar_lancaExcecao_quandoNaoExiste() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.deletar(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }


    @Test
    void normalizarCep_removeHifen() {
        assertThat(service.normalizarCep("55535-000")).isEqualTo("55535000");
    }

    @Test
    void normalizarCep_mantemSemHifen() {
        assertThat(service.normalizarCep("55535000")).isEqualTo("55535000");
    }

    @Test
    void normalizarCep_retornaNull_quandoNulo() {
        assertThat(service.normalizarCep(null)).isNull();
    }

    private FreteRequestDTO buildRequest(String cep, String cidade, String preco, int prazo) {
        FreteRequestDTO dto = new FreteRequestDTO();
        dto.setCepEntrega(cep);
        dto.setCidade(cidade);
        dto.setValorFrete(new BigDecimal(preco));
        dto.setPrazoEntregaDias(prazo);
        return dto;
    }
}