package checkers.common;

public class Square {

	private boolean king = false;
	private SquarePlayer player = SquarePlayer.Empty;


	public Square() {
		clear();
	}

	public void setPlayer(SquarePlayer player) {
		this.player = player;
	}

	public SquarePlayer getPlayer() {
		return player;
	}

	public boolean isEmpty() {
		return (player == SquarePlayer.Empty);
	}

	public boolean isKing() {
		return king;
	}

	public void makeKing() {
		king = true;
	}

	public void clear() {
		player = SquarePlayer.Empty;
		king = false;
	}

	public String toString() {

		String str = "" + player;
		
		if (king) {
			str += "-K";
		}
		return str;
	}
}
