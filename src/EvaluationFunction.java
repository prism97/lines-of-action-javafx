import javafx.scene.paint.Color;

import java.util.ArrayList;

public class EvaluationFunction {
    private static ArrayList<Checker> whiteCheckers;
    private static ArrayList<Checker> blackCheckers;

    private static int[][] getPieceSquareTable() {
        if (LinesOfAction.dimension == 8) {
            return new int[][]{
                    {-80, -25, -20, -20, -20, -20, -25, -80},
                    {-25,  10,  10,  10,  10,  10,  10,  -25},
                    {-20,  10,  25,  25,  25,  25,  10,  -20},
                    {-20,  10,  25,  50,  50,  25,  10,  -20},
                    {-20,  10,  25,  50,  50,  25,  10,  -20},
                    {-20,  10,  25,  25,  25,  25,  10,  -20},
                    {-25,  10,  10,  10,  10,  10,  10,  -25},
                    {-80, -25, -20, -20, -20, -20, -25, -80}
            };
        } else {
            return new int[][]{
                    {-80, -25, -20, -20, -25, -80},
                    {-25,  10,  10, 10,  10,  -25},
                    {-20,  10,  25, 25,  10,  -20},
                    {-20,  10,  25, 25,  10,  -20},
                    {-25,  10,  10, 10,  10,  -25},
                    {-80, -25, -20, -20, -25, -80}
            };
        }
    }

    private static int getPieceSquareTableScore() {
        int[][] table = getPieceSquareTable();
        int whiteScore = 0;
        int blackScore = 0;
        for (Checker c : whiteCheckers) {
            whiteScore += table[c.position.row][c.position.column];
        }
        for (Checker c : blackCheckers) {
            blackScore += table[c.position.row][c.position.column];
        }
        return whiteScore - blackScore;
    }

    private static int getAreaScoreForColor(Color color) {
        ArrayList<Checker> checkers = (color == Color.WHITE) ? whiteCheckers : blackCheckers;
        int len = LinesOfAction.dimension;
        int x, y;
        int top = len-1, right = 0, bottom = 0, left = len-1;
        for (Checker c : checkers) {
            x = c.position.row;
            y = c.position.column;
            if (x < top) top = x;
            if (x > bottom) bottom = x;
            if (y < left) left = y;
            if (y > right) right = y;
        }
        return  (bottom-top)*(right-left);
    }

    private static int getAreaScore() {
        int whiteScore = getAreaScoreForColor(Color.WHITE);
        int blackScore = getAreaScoreForColor(Color.BLACK);
        return blackScore - whiteScore;
    }

    private static int getMobilityScore(Checker[][] checkerMap) {
        int whiteScore = LinesOfAction.checkerBoard.getAllPossibleMoves(checkerMap, Color.WHITE).size();
        int blackScore = LinesOfAction.checkerBoard.getAllPossibleMoves(checkerMap, Color.BLACK).size();
        return whiteScore - blackScore;
    }

    public static int getScore(Checker[][] checkerMap) {
        int totalScore = 0;
        int len = LinesOfAction.dimension;

        whiteCheckers = new ArrayList<>();
        blackCheckers = new ArrayList<>();

        for (int x = 0; x < len; x++) {
            for (int y = 0; y < len; y++) {
                Checker checker = checkerMap[x][y];
                if (checker != null) {
                    if (checker.color == Color.WHITE) {
                        whiteCheckers.add(checker);
                    } else {
                        blackCheckers.add(checker);
                    }
                }
            }
        }

        totalScore += 2 * getPieceSquareTableScore();
        totalScore += 5 * getAreaScore();
        totalScore += 3 * getMobilityScore(checkerMap);
        return totalScore;
    }
}
