package com.carlos.auth.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * QR码生成器
 * </p>
 *
 * <p>生成用于Google Authenticator的TOTP QR码URI</p>
 *
 * @author Carlos
 * @date 2026-02-26
 */
@Slf4j
@Component
public class QrCodeGenerator {

    /**
     * 应用名称（用于QR码）
     */
    private static final String APP_NAME = "CarlosAuth";

    /**
     * 生成TOTP的otpauth URI
     *
     * @param username 用户名
     * @param secret 密钥（Base32编码）
     * @return otpauth URI
     */
    public String generateOtpAuthUri(String username, String secret) {
        if (username == null || secret == null) {
            throw new IllegalArgumentException("Username and secret cannot be null");
        }

        try {
            String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.name());
            String encodedSecret = URLEncoder.encode(secret, StandardCharsets.UTF_8.name());
            String encodedIssuer = URLEncoder.encode(APP_NAME, StandardCharsets.UTF_8.name());

            // otpauth://totp/{issuer}:{username}?secret={secret}&issuer={issuer}
            String uri = String.format(
                "otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30",
                APP_NAME,
                encodedUsername,
                encodedSecret,
                encodedIssuer
            );

            log.debug("Generated TOTP URI for user: {}", username);
            return uri;

        } catch (UnsupportedEncodingException e) {
            log.error("Failed to encode TOTP URI", e);
            throw new IllegalStateException("Failed to generate TOTP URI", e);
        }
    }

    /**
     * 生成TOTP的QR码（返回URL，可转换为二维码图片）
     *
     * @param username 用户名
     * @param secret 密钥（Base32编码）
     * @return 可用于生成QR码的URL
     */
    public String generateQrCodeUrl(String username, String secret) {
        String otpAuthUri = generateOtpAuthUri(username, secret);

        // 使用Google Chart API生成QR码
        String encodedUri = null;
        try {
            encodedUri = URLEncoder.encode(otpAuthUri, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // 返回可生成二维码图片的URL
        return String.format(
            "https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=%s",
            encodedUri
        );
    }

    /**
     * 格式化显示密钥（便于手动输入）
     *
     * @param secret Base32编码的密钥
     * @return 格式化字符串（每4个字符一组）
     */
    public String formatSecretForDisplay(String secret) {
        if (secret == null || secret.isEmpty()) {
            return "";
        }

        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < secret.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ");
            }
            formatted.append(secret.charAt(i));
        }

        return formatted.toString();
    }
}
