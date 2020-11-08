import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;


public class CheckerBoard extends Pane {
    private final Rectangle[][] board;
    ArrayList<Checker> checkers;
    Checker[][] checkerMap;
    boolean moveDisplayed;

    public CheckerBoard() {
        int dimension = LinesOfAction.dimension;
        double side = LinesOfAction.side;
        this.board = new Rectangle[dimension][dimension];
        this.checkers = new ArrayList<>();
        this.checkerMap = new Checker[dimension][dimension];
        this.moveDisplayed = false;

        boolean xLight = true, yLight;
        for (int x = 0; x < dimension; x++) {
            yLight = xLight;
            for (int y = 0; y < dimension; y++) {
                Rectangle r = new Rectangle();
                r.setWidth(side);
                r.setHeight(side);
                r.setX(y * side);
                r.setY(x * side);
                r.setStrokeWidth(1);
                r.setStroke(Color.BLACK);
                if (yLight) r.setFill(Color.YELLOW);
                else r.setFill(Color.ORANGE);
                yLight = !yLight;
                this.board[x][y] = r;
                getChildren().add(this.board[x][y]);
            }
            xLight = !xLight;
        }
        placeCheckers();
    }

    private void placeCheckers() {
        int dimension = LinesOfAction.dimension;
        for (int x = 1; x < dimension-1; x++) {
            Checker left = new Checker(x, 0, Color.WHITE);
            this.checkers.add(left);
            this.checkerMap[x][0] = left;
            Checker right = new Checker(x, dimension-1, Color.WHITE);
            this.checkers.add(right);
            this.checkerMap[x][dimension-1] = right;
            getChildren().add(left);
            getChildren().add(right);
        }
        for (int y = 1; y < dimension-1; y++) {
            Checker top = new Checker(0, y, Color.BLACK);
            this.checkers.add(top);
            this.checkerMap[0][y] = top;
            Checker bottom = new Checker(dimension-1, y, Color.BLACK);
            this.checkers.add(bottom);
            this.checkerMap[dimension-1][y] = bottom;
            getChildren().add(top);
            getChildren().add(bottom);
        }
    }

    public ArrayList<Move> getPossibleMoves(Checker[][] checkerMap, ArrayList<Checker> checkers, Checker checker) {
        int row = checker.position.row;
        int column = checker.position.column;
        int verticalCount = 1;
        int horizontalCount = 1;
        int diagonalCount = 1;
        int inverseDiagonalCount = 1;
        ArrayList<Move> moves = new ArrayList<>();

        for (Checker c : checkers) {
            if (c.position.row == row && c.position.column == column) continue;
            if (row == c.position.row) horizontalCount++;
            if (column == c.position.column) verticalCount++;

            if (c.position.row <= row) { // c is in upper half
                if ((row - c.position.row) == (column - c.position.column)) {
                    diagonalCount++;
                }
                if ((row - c.position.row) == (c.position.column - column)) {
                    inverseDiagonalCount++;
                }
            } else { // c is in lower half
                if ((c.position.row - row) == (c.position.column - column)) {
                    diagonalCount++;
                }
                if ((c.position.row - row) == (column - c.position.column)) {
                    inverseDiagonalCount++;
                }
            }
        }

        moves.addAll(checkHorizontalMoves(checkerMap, checker, horizontalCount));
        moves.addAll(checkVerticalMoves(checkerMap, checker, verticalCount));
        moves.addAll(checkDiagonalMoves(checkerMap, checker, diagonalCount));
        moves.addAll(checkInverseDiagonalMoves(checkerMap, checker, inverseDiagonalCount));
        return moves;
    }


    private ArrayList<Move> checkHorizontalMoves(Checker[][] checkerMap, Checker checker, int horizontalCount) {
        int row = checker.position.row;
        int column = checker.position.column;
        Color color = checker.color;
        boolean opponentFound;
        int y;
        ArrayList<Move> moves = new ArrayList<>();

        if (column - horizontalCount >= 0) {
            // left move
            opponentFound = false;
            for (y = column; y > column - horizontalCount; y--) {
                Checker c = checkerMap[row][y];
                if (c != null && c.color != color) {
                    opponentFound = true;
                    break;
                }
            }
            if (!opponentFound) {
                Checker c = checkerMap[row][y];
                if (c == null || c.color != color) {
                    moves.add(new Move(new Position(row, y), checker.position));
                }
            }
        }

        if (column + horizontalCount < LinesOfAction.dimension) {
            // right move
            opponentFound = false;
            for (y = column; y < column + horizontalCount; y++) {
                Checker c = checkerMap[row][y];
                if (c != null && c.color != color) {
                    opponentFound = true;
                    break;
                }
            }
            if (!opponentFound) {
                Checker c = checkerMap[row][y];
                if (c == null || c.color != color) {
                    moves.add(new Move(new Position(row, y), checker.position));
                }
            }
        }
        return moves;
    }

