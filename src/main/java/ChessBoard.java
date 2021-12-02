
import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.util.Pair;
import javafx.scene.layout.GridPane;
import java.util.HashSet;
import java.util.HashMap;


public class ChessBoard extends GridPane {
	public ChessGame game;
	public Square [][]boardMatrix;
	public Square curSelected = null;

	public ArrayList<ChessPiece> threateningPieces;  // for check situation
	public HashSet<Square> threatenedSquares;  // for check situation
	
	public HashMap<ChessPiece, ArrayList<Pair<Integer, Integer>>> allOpponentMoves;
	public HashMap<ChessPiece, ArrayList<Pair<Integer, Integer>>> allCurPlayerMoves;

	public boolean checkState;
	public boolean checkmateState;
	public boolean stalemateState;

		
	public ChessBoard(ChessGame game) {
		this.game = game;
		
		boardMatrix = new Square[8][8];
		
		int color = 0;
		for (int i = 0; i < 8; i++) {
			color = color == 1 ? 0 : 1;
			for (int j = 0; j < 8; j++) {
				Square s = new Square(color, i, j, null, this);
				this.add(s, i, j);
				boardMatrix[i][j] = s;
				color = color == 1 ? 0 : 1;
			}
		}

		this.setAlignment(Pos.CENTER);
	}
	
	public void tryMove(Square destination) {
		ChessPiece movingPiece = curSelected.piece;
		ChessPiece destPiece = destination.piece;
		
		deselect(curSelected);
		curSelected.removePiece();
		
		// if occupied, take and remove
		if (destPiece != null) {
			game.getOpponent().pieces.remove(destPiece);  // remove piece from data
			destination.removePiece();  // remove piece from square
		}
		
		destination.setPiece(movingPiece);
		destination.piece.move();  // only applies to pawn because of unique hasMoved bool, maybe find different way
		
		startNextTurn();
	}
	
	// put this and dependent functions in ChessGame and ChessLogic
	public void startNextTurn() {
		game.advanceTurn();
		
		getAllOpponentMoves();
		
		// end last check state and for newly created one
		if (inCheckState()) {
			System.out.println("ENDINGCHECK");
			endCheckState();
		}
		checkForCheck();
		
		getAllCurPlayerMoves();
		
		checkForCheckmate();
		
		if (checkmateState == true) {
			System.out.println("CHECKMATE");
		}
	}
	
	public void checkForCheck() {
		King king = game.getCurPlayer().king;
		
		if (isThreatened((Square)king.getParent())) {
			System.out.println("INCHECKAGAIN:)");
			king.setCheck();
			checkState = true;  // only moves that escape check are valid
		}
	}
	
	public void checkForCheckmate() {
		boolean hasMove = false;
		for (ArrayList<Pair<Integer, Integer>> moves : allCurPlayerMoves.values()) {
			if (!moves.isEmpty())
				hasMove = true;
		}
		
		if (!hasMove) {
			if (inCheckState()) 
				checkmateState = true;  // player can't move and in check
			else
				stalemateState = true;  // player can't move and not in check
		}
	}
	
	public void endCheckState() {
		System.out.println("JHDFJHDFJHDFHJDFHJDFHJDFHJ");
		game.getOpponent().king.endCheck();
		checkState = false;
	}
	
	// Loops through all available moves of opponent, storing each threatened square and each piece threatening king
	public void getAllOpponentMoves() {
		PlayerData opp = game.getOpponent();
		
		threatenedSquares = new HashSet<Square>();
		threateningPieces = new ArrayList<ChessPiece>();
		allOpponentMoves = new HashMap<ChessPiece, ArrayList<Pair<Integer, Integer>>>();
		
		for (ChessPiece piece : opp.pieces) {
			ArrayList<Pair<Integer, Integer>> moves = piece.getAvailableMoves(threatenedSquares);  // fill threatenedSquares set
			if (piece.getType() == PieceType.PAWN) {
				modifyPawnThreats(piece, moves);
			}
			
			allOpponentMoves.put(piece, moves);
			
			for (Pair<Integer, Integer> p : moves) {
				Square square = boardMatrix[p.getKey()][p.getValue()];
				// store pieces that directly threaten king
				if (isKing(square) && square.piece.getColor() != opp.num) {  // threateningPieces may be unnecessary
					threateningPieces.add(piece);
				}
			}
		}
	}
	
