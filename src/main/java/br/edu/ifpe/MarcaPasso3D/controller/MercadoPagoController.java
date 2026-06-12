package br.edu.ifpe.MarcaPasso3D.controller;

import br.edu.ifpe.MarcaPasso3D.dto.MercadoPago.MercadoPagoPreferenceRequestDTO;
import br.edu.ifpe.MarcaPasso3D.dto.MercadoPago.MercadoPagoPreferenceResponseDTO;
import br.edu.ifpe.MarcaPasso3D.service.MercadoPagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos/mercadopago")
public class MercadoPagoController {

    private final MercadoPagoService mercadoPagoService;

    public MercadoPagoController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    /**
     * POST /api/pagamentos/mercadopago/preference
     *
     * Recebe os itens do pedido, cria uma Preference no Mercado Pago
     * e retorna o link de checkout (initPoint / sandboxInitPoint).
     *
     * O frontend redireciona o usuário para esse link.
     */
    @PostMapping("/preference")
    public ResponseEntity<MercadoPagoPreferenceResponseDTO> criarPreference(
            @RequestBody MercadoPagoPreferenceRequestDTO request) {

        MercadoPagoPreferenceResponseDTO response = mercadoPagoService.criarPreference(request);
        return ResponseEntity.ok(response);
    }
}
