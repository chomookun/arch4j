package org.chomookun.arch4j.core.security;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base32;
import org.chomookun.arch4j.core.user.model.User;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TotpService {

//    private final SecurityProperties securityProperties;

    public String generateTotpSecret() {
        // creates 160 bit (20 bytes) random
        byte[] buffer = new byte[20];
        new SecureRandom().nextBytes(buffer);
        // base32 encoding
        Base32 base32 = new Base32();
        return base32.encodeToString(buffer);
    }


    public String generateTotpUri(String totpSecret, String issuer, String account) {
//        String secret = user.getTotpSecret();
//        String issuer = securityProperties.getIssuer();
//        String account = user.getUsername();
        return String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
                URLEncoder.encode(issuer, StandardCharsets.UTF_8),
                URLEncoder.encode(account, StandardCharsets.UTF_8),
                totpSecret,
                URLEncoder.encode(issuer, StandardCharsets.UTF_8)
        );
    }

    public String generateTotpQrCode(String totpSecret, String issuer, String account) {
        try {
            String totpUri = generateTotpUri(totpSecret, issuer, account);
            // QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            var bitMatrix = qrCodeWriter.encode(totpUri, BarcodeFormat.QR_CODE, 300, 300, hints);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            // data uri
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            String base64 = java.util.Base64.getEncoder().encodeToString(baos.toByteArray());
            return "data:image/png;base64," + base64;
        } catch (Exception e) {
            throw new RuntimeException("QR Code error", e);
        }
    }

    public boolean verifyTotpCode(String totpSecret, String totpCode) {
        long currentTimeSeconds = Instant.now().getEpochSecond();
        long timeStep = 30;
        long timeWindow = currentTimeSeconds / timeStep;
        for (int i = -1; i <= 1; i++) { // allow 1 step ahead/behind
            String expectedCode = generateTotpCode(totpSecret, timeWindow + i);
            if (expectedCode.equals(totpCode)) {
                return true;
            }
        }
        return false;
    }

    private static String generateTotpCode(String totpSecret, long timeWindow) {
        try {
            byte[] key = new Base32().decode(totpSecret);
            byte[] data = new byte[8];
            long value = timeWindow;
            for (int i = 7; i >= 0; i--) {
                data[i] = (byte) (value & 0xFF);
                value >>= 8;
            }
            SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signKey);
            byte[] hash = mac.doFinal(data);

            int offset = hash[hash.length - 1] & 0xF;
            int binary =
                    ((hash[offset] & 0x7F) << 24) |
                            ((hash[offset + 1] & 0xFF) << 16) |
                            ((hash[offset + 2] & 0xFF) << 8) |
                            (hash[offset + 3] & 0xFF);

            int otp = binary % 1_000_000;
            return String.format("%06d", otp);
        } catch (Exception e) {
            throw new RuntimeException("Error generating TOTP", e);
        }
    }

}
