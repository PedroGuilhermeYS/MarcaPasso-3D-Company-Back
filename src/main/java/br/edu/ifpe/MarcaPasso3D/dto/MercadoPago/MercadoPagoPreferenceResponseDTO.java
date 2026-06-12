package br.edu.ifpe.MarcaPasso3D.dto.MercadoPago;

/**
 * Resposta retornada ao frontend com o link de redirecionamento do Checkout Pro.
 */
public class MercadoPagoPreferenceResponseDTO {

    private String preferenceId;
    private String initPoint;        // URL de produção → redirecionar o usuário
    private String sandboxInitPoint; // URL de sandbox → usar em testes

    public MercadoPagoPreferenceResponseDTO() {}

    public MercadoPagoPreferenceResponseDTO(String preferenceId, String initPoint, String sandboxInitPoint) {
        this.preferenceId = preferenceId;
        this.initPoint = initPoint;
        this.sandboxInitPoint = sandboxInitPoint;
    }

    public String getPreferenceId() { return preferenceId; }
    public void setPreferenceId(String preferenceId) { this.preferenceId = preferenceId; }

    public String getInitPoint() { return initPoint; }
    public void setInitPoint(String initPoint) { this.initPoint = initPoint; }

    public String getSandboxInitPoint() { return sandboxInitPoint; }
    public void setSandboxInitPoint(String sandboxInitPoint) { this.sandboxInitPoint = sandboxInitPoint; }
}
