import java.util.Scanner;

public class Servidor {
	public static void main(String[] args) {

		// --- iniciando a conexao ---
		Conexao conexao = new Conexao(5000);
		conexao.init();
		int natual =10;
		// servidor esperando conexao
		conexao.esperarConexao();

		// ---
		Scanner scanner = new Scanner(System.in);
		String msg = "";
		Package resposta;
		while (!msg.equals("fechar")) {
			//Recebe a mensagem pela rde
			resposta = conexao.receiveMessage();
			 
			System.out.println(resposta.getNumber()+" Cliente: " + new String(resposta.getData()));
			System.out.println("numero da mensagem:"+resposta.getNumber() + "  |"+natual);
			if(resposta.getNumber()==natual){
				msg = scanner.nextLine();
				
				// Gera Assinatura
				byte[] signature = conexao.generateSignature(msg);
				natual +=1;
				// Cria um Package para enviar
				Package p = new Package(signature, msg.getBytes(), natual);
				
				// Converte o Package em um array de bytes
				byte[] data = Util.convertToByteArray(p);
	
				// Envia a mensagem pela rede
				conexao.sendMessage(data);
			}else{
				System.out.println("Envio repetido ou desordenado!");
			}
		}
		scanner.close();
		conexao.fecharConexao();

	} // main
} // classe
