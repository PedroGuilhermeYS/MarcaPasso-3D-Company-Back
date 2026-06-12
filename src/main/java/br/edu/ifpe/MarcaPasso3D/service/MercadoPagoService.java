package br.edu.ifpe.MarcaPasso3D.service;

import br.edu.ifpe.MarcaPasso3D.dto.MercadoPago.MercadoPagoPreferenceRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.MercadoPago.MercadoPagoPreferenceResponseDTO;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    /**
     * URL base do seu frontend (ex: https://meusite.com.br ou
     * http://localhost:5173)
     * Usada para montar as URLs de retorno (success, failure, pending).
     */
    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Value("${mercadopago.sandbox:true}")
    private boolean sandbox;

    @Value("${mercadopago.payer-email-teste:}")
    private String payerEmailTeste;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    /**
     * Cria uma Preference no Mercado Pago e retorna os links do Checkout Pro.
     *
     * Documentação:
     * https://www.mercadopago.com.br/developers/pt/docs/checkout-pro/integrate-preferences
     */
    public MercadoPagoPreferenceResponseDTO criarPreference(MercadoPagoPreferenceRequestDTO req) {
        try {
            // ── 1. Monta os itens ────────────────────────────────
            List<PreferenceItemRequest> itensMp = new ArrayList<>();

            for (MercadoPagoPreferenceRequestDTO.ItemDTO item : req.getItens()) {
                itensMp.add(
                        PreferenceItemRequest.builder()
                                .title(item.getNome())
                                .quantity(item.getQuantidade())
                                .unitPrice(item.getPrecoUnitario())
                                .currencyId("BRL")
                                .build());
            }

            // Frete como item separado (se houver)
            if (req.getFrete() != null && req.getFrete().compareTo(BigDecimal.ZERO) > 0) {
                itensMp.add(
                        PreferenceItemRequest.builder()
                                .title("Frete")
                                .quantity(1)
                                .unitPrice(req.getFrete())
                                .currencyId("BRL")
                                .build());
            }

            // ── 2. Desconto como item negativo (cupom) ───────────
            if (req.getDescontoCupom() != null && req.getDescontoCupom().compareTo(BigDecimal.ZERO) > 0) {
                itensMp.add(
                        PreferenceItemRequest.builder()
                                .title("Desconto Cupom")
                                .quantity(1)
                                .unitPrice(req.getDescontoCupom().negate())
                                .currencyId("BRL")
                                .build());
            }

            // ── 3. Dados do pagador ──────────────────────────────
            String emailPagador = (sandbox && !payerEmailTeste.isBlank())
                    ? payerEmailTeste
                    : req.getClienteEmail();

            PreferencePayerRequest pagador = PreferencePayerRequest.builder()
                    .name(req.getClienteNome())
                    .email(emailPagador)
                    .build();

            // ── 4. Métodos de pagamento excluídos ────────────────
            // Se a forma for "pix", exclui cartão e vice-versa.
            PreferencePaymentMethodsRequest metodosPagamento = null;
            if ("pix".equals(req.getFormaPagamento())) {
                // Somente Pix
                metodosPagamento = PreferencePaymentMethodsRequest.builder()
                        .excludedPaymentTypes(List.of(
                                PreferencePaymentTypeRequest.builder().id("credit_card").build(),
                                PreferencePaymentTypeRequest.builder().id("debit_card").build(),
                                PreferencePaymentTypeRequest.builder().id("ticket").build()))
                        .build();
            } else if ("cartao".equals(req.getFormaPagamento())) {
                // Cartão de crédito (até 2x)
                metodosPagamento = PreferencePaymentMethodsRequest.builder()
                        .excludedPaymentTypes(List.of(
                                PreferencePaymentTypeRequest.builder().id("bank_transfer").build(),
                                PreferencePaymentTypeRequest.builder().id("ticket").build()))
                        .installments(2)
                        .build();
            }

            // ── 5. URLs de retorno ───────────────────────────────
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(frontendUrl + "/pedidos?status=sucesso")
                    .failure(frontendUrl + "/checkout?status=falha")
                    .pending(frontendUrl + "/pedidos?status=pendente")
                    .build();

            // ── 6. Monta e envia a preference ────────────────────
            PreferenceRequest.PreferenceRequestBuilder builder = PreferenceRequest.builder()
                    .items(itensMp)
                    .payer(pagador)
                    .backUrls(backUrls)
                    // .autoReturn("approved")  // <-- remova ou comente essa linha
                    .statementDescriptor("MARCAPASSO3D");

            if (metodosPagamento != null) {
                builder.paymentMethods(metodosPagamento);
            }

            PreferenceRequest preferenceRequest = builder.build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return new MercadoPagoPreferenceResponseDTO(
                    preference.getId(),
                    preference.getInitPoint(),
                    preference.getSandboxInitPoint());

        } catch (

        com.mercadopago.exceptions.MPApiException e) {
            // Extrai o corpo REAL da resposta do Mercado Pago
            String statusCode = "N/A";
            String responseBody = "N/A";
            if (e.getApiResponse() != null) {
                statusCode = String.valueOf(e.getApiResponse().getStatusCode());
                responseBody = e.getApiResponse().getContent();
            }
            System.err.println(">>> [MercadoPago] STATUS: " + statusCode);
            System.err.println(">>> [MercadoPago] BODY: " + responseBody);
            throw new RuntimeException("Erro MP [" + statusCode + "]: " + responseBody, e);

        } catch (Exception e) {
            System.err.println(">>> [MercadoPago] ERRO GENERICO: " + e.getMessage());
            throw new RuntimeException("Erro ao criar preference: " + e.getMessage(), e);
        }
    }
}
