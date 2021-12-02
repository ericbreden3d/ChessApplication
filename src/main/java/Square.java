import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

public class Square extends StackPane{
	public ChessPiece piece;
	public Rectangle tile;
	public int color;
	public Pair<Integer, Integer> coord;
	public boolean isSelected;
	public Shape optionGraphic;
	public ChessBoard board;
	
	public Square(int color, int x, int y, ChessPiece piece, ChessBoard board) {
		this.board = board;
		
		int size = Chess.TILE_SIZE;
		tile = new Rectangle(size, size);
		this.getChildren().add(tile);
		
		isSelected = false;
		
		this.piece = piece;
		this.color = color;
		if (color == 1) {
			tile.setStyle("-fx-fill: white;");
		} else {
			tile.setStyle("-fx-fill: " + Chess.BLACK + ";");
		}
		
		coord = new Pair<Integer, Integer>(x, y);
		
		optionGraphic = null;
		
		this.setOnMouseClicked(e -> board.select(this));
	}
	
	public void setPiece(ChessPiece piece) {
		this.piece = piece;
		this.getChildren().add((Node) piece);
	}
	
	public void removePiece() {
		if (this.getChildren().isEmpty())
			return;
		
		this.getChildren().remove((Node) piece);
		this.piece = null;
	}
	
	public void select() {
		tile.setStyle("-fx-fill: #E7E0DE;");
		isSelected = true;
	}
	
	public void deselect() {
		String col = color == 1 ? "white" : Chess.BLACK;
		tile.setStyle("-fx-fill: " + col + ";");
		isSelected = false;
	}
	
	public void addOptionGraphic(int type) {
		if (type == 1) {
			optionGraphic = new Circle(10);
			optionGraphic.setStyle("-fx-fill: gray;");
			this.getChildren().add(optionGraphic);

		}
		else {
			optionGraphic = new Rectangle(100, 100);
			optionGraphic.setStyle("-fx-fill: #FAE4DD;");
			this.getChildren().remove((Node)piece);
			this.getChildren().add(optionGraphic);
			this.getChildren().add((Node)piece);
			
		}			
	}
	
	public void removeOptionGraphic() {
		this.getChildren().remove(optionGraphic);
		optionGraphic = null;	
	}
	
	public void setCastleTrigger(int x, int y, char sideOfBoard, Rook rook) {
		this.setOnMouseClicked(e -> {
			board.select(this);
			board.castleHandler(x, y, sideOfBoard, rook);
		});
	}
	
	public void setOrigHandler() {
		this.setOnMouseClicked(e -> board.select(this));
	}

	
	
}

