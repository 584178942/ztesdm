package com.zte.mdm.custom.device.util;

import android.util.Base64;
import com.zte.mdm.custom.device.R;
import com.zte.mdm.custom.device.SGTApplication;
import com.zte.mdm.custom.device.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * @author Z T
 * @date 20200928
 */
public class RsaUtils { private static final String TAG = "RsaUtils";
    private static final String PUBLIC_KEY = SGTApplication.getContextApp().getString(R.string.pu_key);
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 公钥加密
     * @param data
     * @return
     *
     */
    public static String encryptByPublicKey(String data) {
        byte[] encryptedData = null;
        try {
            byte[] dataByte = data.getBytes();
            byte[] keyBytes = Base64.decode(PUBLIC_KEY, Base64.DEFAULT);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicK = keyFactory.generatePublic(x509KeySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
            int inputLen = dataByte.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(dataByte, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataByte, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            encryptedData = out.toByteArray();
            out.close();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | InvalidKeyException e){
            LogUtils.info(TAG,"encryptByPublicKey" + e.getMessage());
        } catch (BadPaddingException e) {
            LogUtils.info(TAG,"BadPaddingException" + e.getMessage());
        } catch (IllegalBlockSizeException e) {
            LogUtils.info(TAG,"IllegalBlockSizeException" + e.getMessage());
        } catch (IOException e) {
            LogUtils.info(TAG,"IOException" + e.getMessage());
        }
        return Base64.encodeToString(encryptedData,Base64.NO_WRAP);
    }

    /**
     * 公钥解密
     * @param data 传入字符串
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data) {
        //LogUtils.info(TAG, data);
        String publicStr = "";
        byte[] encryptedData = Base64.decode(data, Base64.DEFAULT);
        byte[] keyBytes = Base64.decode(PUBLIC_KEY,Base64.DEFAULT);
        X509EncodedKeySpec pkcs8KeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateK = keyFactory.generatePublic(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateK);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet,
                            MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet,
                            inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            publicStr = new String(decryptedData);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException
                | NoSuchPaddingException | InvalidKeyException
                | IOException e) {
            //LogUtils.info(TAG,"decryptByPublicKey" + e.getMessage());
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
       // LogUtils.info(TAG, "testEncrypt: 公钥解密 decryStr:" + publicStr );
        return publicStr;
}
}