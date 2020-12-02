package MinMax;

import java.util.LinkedList;
import java.util.List;

public abstract class GenerateurMove {
    int startRow;
    int startCol;

    int endRow;
    int endCol;
    
    protected LinkedList<Pion> listePionsRouge;
    protected LinkedList<Pion> listePionsNoir;
    protected int nbPionRouge;
    protected int nbPionNoir;

    static final int ROUGE = 2;
    static final int NOIR = 4;

    protected int[][] board;

    protected GenerateurMove(int[][] board) {

        this.board = board;
        listePionsRouge = new LinkedList<>();
        listePionsNoir = new LinkedList<>();
    }

    public abstract List<Move> obtenirListeMoves();

    protected boolean estNoir(int couleur) {
        return NOIR == couleur;
    }

    
    public int getNbPionRouge() {
        return nbPionRouge;
    }


    public int getNbPionNoir() {
        return nbPionNoir;
    }
    protected boolean estRouge(int couleur) {
        return ROUGE == couleur;
    }

    protected boolean estVide(int emplacement) {
        return emplacement == 0;
    }

    abstract GenerateurMove newInstance(int[][] board);

    abstract boolean estDeplacableGauche(int xdepart, int ydepart);

    abstract boolean estDeplacableDroite(int xdepart, int ydepart);

    abstract boolean estDeplacableDevant(int xdepart, int ydepart);

    public List<Pion> getListePionsRouge() {
        return listePionsRouge;
    }

    public List<Pion> getListePionsNoir() {
        return listePionsNoir;
    }

}