    private ArrayList<Move> checkVerticalMoves(Checker[][] checkerMap, Checker checker, int verticalCount) {
        int row = checker.position.row;
        int column = checker.position.column;
        Color color = checker.color;
        boolean opponentFound;
        int x;
        ArrayList<Move> moves = new ArrayList<>();

        if (row - verticalCount >= 0) {
            // top move
            opponentFound = false;
            for (x = row; x > row - verticalCount; x--) {
                Checker c = checkerMap[x][column];
                if (c != null && c.color != color) {
                    opponentFound = true;
                    break;
                }
            }
            if (!opponentFound) {
                Checker c = checkerMap[x][column];
                if (c == null || c.color != color) {
                    moves.add(new Move(new Position(x, column), checker.position));
                }
            }
        }

        if (row + verticalCount < LinesOfAction.dimension) {
            // bottom move
            opponentFound = false;
            for (x = row; x < row + verticalCount; x++) {
                Checker c = checkerMap[x][column];
                if (c != null && c.color != color) {
                    opponentFound = true;
                    break;
                }
            }
            if (!opponentFound) {
                Checker c = checkerMap[x][column];
                if (c == null || c.color != color) {
                    moves.add(new Move(new Position(x, column), checker.position));
                }
            }
        }
        return moves;
    }

    private ArrayList<Move> checkDiagonalMoves(Checker[][] checkerMap, Checker checker, int diagonalCount) {
        int row = checker.position.row;
        int column = checker.position.column;
        Color color = checker.color;
        boolean opponentFound;
        int x, y;
        ArrayList<Move> moves = new ArrayList<>();

        if (row - diagonalCount >= 0 && column - diagonalCount >= 0) {
            // upper half
            opponentFound = false;
            for (x = row, y = column; x > row - diagonalCount && y > column - diagonalCount; x--, y--) {
                Checker c = checkerMap[x][y];
                if (c != null && c.color != color) {
                    opponentFound = true;
                    break;
                }
            }
            if (!opponentFound) {
                Checker c = checkerMap[x][y];
                if (c == null || c.color != color) {
                    moves.add(new Move(new Position(x, y), checker.position));
                }
            }
        }

        if (row + diagonalCount < LinesOfAction.dimension && column + diagonalCount < LinesOfAction.dimension) {
            // lower half
            opponentFound = false;
            for (x = row, y = column; x < row + diagonalCount && y < column + diagonalCount; x++, y++) {
                Checker c = checkerMap[x][y];
                if (c != null && c.color != color) {
                    opponentFound = true;
                    break;
                }
            }
            if (!opponentFound) {
                Checker c = checkerMap[x][y];
                if (c == null || c.color != color) {
                    moves.add(new Move(new Position(x, y), checker.position));
                }
            }
        }
        return moves;
    }

    private ArrayList<Move> checkInverseDiagonalMoves(Checker[][] checkerMap, Checker checker, int inverseDiagonalCount) {
        int row = checker.position.row;
        int column = checker.position.column;
        Color color = checker.color;
        boolean opponentFound;
        int x, y;
        ArrayList<Move> moves = new ArrayList<>();

        if (row - inverseDiagonalCount >= 0 && column + inverseDiagonalCount < LinesOfAction.dimension) {
            // upper half
            opponentFound = false;
            for (x = row, y = column; x > row - inverseDiagonalCount && y < column + inverseDiagonalCount; x--, y++) {
                Checker c = checkerMap[x][y];
                if (c != null && c.color != color) {
                    opponentFound = true;
                    break;
                }
            }
            if (!opponentFound) {
                Checker c = checkerMap[x][y];
                if (c == null || c.color != color) {
                    moves.add(new Move(new Position(x, y), checker.position));
                }
            }
        }

        if (row + inverseDiagonalCount < LinesOfAction.dimension && column - inverseDiagonalCount >= 0) {
            // lower half
            opponentFound = false;
            for (x = row, y = column; x < row + inverseDiagonalCount && y > column - inverseDiagonalCount; x++, y--) {
                Checker c = checkerMap[x][y];
                if (c != null && c.color != color) {
                    opponentFound = true;
                    break;
                }
            }
            if (!opponentFound) {
                Checker c = checkerMap[x][y];
                if (c == null || c.color != color) {
                    moves.add(new Move(new Position(x, y), checker.position));
                }
            }
        }
        return moves;
    }



    public ArrayList<Move> getAllPossibleMoves(Checker[][] checkerMap, Color color) {
        ArrayList<Move> moves = new ArrayList<>();
        ArrayList<Checker> checkers = new ArrayList<>();

        int len = checkerMap.length;
        for (int x = 0; x < len; x++) {
            for (int y = 0; y < len; y++) {
                if (checkerMap[x][y] != null) checkers.add(checkerMap[x][y]);
            }
        }
        for (Checker c : checkers) {
            if (c.color == color) {
                moves.addAll(getPossibleMoves(checkerMap, checkers, c));
            }
        }
        return moves;
    }

