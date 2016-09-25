import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;

import sun.misc.BASE64Encoder;


public class RSA {
	public static void main(String[] args)
    {
        RSA generateRSAKeys = new RSA();
        generateRSAKeys.generate();

    }

    private void generate (){
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            // Create the public and private keys
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
            BASE64Encoder b64 = new BASE64Encoder();

            generator.initialize(1024, new SecureRandom());

            KeyPair pair = generator.generateKeyPair();
            Key pubKey = pair.getPublic();
            Key privKey = pair.getPrivate();

            System.out.println("publicKey : " + b64.encode(pubKey.getEncoded()));
            System.out.println("privateKey : " + b64.encode(privKey.getEncoded()));

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

  
}
