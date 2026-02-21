package com.yunjin.docking.jct.api;

import java.security.MessageDigest;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class SaltedMD5Validator {

    // 生成带盐有序签名
    public static String generateSignature(TreeMap<String, Object> params, String salt) {
        try {
            String sortedStr = sortByAscii(params);
            String saltedStr = salt + sortedStr;
            return md5(saltedStr);
        } catch (Exception e) {
            throw new RuntimeException("生成签名失败", e);
        }
    }

    private static String sortByAscii(TreeMap<String, Object> params) {
        return params.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    // MD5生成核心逻辑
    private static String md5(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(input.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}