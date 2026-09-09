package zooPackage.Exception;

public class InvalidPasswordException extends Exception{

	private static final long serialVersionUID = -8906764847467667246L;

	public InvalidPasswordException(String message) {
		super(message);
	}
}
