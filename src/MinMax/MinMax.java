package MinMax;

import java.util.List;
import java.io.Console;
import java.util.Arrays;

public class MinMax {
    static int MAX = 100000;
    static int MIN = -100000;
    static int END = 6666666;

    int turn;
    int toMax;
    int toMin;
    int[][] board;
    GenerateurMove gen;
    GenerateurMove counterGen;

    public MinMax(int toMin, int toMax, int[][] board, int turn) {

        this.turn = turn;
        this.board = board;
        this.toMax = toMax;
        this.toMin = toMin;

        if (toMax == 2) {
            gen = new GenerateurMoveRouge(this.board);
            counterGen = new GenerateurMoveNoir(this.board);
        }
        if (toMax == 4) {
            gen = new GenerateurMoveNoir(this.board);
            counterGen = new GenerateurMoveRouge(this.board);
        }
    }

    public void updateBoard(int[][] board) {
        this.board = board;
        if (toMax == 2) {
            gen = new GenerateurMoveRouge(this.board);
            counterGen = new GenerateurMoveNoir(this.board);
        }
        if (toMax == 4) {
            gen = new GenerateurMoveNoir(board);
            counterGen = new GenerateurMoveRouge(this.board);
        }
    }

    public Move getBestMove() {
        int meilleurScore = MIN;
        List<Move> myMoves = gen.obtenirListeMoves();
        Move meilleurMove = null;
        int[][] nboard = new int[8][8];

        for (Move m : myMoves) {
            // Pour chaque mouvements possibles, on va analyser son score
            nboard = copy(this.board);
            int score = getValueOfBoard(board, m, 0);
            int value = nboard[(int) m.depart.getX()][(int) m.depart.getY()];
            nboard[(int) m.arrive.getX()][(int) m.arrive.getY()] = value;
            nboard[(int) m.depart.getX()][(int) m.depart.getY()] = 0;

            System.out.println(" myMoves " + m.toCoordinate());

            score = score + miniMax(nboard, 0, false, MIN, MAX);

            // on prend toujours le meilleur score
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurMove = m;
            }
            System.out.println("meilleur score: " + score);
        }

