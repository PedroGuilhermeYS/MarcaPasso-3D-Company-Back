package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.Encomenda.CriarEncomendaDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Encomenda.EncomendaDetalheDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Encomenda.EncomendaItemDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Encomenda.EncomendaResumoDTO;
import br.edu.ifpe.MarcaPasso3D.model.Encomenda.Encomenda;
import br.edu.ifpe.MarcaPasso3D.model.Encomenda.EncomendaItem;
import br.edu.ifpe.MarcaPasso3D.model.Produto;
import br.edu.ifpe.MarcaPasso3D.repository.Encomenda.EncomendaRepository;
import br.edu.ifpe.MarcaPasso3D.repository.Produto.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class EncomendaService {

    private final EncomendaRepository encomendaRepository;
    private final ProdutoRepository produtoRepository;
    private final ProdutoService produtoService;
    private final TelegramService telegramService;

    public EncomendaService(EncomendaRepository encomendaRepository,
                             ProdutoRepository produtoRepository,
                             ProdutoService produtoService,
                             TelegramService telegramService) {
        this.encomendaRepository = encomendaRepository;
        this.produtoRepository = produtoRepository;
        this.produtoService = produtoService;
        this.telegramService = telegramService;
    }

    // ── GET: listar resumo de pedidos do usuário ──────────────
    public List<EncomendaResumoDTO> listarPorUsuario(Long idUsuario) {
        return encomendaRepository
                .findByIdUsuarioOrderByDataHoraDesc(idUsuario)
                .stream()
                .map(e -> new EncomendaResumoDTO(
                        e.getId(),
                        e.getNumeroPedido(),
                        e.getDataHora(),
                        e.getFormaPagamento(),
                        e.getStatus(),
                        e.getTotal()))
                .collect(Collectors.toList());
    }

    // ── GET: detalhe completo de um pedido ────────────────────
    public EncomendaDetalheDTO buscarDetalhe(Long idUsuario, Long idEncomenda) {
        Encomenda e = encomendaRepository
                .findByIdAndIdUsuario(idEncomenda, idUsuario)
                .orElseThrow(() -> new RuntimeException("Encomenda não encontrada: " + idEncomenda));

        return toDetalheDTO(e);
    }

    // ── POST: criar novo pedido ───────────────────────────────
    @Transactional
    public EncomendaDetalheDTO criarEncomenda(Long idUsuario, CriarEncomendaDTO dto) {
        Encomenda encomenda = new Encomenda();

        encomenda.setIdUsuario(idUsuario);
        encomenda.setNumeroPedido(gerarNumeroPedido());
        encomenda.setStatus(br.edu.ifpe.MarcaPasso3D.model.Encomenda.StatusEncomenda.PENDENTE);

        // Valores
        encomenda.setSubtotal(dto.getSubtotal());
        encomenda.setFrete(dto.getFrete());
        encomenda.setDesconto(dto.getDesconto() != null ? dto.getDesconto() : java.math.BigDecimal.ZERO);
        encomenda.setDescontoCupom(dto.getDescontoCupom() != null ? dto.getDescontoCupom() : java.math.BigDecimal.ZERO);
        encomenda.setTotal(dto.getTotal());

        // Pagamento
        encomenda.setFormaPagamento(dto.getFormaPagamento());
        encomenda.setTipoPagamento(dto.getTipoPagamento());
        encomenda.setBandeiraCartao(dto.getBandeiraCartao());
        encomenda.setParcelamento(dto.getParcelamento());
        encomenda.setTitularCartao(dto.getTitularCartao());
        encomenda.setCpfTitular(dto.getCpfTitular());
        encomenda.setBinCartao(dto.getBinCartao());

        // Endereço
        encomenda.setEndRua(dto.getEndRua());
        encomenda.setEndNumero(dto.getEndNumero());
        encomenda.setEndComplemento(dto.getEndComplemento());
        encomenda.setEndBairro(dto.getEndBairro());
        encomenda.setEndCidade(dto.getEndCidade());
        encomenda.setEndEstado(dto.getEndEstado());
        encomenda.setEndCep(dto.getEndCep());

        // Cliente
        encomenda.setClienteNome(dto.getClienteNome());
        encomenda.setClienteEmail(dto.getClienteEmail());
        encomenda.setClienteCpf(dto.getClienteCpf());

        // Salvar encomenda primeiro para obter ID
        Encomenda salva = encomendaRepository.save(encomenda);

        // Itens
        String fotoUrl = null;
        StringBuilder listaItens = new StringBuilder();

        if (dto.getItens() != null) {
            for (CriarEncomendaDTO.ItemDTO itemDTO : dto.getItens()) {
                Produto produto = produtoRepository.findById(itemDTO.getIdProduto())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDTO.getIdProduto()));

                produtoService.incrementarVendas(itemDTO.getIdProduto(), itemDTO.getQuantidade());

                EncomendaItem item = new EncomendaItem();
                item.setEncomenda(salva);
                item.setProduto(produto);
                item.setQuantidade(itemDTO.getQuantidade());
                item.setPrecoUnitario(itemDTO.getPrecoUnitario() != null
                        ? itemDTO.getPrecoUnitario()
                        : produto.getPreco());

                salva.getItens().add(item);

                // Pega a foto do primeiro produto para o Telegram
                if (fotoUrl == null && produto.getImagemPrincipal() != null) {
                    fotoUrl = produto.getImagemPrincipal();
                }

                // Monta lista de itens para a mensagem
                listaItens.append("  • ")
                          .append(produto.getNome())
                          .append(" x").append(itemDTO.getQuantidade())
                          .append(" — R$ ").append(item.getPrecoUnitario())
                          .append("\n");
            }
            encomendaRepository.save(salva);
        }

        // ── Notificação Telegram ──────────────────────────────
        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"));

        String endereco = salva.getEndRua() + ", " + salva.getEndNumero()
                + (salva.getEndComplemento() != null && !salva.getEndComplemento().isBlank()
                    ? " - " + salva.getEndComplemento() : "")
                + "\n  " + salva.getEndBairro()
                + " — " + salva.getEndCidade() + "/" + salva.getEndEstado()
                + " — CEP: " + salva.getEndCep();

        String mensagem =
            "━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "🛍️ <b>NOVO PEDIDO RECEBIDO</b>\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━\n\n" +

            "🔢 <b>Pedido:</b> " + salva.getNumeroPedido() + "\n" +
            "📅 <b>Data:</b> " + dataHora + "\n\n" +

            "👤 <b>CLIENTE</b>\n" +
            "Nome: " + salva.getClienteNome() + "\n" +
            "Email: " + salva.getClienteEmail() + "\n\n" +

            "📦 <b>ITENS</b>\n" +
            listaItens +

            "\n💳 <b>PAGAMENTO</b>\n" +
            "Forma: " + salva.getFormaPagamento() + "\n" +
            "Subtotal: R$ " + salva.getSubtotal() + "\n" +
            "Frete: R$ " + salva.getFrete() + "\n" +
            (salva.getDescontoCupom() != null && salva.getDescontoCupom().compareTo(java.math.BigDecimal.ZERO) > 0
                ? "Desconto: -R$ " + salva.getDescontoCupom() + "\n" : "") +
            "<b>Total: R$ " + salva.getTotal() + "</b>\n\n" +

            "🚚 <b>ENTREGA</b>\n" +
            endereco + "\n\n" +

            "⏳ <b>Status:</b> PENDENTE";

        telegramService.enviarMensagemComFoto(fotoUrl, mensagem);

        return toDetalheDTO(salva);
    }

    // ── Helpers ───────────────────────────────────────────────

    private String gerarNumeroPedido() {
        String prefixo = "MP-";
        String numero;
        int tentativas = 0;
        do {
            numero = prefixo + String.format("%05d", ThreadLocalRandom.current().nextInt(1, 99999));
            tentativas++;
            if (tentativas > 10)
                throw new RuntimeException("Não foi possível gerar número de pedido único");
        } while (encomendaRepository.existsByNumeroPedido(numero));
        return numero;
    }

    private EncomendaDetalheDTO toDetalheDTO(Encomenda e) {
        EncomendaDetalheDTO dto = new EncomendaDetalheDTO();

        dto.setId(e.getId());
        dto.setNumeroPedido(e.getNumeroPedido());
        dto.setDataHora(e.getDataHora());
        dto.setStatus(e.getStatus());

        dto.setSubtotal(e.getSubtotal());
        dto.setFrete(e.getFrete());
        dto.setDesconto(e.getDesconto());
        dto.setDescontoCupom(e.getDescontoCupom());
        dto.setTotal(e.getTotal());

        dto.setFormaPagamento(e.getFormaPagamento());
        dto.setTipoPagamento(e.getTipoPagamento());
        dto.setBandeiraCartao(e.getBandeiraCartao());
        dto.setParcelamento(e.getParcelamento());
        dto.setTitularCartao(e.getTitularCartao());
        dto.setCpfTitular(e.getCpfTitular());
        dto.setBinCartao(e.getBinCartao());

        dto.setEndRua(e.getEndRua());
        dto.setEndNumero(e.getEndNumero());
        dto.setEndComplemento(e.getEndComplemento());
        dto.setEndBairro(e.getEndBairro());
        dto.setEndCidade(e.getEndCidade());
        dto.setEndEstado(e.getEndEstado());
        dto.setEndCep(e.getEndCep());

        dto.setClienteNome(e.getClienteNome());
        dto.setClienteEmail(e.getClienteEmail());
        dto.setClienteCpf(e.getClienteCpf());

        List<EncomendaItemDTO> itensDTO = e.getItens().stream()
                .map(item -> new EncomendaItemDTO(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getProduto().getImagemPrincipal(),
                        item.getQuantidade(),
                        item.getPrecoUnitario()))
                .collect(Collectors.toList());

        dto.setItens(itensDTO);
        return dto;
    }
}