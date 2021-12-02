import java.util.ArrayList;
import java.util.HashSet;

import javafx.util.Pair;

public interface ChessPiece {
	public ArrayList<Pair<Integer, Integer>> getAvailableMoves(HashSet<Square> threatened);
	public ArrayList<Pair<Integer, Integer>> getPathToKing(ChessPiece piece);
	public PieceType getType();
	void move();
	void died();
	public int getColor();
}
