import javafx.scene.image.ImageView;
import javafx.util.Pair;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashSet;

public class King extends ImageView implements ChessPiece{
	public int color;
	public boolean hasMoved;
	
	public King(int color) {
		System.out.println("pawn constructed");
		
		this.setFitHeight(Chess.TILE_SIZE*.8);
		this.setFitWidth(Chess.TILE_SIZE*.8);
		
		this.color = color;
		if (color == 1)
			this.setImage(new Image("/Images/White/King.png"));
		else
			this.setImage(new Image("/Images/Black/King.png"));
		
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
		
		ArrayList<Pair<Integer, Integer>> moves = new ArrayList<Pair<Integer, Integer>>();
		
		Pair<Integer, Integer> p;
		
		p = new Pair<Integer, Integer>(x, y + 1);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x, y - 1);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x + 1, y);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x - 1, y);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x + 1, y + 1);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x - 1, y - 1);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x + 1, y - 1);
		getMovesHelper(moves, p, b, threatened);

		p = new Pair<Integer, Integer>(x - 1, y + 1);
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
	
	public void setCheck() {
		Square parent = (Square) this.getParent();
		parent.tile.setStyle("-fx-fill: #B0F7FF;");
	}
	
	public void endCheck() {
		Square parent = (Square) this.getParent();
		parent.deselect();  // reverts color to normal
	}
	
	@Override
	public void move() {
		hasMoved = true;
	}

	@Override
	public void died() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public PieceType getType() {
		return PieceType.KING;
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
