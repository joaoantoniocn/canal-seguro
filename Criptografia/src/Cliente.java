import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.crypto.paddings.PKCS7Padding;

import com.sun.corba.se.spi.activation.Repository;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Cliente {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// Usuario c1, c2;
		// c1 = new Usuario();
		// c1.setNome("jose");
		// c1.setCpf("123");
		// byte[] encr = null;
		// String s = Usuario.convertToString(c1);
		// System.out.println("Usuário: " + s);
		// byte[] ba = null;
		//
		// try {
		// KeyGenerator kg = KeyGenerator.getInstance("AES");
		// kg.init(256);
		// SecretKey sk = kg.generateKey();
		//
		// AES abc = new AES();
		// abc.setPadding(new PKCS7Padding());
		// abc.setKey(sk.getEncoded());
		//
		// ba = s.getBytes("UTF-8");
		//
		// encr = abc.encrypt(ba);
		// System.out.println("User encrypted: " + encr.length + ": " +
		// Base64.encode(encr));
		//
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// Socket client = new Socket("192.168.0.107", 7022);
		//
		// ObjectOutputStream output = new
		// ObjectOutputStream(client.getOutputStream());
		// ObjectInputStream input = new
		// ObjectInputStream(client.getInputStream());
		// // output.writeObject(new Package(encr, ba.length));

		// --- iniciando a conexao ---
		// cliente
		Conexao conexao = new Conexao("localhost", 5000);
		
		Conexao.init();
		
		// cliente iniciando conexao
		conexao.iniciarConexao();

		
		// ---
		Scanner scanner = new Scanner(System.in);
		String msg = "";
		String resposta = "";
		while (!msg.equals("fechar")) {
			msg = scanner.nextLine();
			
			conexao.enviarMensagem(msg);
			resposta = conexao.receberMensagem();
			System.out.println("Servidor: " + resposta);
		}

		conexao.fecharConexao();

		//
		// Usuario testeModificado = (Usuario) input.readObject();
		// System.out.println(testeModificado.getNome());
	} 
} 
