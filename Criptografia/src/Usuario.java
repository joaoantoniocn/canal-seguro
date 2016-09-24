import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@SuppressWarnings("serial")
public class Usuario implements Serializable{
	private String nome;
	private String cpf;




	public static String convertToString(Usuario c) {
		try {
			String str;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(c);
			byte[] objeto = baos.toByteArray();
			str = Base64.encode(objeto);
			oos.close();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Usuario convertFromString(String str) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(str));
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Usuario) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}
	
	
	public static void main(String[] args) {
		Usuario c1, c2;
		
		c1 = new Usuario();
		c1.setNome("jose");
		c1.setCpf("123");
		
		String s = Usuario.convertToString(c1);
		System.out.println("Usuário: "+s);
		
		
		c2 = Usuario.convertFromString(s);
		
		System.out.println(c2.getNome());
		System.out.println(c2.getCpf());
		
		
	}
	

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
}
