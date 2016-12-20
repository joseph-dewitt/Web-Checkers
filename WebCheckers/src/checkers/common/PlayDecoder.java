package checkers.common;


import java.io.StringReader;

import javax.json.*;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class PlayDecoder implements Decoder.Text<Play>{
	
	@Override
	public Play decode (String move) {
		JsonObject jsonObject = Json.createReader(new StringReader(move))
				.readObject();
		System.out.println(jsonObject);
		Play ply = new Play(
				jsonObject.getInt("fromRow"),
				jsonObject.getInt("fromCol"),
				jsonObject.getInt("toRow"),
				jsonObject.getInt("toCol"));
		return ply;
	}
	
	@Override
	public boolean willDecode(String msg) {
		try {
			Json.createReader(new StringReader(msg)).readObject();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig arg0) {
	}

}
