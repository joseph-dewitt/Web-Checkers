package checkers.common;

public class chatMessage extends Message {
	private String msg;
	
	public chatMessage (String msg) {
		this.msg = msg;
	}
	
	public String get() {
		return msg;
	}
}
