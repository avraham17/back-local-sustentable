package edu.co.sena.worksite.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void enviarCorreoHtml(String destinatario, String asunto, String contenidoHtml) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true);

            try {
                ClassPathResource logo = new ClassPathResource("static/images/logo.png");
                if (logo.exists()) {
                    helper.addInline("logoWorksite", logo);
                }
            } catch (Exception logoError) {

                System.err.println("No se pudo adjuntar el logo (se envía el correo igual): " + logoError.getMessage());
            }

            mailSender.send(mensaje);
            System.out.println(">>> Correo HTML enviado a: " + destinatario);

        } catch (Exception e) {
            System.err.println(">>> ERROR enviando correo a " + destinatario + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String construirCorreoBienvenida(String nombre) {
        String nombreSeguro = escaparHtml(nombre);
        return "<html>"
                + "<body style=\"font-family: Arial, sans-serif; color: #333;\">"
                + "<img src=\"cid:logoWorksite\" alt=\"Worksite\" style=\"max-width:180px; margin-bottom:16px;\" />"
                + "<p>Hola " + nombreSeguro + ",</p>"
                + "<p>Tu cuenta ha sido creada exitosamente. ¡Ya puedes disfrutar de nuestros servicios!</p>"
                + "<p>Saludos,<br/>El equipo de Worksite</p>"
                + "</body>"
                + "</html>";
    }

    private String escaparHtml(String texto) {
        if (texto == null) {
            return "";
        }   
        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}