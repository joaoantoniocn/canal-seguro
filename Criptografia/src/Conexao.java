import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.DataLengthException;

public class Conexao {

	private static KeyPairGenerator assimetrica;
	private static PrivateKey chavePrivada;
	private static PublicKey chavePublica;

	private Socket client;
	private ServerSocket server;
	private PublicKey chavePublicaDestinatario;
	private SecretKey chaveSimetrica;

	private ObjectOutputStream output;
	private ObjectInputStream input;

	public Conexao(String ip, int porta) {
		try {
			client = new Socket(ip, porta);
			output = new ObjectOutputStream(client.getOutputStream());
			input = new ObjectInputStream(client.getInputStream());

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

			System.out.println("Conexao estabelecida com sucesso!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() {
		try {
			assimetrica = KeyPairGenerator.getInstance("RSA");
			assimetrica.initialize(1024, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {
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

			chaveSimetrica = new SecretKeySpec(decryptedText, 0, decryptedText.length, "DES");
			System.out.println("Chave simétrica destinatario recebida com sucesso!");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 *  
	 */
	public void enviarMensagem(String msg) {
		try {
			byte[] msgCriptografada = criptografaSimetrica(msg);

			output.writeObject(msgCriptografada);
		} catch (DataLengthException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String receberMensagem() {
		try {
			byte[] msgBytes = (byte[]) input.readObject();

			String msg = decriptografaSimetrica(msgBytes);
			return msg;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Problema ao receber mensagem!";
	}

	/**
	 * Espera uma conexao, primeiro recebe a chave publica de quem ta tentando
	 * se conectar e depois envia sua propria chave publica
	 */
	public void esperarConexao() {
		receberChavePublicaDestinatario();
		enviarChavePublica();

		try {
			KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
			chaveSimetrica = keygenerator.generateKey();

			System.out.println("Chave simetrica criada com sucesso!");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		enviarChaveSimetrica();
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
	 * Criptografa o texto puro usando chave pública.
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
	 * Decriptografa o texto puro usando chave privada.
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
	 * Criptografa o texto puro usando chave simétrica.
	 */
	private byte[] criptografaSimetrica(String texto) {
		byte[] cipherText = null;

		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// Criptografa o texto puro usando a chave simétrica
			cipher.init(Cipher.ENCRYPT_MODE, chaveSimetrica);
			cipherText = cipher.doFinal(texto.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherText;
	}

	/**
	 * Decriptografa o texto puro usando chave simétrica.
	 */
	private String decriptografaSimetrica(byte[] texto) {
		byte[] decryptedText = null;
		String result = "";
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

			// Decriptografa o texto puro usando a chave simétrica
			cipher.init(Cipher.DECRYPT_MODE, chaveSimetrica);
			decryptedText = cipher.doFinal(texto);

			result = new String(decryptedText);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	

}
