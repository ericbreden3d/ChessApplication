import javafx.scene.image.ImageView;
import javafx.util.Pair;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashSet;


public class Bishop extends ImageView implements ChessPiece{
	public int color;
	
	public Bishop(int color) {
		System.out.println("pawn constructed");
		
		this.setFitHeight(Chess.TILE_SIZE*.8);
		this.setFitWidth(Chess.TILE_SIZE*.8);
		
		this.color = color;
		if (color == 1)
			this.setImage(new Image("/Images/White/Bishop.png"));
		else
			this.setImage(new Image("/Images/Black/Bishop.png"));
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
		
		for (int i = 1; i < 8; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x + i, y + i);
			if (!getMovesHelper(moves, p, b, threatened)) {break;}
		}
		for (int i = 1; i < 8; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x + i, y - i);
			if (!getMovesHelper(moves, p, b, threatened)) {break;}
		}
		for (int i = 1; i < 8; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x - i, y + i);
			if (!getMovesHelper(moves, p, b, threatened)) {break;}
		}
		for (int i = 1; i < 8; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x - i, y - i);
			if (!getMovesHelper(moves, p, b, threatened)) {break;}
		}
		
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
	
	public ArrayList<Pair<Integer, Integer>> getPathToKing(ChessPiece piece) {  // erase coord if not using
		// find path using coordinates, eliminate all possibleMoves that are are notOnPath()
		Square parentSquare = (Square) getParent();
		int x = parentSquare.coord.getKey();
		int y = parentSquare.coord.getValue();
		ChessBoard b = parentSquare.board;
		myBool found = new myBool(false);
				
		ArrayList<Pair<Integer, Integer>> path;
		
		path = new ArrayList<Pair<Integer, Integer>>();
		for (int i = 1; i < 8 && found.b == false; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x + i, y + i);
			if (!getPathHelper(path, p, b, found, piece)) {
				if (found.b == true) {
					path.add(b.getPieceCoord(this));
					return path;
				}
				break;
			}
		}
		
		path = new ArrayList<Pair<Integer, Integer>>();
		for (int i = 1; i < 8 && found.b == false; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x + i, y - i);
			if (!getPathHelper(path, p, b, found, piece)) {
				if (found.b == true) {
					path.add(b.getPieceCoord(this));
					return path;
				}
				break;
			}
		}
		
		path = new ArrayList<Pair<Integer, Integer>>();
		for (int i = 1; i < 8 && found.b == false; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x - i, y + i);
			if (!getPathHelper(path, p, b, found, piece)) {
				if (found.b == true) {
					path.add(b.getPieceCoord(this));
					return path;
				}
				break;
			}
		}
		
		path = new ArrayList<Pair<Integer, Integer>>();
		for (int i = 1; i < 8 && found.b == false; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x - i, y - i);
			if (!getPathHelper(path, p, b, found, piece)) {
				if (found.b == true) {
					path.add(b.getPieceCoord(this));
					return path;
				}
				break;
			}
		}
		return null;
	}
	
	public boolean getPathHelper(ArrayList<Pair<Integer, Integer>> path, Pair<Integer, Integer> p, ChessBoard b, myBool found, ChessPiece piece) {
		if (!b.isOnBoard(p)) {
			return false;
		}
		
		// clean up
		if (b.isOccupied(p)) {
			ChessPiece occ = b.getPiece(p);
			if (occ.getColor() != this.color) {
				if (b.isKing(occ)) {
					found.b = true;
					return false;
				}
			}
			
			// keep looking past intercepting piece
			if (occ == piece) {
				path.add(p);
				return true;
			}
			return false;
		}
		path.add(p);
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
		return PieceType.BISHOP;
	}

	@Override
	public int getColor() {
		return color;
	}
	
}
