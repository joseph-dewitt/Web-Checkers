package checkers.common;

import javax.json.*;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class chatMessageEncoder implements Encoder.Text<chatMessage>{
	
	@Override
	public String encode (chatMessage msg) throws EncodeException {
		JsonObject jsonChat = Json.createObjectBuilder()
				.add("type", "chat")
				.add("chat", msg.get())
				.build();
		return jsonChat.toString();
	}
	

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig arg0) {
	}
	
}