    public void enableBlock(Move move) {
        Rectangle r = this.board[move.to.row][move.to.column];
        r.setStrokeWidth(5);
        r.setStroke(Color.CRIMSON);
        Checker c = this.checkerMap[move.to.row][move.to.column];
        Objects.requireNonNullElse(c, r).setOnMouseClicked(mouseEvent -> makeMove(move, Player.HUMAN));
    }

    public void disableBlock(Move move) {
        Rectangle r = this.board[move.to.row][move.to.column];
        r.setStrokeWidth(1);
        r.setStroke(Color.BLACK);
        Checker c = this.checkerMap[move.to.row][move.to.column];
        if (c == null) r.setOnMouseClicked(null);
        else c.resetClickEvent();
    }

    public void makeMove(Move move, Player player) {
        System.out.println(player);
        Checker checker = this.checkerMap[move.from.row][move.from.column];
        Checker checkerInPosition = this.checkerMap[move.to.row][move.to.column];

        if (checkerInPosition != null && checkerInPosition.color != checker.color) {
            // capture move
            this.checkers.remove(checkerInPosition);
            getChildren().remove(checkerInPosition);
            System.out.println("capture");
        }
        checker.move(move.to, player);
        this.checkerMap[move.from.row][move.from.column] = null;
        this.checkerMap[move.to.row][move.to.column] = checker;

        if (checkIfGameOver()) return;

        String currentMove = LinesOfAction.moveText.getText().substring(0, 5);
        String nextMove = (currentMove.compareTo("Black") == 0) ? "White" : "Black";
        LinesOfAction.moveText.setText(nextMove + " move");
        for (Checker c : this.checkers) {
            if (c.color == checker.color) {
                c.setOnMouseClicked(null);
            } else {
                c.resetClickEvent();
            }
        }

        if (LinesOfAction.opponent == Player.AI && player == Player.HUMAN) {
            // next move for AI
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    Move selectedMove = MinimaxAlgorithm.run(checkerMap);
                    Platform.runLater(() -> makeMove(selectedMove, Player.AI));
                    return null;
                }
            };
            new Thread(task).start();
        }
    }

    private int countConnectedCheckers(ArrayList<Checker> checkers) {
        int count = 0;
        Checker checker = checkers.get(0);
        Queue<Checker> queue = new LinkedList<>();
        queue.add(checker);
        checker.visited = true;
        while (!queue.isEmpty()) {
            Checker c = queue.poll();
            c.connected = true;
            count++;
            ArrayList<Checker> neighbours = getNeighbours(c);
            queue.addAll(neighbours);
        }
        return count;
    }

    private boolean checkIfGameOver() {
        ArrayList<Checker> whiteCheckers = new ArrayList<>();
        ArrayList<Checker> blackCheckers = new ArrayList<>();

        for (Checker c : this.checkers) {
            c.connected = false;
            c.visited = false;
            if (c.color == Color.WHITE) whiteCheckers.add(c);
            else blackCheckers.add(c);
        }

        int whiteCount = countConnectedCheckers(whiteCheckers);
        int blackCount = countConnectedCheckers(blackCheckers);

        boolean gameOver = false;
        String message = null;
        if (whiteCount == whiteCheckers.size()) {
            gameOver = true;
            message = "White won!";
        }
        if (blackCount == blackCheckers.size()) {
            message = gameOver ? "Game drawn" : "Black won!";
            gameOver = true;
        }

        if (gameOver) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setContentText(message);
            alert.show();
            alert.setOnCloseRequest(event -> Platform.exit());
        }
        return gameOver;
    }

    private ArrayList<Checker> getNeighbours(Checker checker) {
        int row = checker.position.row;
        int column = checker.position.column;
        int dimension = LinesOfAction.dimension;
        ArrayList<Checker> rawNeighbours = new ArrayList<>();
        ArrayList<Checker> neighbours = new ArrayList<>();

        if (row != 0) rawNeighbours.add(this.checkerMap[row-1][column]); // top
        if (row != dimension-1) rawNeighbours.add(this.checkerMap[row+1][column]); // bottom
        if (column != 0) rawNeighbours.add(this.checkerMap[row][column-1]); // left
        if (column != dimension-1) rawNeighbours.add(this.checkerMap[row][column+1]); // right
        if (row != 0 && column != 0) rawNeighbours.add(this.checkerMap[row-1][column-1]); // top-left
        if (row != 0 && column != dimension-1) rawNeighbours.add(this.checkerMap[row-1][column+1]); // top-right
        if (row != dimension-1 && column != 0) rawNeighbours.add(this.checkerMap[row+1][column-1]); // bottom-left
        if (row != dimension-1 && column != dimension-1) rawNeighbours.add(this.checkerMap[row+1][column+1]); // bottom-right

        for (Checker neighbour : rawNeighbours) {
            if (neighbour != null && neighbour.color == checker.color && !neighbour.connected && !neighbour.visited) {
                neighbours.add(neighbour);
                neighbour.visited = true;
            }
        }
        return neighbours;
    }



}
