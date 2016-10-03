import java.io.Serializable;

public class Package implements Serializable {

	private static final long serialVersionUID = -916243193401875381L;
	private byte[] signature;
	private byte[] data;
	private int number;

	public Package(byte[] signature, byte[] data, int number) {
		this.signature = signature;
		this.data = data;
		this.setNumber(number);
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
