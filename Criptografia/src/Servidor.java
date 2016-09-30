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
			resposta = conexao.receberMensagem();

			System.out.println("Cliente: " + resposta);
			msg = scanner.nextLine();

			conexao.enviarMensagem(msg);

		}
		scanner.close();
		conexao.fecharConexao();

	} // main
} // classe
