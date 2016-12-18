package checkers.common;

import javax.json.*;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class PiecesEncoder implements Encoder.Text<Pieces>{
	
	@Override
	public String encode(Pieces piece) {
		JsonObject jsonPieces = Json.createObjectBuilder()
				.add("row", piece.getRow())
				.add("col", piece.getCol())
				.add("player", piece.getPlayer())
				.build();
		System.out.println(jsonPieces);
		return jsonPieces.toString();
	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig arg0) {
	}

}
