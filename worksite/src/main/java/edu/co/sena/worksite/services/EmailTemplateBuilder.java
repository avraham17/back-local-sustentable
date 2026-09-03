package edu.co.sena.worksite.services;

public class EmailTemplateBuilder {

    public static String construir(String titulo, String saludo, String cuerpo, String colorBoton) {

        return """
                <!DOCTYPE html>
                        <html>
                        <body style="margin:0; padding:0; background-color:#f4f4f7; font-family: Arial, Helvetica, sans-serif;">
                        <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f4f7; padding: 30px 0;">
                        <tr>
                        <td align="center">
                        <table width="500" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:8px; overflow:hidden; box-shadow: 0 2px 6px rgba(0,0,0,0.08);">
                        <tr>
                        <td style="background-color:#ffffff; padding: 24px 32px; text-align:center; border-bottom: 1px solid #e2e8f0;">
                        <img src="cid:logoWorksite" alt="WorkSite" style="height:70px; display:inline-block;">
                        </td>
                        </tr>
                        <tr>
                        <td style="padding: 32px;">
                        <h2 style="color:#1e293b; margin-top:0;">%s</h2>
                        <p style="color:#334155; font-size:15px; line-height:1.6;">%s</p>
                        <div style="color:#334155; font-size:15px; line-height:1.6; margin-top: 16px;">%s</div>
                        </td>
                        </tr>
                        <tr>
                        <td style="background-color:#f8fafc; padding: 20px 32px; text-align:center;">
                        <p style="color:#94a3b8; font-size:12px; margin:0;">Este es un mensaje automático de WorkSite, no respondas a este correo.</p>
                        </td>
                        </tr>
                        </table>
                        </td>
                        </tr>
                        </table>
                        </body>
                        </html>
                """.formatted(titulo, saludo, cuerpo);
    }
}