	// Get all possible moves for current player, accounting for invalid moves and check conditions
	public void getAllCurPlayerMoves() {
		ArrayList<ChessPiece> pieces = game.getCurPlayer().pieces;

		allCurPlayerMoves = new HashMap<ChessPiece, ArrayList<Pair<Integer, Integer>>>();
		
		for (ChessPiece piece : pieces) {
			ArrayList<Pair<Integer, Integer>> moves = piece.getAvailableMoves(null);
			
			// filter out moves that put king in check
			noCheckMoves(piece, moves);  
			
			// filter out moves that don't escape check
			if (inCheckState() && !isKing(piece)) {
				System.out.println("INCHECK");
				onlyEscapeMoves(piece, moves);
			}
			
			// add castling moves for King if not in check
			if (!inCheckState() && isKing(piece)) {
				addCastleMoves((King)piece, moves);
			}
			
			allCurPlayerMoves.put(piece, moves);
		}
	}
	
	// non-diagonal pawn moves are not threat
	public void modifyPawnThreats(ChessPiece pawn, ArrayList<Pair<Integer, Integer>> moves) {
		Pair<Integer, Integer> coord = getPieceCoord(pawn);
		moves.removeIf(m -> {
			if (coord.getKey().equals(m.getKey())) {
				return true;
			}
			return false;
		});
	}
	
	public boolean checkIfValidCastle(char direction, int y) {		
		switch (direction) {
		case 'L': for (int i = 1; i  <= 3; i++) {
					if (boardMatrix[i][y].piece != null || isThreatened(boardMatrix[i][y])) 
						return false;
				  }
				  break;	
		case 'R': for (int i = 5; i  <= 6; i++) {
					if (boardMatrix[i][y].piece != null || isThreatened(boardMatrix[i][y]))
						return false;
		  		  }
		}
		return true;
	}
	
	public void addCastleMoves(King king, ArrayList<Pair<Integer, Integer>> moves) {
		// king and rooks must be in original position
		if (king.hasMoved == true)
			return;
		
		PlayerData curPlayer = game.getCurPlayer();
		int y = curPlayer.num == 0 ? 0 : 7;  // row 0 for black, row 7 for white

		ArrayList<Rook> rooks = new ArrayList<Rook>();
		rooks.add(curPlayer.rookLeft);
		rooks.add(curPlayer.rookRight);
		
		for (Rook rook : rooks) {
			if (rook == null) return;
			char sideOfBoard = rook.sideOfBoard;
			int x = sideOfBoard == 'L' ? 2 : 6;
			if (rook.hasMoved == false) {
				if (checkIfValidCastle(sideOfBoard, y)) {
						moves.add(new Pair<Integer, Integer>(x, y));
						boardMatrix[x][y].setCastleTrigger(x, y, sideOfBoard, rook);
				}
			}
		}
	}
	
	public void castleHandler(int x, int y, char sideOfBoard, Rook rook) {
		if (!isKing(getSquare(new Pair<Integer, Integer>(x, y)))) {
			return;
		}
		
		int kingOffset = sideOfBoard == 'L' ? 1 : -1;  // left rook moves 1 right of king, right moves 1 left of king
		
		Square origRookSq = (Square) rook.getParent();
		Square newRookSq = boardMatrix[x + kingOffset][y];
		Square newKingSq = boardMatrix[x][y];
		
		newRookSq.setPiece(rook);
		origRookSq.removePiece();
		
		// update hasMoved
		rook.move(); 
		
		newKingSq.setOrigHandler();
	}
	
	public ArrayList<ChessPiece> checkIntercepting(ChessPiece piece) {
		ArrayList<ChessPiece> threats = new ArrayList<ChessPiece>();
		Pair<Integer, Integer> coord = getPieceCoord(piece);
		
		// check if piece lies on opponent paths
		allOpponentMoves.forEach((opp, moves) -> {
			if (moves.contains(coord))
				threats.add(opp);
		});
		
		return threats;
	}
	
