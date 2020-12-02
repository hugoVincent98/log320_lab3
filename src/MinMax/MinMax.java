package MinMax;

import java.util.List;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;

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

            int score = getValueOfBoard(m.getBoard(), m, 0);
            // effectue le mouvement

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

    static final int MAXDEPTH = 3;

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

                int score = getValueOfBoard(move.getBoard(), move, depth);

                // on effectue le déplacement dans le board

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
                int score = 0;

                score += miniMax(move.getBoard(), depth + 1, true, alpha, beta);

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

    static final int ROUGE = 2;
    static final int NOIR = 4;
    static final int VALUE_DEFENCE = 50;
    static final int VALUE_ATTACK = 200;
    static final int VALUE_SIZE = 1000;
    static final int VALUE_AUTOWIN = 1000000;
    static final int VALUE_LOSE = -99999999;
    static final int VALUE_CASTLE = 100000;

    /**
     * regarde l'état du board et lui associe un score
     * 
     * @param board
     * @param mouvement
     * @param depth
     * @param list
     * @return le score associé au board évalué
     */
    public int getValueOfBoard(int[][] board, Move mouvement, int depth) {
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

            // strategie du castle
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
            } else {
                // strategie main

                // analyzeNumbers
                value += (pionsnoir.isEmpty() ? VALUE_LOSE : pionsnoir.size() * VALUE_SIZE);
                value -= (pionsrouge.isEmpty() ? VALUE_LOSE : pionsrouge.size() * VALUE_SIZE);

                // analyze potentiel de attack de chaque team
                value += potentielAttackNoir(pionsnoir, board);
                value -= potentielAttackRouge(pionsrouge, board);

                // autowin conditions
                int autoWin = autowinNoir(pionsnoir, pionsrouge);
                int autoWinennemy = autowinRouge(pionsnoir, pionsrouge);
                if (autoWin > autoWinennemy){
                    value += autoWin;
                }else if (autoWin < autoWinennemy){
                    value -= autoWinennemy;
                }
                return value;
            }
        }

        if (toMax == ROUGE) {

            // stratégie du castle
            if (depth == 0 && turn < 7) {

                // pion noir de B2 à C3
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
            }

        }
        return value;
    }

    private int autowinRouge(List<int[]> pionsrouge, List<int[]> pionsnoir) {
        boolean isInEnemySide;
        boolean autoWinLeft = true;
        boolean autoWinRight = true;
        boolean isInFront;
        boolean isInFront2;
        int value = 0;
        for (int[] pionrouge : pionsrouge) {
            boolean cannotGoInFront = false;
            int minPionValueLeft = pionrouge[0];
            int minPionValueRight = 7 - pionrouge[0];
            isInEnemySide = pionrouge[1] >= 4; // si coté noir
            if (isInEnemySide) {
                autoWinLeft = true;
                autoWinRight = true;
                for (int[] pionnoir : pionsnoir) {
                    if (pionrouge[0] == pionnoir[0] && pionrouge[1] + 1 == pionnoir[1]) {// Check right in front
                        cannotGoInFront = true;
                    } else {
                        isInFront = pionnoir[0] == pionrouge[0] && pionnoir[1] > pionrouge[1] + 1;
                        // Check if there is an enemy pawn in front of the current pawn
                        if (isInFront) {
                            autoWinLeft = false;
                            autoWinRight = false;
                            break;
                        } else if (Math.abs(pionnoir[0] - pionrouge[0]) == 1 && pionrouge[1] + 1 == pionnoir[1]) {
                            // Check if our pawn is under-attack
                            autoWinLeft = false;
                            autoWinRight = false;
                            break;
                        } else if (Math.abs((pionnoir[0] - pionrouge[0])) == 1
                                && (pionrouge[1] + (1 * 3)) == pionnoir[1]) { // Check if our pawn is
                                                                               // under-controlled
                            autoWinLeft = false;
                            autoWinRight = false;
                            break;
                        } else if (pionnoir[0] < pionrouge[0]) {
                            // Check left pawns for AutoWinRight
                            isInFront2 = pionnoir[1] > pionrouge[1] + 1;
                            if (isInFront2) {
                                int pawnValueLeft = Math.abs(pionnoir[0] - pionrouge[0])
                                        - Math.abs(pionnoir[1] - pionrouge[1]);
                                if (pawnValueLeft < minPionValueLeft) {
                                    minPionValueLeft = pawnValueLeft;
                                }
                            }
                        } else if (pionnoir[0] > pionrouge[0]) {
                            // Check right pawns for AutoWinRight
                            isInFront2 = (pionnoir[1] > pionrouge[1] + 1);
                            if (isInFront2) {
                                int pawnValueRight = Math.abs(pionnoir[0] - pionrouge[0])
                                        - Math.abs(pionnoir[1] - pionrouge[1]);
                                if (pawnValueRight < minPionValueRight) {
                                    minPionValueRight = pawnValueRight;

                                }
                            }
                        }
                    }
                } // for

                if ((minPionValueLeft + minPionValueRight) >= 0 && autoWinLeft && autoWinRight) {
                    if ((minPionValueLeft + minPionValueRight) == 0 && cannotGoInFront) {
                        // System.out.println("cannotGoInFront");
                    } else {
                        int offsetYtoWin = 4 - (7 - pionrouge[1]);
                        if (VALUE_AUTOWIN * offsetYtoWin >= value) {
                            value = VALUE_AUTOWIN * offsetYtoWin;
                        }
                    }
                }
            }
        }
        return value;
    }

    private int autowinNoir(List<int[]> pionsnoir, List<int[]> pionsrouge) {
        boolean isInEnemySide;
        boolean autoWinLeft = true;
        boolean autoWinRight = true;
        boolean isInFront;
        boolean isInFront2;
        int value = 0;
        for (int[] pionnoir : pionsnoir) {
            boolean cannotGoInFront = false;
            int minPionValueLeft = pionnoir[0];
            int minPionValueRight = 7 - pionnoir[0];
            isInEnemySide = pionnoir[1] <= 3; // si coté rouge
            if (isInEnemySide) {
                autoWinLeft = true;
                autoWinRight = true;
                for (int[] pionrouge : pionsrouge) {
                    if (pionnoir[0] == pionrouge[0] && pionnoir[1] - 1 == pionrouge[1]) {// Check right in front
                        cannotGoInFront = true;
                    } else {
                        isInFront = pionrouge[0] == pionnoir[0] && pionrouge[1] < pionnoir[1] - 1;
                        // Check if there is an enemy pawn in front of the current pawn
                        if (isInFront) {
                            autoWinLeft = false;
                            autoWinRight = false;
                            break;
                        } else if (Math.abs(pionrouge[0] - pionnoir[0]) == 1 && pionnoir[1] - 1 == pionrouge[1]) {
                            // Check if our pawn is under-attack
                            autoWinLeft = false;
                            autoWinRight = false;
                            break;
                        } else if (Math.abs((pionrouge[0] - pionnoir[0])) == 1
                                && (pionnoir[1] + (-1 * 3)) == pionrouge[1]) { // Check if our pawn is
                                                                               // under-controlled
                            autoWinLeft = false;
                            autoWinRight = false;
                            break;
                        } else if (pionrouge[0] < pionnoir[0]) {
                            // Check left pawns for AutoWinRight
                            isInFront2 = pionrouge[1] < pionnoir[1] + 1;
                            if (isInFront2) {
                                int pawnValueLeft = Math.abs(pionrouge[0] - pionnoir[0])
                                        - Math.abs(pionrouge[1] - pionnoir[1]);
                                if (pawnValueLeft < minPionValueLeft) {
                                    minPionValueLeft = pawnValueLeft;
                                }
                            }
                        } else if (pionrouge[0] > pionnoir[0]) {
                            // Check right pawns for AutoWinRight
                            isInFront2 = (pionrouge[1] < pionnoir[1] - 1);
                            if (isInFront2) {
                                int pawnValueRight = Math.abs(pionrouge[0] - pionnoir[0])
                                        - Math.abs(pionrouge[1] - pionnoir[1]);
                                if (pawnValueRight < minPionValueRight) {
                                    minPionValueRight = pawnValueRight;

                                }
                            }
                        }
                    }
                } // for

                if ((minPionValueLeft + minPionValueRight) >= 0 && autoWinLeft && autoWinRight) {
                    if ((minPionValueLeft + minPionValueRight) == 0 && cannotGoInFront) {
                        // System.out.println("cannotGoInFront");
                    } else {
                        int offsetYtoWin = 4 - pionnoir[1];
                        if (VALUE_AUTOWIN * offsetYtoWin >= value) {
                            value = VALUE_AUTOWIN * offsetYtoWin;
                        }
                    }
                }
            }
        }
        return value;
    }

    private int potentielAttackRouge(List<int[]> pionsrouge, int[][] board) {
        int value = 0;
        for (int[] pionrouge : pionsrouge) {
            // verifier attack vers lavant sa gauche
            if (caseLegit(pionrouge[0] - 1, pionrouge[1] + 1)) {
                if (caseEstRouge(board, pionrouge[0] - 1, pionrouge[1] + 1)) {
                    value += VALUE_ATTACK;

                    // verifier defense en arriere a gauche
                    if (caseLegit(pionrouge[0] - 1, pionrouge[1] - 1)
                            && caseEstNoir(board, pionrouge[0] - 1, pionrouge[1] - 1)) {
                        value += VALUE_DEFENCE;
                    }

                    // verifier defense en arriere a droite
                    if (caseLegit(pionrouge[0] + 1, pionrouge[1] - 1)
                            && caseEstNoir(board, pionrouge[0] + 1, pionrouge[1] - 1)) {
                        value += VALUE_DEFENCE;
                    }
                }
            }
            // verifier attack vers lavant sa droite
            if (caseLegit(pionrouge[0] + 1, pionrouge[1] + 1)) {
                if (caseEstRouge(board, pionrouge[0] + 1, pionrouge[1] + 1)) {
                    value += VALUE_ATTACK;

                    // verifier defense en arriere a gauche
                    if (caseLegit(pionrouge[0] - 1, pionrouge[1] - 1)
                            && caseEstNoir(board, pionrouge[0] - 1, pionrouge[1] - 1)) {
                        value += VALUE_DEFENCE;
                    }

                    // verifier defense en arriere a droite
                    if (caseLegit(pionrouge[0] + 1, pionrouge[1] - 1)
                            && caseEstNoir(board, pionrouge[0] + 1, pionrouge[1] - 1)) {
                        value += VALUE_DEFENCE;
                    }
                }
            }
        }
        return value;
    }

    private int potentielAttackNoir(List<int[]> pionsnoir, int[][] board) {
        int value = 0;
        for (int[] pionnoir : pionsnoir) {
            // verifier attack vers lavant sa gauche
            if (caseLegit(pionnoir[0] + 1, pionnoir[1] - 1)) {
                if (caseEstRouge(board, pionnoir[0] + 1, pionnoir[1] - 1)) {
                    value += VALUE_ATTACK;

                    // verifier defense en arriere a gauche
                    if (caseLegit(pionnoir[0] + 1, pionnoir[1] + 1)
                            && caseEstNoir(board, pionnoir[0] + 1, pionnoir[1] + 1)) {
                        value += VALUE_DEFENCE;
                    }

                    // verifier defense en arriere a droite
                    if (caseLegit(pionnoir[0] - 1, pionnoir[1] + 1)
                            && caseEstNoir(board, pionnoir[0] - 1, pionnoir[1] + 1)) {
                        value += VALUE_DEFENCE;
                    }
                }
            }
            // verifier attack vers lavant sa droite
            if (caseLegit(pionnoir[0] - 1, pionnoir[1] - 1)) {
                if (caseEstRouge(board, pionnoir[0] - 1, pionnoir[1] - 1)) {
                    value += VALUE_ATTACK;

                    // verifier defense en arriere a gauche
                    if (caseLegit(pionnoir[0] + 1, pionnoir[1] + 1)
                            && caseEstNoir(board, pionnoir[0] + 1, pionnoir[1] + 1)) {
                        value += VALUE_DEFENCE;
                    }

                    // verifier defense en arriere a droite
                    if (caseLegit(pionnoir[0] - 1, pionnoir[1] + 1)
                            && caseEstNoir(board, pionnoir[0] - 1, pionnoir[1] + 1)) {
                        value += VALUE_DEFENCE;
                    }
                }
            }
        }
        return value;
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
