package MinMax;

import java.util.List;
import java.io.Console;
import java.util.Arrays;

public class MinMax {
    static int MAX = 100000000;
    static int MIN = -100000000;
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
            int score = getValueOfBoard(board, m, 0, gen.getNbPion());
            int value = nboard[(int) m.depart.getX()][(int) m.depart.getY()];
            nboard[(int) m.arrive.getX()][(int) m.arrive.getY()] = value;
            nboard[(int) m.depart.getX()][(int) m.depart.getY()] = 0;

            System.out.println(" myMoves " + m.toCoordinate());

            score = score + miniMax(nboard, 1, false, MIN, MAX);

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

    private final int MAXDEPTH = 4;

    public int miniMax(int[][] board, int depth, boolean isMax, int alpha, int beta) {

        if (checkWinner(board) != 0) {
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
            int[][] nboard;
            // pour chaque mouvements possible
            for (int i = 0; i < myMoves.size(); i++) {
                nboard = copy(board);
                int score = getValueOfBoard(board, myMoves.get(i), depth, tempGen.getNbPion());
                // on effectue le déplacement dans le board
                int value = nboard[(int) myMoves.get(i).depart.getX()][(int) myMoves.get(i).depart.getY()];
                nboard[(int) myMoves.get(i).arrive.getX()][(int) myMoves.get(i).arrive.getY()] = value;
                nboard[(int) myMoves.get(i).depart.getX()][(int) myMoves.get(i).depart.getY()] = 0;

                score += miniMax(nboard, depth + 1, false, alpha, beta);

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
            int[][] nboard;

            for (int i = 0; i < myMoves.size(); i++) {
                nboard = copy(board);
                int score = 0;
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

    public int checkWinner(int[][] board) {
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

    static final int ROUGE = 2;
    static final int NOIR = 4;
    static final int VALUE_DEFENCE = 50;
    static final int VALUE_ATTACK = 200;
    static final int VALUE_SIZE = 1000;
    static final int DANGER_3_LAST_COL = 100000;
    static final int DANGER_NO_LAST_DEF = 100000;
    static final int WIN_VALUE = 9999999;

    // test (on joue les noirs)
    public int getValueOfBoard(int[][] board, Move mouvement, int depth, int nbPion) {
        int value = 0;

        if (toMax == 4) {

            // score survie des pions noirs
            value += VALUE_SIZE * nbPion;

            // regarde si le pion noir tue un rouge
            if (board[mouvement.arrive.x][mouvement.arrive.y] == ROUGE) {

                // regard backup gauche
                if (mouvement.depart.x <= 6 && mouvement.depart.y <= 6
                        && board[mouvement.depart.x + 1][mouvement.depart.y - 1] == NOIR) {
                    value += VALUE_DEFENCE;
                }

                // regard backup droite
                if (mouvement.depart.x >= 1 && mouvement.depart.y <= 6
                        && board[mouvement.depart.x - 1][mouvement.depart.y - 1] == NOIR) {
                    value += VALUE_DEFENCE;
                }

                value += 10;
            }

            // regard si move est suicide a gauche
            if (mouvement.arrive.x <= 6 && mouvement.arrive.y >= 1
                    && board[mouvement.arrive.x + 1][mouvement.arrive.y + -1] == ROUGE) {
                value -= 100;

            }

            // regard si move est suicide a droite
            if (mouvement.arrive.x >= 1 && mouvement.arrive.y >= 1
                    && board[mouvement.arrive.x - 1][mouvement.arrive.y - 1] == ROUGE) {
                value -= 100;
            }

            // regard si il va avoir du backup apres son move a gauche

            if (mouvement.depart.y != 7 && mouvement.arrive.x <= 6 && mouvement.arrive.y >= 1
                    && board[mouvement.arrive.x + 1][mouvement.arrive.y + 1] == NOIR) {
                value += VALUE_DEFENCE;
            }

            // regard si il va avoirt du backup apres son move a droite

            if (mouvement.depart.y != 7 && mouvement.arrive.x >= 1 && mouvement.arrive.y >= 1
                    && board[mouvement.arrive.x - 1][mouvement.arrive.y + 1] == NOIR) {
                value += VALUE_DEFENCE;
            }
            // danger value les rouges dans les 3 dernières col
            if (depth == 0 && board[mouvement.arrive.x][mouvement.arrive.y] == ROUGE && mouvement.arrive.y > 4) {
                value = value + DANGER_3_LAST_COL;

            }
           
            // si le pion est a 1, 2 ou 3 moves de win
            if ((depth == 0 || depth == 2) && mouvement.arrive.y == 0) {

                value += WIN_VALUE;

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
         * if(toMax = 2){
         */
        // }
        return value;
    }

}
