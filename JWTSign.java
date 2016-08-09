import java.security.*;
import java.security.interfaces.*;
import java.util.*;
import javax.crypto.*;

import com.nimbusds.jose.*;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;

public class JWTSign {
  public static void main(String[] args) throws Exception {
    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
    keyGenerator.initialize(2048);

    KeyPair kp = keyGenerator.genKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey)kp.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey)kp.getPrivate();

    JWSSigner signer = new RSASSASigner(privateKey);

    Map<String, Integer> licences = new HashMap<String, Integer>();
    licences.put("pool", 5);
    JWTClaimsSet payload = new JWTClaimsSet.Builder()
      .subject("alice")
      .issuer("https://c2id.com")
      .expirationTime(new Date(new Date().getTime() + 60 * 1000))
      .claim("licences", licences)
      .build();

    RSAKey jwk = new RSAKey(publicKey, null, null, null, null, null, null, null);
    JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
      .keyID(jwk.computeThumbprint().toString())
      .type(JOSEObjectType.JWT)
      .build();

    SignedJWT signedJWT = new SignedJWT(header, payload);
    signedJWT.sign(signer);

    System.out.println(signedJWT.serialize());
  }
}