import java.io.Serializable;

@SuppressWarnings("serial")
public class Package implements Serializable{
	byte[] data;
	int ba_lenght;
	
	public Package(byte[] data, int ba_length){
		this.ba_lenght = ba_length;
		this.data = data;
	}
}
