import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Conexao {

	private static KeyPairGenerator assimetrica;
	private static PrivateKey chavePrivada;
	private static PublicKey chavePublica;

	private Socket client;
	private PublicKey chavePublicaDestinatario;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	public Conexao(String ip, int porta) {
		try {
			client = new Socket(ip, porta);
			output = new ObjectOutputStream(client.getOutputStream());
			input = new ObjectInputStream(client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void init() {
		try {
			assimetrica = KeyPairGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chavePrivada = assimetrica.generateKeyPair().getPrivate();
		chavePublica = assimetrica.generateKeyPair().getPublic();
	}

	/**
	 * Inicia a conexao fazendo a troca de chaves publicas
	 */
	public void iniciarConexao() {
		enviarChavePublica();
		receberChavePublicaDestinatario();

	}

	/**
	 * Espera uma conexao, primeiro recebe a chave publica de quem ta tentando se conectar
	 * e depois envia sua propria chave publica
	 */
	public void esperarConexao() {
		receberChavePublicaDestinatario();
		enviarChavePublica();
	}

	private void enviarChavePublica() {
		try {
			output.writeObject(chavePublica);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void receberChavePublicaDestinatario() {
		try {
			chavePublicaDestinatario = (PublicKey) input.readObject();
			System.out.println("Chave publica destinatario: " + chavePublicaDestinatario.toString());
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String convertToString(Object obj) {
		try {
			String str;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			byte[] objeto = baos.toByteArray();
			str = Base64.encode(objeto);
			oos.close();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void fecharConexao() {
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
