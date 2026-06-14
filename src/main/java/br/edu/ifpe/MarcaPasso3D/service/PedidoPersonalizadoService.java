package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.Personalizado.AtualizarStatusPersonalizadoDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Personalizado.PedidoPersonalizadoRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.Personalizado.PedidoPersonalizadoResponseDTO;
import br.edu.ifpe.MarcaPasso3D.model.Personalizado.PedidoPersonalizado;
import br.edu.ifpe.MarcaPasso3D.model.Personalizado.StatusPedidoPersonalizado;
import br.edu.ifpe.MarcaPasso3D.repository.Personalizado.PedidoPersonalizadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoPersonalizadoService {

    private final PedidoPersonalizadoRepository repository;
    private final TelegramService telegramService;

    public PedidoPersonalizadoService(PedidoPersonalizadoRepository repository,
                                      TelegramService telegramService) {
        this.repository      = repository;
        this.telegramService = telegramService;
    }

    // ── Criar (usuário autenticado) ──────────────────────────

    @Transactional
    public PedidoPersonalizadoResponseDTO criar(Long idUsuario, PedidoPersonalizadoRequestDTO dto) {
        PedidoPersonalizado pedido = new PedidoPersonalizado();

        pedido.setIdUsuario(idUsuario);
        pedido.setNomePedido(dto.getNomePedido());
        pedido.setDescricao(dto.getDescricao());
        pedido.setFinalidade(dto.getFinalidade());
        pedido.setTamanho(dto.getTamanho());
        pedido.setQuantidade(dto.getQuantidade() != null ? dto.getQuantidade() : 1);
        pedido.setCores(dto.getCores());

        if (dto.getFotosReferencia() != null) {
            pedido.setFotosReferencia(dto.getFotosReferencia());
        }

        pedido.setNomeCliente(dto.getNomeCliente());
        pedido.setWhatsapp(dto.getWhatsapp());
        pedido.setPrazoDesejadoDias(dto.getPrazoDesejadoDias());

        PedidoPersonalizado salvo = repository.save(pedido);

        notificarTelegram(salvo);

        return toResponse(salvo);
    }

    // ── Listar do usuário (tela "Meus Pedidos") ──────────────

    @Transactional(readOnly = true)
    public List<PedidoPersonalizadoResponseDTO> listarPorUsuario(Long idUsuario) {
        return repository.findByIdUsuarioOrderByCriadoEmDesc(idUsuario)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Buscar detalhe do usuário ────────────────────────────

    @Transactional(readOnly = true)
    public PedidoPersonalizadoResponseDTO buscarPorIdEUsuario(Long idUsuario, Long id) {
        PedidoPersonalizado pedido = repository.findByIdAndIdUsuario(id, idUsuario)
                .orElseThrow(() -> new RuntimeException("Pedido personalizado não encontrado: " + id));
        return toResponse(pedido);
    }

    // ── Listar todos (somente ADMIN) ─────────────────────────

    @Transactional(readOnly = true)
    public List<PedidoPersonalizadoResponseDTO> listarTodos() {
        return repository.findAllByOrderByCriadoEmDesc()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Listar por status (somente ADMIN) ────────────────────

    @Transactional(readOnly = true)
    public List<PedidoPersonalizadoResponseDTO> listarPorStatus(StatusPedidoPersonalizado status) {
        return repository.findByStatusOrderByCriadoEmDesc(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Buscar por id (somente ADMIN) ────────────────────────

    @Transactional(readOnly = true)
    public PedidoPersonalizadoResponseDTO buscarPorId(Long id) {
        PedidoPersonalizado pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido personalizado não encontrado: " + id));
        return toResponse(pedido);
    }

    // ── Atualizar status (somente ADMIN) ─────────────────────

    @Transactional
    public PedidoPersonalizadoResponseDTO atualizarStatus(Long id, AtualizarStatusPersonalizadoDTO dto) {
        PedidoPersonalizado pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido personalizado não encontrado: " + id));

        pedido.setStatus(dto.getStatus());
        return toResponse(repository.save(pedido));
    }

    // ── Deletar (somente ADMIN) ──────────────────────────────

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Pedido personalizado não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    // ── Notificação Telegram ─────────────────────────────────

    private void notificarTelegram(PedidoPersonalizado p) {
        String mensagem = montarMensagemTelegram(p);

        boolean temFotos = p.getFotosReferencia() != null && !p.getFotosReferencia().isEmpty();

        if (temFotos) {
            telegramService.enviarMensagemComFoto(p.getFotosReferencia().get(0), mensagem);

            List<String> demais = p.getFotosReferencia().stream().skip(1).collect(Collectors.toList());
            for (String url : demais) {
                telegramService.enviarMensagemComFoto(url, null);
            }
        } else {
            telegramService.enviarMensagem(mensagem);
        }
    }

    private String montarMensagemTelegram(PedidoPersonalizado p) {
        StringBuilder sb = new StringBuilder();
        sb.append("🖨️ <b>Novo Pedido Personalizado!</b>\n");
        sb.append("━━━━━━━━━━━━━━━━━━━━\n\n");

        sb.append("📦 <b>Sobre o pedido</b>\n");
        sb.append("• <b>Nome:</b> ").append(p.getNomePedido()).append("\n");
        sb.append("• <b>Descrição:</b> ").append(p.getDescricao()).append("\n");

        if (p.getFinalidade() != null && !p.getFinalidade().isBlank()) {
            sb.append("• <b>Finalidade:</b> ").append(p.getFinalidade()).append("\n");
        }
        if (p.getTamanho() != null && !p.getTamanho().isBlank()) {
            sb.append("• <b>Tamanho:</b> ").append(p.getTamanho()).append("\n");
        }

        sb.append("• <b>Quantidade:</b> ").append(p.getQuantidade()).append(" unidade(s)\n");

        if (p.getCores() != null && !p.getCores().isBlank()) {
            sb.append("• <b>Cores:</b> ").append(p.getCores()).append("\n");
        }

        sb.append("\n👤 <b>Contato</b>\n");
        sb.append("• <b>Nome:</b> ").append(p.getNomeCliente()).append("\n");
        sb.append("• <b>WhatsApp:</b> ").append(p.getWhatsapp()).append("\n");

        if (p.getPrazoDesejadoDias() != null) {
            sb.append("• <b>Prazo desejado:</b> ").append(p.getPrazoDesejadoDias()).append(" dias\n");
        }

        int qtdFotos = (p.getFotosReferencia() != null) ? p.getFotosReferencia().size() : 0;
        sb.append("\n📷 <b>Fotos de referência:</b> ").append(qtdFotos).append("\n");

        sb.append("\n⏳ <i>Status: Aguardando orçamento</i>");

        return sb.toString();
    }

    // ── Mapper ───────────────────────────────────────────────

    private PedidoPersonalizadoResponseDTO toResponse(PedidoPersonalizado p) {
        PedidoPersonalizadoResponseDTO dto = new PedidoPersonalizadoResponseDTO();
        dto.setId(p.getId());
        dto.setIdUsuario(p.getIdUsuario());
        dto.setNomePedido(p.getNomePedido());
        dto.setDescricao(p.getDescricao());
        dto.setFinalidade(p.getFinalidade());
        dto.setTamanho(p.getTamanho());
        dto.setQuantidade(p.getQuantidade());
        dto.setCores(p.getCores());
        dto.setFotosReferencia(p.getFotosReferencia());
        dto.setNomeCliente(p.getNomeCliente());
        dto.setWhatsapp(p.getWhatsapp());
        dto.setPrazoDesejadoDias(p.getPrazoDesejadoDias());
        dto.setStatus(p.getStatus());
        dto.setCriadoEm(p.getCriadoEm());
        dto.setAtualizadoEm(p.getAtualizadoEm());
        return dto;
    }
}
