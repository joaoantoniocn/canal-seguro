import java.io.Serializable;

public class Package implements Serializable {

	private static final long serialVersionUID = -916243193401875381L;
	private byte[] signature;
	private byte[] data;

	public Package(byte[] signature, byte[] data) {
		this.signature = signature;
		this.data = data;
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

}
