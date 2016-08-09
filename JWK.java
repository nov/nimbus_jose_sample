import java.security.*;
import java.security.interfaces.*;

import com.nimbusds.jose.jwk.RSAKey;

public class JWK {
  public static void main(String[] args) throws Exception {
    // NOTE: 実際には DB などから適宜鍵を取得するようにしてください。
    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
    keyGenerator.initialize(2048);
    KeyPair kp = keyGenerator.genKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey)kp.getPublic();

    RSAKey jwk = new RSAKey.Builder(publicKey)
      .keyIDFromThumbprint()
      .build();

    System.out.println(jwk.toJSONObject().toJSONString());
  }
}