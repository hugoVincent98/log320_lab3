package MinMax;


import java.util.List;

public abstract class GenerateurMove {
    int startRow, startCol;

    int endRow, endCol;

    static final int ROUGE = 2;
    static final int NOIR = 4;

    protected int[][] board;

    protected GenerateurMove(int[][] board) {

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

    abstract boolean estDeplacableGauche(int xdepart, int ydepart);

    abstract boolean estDeplacableDroite(int xdepart, int ydepart);

    abstract boolean estDeplacableDevant(int xdepart, int ydepart);

}