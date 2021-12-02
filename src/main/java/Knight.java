import javafx.scene.image.ImageView;
import javafx.util.Pair;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.HashSet;

public class Knight extends ImageView implements ChessPiece{
	public int color;
	
	public Knight(int color) {
		System.out.println("pawn constructed");
		
		this.setFitHeight(Chess.TILE_SIZE*.8);
		this.setFitWidth(Chess.TILE_SIZE*.8);
		
		this.color = color;
		if (color == 1)
			this.setImage(new Image("/Images/White/Knight.png"));
		else
			this.setImage(new Image("/Images/Black/Knight.png"));
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
		
		ArrayList<Pair<Integer, Integer>> moves = new ArrayList<Pair<Integer, Integer>>();
		
		Pair<Integer, Integer> p;
		
		p = new Pair<Integer, Integer>(x + 2, y + 1);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x - 2, y + 1);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x + 2, y - 1);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x - 2, y - 1);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x + 1, y + 2);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x + 1, y - 2);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x - 1, y + 2);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x - 1, y - 2);
		getMovesHelper(moves, p, b, threatened);
		
		
		return moves;
	}
	
	public boolean getMovesHelper(ArrayList<Pair<Integer, Integer>> moves, Pair<Integer, Integer> p, ChessBoard b, HashSet<Square> threatened) {
		if (!b.isOnBoard(p)) {
			return false;
		}
		if (b.isOccupied(p)) {
			if (b.getPiece(p).getColor() != this.color)
				moves.add(p);
			if (threatened != null)
				threatened.add(b.getSquare(p));
			
			return false;
		}
		
		if (threatened != null)
			threatened.add(b.getSquare(p));
		
		moves.add(p);
		return true;
	}
	
	@Override
	public void move() {
			
	}

	@Override
	public void died() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public PieceType getType() {
		return PieceType.KNIGHT;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public ArrayList<Pair<Integer, Integer>> getPathToKing(ChessPiece piece) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
