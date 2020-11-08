import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Checker extends Circle {
    boolean selected;
    Position position;
    final Color color;
    ArrayList<Move> possibleMoves;
    boolean connected;
    boolean visited;

    public Checker(int row, int column, Color color) {
        this.connected = false;
        this.visited = false;

        double side = LinesOfAction.side;
        this.position = new Position(row, column);
        this.selected = false;
        this.color = color;
        double radius = side/2 - 5;

        setCenterX(column * side + side/2);
        setCenterY(row * side + side/2);
        setRadius(radius);
        setFill(color);
        if (color == Color.BLACK) resetClickEvent();
    }

    public void resetClickEvent() {
        setOnMouseClicked(mouseEvent -> {
            if (LinesOfAction.checkerBoard.moveDisplayed && !this.selected) return;
            this.selected = !this.selected;
            if (this.selected) {
                LinesOfAction.checkerBoard.moveDisplayed = true;
                this.enableMoves();
            } else {
                LinesOfAction.checkerBoard.moveDisplayed = false;
                this.disableMoves();
            }
        });
    }

    public void move(Position position, Player player) {
        System.out.println("from " + this.position.row + "--" + this.position.column);
        System.out.println("to " + position.row + "--" + position.column);
        this.position = position;
        int row = position.row;
        int column = position.column;
        double side = LinesOfAction.side;
        setCenterX(column * side + side/2);
        setCenterY(row * side + side/2);
        if (player == Player.HUMAN) {
            this.selected = !this.selected;
            disableMoves();
            LinesOfAction.checkerBoard.moveDisplayed = false;
        }
    }

    private void enableMoves() {
        this.possibleMoves = LinesOfAction.checkerBoard.getPossibleMoves(LinesOfAction.checkerBoard.checkerMap, LinesOfAction.checkerBoard.checkers, this);
        for (Move move : this.possibleMoves) {
            LinesOfAction.checkerBoard.enableBlock(move);
        }
    }

    private void disableMoves() {
        for (Move move : this.possibleMoves) {
            LinesOfAction.checkerBoard.disableBlock(move);
        }
        this.possibleMoves = null;
    }
}
