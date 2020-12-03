package MinMax;

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

                int score = getValueOfBoard(move.getBoard(), move, depth, toMax);

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

                int score = getValueOfBoard(move.getBoard(), move, depth, toMin);
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

    static final int VIDE = 0;
    static final int ROUGE = 2;
    static final int NOIR = 4;
    static final int WINVALUE = 500000;
    static final int PIECEALMOSTWINVALUE = 10000;
    static final int PIECEVALUE = 1300;
    static final int PIECEDANGERVALUE = 10;
    static final int PIECEHIGHDANGERVALUE = 100;
    static final int PIECEATTACKVALUE = 50;
    static final int PIECEPROTECTIONVALUE = 65;
    static final int PIECECONNECTIONHVALUE = 35;
    static final int PIECECONNECTIONVVALUE = 15;
    static final int PIECECOLUMNHOLEVALUE = 20;
    static final int PIECEHOMEGROUNDVALUE = 10;
    static final int VALUE_CASTLE = 1000;

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

        // si notre AI joue les noirs
        if (toMax == NOIR) {

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
            } else {
                // algo pour les noirs
            }
        }

        if (toMax == ROUGE) {
            // score survie des pions noirs

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

    private int getOppositeColor(int couleur) {
        if (couleur == NOIR)
            return ROUGE;
        else
            return NOIR;
    }

    private int getPieceValue(int[][] board, int i, int j, int couleur) {

        int value = PIECEVALUE;

        // valeur connexion horizontale
        if ((j > 1 || j < 7) && ((caseLegit(i, j - 1) && couleur == board[i][j - 1])
                || (caseLegit(i, j + 1) && couleur == board[i][j + 1]))) {
            value += PIECECONNECTIONHVALUE;
        }

        // valeur connexion verticale
        if ((caseLegit(i + 1, j) && board[i + 1][j] == couleur)
                || (caseLegit(i - 1, j) && board[i - 1][j] == couleur)) {
            value += PIECECONNECTIONVVALUE;
        }
        int validMoveCount = 0;
        boolean estProtected = false;
        int attackvaluescompteur = 0;
        // valeur de attaque
        if (couleur == NOIR) {
            if (caseLegit(i - 1, j - 1)) {
                if (caseEstRouge(board, i - 1, j - 1)) {
                    value -= PIECEATTACKVALUE;
                    attackvaluescompteur++;
                } else if (caseEstNoir(board, i - 1, j)) {
                    value += PIECEPROTECTIONVALUE;
                    estProtected = true;
                } else {
                    validMoveCount++;
                }
            }
            if (caseLegit(i + 1, j - 1)) {
                if (caseEstRouge(board, i + 1, j - 1)) {
                    value -= PIECEATTACKVALUE;
                    attackvaluescompteur++;
                } else if (caseEstNoir(board, i + 1, j - 1)) {
                    value += PIECEPROTECTIONVALUE;
                    estProtected = true;
                } else {
                    validMoveCount++;
                }
            }

        } else {
            if (caseLegit(i - 1, j + 1)) {
                if (caseEstNoir(board, i - 1, j + 1)) {

                    value -= PIECEATTACKVALUE;
                    attackvaluescompteur++;
                } else if (caseEstRouge(board, i - 1, j + 1)) {
                    value += PIECEPROTECTIONVALUE;
                    estProtected = true;
                } else {
                    validMoveCount++;
                }
            }
            if (caseLegit(i + 1, j + 1)) {
                if (caseEstNoir(board, i + 1, j + 1)) {
                    value -= PIECEATTACKVALUE;
                    attackvaluescompteur++;
                } else if (caseEstRouge(board, i + 1, j + 1)) {
                    value += PIECEPROTECTIONVALUE;
                    estProtected = true;
                } else {
                    validMoveCount++;
                }
            }
        }

        if (!estProtected) {
            value -= (PIECEATTACKVALUE * attackvaluescompteur);
        } else {
            if (couleur == ROUGE) {
                if (j == 5)
                    value += PIECEDANGERVALUE;
                else if (j == 6)
                    value += PIECEHIGHDANGERVALUE;
            } else {
                if (j == 2)
                    value += PIECEDANGERVALUE;
                else if (j == 1)
                    value += PIECEHIGHDANGERVALUE;
            }
        }

        // danger value
        if (couleur == ROUGE)
            value += j * PIECEDANGERVALUE;
        else
            value += (8 - j) * PIECEDANGERVALUE;

        // mobility feature
        if (couleur == ROUGE && caseLegit(i, j + 1) && board[i][j + 1] == VIDE) {
            validMoveCount++;
        } else if (couleur == NOIR && caseLegit(i, j - 1) && board[i][j - 1] == VIDE) {
            validMoveCount++;
        }
        value += Math.min(3, validMoveCount);
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
