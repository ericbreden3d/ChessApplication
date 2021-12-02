import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class ChessGame {
	public ChessBoard board;
	public BorderPane pane;
	public Scene scene;
	public int player;
	HashMap<Integer, PlayerData> playerMap;
	public PlayerData p1;
	public PlayerData p2;
	
	public ChessGame() {
		player = 0;  // will switch to 1 when game starts
		board = new ChessBoard(this);
		pane = new BorderPane(board);
		scene = new Scene(pane);
		
		initializeGame();
		
		board.startNextTurn();
	}
	
	public void initializeGame() {
		playerMap = new HashMap<Integer, PlayerData>();
		p1 = new PlayerData(0);
		p2 = new PlayerData(1);
		board.setPieces(p1, p2);  // fills board and adds pieces to PlayerData objects
		playerMap.put(0, p1);
		playerMap.put(1, p2);
		
		// bring game state stuff
	}
	
	public Scene getScene() {
		return scene;
	}
	
	public void advanceTurn() {
		player = player == 1 ? 0 : 1;
	}
	
//	public void startNextTurn() {
//		advanceTurn();
//		
//		getAllOpponentMoves();
//		
//		// end last check state and for newly created one
//		if (inCheckState())
//			endCheckState();
//		checkForCheck();
//		
//		getAllCurPlayerMoves();
//		
//		checkForCheckmate();
//		
//		if (checkmateState == true) {
//			System.out.println("CHECKMATE");
//		}
//	}
	
	public boolean isTurn(int color) {
		return color == player;
	}
	
	public PlayerData getOpponent() {
		return player == 1 ? playerMap.get(0) : playerMap.get(1);
	}
	
	public PlayerData getCurPlayer() {
		return player == 1 ? playerMap.get(1) : playerMap.get(0);
	}
}
