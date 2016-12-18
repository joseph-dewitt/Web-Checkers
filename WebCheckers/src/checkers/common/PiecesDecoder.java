package checkers.common;

import javax.websocket.DecodeException;

import java.io.StringReader;

import javax.json.*;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class PiecesDecoder implements Decoder.Text<Pieces> {

	@Override
	public Pieces decode(String piece) throws DecodeException {
		JsonObject jsonObject = Json.createReader(new StringReader(piece))
				.readObject();
		Pieces thing = new Pieces(
				jsonObject.getInt("row"), 
				jsonObject.getInt("col"), 
				SquarePlayer.valueOf(jsonObject.getString("player"))
				);
		System.out.println(jsonObject);
		return thing;
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
