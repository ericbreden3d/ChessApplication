package ChessPackage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashSet;

public class Pawn extends ImageView implements ChessPiece {
	public int color;
	public boolean hasMoved;
	
	public Pawn(int color) {
		this.setFitHeight(Chess.TILE_SIZE*.8);
		this.setFitWidth(Chess.TILE_SIZE*.8);
		
		this.color = color;
		if (color == 1)
			this.setImage(new Image("/Images/White/Pawn.png"));
		else
			this.setImage(new Image("/Images/Black/Pawn.png"));
		
		hasMoved = false;	
	}
	
	public void select() {
		Square parentSquare = (Square) this.getParent();
		parentSquare.select();
	}
	
	@Override
	public ArrayList<Pair<Integer, Integer>> getAvailableMoves(HashSet<Square> threatened) {
		Square parentSquare = (Square) getParent();
		int x = parentSquare.coord.getKey();
		int y = parentSquare.coord.getValue();
		ChessBoard b = parentSquare.board;
		
		int directionMult = this.color == 1 ? -1 : 1;
		
		int amount = hasMoved ? 1 : 2;
		
		ArrayList<Pair<Integer, Integer>> moves = new ArrayList<Pair<Integer, Integer>>();
		
		for (int i = 1; i <= amount; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x, y + (i*directionMult));
			if (!b.isOnBoard(p)) {
				break;
			}
			if (b.isOccupied(p)) {
				break;
			}
			moves.add(p);
		}
		
		Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(x + 1, y + directionMult);
		Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(x - 1, y + directionMult);
		
		ArrayList<Pair<Integer, Integer>> takeMoves = new ArrayList<Pair<Integer, Integer>>();
		takeMoves.add(p1);
		takeMoves.add(p2);
		
		for (int i = 0; i < 2; i++) {
			if (b.isOnBoard(takeMoves.get(i)) && b.isOccupied(takeMoves.get(i))) {
				if (b.getPiece(takeMoves.get(i)).getColor() != this.color)
					moves.add(takeMoves.get(i));	
			}
			if (b.isOnBoard(takeMoves.get(i)) && threatened != null)
				threatened.add(b.getSquare(takeMoves.get(i)));  // diagonals are defended
		}	
		return moves;
	}
	
	@Override
	public void move() {
		hasMoved = true;
		
		// if Pawn reaches opposite end, becomes queen. Note: maybe implement choice later
		int endRow = color == 1 ? 0 : 7;  // opposite end row is 0 for white and 7 for black
		if ( ( (Square) getParent()).board.getPieceCoord(this).getValue() == endRow) {
			((Square) getParent()).board.giveQueen(this);
		}
	}

	@Override
	public void died() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public PieceType getType() {
		return PieceType.PAWN;
	}

	@Override
	public ArrayList<Pair<Integer, Integer>> getPathToKing(ChessPiece piece) {
		// TODO Auto-generated method stub
		return null;
	}
}
