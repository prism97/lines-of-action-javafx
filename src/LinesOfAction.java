import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LinesOfAction extends Application {
    private static Stage stage;
    public static BoardSize boardSize;
    public static Player opponent;
    public static CheckerBoard checkerBoard;
    public static int dimension;
    public static double side;
    public static Text moveText;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        primaryStage.setTitle("Lines of Action");
        primaryStage.setScene(startingScene());
        primaryStage.show();
    }

    Scene startingScene() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(50);
        HBox options = new HBox(boardSizeMenu(), opponentMenu());
        options.setPadding(new Insets(50));
        options.setSpacing(100);
        options.setAlignment(Pos.CENTER);
        root.getChildren().add(options);
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> stage.setScene(playingScene()));
        root.getChildren().add(startButton);
        return new Scene(root, 500, 500);
    }

    Node boardSizeMenu() {
        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton six = new RadioButton("6x6");
        six.setOnAction(e -> boardSize = BoardSize.SIX_BY_SIX);
        six.setToggleGroup(toggleGroup);
        RadioButton eight = new RadioButton("8x8");
        eight.setOnAction(e -> boardSize = BoardSize.EIGHT_BY_EIGHT);
        eight.setToggleGroup(toggleGroup);

        boardSize = BoardSize.EIGHT_BY_EIGHT;
        eight.setSelected(true);

        VBox vBox = new VBox( new Text("BOARD SIZE"), six, eight);
        vBox.setSpacing(20);
        return vBox;
    }

    Node opponentMenu() {
        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton human = new RadioButton("Human vs Human");
        human.setOnAction(e -> opponent = Player.HUMAN);
        human.setToggleGroup(toggleGroup);
        RadioButton ai = new RadioButton("Human vs AI");
        ai.setOnAction(e -> opponent = Player.AI);
        ai.setToggleGroup(toggleGroup);

        opponent = Player.AI;
        ai.setSelected(true);

        VBox vBox = new VBox( new Text("OPPONENT"), human, ai);
        vBox.setSpacing(20);
        return vBox;
    }


    Scene playingScene() {
        dimension = (boardSize == BoardSize.EIGHT_BY_EIGHT) ? 8 : 6;
        side = 500.0 /dimension;
        StackPane root = new StackPane();
        root.setAlignment(Pos.BASELINE_CENTER);
        root.setPadding(new Insets(20, 50, 50, 50));

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        moveText = new Text("Black Move"); // black moves first
        moveText.setX(10);
        moveText.setY(10);
        vBox.getChildren().add(moveText);

        checkerBoard = new CheckerBoard();
        vBox.getChildren().add(checkerBoard);
        root.getChildren().add(vBox);
        return new Scene(root, 600, 600);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
