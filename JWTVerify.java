import java.security.*;
import java.security.interfaces.*;
import java.util.*;
import javax.crypto.*;

import com.nimbusds.jose.*;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;

public class JWTVerify {
  public static void main(String[] args) throws Exception {
    // NOTE: 実際には DB などから適宜鍵を取得するようにしてください。
    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
    keyGenerator.initialize(2048);
    KeyPair kp = keyGenerator.genKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey)kp.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey)kp.getPrivate();

    String signedJWTString = generateSignedJWT(privateKey, publicKey);
    SignedJWT signedJWT = SignedJWT.parse(signedJWTString);
    JWSVerifier verifier = new RSASSAVerifier(publicKey);

    if (signedJWT.verify(verifier)) {
      System.out.println("Verified!");
      System.out.println(signedJWT.getJWTClaimsSet().toJSONObject().toJSONString());
    } else {
      System.out.println("Verification Failed!");
    }
  }

  private static String generateSignedJWT(RSAPrivateKey privateKey, RSAPublicKey publicKey) throws Exception {
    JWTClaimsSet payload = new JWTClaimsSet.Builder()
      .claim("foo", "bar")
      .claim("hoge", "fuga")
      .build();
    RSAKey jwk = new RSAKey.Builder(publicKey)
      .keyIDFromThumbprint()
      .build();
    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
      .keyID(jwk.getKeyID())
      .type(JOSEObjectType.JWT)
      .build();
    JWSSigner signer = new RSASSASigner(privateKey);
    SignedJWT signedJWT = new SignedJWT(header, payload);
    signedJWT.sign(signer);

    return signedJWT.serialize();
  }
}