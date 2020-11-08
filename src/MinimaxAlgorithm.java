import javafx.scene.paint.Color;
import java.util.ArrayList;

public class MinimaxAlgorithm {
    private static final int positiveInfinity = 9999;
    private static final int negativeInfinity = -9999;

    public static Move run(Checker[][] checkerMap) {

        int depth = 4;
        int bestMoveScore = negativeInfinity, moveScore;
        Move bestMove = null;

        int dimension = LinesOfAction.dimension;
        Checker[][] currentCheckerMap = new Checker[dimension][dimension];
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                if (checkerMap[x][y] == null) currentCheckerMap[x][y] = null;
                else {
                    currentCheckerMap[x][y] = new Checker(x, y, checkerMap[x][y].color);
                }
            }
        }

        ArrayList<Move> moves = LinesOfAction.checkerBoard.getAllPossibleMoves(currentCheckerMap, Color.WHITE);

        long start = System.currentTimeMillis();
        // 2 seconds for 8x8 and 1 second for 6x6
        long end = (LinesOfAction.dimension == 8) ? (start + 2*1000) : (start + 1000);

        for (Move move : moves) {
            if (System.currentTimeMillis() >= end) break;
            Checker[][] newCheckerMap = currentCheckerMap.clone();
            Checker checkerInPosition = makeMove(newCheckerMap, move);
            moveScore = minimax(depth-1, negativeInfinity-1, positiveInfinity+1, newCheckerMap, Color.BLACK, false, end);
            undoMove(newCheckerMap, move, checkerInPosition);
            if (moveScore > bestMoveScore) {
                bestMoveScore = moveScore;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private static int minimax(int depth, int alpha, int beta, Checker[][] currentCheckerMap, Color color, boolean isMaximizingPlayer, long end) {
        ArrayList<Move> moves = LinesOfAction.checkerBoard.getAllPossibleMoves(currentCheckerMap, color);

        if (depth == 0 || moves.size() == 0) {
            return EvaluationFunction.getScore(currentCheckerMap);
        }

        int bestMoveScore;
        Color nextColor = (color == Color.BLACK) ? Color.WHITE : Color.BLACK;
        if (isMaximizingPlayer) {
            bestMoveScore = negativeInfinity;
            for (Move move : moves) {
                if (System.currentTimeMillis() >= end) break;
                Checker[][] checkerMap = currentCheckerMap.clone();
                Checker checkerInPosition = makeMove(checkerMap, move);
                bestMoveScore = Math.max(bestMoveScore, minimax(depth-1, alpha, beta, checkerMap, nextColor, false, end));
                undoMove(checkerMap, move, checkerInPosition);
                alpha = Math.max(alpha, bestMoveScore);
                if (beta <= alpha) return bestMoveScore;
            }
        } else {
            bestMoveScore = positiveInfinity;
            for (Move move : moves) {
                if (System.currentTimeMillis() >= end) break;
                Checker[][] checkerMap = currentCheckerMap.clone();
                Checker checkerInPosition = makeMove(checkerMap, move);
                bestMoveScore = Math.min(bestMoveScore, minimax(depth-1, alpha, beta, checkerMap, nextColor, true, end));
                undoMove(checkerMap, move, checkerInPosition);
                beta = Math.min(beta, bestMoveScore);
                if (beta <= alpha) return bestMoveScore;
            }
        }
        return bestMoveScore;
    }

    private static Checker makeMove(Checker[][] checkerMap, Move move) {
        Checker c = checkerMap[move.from.row][move.from.column];
        Checker checkerInPosition = checkerMap[move.to.row][move.to.column];
        c.position = move.to;
        checkerMap[move.to.row][move.to.column] = c;
        checkerMap[move.from.row][move.from.column] = null;
        return checkerInPosition;
    }

    private static void undoMove(Checker[][] checkerMap, Move move, Checker checkerInPosition) {
        Checker c = checkerMap[move.to.row][move.to.column];
        c.position = move.from;
        checkerMap[move.from.row][move.from.column] = c;
        checkerMap[move.to.row][move.to.column] = checkerInPosition;
    }
}
