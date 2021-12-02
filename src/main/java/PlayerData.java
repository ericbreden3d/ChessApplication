import java.util.ArrayList;

public class PlayerData {
	public ArrayList<ChessPiece> pieces;
	public King king;
	public Rook rookLeft;
	public Rook rookRight;
	public boolean hasCastled;
	public int num;  // 0 for black, 1 for white
	
	public PlayerData(int num) {
		hasCastled = false;
		this.num  = num;
	}
	
	public void setPiecesData(ArrayList<ChessPiece> pieces, King king, Rook rookLeft, Rook rookRight) {
		this.pieces = pieces;
		this.king = king;
		this.rookLeft = rookLeft;
		this.rookRight = rookRight;
	}
}
