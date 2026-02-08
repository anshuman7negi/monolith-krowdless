package com.krowdless.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender javaMailSender;
	
	//@Value("${spring.mail.username}")
    private String fromEmail;
	
    public void sendOtpEmail(String email, String otp, String subject) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject(subject);

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                  <style>
                    body { margin:0; padding:0; background:#fdf2f8; font-family:'Segoe UI', Arial, sans-serif; }
                    .container { max-width:600px; margin:30px auto; background:#fff; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.1); overflow:hidden; }
                    .header { background: #ffffff; padding:20px; text-align:center; }
                    .content { padding:40px 30px; text-align:center; }
                    .content h2 { color:#111; font-size:26px; margin-bottom:15px; }
                    .content p { color:#555; font-size:16px; line-height:1.6; margin-bottom:20px; }
                    .otp-box { display:inline-block; background:#FA4548; color:#fff; font-size:30px; font-weight:bold; padding:15px 45px; border-radius:10px; letter-spacing:4px; margin:20px 0; }
                    .footer { background:linear-gradient(90deg,#FA4548,#496EFF); padding:20px; text-align:center; font-size:14px; color:#fff; }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <div class="header">
                      <img src="https://javasrvbucket.s3.amazonaws.com/e06101c2-925e-4b2b-b3c1-66abe2c81590.png"
                           alt="Krowdless Logo"
                           style="max-height:60px; width:auto; display:block; margin:0 auto;" />
                    </div>
                    <div class="content">
                      <h2>Verify Your Account</h2>
                      <p>Use the OTP below to complete your verification process:</p>
                      <div class="otp-box">%%OTP%%</div>
                      <p>If you didn’t request this, you can safely ignore this email.</p>
                      <p>Need help? <a href="mailto:support@krowdless.com">Contact Support</a></p>
                    </div>
                    <div class="footer">
                      <p>Thanks for being part of Krowdless! 💖</p>
                      <p>&copy; 2026 Krowdless. All rights reserved.</p>
                      <p><a href="https://krowdless.com">www.krowdless.com</a></p>
                    </div>
                  </div>
                </body>
                </html>
                """;

            // Replace OTP placeholder
            htmlContent = htmlContent.replace("%%OTP%%", otp);

            helper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
    
}
