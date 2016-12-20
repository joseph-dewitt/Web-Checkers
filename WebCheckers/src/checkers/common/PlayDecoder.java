package checkers.common;


import java.io.StringReader;

import javax.json.*;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class PlayDecoder implements Decoder.Text<Message>{
	
	@Override
	public Message decode (String something) {
		JsonObject jsonObject = Json.createReader(new StringReader(something))
				.readObject();
		System.out.println(jsonObject.getString("type"));
		if (jsonObject.getString("type").equals("chat")) {
			chatMessage msg = new chatMessage (jsonObject.getString("chat"));
			return msg;
		}
		if (jsonObject.getString("type").equals("play")) {
		Play ply = new Play(
				jsonObject.getInt("fromRow"),
				jsonObject.getInt("fromCol"),
				jsonObject.getInt("toRow"),
				jsonObject.getInt("toCol"));
			return ply;
		}
		if (jsonObject.getString("type").equals("piece")) {
			Pieces thing = new Pieces(
					jsonObject.getInt("row"), 
					jsonObject.getInt("col"), 
					SquarePlayer.valueOf(jsonObject.getString("player"))
					);
			return thing;
		}
		return null;
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
