package src.MinMax;

import java.util.ArrayList;
import java.util.List;

public class GenerateurMoveRouge extends GenerateurMouvements {

  public GenerateurMoveRouge(int[][] board) {
    super(board);
    // TODO Auto-generated constructor stub
  }

  @Override
  public List<Move> obtenirListeMoves() {
    // TODO Auto-generated method stub
    ArrayList<Move> listeMoves = new ArrayList();

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {

        if (estRouge(board[i][j])) {

          // deplacement à gauche
          if (estDeplacableGauche(i, j)) {
            Move enDroiteHaut = new Move(i, j, i - 1, j + 1);
            listeMoves.add(enDroiteHaut);
          }
          // deplacement au centre
          if (estDeplacableDevant(i, j)) {
            Move enDroiteCentre = new Move(i, j, i, j + 1);
            listeMoves.add(enDroiteCentre);
          }

          if (!estDeplacableDroite(i, j)) {
            Move enDroiteBas = new Move(i, j, i + 1, j + 1);
            listeMoves.add(enDroiteBas);
          }

        }
      }
    }
    return null;
  }

  boolean estDeplacableGauche(int xdepart, int ydepart) {
    // ne pas sortir du board a gauche
    if (xdepart == 0)
      return false;

    // si le pion a gauche est rouge
    if (estRouge(board[xdepart - 1][ydepart + 1]))
      return false;

    return true;
  }

  boolean estDeplacableDroite(int xdepart, int ydepart) {
    // ne pas sortir du board a droite
    if (xdepart == 7)
      return false;

    // si le pion a droite est rouge
    if (estRouge(board[xdepart + 1][ydepart + 1]))
      return false;

    return true;
  }

  boolean estDeplacableDevant(int xdepart, int ydepart) {
    return estVide(board[xdepart][ydepart + 1]);
  }
}
