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

public class Servidor {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
 
		// --- iniciando a conexao ---
		Conexao conexao = new Conexao(5000);
		Conexao.init();
		

		// servidor esperando conexao
		conexao.esperarConexao();

		// ---
		Scanner scanner = new Scanner(System.in);
		String msg = "";
		String resposta = "";
		while (!msg.equals("fechar")) {
			resposta = conexao.receberMensagem();
			
			
			
			System.out.println("Cliente: " + resposta);
			msg = scanner.nextLine();
			
			conexao.enviarMensagem(msg);
			
		}

		conexao.fecharConexao();

	} // main
} // classe
