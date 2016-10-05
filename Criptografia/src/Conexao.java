import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.DataLengthException;

public class Conexao {

	private KeyPairGenerator assimetrica;
	private PrivateKey chavePrivada;
	private PublicKey chavePublica;

	private Socket client;
	private ServerSocket server;
	private PublicKey chavePublicaDestinatario;
	private SecretKey chaveSimetrica;

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private int msgCount;

	public Conexao(String ip, int porta) {
		try {
			client = new Socket(ip, porta);
			output = new ObjectOutputStream(client.getOutputStream());
			input = new ObjectInputStream(client.getInputStream());
			setMsgCount(new Random().nextInt());
			System.out.println("Conexao estabelecida com sucesso!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Conexao(int porta) {
		try {
			server = new ServerSocket(porta);
			client = server.accept();
			output = new ObjectOutputStream(client.getOutputStream());
			input = new ObjectInputStream(client.getInputStream());
			setMsgCount(new Random().nextInt());
			System.out.println("Conexao estabelecida com sucesso!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		try {
			assimetrica = KeyPairGenerator.getInstance("RSA", "BC");
			assimetrica.initialize(1024, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		KeyPair keyPair = assimetrica.generateKeyPair();
		chavePrivada = keyPair.getPrivate();
		chavePublica = keyPair.getPublic();

		System.out.println("Chaves publica e privadas criadas com sucesso!");
	}

	/**
	 * Inicia a conexao fazendo a troca de chaves publicas
	 */
	public void iniciarConexao() {
		enviarChavePublica();
		receberChavePublicaDestinatario();

		receberChaveSimetrica();
	}

	/**
	 * Espera uma conexao, primeiro recebe a chave publica de quem ta tentando
	 * se conectar e depois envia sua propria chave publica
	 */
	public void esperarConexao() {
		receberChavePublicaDestinatario();
		enviarChavePublica();

		try {
			KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
			chaveSimetrica = keygenerator.generateKey();

			System.out.println("Chave simetrica criada com sucesso!");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		enviarChaveSimetrica();
	}

	public void enviarChaveSimetrica() {
		try {
			byte[] ciphertext = criptografa(chaveSimetrica.getEncoded(), chavePublicaDestinatario);
			output.writeObject(ciphertext);

			System.out.println("Chave simétrica enviada...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void receberChaveSimetrica() {
		try {
			byte[] ba = (byte[]) input.readObject();
			byte[] decryptedText = decriptografa(ba, chavePrivada);

			chaveSimetrica = new SecretKeySpec(decryptedText, 0, decryptedText.length, "AES");
			System.out.println("Chave simétrica destinatario recebida com sucesso!");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void enviarChavePublica() {
		try {
			output.writeObject(chavePublica);
			System.out.println("Chave Publica enviada...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void receberChavePublicaDestinatario() {
		try {
			chavePublicaDestinatario = (PublicKey) input.readObject();
			System.out.println("Chave Publica destinatario recebida com sucesso!");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void fecharConexao() {
		try {
			client.close();
			System.out.println("Conexao fechada com sucesso!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Criptografa o texto puro usando a chave pública.
	 */
	private byte[] criptografa(byte[] texto, PublicKey chave) {
		byte[] cipherText = null;

		try {
			Cipher cipher = Cipher.getInstance("RSA");

			// Criptografa o texto puro usando a chave Pública
			cipher.init(Cipher.ENCRYPT_MODE, chave);
			cipherText = cipher.doFinal(texto);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherText;
	}

	/**
	 * Decriptografa o texto puro usando a chave privada.
	 */
	private byte[] decriptografa(byte[] texto, PrivateKey chave) {
		byte[] dectyptedText = null;
		try {
			Cipher cipher = Cipher.getInstance("RSA");

			// Decriptografa o texto puro usando a chave Privada
			cipher.init(Cipher.DECRYPT_MODE, chave);
			dectyptedText = cipher.doFinal(texto);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return dectyptedText;
	}

	/**
	 * Criptografa o texto puro usando a chave simétrica.
	 */
	private byte[] criptografaSimetrica(byte[] data) {
		byte[] encryptedIVAndText = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

			// Gera o vetor inicial		
			byte[] iv = new byte[cipher.getBlockSize()];
			SecureRandom random = new SecureRandom();
			random.nextBytes(iv);
			IvParameterSpec ivParameter = new IvParameterSpec(iv);
			
			// Criptografa o texto puro usando a chave simétrica
			cipher.init(Cipher.ENCRYPT_MODE, chaveSimetrica, ivParameter);
			byte[] cipherText = cipher.doFinal(data);
			
			// Combina o vetor inicial com o texto criptografado
			encryptedIVAndText = new byte[cipher.getBlockSize() + cipherText.length];
			System.arraycopy(iv, 0, encryptedIVAndText, 0, cipher.getBlockSize());
			System.arraycopy(cipherText, 0, encryptedIVAndText, cipher.getBlockSize(), cipherText.length);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return encryptedIVAndText;
	}

	/**
	 * Decriptografa o texto puro usando a chave simétrica.
	 */
	private byte[] decriptografaSimetrica(byte[] encryptedIvTextBytes) {
		byte[] decryptedText = null;
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");			
			
			// Extrai o IV.
			byte[] iv = new byte[cipher.getBlockSize()];
			System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

			// Extrai a mensagem criptografada
			int encryptedSize = encryptedIvTextBytes.length - cipher.getBlockSize();
			byte[] encryptedBytes = new byte[encryptedSize];
			System.arraycopy(encryptedIvTextBytes, cipher.getBlockSize(), encryptedBytes, 0, encryptedSize);
			
			// Decrypt	
			cipher.init(Cipher.DECRYPT_MODE, chaveSimetrica, ivParameterSpec);
			decryptedText = cipher.doFinal(encryptedBytes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return decryptedText;
	}

	public byte[] generateSignature(String mensagem) {
		Signature sig = null;
		byte[] signature = null;
		try {
			sig = Signature.getInstance("SHA1withRSA", "BC");

			// Inicializando Obj Signature com a Chave Privada
			sig.initSign(chavePrivada, new SecureRandom());

			// Gerar assinatura
			sig.update(mensagem.getBytes());

			signature = sig.sign();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return signature;
	}

	public void sendMessage(byte[] data) {
		try {
			byte[] msgCriptografada = criptografaSimetrica(data);

			output.writeObject(msgCriptografada);
		} catch (DataLengthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Package receiveMessage() {
		try {
			byte[] msgBytes = (byte[]) input.readObject();

			byte[] data = decriptografaSimetrica(msgBytes);
			Package p = Util.convertFromByteArray(data);

			String msg = new String(p.getData());

			if (this.verifySignature(msg, p.getSignature())) {
				System.out.println("A Mensagem recebida foi assinada corretamente.");
				return p;
			} else {
				System.out.println("A Mensagem recebida NÃO pode ser validada.");
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private boolean verifySignature(String msg, byte[] signature) {
		Signature clientSig;
		try {
			clientSig = Signature.getInstance("SHA1withRSA", "BC");
			clientSig.initVerify(chavePublicaDestinatario);
			clientSig.update(msg.getBytes());

			if (clientSig.verify(signature)) {
				return true; // Mensagem corretamente assinada
			} else {
				return false; // Mensagem não pode ser validada
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

}
