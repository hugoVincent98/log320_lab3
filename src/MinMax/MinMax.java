package MinMax;

import java.util.List;
import java.util.Arrays;

public class MinMax {
    static final int MAX = 100000000;
    static final int MIN = -100000000;
    static final int END = 6666666;

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
        updateBoard(board);
    }

    public void updateBoard(int[][] board) {
        this.board = board;
        if (toMax == ROUGE) {
            gen = new GenerateurMoveRouge(this.board);
            counterGen = new GenerateurMoveNoir(this.board);
        }
        if (toMax == NOIR) {
            gen = new GenerateurMoveNoir(board);
            counterGen = new GenerateurMoveRouge(this.board);
        }
    }

    /**
     * Retourne le prochain mouvement jugé le meilleur
     * 
     * @return
     */
    public Move getBestMove() {
        int meilleurScore = MIN;
        List<Move> myMoves = gen.obtenirListeMoves();
        Move meilleurMove = null;
        int[][] nboard;
        for (Move m : myMoves) {
            nboard = copy(this.board);
            // Pour chaque mouvements possibles, on va analyser son score

            int score = getValueOfBoard(board, m, 0, gen.getNbPionNoir(), gen.getNbPionRouge());
            // effectue le mouvement
            applyMove(nboard, m);

            System.out.println(" myMoves " + m.toCoordinate());

            score = score + miniMax(nboard, 1, false, MIN, MAX);

            // on prend toujours le meilleur score
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurMove = m;
            }
            System.out.println("meilleur score: " + score);
        }
        return meilleurMove;
    }

    static final int MAXDEPTH = 4;

    public int miniMax(int[][] board, int depth, boolean isMax, int alpha, int beta) {

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
            int[][] nboard;
            // pour chaque mouvements possible
            for (Move move : myMoves) {
                nboard = copy(board);
                int score = getValueOfBoard(board, move, depth, tempGen.getNbPionNoir(), tempGen.getNbPionRouge());

                // on effectue le déplacement dans le board
                applyMove(nboard, move);

                score += miniMax(nboard, depth + 1, false, alpha, beta);

                // si ce mouvement a un meilleur score que le meilleur score actuelle
                // on le garde en mémoire
                if (score > alpha) {
                    alpha = score;
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
            int[][] nboard;

            for (Move move : myMoves) {
                nboard = copy(board);
                int score = 0;
                applyMove(nboard, move);

                score += miniMax(nboard, depth + 1, true, alpha, beta);

                if (score < beta) {
                    beta = score;
                }
                if (alpha >= beta) {
                    break;
                }
            }
            return meilleurScore;
        }
    }

    /**
     * Applique le mouvement d'un object move dans le board
     * 
     * @param board
     * @param move
     */
    private void applyMove(int[][] board, Move move) {
        int value = board[move.depart.x][move.depart.y];
        board[move.arrive.x][move.arrive.y] = value;
        board[move.depart.x][move.depart.y] = 0;
    }

    /**
     * détermine dans un board qui a atteint la ligne d'arrivé
     * 
     * @param board
     * @return la couleur de l'équipe qui a win
     */
    public int checkWinner(int[][] board) {

        for (int i = 0; i < 8; i++) {
            if (board[i][0] == NOIR) {
                return NOIR;
            }
            if (board[i][7] == ROUGE) {
                return ROUGE;
            }
        }
        return 0;
    }

    /**
     * copy un board afin de ne pas garder la référence pour le modifier dans chaque
     * move
     * 
     * @param src
     * @return une copy du board
     */
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
    static final int VALUE_CASTLE = 100000;
    static final int DANGER_3_LAST_COL = 100000;
    static final int DANGER_NO_LAST_DEF = 100000;
    static final int WIN_VALUE = 9999999;

    /**
     * regarde l'état du board et lui associe un score
     * 
     * @param board
     * @param mouvement
     * @param depth
     * @param nbPionNoir
     * @return le score associé au board évalué
     */
    public int getValueOfBoard(int[][] board, Move mouvement, int depth, int nbPionNoir, int nbPionRouge) {
        int value = 0;

        // si notre AI joue les noirs
        if (toMax == NOIR) {

            // score survie des pions noirs
            value = value + (VALUE_SIZE * nbPionNoir);
            value = value - (VALUE_SIZE * nbPionRouge);

            // regarde si le pion noir tue un rouge
            if (board[mouvement.arrive.x][mouvement.arrive.y] == ROUGE) {
                value += VALUE_SIZE;
                value += VALUE_ATTACK;
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
                value -= 40;

            }

            // regard si move est suicide a droite
            if (mouvement.arrive.x >= 1 && mouvement.arrive.y >= 1
                    && board[mouvement.arrive.x - 1][mouvement.arrive.y - 1] == ROUGE) {
                value -= 40;
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

            // si le pion est a 1 ou 2 moves de win
            if ((depth == 0 || depth == 2) && mouvement.arrive.y == 0) {

                value += WIN_VALUE;

            }

            // stratégie du castle
            if (depth == 0 && turn < 7) {

                // pion noir de B7 à C6
                if (turn == 1 && mouvement.depart.equals(1, 6) && mouvement.arrive.equals(2, 5)) {
                    value += VALUE_CASTLE;
                }
                // pion noir de G7 à F6
                if (turn == 2 && mouvement.depart.equals(6, 6) && mouvement.arrive.equals(5, 5)) {
                    value += VALUE_CASTLE;
                }

                // pion noir de A8 à B7
                if (turn == 3 && mouvement.depart.equals(0, 7) && mouvement.arrive.equals(1, 6)) {
                    value += VALUE_CASTLE;
                }
                // pion noir de H8 à G7
                if (turn == 4 && mouvement.depart.equals(7, 7) && mouvement.arrive.equals(6, 6)) {
                    value += VALUE_CASTLE;
                }

                // pion noir de A7 à B6
                if (turn == 5 && mouvement.depart.equals(0, 6) && mouvement.arrive.equals(1, 5)) {
                    value += VALUE_CASTLE;
                }

                // pion noir de H7 à G6
                if (turn == 6 && mouvement.depart.equals(7, 6) && mouvement.arrive.equals(6, 5)) {
                    value += VALUE_CASTLE;
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
