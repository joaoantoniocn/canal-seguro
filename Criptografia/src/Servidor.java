//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//
//import org.bouncycastle.crypto.paddings.PKCS7Padding;
//
//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
//public class Servidor {
//	public static void main(String[] args) throws IOException, ClassNotFoundException {
//		ServerSocket server = new ServerSocket(7022);
//		Socket connection = server.accept(); 
//		ObjectInputStream input = new ObjectInputStream(connection.getInputStream());
//		ObjectOutputStream output = new ObjectOutputStream(connection.getOutputStream()); 
//		
//		Package pkg = (Package) input.readObject();
//		
//		byte[] encr = pkg.user.getBytes("UTF-8");
//	    
//		
//		Usuario c2 = new Usuario();
//		String s = " ";
//		
//		byte[] ba = new byte[pkg.ba_lenght];
//		
//		try {
//			KeyGenerator kg = KeyGenerator.getInstance("AES");
//			kg.init(256);
//	    	SecretKey sk = kg.generateKey();
//
//	    	AES abc = new AES();
//	    	abc.setPadding(new PKCS7Padding());
//	    	abc.setKey(sk.getEncoded());
//			
//	        
//	    	
//	    	byte[] retr = abc.decrypt(encr);
//	    	
//	    	if ( retr.length == ba.length) {
//	    	    ba = retr;
//	    	} else {
//	    	    System.arraycopy(retr, 0, ba, 0, ba.length);
//	    	}
//	    	 
//	    	String decrypted = new String(ba, "UTF-8");
//	    	System.out.println("User decrypted: "+decrypted);
//	    	System.out.println(s.length());
//	    	System.out.println(decrypted.length());
//	    	 
//			
//			Usuario user = Usuario.convertFromString(decrypted);		    	
//			System.out.println(user.getNome());
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		
//		
//		
//		
//		
//		
//		
//		
////		c2.setNome("Cabeçalho Modificado");
////		output.writeObject(teste);
//		output.flush();
//		output.close();
//	}
//}
