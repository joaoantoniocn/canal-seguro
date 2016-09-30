import java.util.Scanner;

public class Servidor {
	public static void main(String[] args) {

		// --- iniciando a conexao ---
		Conexao conexao = new Conexao(5000);
		conexao.init();

		// servidor esperando conexao
		conexao.esperarConexao();

		// ---
		Scanner scanner = new Scanner(System.in);
		String msg = "";
		String resposta = "";
		while (!msg.equals("fechar")) {
			//Recebe a mensagem pela rde
			resposta = conexao.receiveMessage();
			System.out.println("Cliente: " + resposta);

			msg = scanner.nextLine();

			// Gera Assinatura
			byte[] signature = conexao.generateSignature(msg);

			// Cria um Package para enviar
			Package p = new Package(signature, msg.getBytes());

			// Converte o Package em um array de bytes
			byte[] data = Util.convertToByteArray(p);

			// Envia a mensagem pela rede
			conexao.sendMessage(data);
		}
		scanner.close();
		conexao.fecharConexao();

	} // main
} // classe
