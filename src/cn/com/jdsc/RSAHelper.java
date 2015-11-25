package cn.com.jdsc;
import java.security.Key;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
 
import javax.crypto.Cipher;
 
import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
 
 
public class RSAHelper {
 
      /**
       * µÃµ½¹«Ô¿
       * @param key ÃÜÔ¿×Ö·û´®£¨¾­¹ýbase64±àÂë£©
       * @throws Exception
       */
      public static PublicKey getPublicKey(String key) throws Exception {
            byte[] keyBytes;
            keyBytes = (new BASE64Decoder()).decodeBuffer(key);
 
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
      }
      /**
       * µÃµ½Ë½Ô¿
       * @param key ÃÜÔ¿×Ö·û´®£¨¾­¹ýbase64±àÂë£©
       * @throws Exception
       */
      public static PrivateKey getPrivateKey(String key) throws Exception {
            byte[] keyBytes;
            keyBytes = (new BASE64Decoder()).decodeBuffer(key);
 
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
      }
 
      /**
       * µÃµ½ÃÜÔ¿×Ö·û´®£¨¾­¹ýbase64±àÂë£©
       * @return
       */
      public static String getKeyString(Key key) throws Exception {
            byte[] keyBytes = key.getEncoded();
            String s = (new BASE64Encoder()).encode(keyBytes);
            return s;
      }
}
