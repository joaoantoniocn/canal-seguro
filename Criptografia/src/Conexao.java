import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.SynchronousQueue;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Conexao {

	private static KeyPairGenerator assimetrica;
	private static PrivateKey chavePrivada;
	private static PublicKey chavePublica;

	private Socket client;
	private ServerSocket server;
	private PublicKey chavePublicaDestinatario;
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
			server = new ServerSocket(7022);
			client = server.accept();
			output = new ObjectOutputStream(client.getOutputStream());
			input = new ObjectInputStream(client.getInputStream());
			
			System.out.println("Conexao estabelecida com sucesso!");
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
		
		System.out.println("Chaves publica e privadas criadas com sucesso!");
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
			System.out.println("Conexao fechada com sucesso!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
