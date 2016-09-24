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
//		Usuario c1, c2;
//		c1 = new Usuario();
//		c1.setNome("jose");
//		c1.setCpf("123");
//		byte[] encr = null;
//		String s = Usuario.convertToString(c1);
//		System.out.println("Usuário: " + s);
//		byte[] ba = null;
//
//		try {
//			KeyGenerator kg = KeyGenerator.getInstance("AES");
//			kg.init(256);
//			SecretKey sk = kg.generateKey();
//
//			AES abc = new AES();
//			abc.setPadding(new PKCS7Padding());
//			abc.setKey(sk.getEncoded());
//
//			ba = s.getBytes("UTF-8");
//
//			encr = abc.encrypt(ba);
//			System.out.println("User encrypted: " + encr.length + ": " + Base64.encode(encr));
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		Socket client = new Socket("192.168.0.107", 7022);
//
//		ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
//		ObjectInputStream input = new ObjectInputStream(client.getInputStream());
//		// output.writeObject(new Package(encr, ba.length));
//		Scanner scanner = new Scanner(System.in);
		
		// --- iniciando a conexao ---
		Conexao conexao = new Conexao("192.168.0.107", 5000);
		//conexao.init();
		// cliente iniciando conexao
		conexao.iniciarConexao();
		
		// servidor esperando conexao
		//conexao.esperarConexao();
		conexao.fecharConexao();
		// ---
		
//		while (true) {
//			String msg = scanner.nextLine();
//			output.writeObject(msg);
//			//System.out.println(encr);
//			//System.out.println(ba.length);
//
//			String resposta = (String) input.readObject();
//			System.out.println("Servidor: " + resposta);
//		}
		// client.close();
		//
		// Usuario testeModificado = (Usuario) input.readObject();
		// System.out.println(testeModificado.getNome());
	}
}
