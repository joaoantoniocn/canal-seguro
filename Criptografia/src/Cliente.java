import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) {
		// --- iniciando a conexao ---
		// cliente
		Conexao conexao = new Conexao("localhost", 5000);

		conexao.init();

		// cliente iniciando conexao
		conexao.iniciarConexao();

		// ---
		Scanner scanner = new Scanner(System.in);
		String msg = "";
		String resposta = "";
		while (!msg.equals("fechar")) {
			msg = scanner.nextLine();

			// Gera Assinatura
			byte[] signature = conexao.generateSignature(msg);

			// Cria um Package para enviar
			Package p = new Package(signature, msg.getBytes());

			// Converte o Package em um array de bytes
			byte[] data = Util.convertToByteArray(p);

			// Envia a mensagem pela rede
			conexao.sendMessage(data);

			// Recebe mensagem pela rede
			resposta = conexao.receiveMessage();

			System.out.println("Servidor: " + resposta);
		}

		scanner.close();
		conexao.fecharConexao();
	}
}
