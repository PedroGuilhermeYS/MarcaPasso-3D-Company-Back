package br.edu.ifpe.MarcaPasso3D.dto.Personalizado;

import br.edu.ifpe.MarcaPasso3D.model.Personalizado.StatusPedidoPersonalizado;
import jakarta.validation.constraints.NotNull;

public class AtualizarStatusPersonalizadoDTO {

    @NotNull(message = "Status é obrigatório")
    private StatusPedidoPersonalizado status;

    public StatusPedidoPersonalizado getStatus() { return status; }
    public void setStatus(StatusPedidoPersonalizado status) { this.status = status; }
}
