import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class AESCBC {
	private final CBCBlockCipher cbcBlockCipher = new CBCBlockCipher(new AESEngine());
	private final SecureRandom random = new SecureRandom();
	private KeyParameter key;
	private BlockCipherPadding bcp = new PKCS7Padding();

	public void setPadding(BlockCipherPadding bcp) {
		this.bcp = bcp;
	}

	public void setKey(byte[] key) {
		this.key = new KeyParameter(key);
	}

	public byte[] encrypt(byte[] input) throws DataLengthException, InvalidCipherTextException {
		return processing(input, true);
	}

	public byte[] decrypt(byte[] input) throws DataLengthException, InvalidCipherTextException {
		return processing(input, false);
	}

	private byte[] processing(byte[] input, boolean encrypt) throws DataLengthException, InvalidCipherTextException {

		PaddedBufferedBlockCipher pbbc = new PaddedBufferedBlockCipher(cbcBlockCipher, bcp);

		int blockSize = cbcBlockCipher.getBlockSize();
		int inputOffset = 0;
		int inputLength = input.length;
		int outputOffset = 0;

		byte[] iv = new byte[blockSize];
		if (encrypt) {
			random.nextBytes(iv);
			outputOffset += blockSize;
		} else {
			System.arraycopy(input, 0, iv, 0, blockSize);
			inputOffset += blockSize;
			inputLength -= blockSize;
		}

		pbbc.init(encrypt, new ParametersWithIV(key, iv));
		byte[] output = new byte[pbbc.getOutputSize(inputLength) + outputOffset];

		if (encrypt) {
			System.arraycopy(iv, 0, output, 0, blockSize);
		}

		int outputLength = outputOffset + pbbc.processBytes(input, inputOffset, inputLength, output, outputOffset);

		outputLength += pbbc.doFinal(output, outputLength);

		byte[] out = new byte[outputLength];
		System.arraycopy(output, 0, out, 0, outputLength);

		return out;

	}

	public static void main(String[] args) {
		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			kg.init(256);
			SecretKey sk = kg.generateKey();

			AESCBC abc = new AESCBC();
			abc.setPadding(new PKCS7Padding());
			abc.setKey(sk.getEncoded());

			String secret = "This is a secret message!";
			System.out.println(secret);
			byte[] ba = secret.getBytes("UTF-8");

			byte[] encr = abc.encrypt(ba);
			System.out.println("Encrypted " + encr.length + ": " + Base64.encode(encr));
			byte[] retr = abc.decrypt(encr);

			if (retr.length == ba.length) {
				ba = retr;
			} else {
				System.arraycopy(retr, 0, ba, 0, ba.length);
			}

			String decrypted = new String(ba, "UTF-8");
			System.out.println(decrypted);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
