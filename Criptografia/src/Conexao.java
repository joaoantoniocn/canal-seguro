import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.concurrent.SynchronousQueue;

import javax.crypto.Cipher;

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

	public static void init() {
		try {
			assimetrica = KeyPairGenerator.getInstance("RSA");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			assimetrica.initialize(1024, random);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
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
	 *  
	 */
	public void enviarMensagem(String msg){
		try {
			output.writeObject(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String receberMensagem(){
		try {
			return (String) input.readObject();
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
			//System.out.println(chavePublica.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void receberChavePublicaDestinatario() {
		try {
			chavePublicaDestinatario = (PublicKey) input.readObject();
			System.out.println("Chave Publica destinatario recebida com sucesso!");
			//System.out.println("Chave publica destinatario: " + chavePublicaDestinatario.toString());
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
	
	public void printChavePublica(){
		System.out.println(chavePublica.toString());
	}
	
	
    /**
     * Criptografa o texto puro usando chave pública.
     */
    public static byte[] criptografa(String texto, PublicKey chave) {
      byte[] cipherText = null;
      
      try {
        final Cipher cipher = Cipher.getInstance("RSA");
        // Criptografa o texto puro usando a chave Púlica
        cipher.init(Cipher.ENCRYPT_MODE, chave);
        cipherText = cipher.doFinal(texto.getBytes());
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      return cipherText;
    }
   
    /**
     * Decriptografa o texto puro usando chave privada.
     */
    public static String decriptografa(byte[] texto, PrivateKey chave) {
      byte[] dectyptedText = null;
      
      try {
        final Cipher cipher = Cipher.getInstance("RSA");
        // Decriptografa o texto puro usando a chave Privada
        cipher.init(Cipher.DECRYPT_MODE, chave);
        dectyptedText = cipher.doFinal(texto);
   
      } catch (Exception ex) {
        ex.printStackTrace();
      }
   
      return new String(dectyptedText);
    }
	
	
	
	
}
