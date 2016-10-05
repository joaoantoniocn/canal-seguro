import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) {
		System.out.println("<< CLIENTE >>");

		// --- iniciando a conexao ---
		// cliente
		Conexao conexao = new Conexao("localhost", 5000);
		int natual = 10;
		conexao.init();

		// cliente iniciando conexao
		conexao.iniciarConexao();

		// ---
		Scanner scanner = new Scanner(System.in);
		String msg = "";
		Package resposta = null;
		while (!msg.equals("fechar")) {
			msg = scanner.nextLine();

			// Gera Assinatura
			byte[] signature = conexao.generateSignature(msg);

			// Cria um Package para enviar
			Package p = new Package(signature, msg.getBytes(), natual);

			// Converte o Package em um array de bytes
			byte[] data = Util.convertToByteArray(p);

			// Envia a mensagem pela rede
			conexao.sendMessage(data);
			natual += 1;

			// Recebe mensagem pela rede
			resposta = conexao.receiveMessage();
			System.out.println("numero da mensagem:" + resposta.getNumber() + "  |" + natual);

			if (resposta.getNumber() == natual) {
				System.out.println("Servidor: " + new String(resposta.getData()));
			} else {
				System.out.println("Envio repetido ou desordenado!");
			}

		}

		scanner.close();
		conexao.fecharConexao();
	}
}
