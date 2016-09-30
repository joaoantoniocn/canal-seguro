import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Util {
	public static byte[] convertToByteArray(Package p) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(p);

			byte[] objeto = baos.toByteArray();
			oos.close();

			return objeto;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Package convertFromByteArray(byte[] str) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(str);
			ObjectInputStream ois = new ObjectInputStream(bais);

			return (Package) ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;

	}
}
