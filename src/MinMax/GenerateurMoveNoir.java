package MinMax;
import java.util.ArrayList;
import java.util.List;

public class GenerateurMoveNoir extends GenerateurMove {

  public GenerateurMoveNoir(int[][] board) {
    super(board);
    // TODO Auto-generated constructor stub
  }

  @Override
  public GenerateurMove newInstance(int[][] board) {
    // TODO Auto-generated method stub
    return new GenerateurMoveNoir(board);
  }

  @Override
  public List<Move> obtenirListeMoves() {
    // TODO Auto-generated method stub
    ArrayList<Move> listemoves = new ArrayList<>();

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        
        if (estNoir(board[i][j])) {
          // deplacement à gauche
          if (estDeplacableGauche(i, j)) {
            Move enDroiteHaut = new Move(i, j, i + 1, j - 1, board);
            listemoves.add(enDroiteHaut);
          }
          // deplacement au centre
          if (estDeplacableDevant(i, j)) {
            Move enDroiteCentre = new Move(i, j, i, j-1,board);
            listemoves.add(enDroiteCentre);
          }
          
          if (estDeplacableDroite(i, j)) {
            Move enDroiteBas = new Move(i, j, i - 1, j - 1,board);
            listemoves.add(enDroiteBas);
          }

        }
      }
    }
    return listemoves;
  }

  boolean estDeplacableGauche(int i, int j) {
    // ne pas sortir du board a gauche
    if (j == 0 || i == 7)
      return false;

    // si le pion a gauche est noir
    return !estNoir(board[i + 1][j - 1]);
  }

  boolean estDeplacableDroite(int i, int j) {
    // ne pas sortir du board a droite
    if (i == 0 || j == 0)
      return false;

    // si le pion a droite est rouge
    return !estNoir(board[i - 1][j - 1]);
  }

  boolean estDeplacableDevant(int i, int j) {
    if (j == 0)
      return false;
    return estVide(board[i][j-1]);
  }

}