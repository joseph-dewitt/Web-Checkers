package checkers.common;

import javax.json.*;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class PlayEncoder implements Encoder.Text<Play>{
	
	@Override
	public String encode(Play play) throws EncodeException {
		JsonObject jsonPlay = Json.createObjectBuilder()
				.add("fromRow", play.getfromRow())
				.add("fromCol", play.getfromCol())
				.add("toRow", play.gettoRow())
				.add("toCol", play.gettoCol())
                .build();

        return jsonPlay.toString();
    }	

	@Override
	public void destroy() {
	}

	@Override
	public void init(EndpointConfig arg0) {
	}


}
