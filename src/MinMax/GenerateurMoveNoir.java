package src.minmax;

import java.util.ArrayList;
import java.util.List;

public class GenerateurMoveNoir extends GenerateurMove {

  public GenerateurMoveNoir(int[][] board) {
    super(board);
    // TODO Auto-generated constructor stub
  }

  @Override
  public List<Move> obtenirListeMoves() {
    // TODO Auto-generated method stub
    ArrayList<Move> listemoves = new ArrayList<>();

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {

        if (estRouge(board[i][j])) {

          // deplacement à gauche
          if (estDeplacableGauche(i, j)) {
            Move enDroiteHaut = new Move(i, j, i + 1, j - 1);
            listemoves.add(enDroiteHaut);
          }
          // deplacement au centre
          if (estDeplacableDevant(i, j)) {
            Move enDroiteCentre = new Move(i, j, i, j - 1);
            listemoves.add(enDroiteCentre);
          }

          if (!estDeplacableDroite(i, j)) {
            Move enDroiteBas = new Move(i, j, i - 1, j - 1);
            listemoves.add(enDroiteBas);
          }

        }
      }
    }
    return listemoves;
  }

  boolean estDeplacableGauche(int xdepart, int ydepart) {
    // ne pas sortir du board a gauche
    if (xdepart == 7)
      return false;

    // si le pion a gauche est noir
    return !estNoir(board[xdepart + 1][ydepart - 1]);
  }

  boolean estDeplacableDroite(int xdepart, int ydepart) {
    // ne pas sortir du board a droite
    if (xdepart == 0)
      return false;

    // si le pion a droite est rouge
    return !estNoir(board[xdepart - 1][ydepart - 1]);
  }

  boolean estDeplacableDevant(int xdepart, int ydepart) {
    return estVide(board[xdepart][ydepart - 1]);
  }
}