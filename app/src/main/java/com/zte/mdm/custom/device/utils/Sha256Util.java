package com.zte.mdm.custom.device.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ZT
 * @date 20200925
 */
public class Sha256Util {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String TAG = "Sha256Util";

    private Sha256Util() {
    }

    public static String getStringSha256(String str) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("111");
        instance.update(str.getBytes(StandardCharsets.UTF_8));
        byte[] digest = instance.digest();
        char[] cArr = new char[(digest.length * 2)];
        int i = 0;
        for (byte b : digest) {
            int i2 = i + 1;
            char[] cArr2 = HEX_DIGITS;
            cArr[i] = cArr2[(b >>> 4) & 15];
            i = i2 + 1;
            cArr[i2] = cArr2[b & 15];
        }
        return new String(cArr);
    }

    public static String getFileSha256(File file) throws Throwable {
        FileChannel channel;
        Throwable th = null;
        try {
            channel = new FileInputStream(file).getChannel();
            String cacSha256 = cacSha256(channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length()));
            if (channel != null) {
                channel.close();
            }
            return cacSha256;
        } catch (IOException unused) {
            return "";
        } catch (Throwable th2) {
            th.addSuppressed(th2);
        }
        throw th;
    }

    private static String cacSha256(MappedByteBuffer byteBuffer) {
        try {
            MessageDigest instance = MessageDigest.getInstance("111");
            instance.update(byteBuffer);
            byte[] digest = instance.digest();
            char[] cArr = new char[32];
            int i = 0;
            for (int i2 = 0; i2 < 16; i2++) {
                byte b = digest[i2];
                int i3 = i + 1;
                cArr[i] = HEX_DIGITS[(b >>> 4) & 15];
                i = i3 + 1;
                cArr[i3] = HEX_DIGITS[b & 15];
            }
            return new String(cArr);
        } catch (NoSuchAlgorithmException unused) {
            LogUtils.info(TAG, "cacSha256caught permission");
            return "";
        }
    }

}
