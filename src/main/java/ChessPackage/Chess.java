package ChessPackage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

public class Chess extends Application{
	public static final int TILE_SIZE = 100;
	public static final int HEIGHT = 8;
	public static final int WIDTH = 8;
	public static final String BLACK = "#A88178";
	
	public ChessBoard board;
	public BorderPane pane;
	public ChessGame game;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		

		
		primaryStage.setTitle("Who want's coffee!!!");
		
//		Parent root = FXMLLoader.load(getClass().getResource("/FXML/chess.fxml"));
		
		game = new ChessGame();
		Scene scene = game.getScene();
		
//		board = new ChessBoard();
//		pane = new BorderPane(board);
//		Scene scene = new Scene(pane);
		
        scene.getStylesheets().add("/CSS/chess.css");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
}
