import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;



public class AES {
  
    private final BlockCipher AESCipher = new AESEngine();
    
    private PaddedBufferedBlockCipher pbbc;
    private KeyParameter key;
 
    public void setPadding(BlockCipherPadding bcp) {
        this.pbbc = new PaddedBufferedBlockCipher(AESCipher, bcp);
    }
 
    public void setKey(byte[] key) {
        this.key = new KeyParameter(key);
    }
 
    public byte[] encrypt(byte[] input)
            throws DataLengthException, InvalidCipherTextException {
        return processing(input, true);
    }
 
    public byte[] decrypt(byte[] input)
            throws DataLengthException, InvalidCipherTextException {
        return processing(input, false);
    }
 
    private byte[] processing(byte[] input, boolean encrypt)
            throws DataLengthException, InvalidCipherTextException {
 
        pbbc.init(encrypt, key);
 
        byte[] output = new byte[pbbc.getOutputSize(input.length)];
        int bytesWrittenOut = pbbc.processBytes(
            input, 0, input.length, output, 0);
 
        pbbc.doFinal(output, bytesWrittenOut);
 
        return output;
 
    }
	
	    
	    public static void main(String[] args) {
	    	Usuario c1, c2;
			
			c1 = new Usuario();
			c1.setNome("jose");
			c1.setCpf("123");
			
			String s = Usuario.convertToString(c1);
			System.out.println("Usuário: "+s);
			
			
			
			try {
				KeyGenerator kg = KeyGenerator.getInstance("AES");
				kg.init(256);
		    	SecretKey sk = kg.generateKey();

		    	AES abc = new AES();
		    	abc.setPadding(new PKCS7Padding());
		    	abc.setKey(sk.getEncoded());
		    	
		    	byte[] ba = s.getBytes("UTF-8");
		    
		    	byte[] encr = abc.encrypt(ba);
		    	System.out.println("User encrypted: "+ encr.length + ": "+ Base64.encode(encr));
		    	
		    	byte[] retr = abc.decrypt(encr);
		    	if ( retr.length == ba.length ) {
		    	    ba = retr;
		    	} else {
		    	    System.arraycopy(retr, 0, ba, 0, ba.length);
		    	}
		    	 
		    	String decrypted = new String(ba, "UTF-8");
		    	System.out.println("User decrypted: "+decrypted);
		    	System.out.println(s.length());
		    	System.out.println(decrypted.length());
		    	 
				
				Usuario user = Usuario.convertFromString(decrypted);		    	
				System.out.println(user.getNome());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	    
}