	// Filter out curPlayer invalid moves that would result in check
	public void noCheckMoves(ChessPiece piece, ArrayList<Pair<Integer, Integer>> moves) {
		// King can't move into check
		if (isKing(piece)) {
			moves.removeIf(m -> {
				int x = m.getKey();
				int y = m.getValue();
				// king cannot move to or take at threatened/defended square
				return isThreatened(boardMatrix[x][y]);
			});
			return;
		}
		
		// Can't move a piece that's preventing check
		ArrayList<ChessPiece> threats = checkIntercepting(piece);
		for (ChessPiece threat : threats) {
			PieceType t = threat.getType();
			if (t == PieceType.QUEEN || t == PieceType.BISHOP || t == PieceType.ROOK) {
				ArrayList<Pair<Integer, Integer>> path = threat.getPathToKing(piece); 
				
				// if piece is blocking king then remove all moves that would remove block
				if (path != null && path.contains(getPieceCoord(piece)))
					moves.removeIf(m -> !path.contains(m));
			}
		}
	}
	
	public void onlyEscapeMoves(ChessPiece piece, ArrayList<Pair<Integer, Integer>> moves) {
		System.out.println("CHECKING FO ESCAPE MOVES");
		for (ChessPiece threat : threateningPieces) {	
			Pair<Integer, Integer> coord = getPieceCoord(threat);
			PieceType t = threat.getType();
			
			// Queen, Bishop, and Rook paths must be blocked or pieces must be taken to end check
			if (t == PieceType.QUEEN || t == PieceType.BISHOP || t == PieceType.ROOK) {
				ArrayList<Pair<Integer, Integer>> path = threat.getPathToKing(null);
				System.out.println(path.toString());
				if (path != null)
					moves.removeIf(m -> !path.contains(m));	
			}
			
			// Knight and Pawns must be taken to end check
			if (t == PieceType.KNIGHT || t == PieceType.PAWN) {
				moves.removeIf(m -> !m.equals(coord));
			}
		}
	}
	
	// TODO have other functions use this as well
	public Pair<Integer, Integer> getPieceCoord(ChessPiece piece) {
		if (piece == null)
			return null;
		Square parent = (Square)((Node) piece).getParent();
		return parent.coord;
	}
	
	public boolean isThreatened(Square square) {
		if (threatenedSquares.contains(square))
			return true;
		return false;
	}
	
	public void showAvailableMoves(ChessPiece piece) {
		ArrayList<Pair<Integer, Integer>> possibleMoves = allCurPlayerMoves.get(piece);
		
		for (Pair<Integer,Integer> coord : possibleMoves) {
			Square s = boardMatrix[coord.getKey()][coord.getValue()];
			if (isOccupied(coord))
				s.addOptionGraphic(2);  // opponent
			else
				s.addOptionGraphic(1);  // unoccupied
		}
	}
	
	public void removeAvailableMoves(ArrayList<Pair<Integer, Integer>> moves) {
		if (moves == null)
			return;
		for (Pair<Integer,Integer> coord : moves) {
			boardMatrix[coord.getKey()][coord.getValue()].removeOptionGraphic();
		}
	}
	
	public boolean inCheckState() {
		return checkState;
	}
	
	public void select(Square square) {
		//Unoccupied Square
		if (square.piece == null) {
			if (square.optionGraphic == null)
				return;
			else {
				tryMove(square);
				curSelected = null;
			}
			return;
		}
		
		// Occupied Square
		// Not already selected, same team, so select then deselect currently selected
		if (!square.isSelected && game.isTurn(square.piece.getColor())) {
			square.select();
			
			if (curSelected != null) {
				deselect(curSelected);
			}
			curSelected = square;	
			
			showAvailableMoves(square.piece);
			
		// Occupying Opponent, Potential take
		} else if (!square.isSelected && square.optionGraphic != null) {
			tryMove(square);
			deselect(curSelected);
			curSelected = null;
			
		// already selected, so deselect
		} else if (square.isSelected) {
			deselect(square);
			
			// Keep check style on King
			if (inCheckState() && isKing(square.piece)) {
				System.out.println("OOPS");
				((King)(square.piece)).setCheck();
			}
			
			curSelected = null;
			
		}
	}
	
	public void deselect(Square square) {		
		removeAvailableMoves(allCurPlayerMoves.get(square.piece));
		square.deselect();
	}
	
	public void giveQueen(Pawn pawn) {
		Pair<Integer, Integer> coord = getPieceCoord(pawn);
		int x = coord.getKey();
		int y = coord.getValue();
		
		PlayerData player = game.getCurPlayer();
		Square square = boardMatrix[x][y];
		
		player.pieces.remove(pawn);
		square.removePiece();
		
		Queen newQueen = new Queen(player.num);
		square.setPiece(newQueen);
		player.pieces.add(newQueen);
	}
	
