package checkers.common;

public enum SquarePlayer {
	Empty, PlayerOne, PlayerTwo;
	
	private SquarePlayer swap;
	
	static {
		PlayerOne.swap = PlayerTwo;
		PlayerTwo.swap = PlayerOne;
	}
	
	public SquarePlayer swap () {
		return swap;
	}
}
