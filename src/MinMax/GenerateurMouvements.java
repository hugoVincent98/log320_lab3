package src.MinMax;

import java.util.List;

public abstract class GenerateurMouvements {
    int startRow, startCol;

    int endRow, endCol;

    static final int ROUGE = 2;
    static final int NOIR = 4;

    protected int[][] board;

    public GenerateurMouvements(int[][] board) {

        this.board = board;
    }

    public abstract List<Move> obtenirListeMoves();

    protected boolean estNoir(int couleur) {
        return NOIR == couleur;
    }

    protected boolean estRouge(int couleur) {
        return ROUGE == couleur;
    }

    protected boolean estVide(int emplacement) {
        return emplacement == 0;
    }
}