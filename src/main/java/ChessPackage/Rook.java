package ChessPackage;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.HashSet;

public class Rook extends ImageView implements ChessPiece{
	public int color;
	public boolean hasMoved;
	public char sideOfBoard;
	
	public Rook(int color, char side) {
		this.setFitHeight(Chess.TILE_SIZE*.8);
		this.setFitWidth(Chess.TILE_SIZE*.8);
		
		this.color = color;
		if (color == 1)
			this.setImage(new Image("/Images/White/Rook.png"));
		else
			this.setImage(new Image("/Images/Black/Rook.png"));
		
		hasMoved = false;
		this.sideOfBoard  = side;
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
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x, y + i);
			if (!getMovesHelper(moves, p, b, threatened)) {break;}
		}
		for (int i = 1; i < 8; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x, y - i);
			if (!getMovesHelper(moves, p, b, threatened)) {break;}
		}
		for (int i = 1; i < 8; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x + i, y);
			if (!getMovesHelper(moves, p, b, threatened)) {break;}
		}
		for (int i = 1; i < 8; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x - i, y);
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
		// find path to king using coordinates, if target piece argument is king the return +1 move along path. explained in ChessBoard.noCheckMoves()
		Square parentSquare = (Square) getParent();
		int x = parentSquare.coord.getKey();
		int y = parentSquare.coord.getValue();
		ChessBoard b = parentSquare.board;
		myBool found = new myBool(false);
				
		ArrayList<Pair<Integer, Integer>> path;
		
		path = new ArrayList<Pair<Integer, Integer>>();
		for (int i = 1; i < 8 && found.b == false; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x, y + i);
			if (!getPathHelper(path, p, b, found, piece)) {
				if (found.b == true) {
					path.add(b.getPieceCoord(this));
					if (b.isKing(piece)) {
						path.add(new Pair<Integer, Integer>(x, y + i + 1));
					}
					return path;
				}
				break;
			}
		}
		
		path = new ArrayList<Pair<Integer, Integer>>();
		for (int i = 1; i < 8 && found.b == false; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x, y - i);
			if (!getPathHelper(path, p, b, found, piece)) {
				if (found.b == true) {
					path.add(b.getPieceCoord(this));
					if (b.isKing(piece)) {
						path.add(new Pair<Integer, Integer>(x, y - i - 1));
					}
					return path;
				}
				break;
			}
		}
		
		path = new ArrayList<Pair<Integer, Integer>>();
		for (int i = 1; i < 8 && found.b == false; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x + i, y);
			if (!getPathHelper(path, p, b, found, piece)) {
				if (found.b == true) {
					path.add(b.getPieceCoord(this));
					if (b.isKing(piece)) {
						path.add(new Pair<Integer, Integer>(x + i + 1, y));
					}
					return path;
				}
				break;
			}
		}
		
		path = new ArrayList<Pair<Integer, Integer>>();
		for (int i = 1; i < 8 && found.b == false; i++) {
			Pair<Integer, Integer> p = new Pair<Integer, Integer>(x - i, y);
			if (!getPathHelper(path, p, b, found, piece)) {
				if (found.b == true) {
					path.add(b.getPieceCoord(this));
					if (b.isKing(piece)) {
						path.add(new Pair<Integer, Integer>(x - i - 1, y));
					}
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
		hasMoved = true;
	}

	@Override
	public void died() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PieceType getType() {
		return PieceType.ROOK;
	}
	
	@Override
	public int getColor() {
		return color;
	}
	
}
