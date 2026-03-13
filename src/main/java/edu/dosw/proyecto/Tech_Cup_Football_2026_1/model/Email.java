package edu.dosw.proyecto.Tech_Cup_Football_2026_1.model;

public class Email {
    private Long id;
    private String destinatario;
    private String asunto;
    private String cuerpo;
    private String remitente;
    private boolean enviado;

    public Email() {
    }

    public Email(Long id, String destinatario, String asunto, String cuerpo, String remitente, boolean enviado) {
        this.id = id;
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.remitente = remitente;
        this.enviado = enviado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", destinatario='" + destinatario + '\'' +
                ", asunto='" + asunto + '\'' +
                ", remitente='" + remitente + '\'' +
                ", enviado=" + enviado +
                '}';
    }
}
