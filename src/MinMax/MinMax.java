package MinMax;

import java.util.LinkedList;
import java.util.List;

import java.time.Duration;
import java.time.Instant;

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
        for (Move m : myMoves) {
            // Pour chaque mouvements possibles, on va analyser son score

            int score = getValueOfBoard(m.getBoard(), m, 0, toMax);
            // effectue le mouvement
            m.makeMove();
            System.out.println(" myMoves " + m.toCoordinate());

            score = score + miniMax(m.getBoard(), 1, false, MIN, MAX);

            // on prend toujours le meilleur score
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleurMove = m;
            }
            System.out.println("meilleur score: " + score);
        }
        return meilleurMove;
    }

    static final int MAXDEPTH = 1;

    public int miniMax(int[][] board, int depth, boolean isMax, int alpha, int beta) {
        // || hasFourSecondPassed()
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
            // pour chaque mouvements possible
            for (Move move : myMoves) {

                int score = getValueOfBoard(move.getBoard(), move, depth, toMax);

                // on effectue le déplacement dans le board
                move.makeMove();
                score += miniMax(move.getBoard(), depth + 1, false, alpha, beta);

                // si ce mouvement a un meilleur score que le meilleur score actuelle
                // on le garde en mémoire
                meilleurScore = Math.max(score, meilleurScore);
                alpha = Math.max(alpha, score);

                if (alpha >= beta) {
                    break;
                }
            }
            return alpha;
            // toMinimize
        } else {
            int meilleurScore = MAX;
            GenerateurMove tempCounterGen = counterGen.newInstance(board);
            List<Move> myMoves = tempCounterGen.obtenirListeMoves();

            for (Move move : myMoves) {
                move.makeMove();
                //int score = getValueOfBoard(move.getBoard(), move, depth, toMin);
                int score = miniMax(move.getBoard(), depth + 1, true, alpha, beta);

                meilleurScore = Math.min(score, meilleurScore);
                beta = Math.min(beta, score);
                if (alpha >= beta) {
                    break;
                }
            }
            return meilleurScore;
        }
    }

    private static Instant timer;

    public static void startTimer() {
        timer = Instant.now();
    }

    public static boolean hasFourSecondPassed() {
        return Duration.between(timer, Instant.now()).getSeconds() >= 4;
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

    static final int VIDE = 0;
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
     * @param toMax2
     * @param list
     * @return le score associé au board évalué
     */
    public int getValueOfBoard(int[][] board, Move mouvement, int depth, int couleur) {
        int value = 0;

        List<int[]> pionsrouge = new LinkedList<>();
        List<int[]> pionsnoir = new LinkedList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == NOIR)
                    pionsnoir.add((new int[] { i, j }));
                if (board[i][j] == ROUGE)
                    pionsrouge.add((new int[] { i, j }));
            }
        }

        // si notre AI joue les noirs
        if (toMax == NOIR) {

            // score survie des pions noirs
            value = value + (VALUE_SIZE * pionsrouge.size());
            value = value - (VALUE_SIZE * pionsnoir.size());

            // regarde si le pion noir tue un rouge
            if (board[mouvement.arrive.x][mouvement.arrive.y] == ROUGE) {
                value += VALUE_SIZE;
                value += VALUE_ATTACK;
                // regard backup gauche
                if (mouvement.depart.x <= 6 && mouvement.depart.y <= 6
                        && board[mouvement.depart.x + 1][mouvement.depart.y + 1] == NOIR) {
                    value += VALUE_DEFENCE;
                }

                // regard backup droite
                if (mouvement.depart.x >= 1 && mouvement.depart.y <= 6
                        && board[mouvement.depart.x - 1][mouvement.depart.y + 1] == NOIR) {
                    value += VALUE_DEFENCE;
                }

                value += 10;
            }

            // regard si move est suicide a gauche
            if (mouvement.arrive.x <= 6 && mouvement.arrive.y >= 1
                    && board[mouvement.arrive.x + 1][mouvement.arrive.y - 1] == ROUGE) {
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
            if ((depth == 0 || depth == 2 || depth == 4) && mouvement.arrive.y == 0) {

                value += WIN_VALUE / (depth == 0 ? 1 : depth);
                System.out.println("Pion au niv " + depth + " : " + mouvement.toCoordinate() + " value : " + value);

            }

            // stratégie du castle
            if (depth == 0 && turn < 7) {
                value += strategieCastleNoir(turn, mouvement);

            }
        }

        if (toMax == ROUGE) {
            // score survie des pions noirs
            value = value + (VALUE_SIZE * pionsrouge.size());
            value = value - (VALUE_SIZE * pionsnoir.size());

            // regarde si le pion noir tue un rouge
            if (board[mouvement.arrive.x][mouvement.arrive.y] == NOIR) {
                value += VALUE_SIZE;
                value += VALUE_ATTACK;
                // regard backup gauche
                if (mouvement.depart.x >= 1 && mouvement.depart.y <= 6
                        && board[mouvement.depart.x - 1][mouvement.depart.y + 1] == ROUGE) {
                    value += VALUE_DEFENCE;
                }

                // regard backup droite
                if (mouvement.depart.x <= 6 && mouvement.depart.y <= 6
                        && board[mouvement.depart.x + 1][mouvement.depart.y + 1] == ROUGE) {
                    value += VALUE_DEFENCE;
                }

                value += 10;
            }

            // regard si move est suicide a gauche
            if (mouvement.arrive.x <= 6 && mouvement.arrive.y <= 6
                    && board[mouvement.arrive.x + 1][mouvement.arrive.y + 1] == NOIR) {
                value -= 100;

            }

            // regard si move est suicide a droite
            if (mouvement.arrive.x >= 1 && mouvement.arrive.y <= 6
                    && board[mouvement.arrive.x - 1][mouvement.arrive.y + 1] == NOIR) {
                value -= 100;
            }

            // regard si il va avoir du backup apres son move a droite

            if (mouvement.depart.y != 0 && mouvement.arrive.x <= 6 && mouvement.arrive.y >= 1
                    && board[mouvement.arrive.x + 1][mouvement.arrive.y - 1] == ROUGE) {
                value += VALUE_DEFENCE;
            }

            // regard si il va avoir du backup apres son move a gauche
            if (mouvement.depart.y != 0 && mouvement.arrive.x >= 1 && mouvement.arrive.y >= 1
                    && board[mouvement.arrive.x - 1][mouvement.arrive.y - 1] == ROUGE) {
                value += VALUE_DEFENCE;
            }
            // danger value les noirs dans les 3 dernières col
            if (depth == 0 && board[mouvement.arrive.x][mouvement.arrive.y] == NOIR && mouvement.arrive.y < 2) {
                value = value + DANGER_3_LAST_COL;

            }

            // si le pion est a 5 ou 6 moves de win
            if ((depth == 0 || depth == 2 || depth == 4) && mouvement.arrive.y == 7) {

                value += WIN_VALUE / (depth == 0 ? 1 : depth);
                System.out.println("Pion au niv " + depth + " : " + mouvement.toCoordinate());

            }

            // stratégie du castle
            if (depth == 0 && turn < 7) {

                value += strategieCastleRouge(mouvement, turn);
            }

        }

        return value;
    }

    private int strategieCastleRouge(Move mouvement, int turn) {
        // pion noir de B2 à C3
        int value = 0;
        if (turn == 1 && mouvement.depart.equals(1, 1) && mouvement.arrive.equals(2, 2)) {
            value += VALUE_CASTLE;
        }
        // pion noir de G2 à F3
        if (turn == 2 && mouvement.depart.equals(6, 1) && mouvement.arrive.equals(5, 2)) {
            value += VALUE_CASTLE;
        }

        // pion noir de A1 à B2
        if (turn == 3 && mouvement.depart.equals(0, 0) && mouvement.arrive.equals(1, 1)) {
            value += VALUE_CASTLE;
        }
        // pion noir de H1 à G2
        if (turn == 4 && mouvement.depart.equals(7, 0) && mouvement.arrive.equals(6, 1)) {
            value += VALUE_CASTLE;
        }

        // pion noir de A2 à B3
        if (turn == 5 && mouvement.depart.equals(0, 1) && mouvement.arrive.equals(1, 2)) {
            value += VALUE_CASTLE;
        }

        // pion noir de H2 à G3
        if (turn == 6 && mouvement.depart.equals(7, 1) && mouvement.arrive.equals(6, 2)) {
            value += VALUE_CASTLE;
        }
        return value;
    }

    private int strategieCastleNoir(int turn, Move mouvement) {
        int value = 0;
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
        return value;
    }

    private int getOppositeColor(int couleur) {
        if (couleur == NOIR)
            return ROUGE;
        else
            return NOIR;
    }

    private boolean caseLegit(int x, int y) {
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }

    private boolean caseEstNoir(int[][] board, int x, int y) {
        return board[x][y] == NOIR;
    }

    private boolean caseEstRouge(int[][] board, int x, int y) {
        return board[x][y] == ROUGE;
    }

}