	public Square getSquare(Pair<Integer, Integer> coord) {
		return boardMatrix[coord.getKey()][coord.getValue()];
	}
	
	public boolean isKing(Square square) {
		ChessPiece piece = square.piece;
		if (piece != null)
			return piece.getType() == PieceType.KING;
		return false;
	}
	
	public boolean isKing(ChessPiece piece) {
		if (piece != null)
			return piece.getType() == PieceType.KING;
		return false;
	}
	
	public boolean isOnBoard(Pair<Integer, Integer> p) {
		int x = p.getKey();
		int y = p.getValue();
		if (x > 7 || x < 0 || y > 7 || y < 0) {
			return false;
		}
		return true;
	}
	
	public ChessPiece getPiece(Pair<Integer, Integer> p) {
		int x = p.getKey();
		int y = p.getValue();
		
		return boardMatrix[x][y].piece;
	}
	
	public boolean isOccupied(Pair<Integer, Integer> p) {
		int x = p.getKey();
		int y = p.getValue();
		
		return boardMatrix[x][y].piece != null ? true : false;
	}
	
	public void setPieces(PlayerData p1, PlayerData p2) {
		ArrayList<ChessPiece> whitePieces = new ArrayList<ChessPiece>();
		ArrayList<ChessPiece> blackPieces = new ArrayList<ChessPiece>();
		
		whitePieces.add(new Rook(1, 'L'));
		whitePieces.add(new Knight(1));
		whitePieces.add(new Bishop(1));
		whitePieces.add(new Queen(1));
		whitePieces.add(new King(1));
		whitePieces.add(new Bishop(1));
		whitePieces.add(new Knight(1));
		whitePieces.add(new Rook(1, 'R'));
		
		blackPieces.add(new Rook(0, 'L'));
		blackPieces.add(new Knight(0));
		blackPieces.add(new Bishop(0));
		blackPieces.add(new Queen(0));
		blackPieces.add(new King(0));
		blackPieces.add(new Bishop(0));
		blackPieces.add(new Knight(0));
		blackPieces.add(new Rook(0, 'R'));

		
		for (int i = 0; i < 8; i++) {
			Pawn pW = new Pawn(1);
			Pawn pB = new Pawn(0);
			whitePieces.add(pW);
			blackPieces.add(pB);
			boardMatrix[i][6].setPiece(pW);
			boardMatrix[i][1].setPiece(pB);
			
			switch (i) {
				case 0: boardMatrix[i][7].setPiece(whitePieces.get(i)); 
						boardMatrix[i][0].setPiece(blackPieces.get(i)); break;
				case 1: boardMatrix[i][7].setPiece(whitePieces.get(i)); 
						boardMatrix[i][0].setPiece(blackPieces.get(i)); break;
				case 2: boardMatrix[i][7].setPiece(whitePieces.get(i)); 
						boardMatrix[i][0].setPiece(blackPieces.get(i)); break;
				case 3: boardMatrix[i][7].setPiece(whitePieces.get(i)); 
						boardMatrix[i][0].setPiece(blackPieces.get(i)); break;
				case 4: boardMatrix[i][7].setPiece(whitePieces.get(i)); 
						boardMatrix[i][0].setPiece(blackPieces.get(i)); break;
				case 5: boardMatrix[i][7].setPiece(whitePieces.get(i)); 
						boardMatrix[i][0].setPiece(blackPieces.get(i)); break;
				case 6: boardMatrix[i][7].setPiece(whitePieces.get(i)); 
						boardMatrix[i][0].setPiece(blackPieces.get(i)); break;
				case 7: boardMatrix[i][7].setPiece(whitePieces.get(i)); 
						boardMatrix[i][0].setPiece(blackPieces.get(i)); break;
			}
		}
		
		// store kings and Rooks to check for win/lose/check conditions
		King whiteKing = (King) boardMatrix[4][7].piece;
		King blackKing = (King) boardMatrix[4][0].piece;
		Rook leftRookW = (Rook) boardMatrix[0][7].piece;
		Rook rightRookW = (Rook) boardMatrix[7][7].piece;
		Rook leftRookB = (Rook) boardMatrix[0][0].piece;
		Rook rightRookB = (Rook) boardMatrix[7][0].piece;
		
		p1.setPiecesData(blackPieces, blackKing, leftRookB, rightRookB);
		p2.setPiecesData(whitePieces,  whiteKing, leftRookW, rightRookW);
	}
	
}
