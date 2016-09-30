import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) {
		// --- iniciando a conexao ---
		// cliente
		Conexao conexao = new Conexao("localhost", 5000);

		conexao.init();

		// cliente iniciando conexao
		conexao.iniciarConexao();
		
		System.out.println("PRONTO");
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
		scanner.close();
		conexao.fecharConexao();
	}
}