        board = nboard;
        return meilleurMove;
    }

    private final int MAXDEPTH = 0;

    public int miniMax(int[][] board, int depth, boolean isMax, int alpha, int beta) {

        if (checkWinner() != 0) {
            return END;
        }
        if (depth > MAXDEPTH) {
            return 0;
        }

        // to Maximize
        if (isMax) {
            int meilleurScore = MIN;
            // on crée un générateur avec le nouveau board
            // pour récupérer la liste des moves possible sur ce dernier
            GenerateurMove tempGen = gen.newInstance(board);
            List<Move> myMoves = tempGen.obtenirListeMoves();
            Move meilleurMove = null;
            int[][] nboard = new int[8][8];

            // pour chaque mouvements possible
            for (int i = 0; i < myMoves.size(); i++) {
                nboard = copy(board);
                // on effectue le déplacement dans le board
                int value = nboard[(int) myMoves.get(i).depart.getX()][(int) myMoves.get(i).depart.getY()];
                nboard[(int) myMoves.get(i).arrive.getX()][(int) myMoves.get(i).arrive.getY()] = value;
                nboard[(int) myMoves.get(i).depart.getX()][(int) myMoves.get(i).depart.getY()] = 0;

                int score = miniMax(nboard, depth + 1, false, alpha, beta);

                // si ce mouvement a un meilleur score que le meilleur score actuelle
                // on le garde en mémoire
                if (score > alpha) {
                    alpha = score;
                    meilleurMove = myMoves.get(i);
                }

                if (alpha >= beta) {
                    break;
                }
            }
            return meilleurScore;
            // toMinimize
        } else {
            int meilleurScore = MAX;
            GenerateurMove tempCounterGen = counterGen.newInstance(board);
            List<Move> myMoves = tempCounterGen.obtenirListeMoves();
            Move meilleurMove = null;
            int[][] nboard = copy(board);

            for (int i = 0; i < myMoves.size(); i++) {
                int score = 0; // getValueOfBoard(board, myMoves.get(i));
                int value = nboard[(int) myMoves.get(i).depart.getX()][(int) myMoves.get(i).depart.getY()];
                nboard[(int) myMoves.get(i).arrive.getX()][(int) myMoves.get(i).arrive.getY()] = value;
                nboard[(int) myMoves.get(i).depart.getX()][(int) myMoves.get(i).depart.getY()] = 0;

                score = score + miniMax(nboard, depth + 1, true, alpha, beta);

                if (score < beta) {
                    beta = score;
                    meilleurMove = myMoves.get(i);
                }
                if (alpha >= beta) {
                    break;
                }
            }
            return meilleurScore;
        }
    }

    public int checkWinner() {
        if (toMax == 2) {
            for (int j = 0; j < 8; j++) {
                if (board[0][j] == 4) {
                    return -1;
                }
                if (board[7][j] == 2) {
                    return 1;
                }
            }
        }

        if (toMax == 4) {
            for (int j = 0; j < 8; j++) {
                if (board[7][j] == 2) {
                    return -1;
                }
                if (board[0][j] == 4) {
                    return 1;
                }
            }
        }
        return 0;
    }

    public static int[][] copy(int[][] src) {
        int[][] dst = new int[src.length][];
        for (int i = 0; i < src.length; i++) {
            dst[i] = Arrays.copyOf(src[i], src[i].length);
        }
        return dst;
    }

    // test (on joue les noirs)
    public int getValueOfBoard(int[][] board, Move mouvement, int depth) {
        int value = 0;

        // danger value les rouges dans les 3 dernières col 
        if (toMax == 4) {
            if (board[mouvement.arrive.x][mouvement.arrive.y] == 2 && mouvement.arrive.y > 4) {
                value = value + 10000;
            }

            if (depth == 0) {

                // pion noir de B7 à C6
                if (turn == 1 && mouvement.depart.equals(1, 6) && mouvement.arrive.equals(2, 5)) {
                    value = value + 1000;
                }
                // pion noir de G7 à F6
                if (turn == 2 && mouvement.depart.equals(6, 6) && mouvement.arrive.equals(5, 5)) {
                    value = value + 1000;
                }

                // pion noir de A8 à B7
                if (turn == 3 && mouvement.depart.equals(0, 7) && mouvement.arrive.equals(1, 6)) {
                    value = value + 1000;
                }
                // pion noir de H8 à G7
                if (turn == 4 && mouvement.depart.equals(7, 7) && mouvement.arrive.equals(6, 6)) {
                    value = value + 1000;
                }

                // pion noir de A7 à B6
                if (turn == 5 && mouvement.depart.equals(0, 6) && mouvement.arrive.equals(1, 5)) {
                    value = value + 1000;
                }

                // pion noir de H7 à G6
                if (turn == 6 && mouvement.depart.equals(7, 6) && mouvement.arrive.equals(6, 5)) {
                    value = value + 1000;
                }
            }
        }

        /*
         * if(toMax = 2){ if (board[mouvement.arrive.x][mouvement.arrive.y] == 2 &&
         * mouvement.arrive.y > 4) { value = value + 10000; }
         * 
         * // mettre de B7 à C6 if(turn == 1 && mouvement.depart.x == 1 &&
         * mouvement.depart.y == && mouvement.arrive.x == 2 && mouvement.arrive.y == 2){
         * value = value + 1000; }
         * 
         * if(turn == 2 && mouvement.depart.x == 1 && mouvement.depart.y == &&
         * mouvement.arrive.x == ){ value = value + 1000; }
         * 
         * if(turn == 3 && mouvement.depart.x == 1 && mouvement.depart.y == &&
         * mouvement.arrive.x == ){ value = value + 1000; }
         * 
         * if(turn == 4 && mouvement.depart.x == 1 && mouvement.depart.y == &&
         * mouvement.arrive.x == ){ value = value + 1000; }
         * 
         * if(turn == 5 && mouvement.depart.x == 1 && mouvement.depart.y == &&
         * mouvement.arrive.x == ){ value = value + 1000; }
         * 
         * if(turn == 6 && mouvement.depart.x == 1 && mouvement.depart.y == &&
         * mouvement.arrive.x == ){ value = value + 1000; }
         */
        // }
        return value;
    }
}
