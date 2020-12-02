package MinMax;

import java.util.ArrayList;
import java.util.List;

public class GenerateurMoveRouge extends GenerateurMove {

  public GenerateurMoveRouge(int[][] board) {
    super(board);
    // TODO Auto-generated constructor stub
  }

  @Override
  public GenerateurMove newInstance(int[][] board) {
    // TODO Auto-generated method stub
    return new GenerateurMoveRouge(board);
  }

  @Override
  public List<Move> obtenirListeMoves() {
    // TODO Auto-generated method stub
    ArrayList<Move> listeMoves = new ArrayList<>();

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        // System.out.print(board[i][j]+" ");

        if (estRouge(board[i][j])) {
          // deplacement à gauche

          if (estDeplacableGauche(i, j)) {

            Move enDroiteHaut = new Move(i, j, i - 1, j + 1, board);
            listeMoves.add(enDroiteHaut);
          }
          // deplacement au centre
          if (estDeplacableDevant(i, j)) {
            Move enDroiteCentre = new Move(i, j, i, j + 1,board);
            listeMoves.add(enDroiteCentre);
          }

          if (estDeplacableDroite(i, j)) {
            Move enDroiteBas = new Move(i, j, i + 1, j + 1,board);
            listeMoves.add(enDroiteBas);
          }

        }
      }
      // System.out.println("");
    }
    return listeMoves;
  }

  // 0-1
  boolean estDeplacableGauche(int i, int j) {
    // ne pas sortir du board a gauche
    if (i == 0 || j == 7)
      return false;

    // si le pion a gauche est rouge
    return !estRouge(board[i - 1][j + 1]);
  }

  boolean estDeplacableDroite(int i, int j) {
    // ne pas sortir du board a droite
    if (j == 7 || i == 7)
      return false;

    // si le pion a droite est rouge
    return !estRouge(board[i + 1][j + 1]);
  }

  boolean estDeplacableDevant(int i, int j) {
    if (j == 7)
      return false;

    return estVide(board[i][j + 1]);
  }

}
