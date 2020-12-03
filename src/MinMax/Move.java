package MinMax;

import java.awt.*;
import java.util.Arrays;

public class Move {

  MovePoint depart;
  MovePoint arrive;
  int[][] boardupdated;

  public Move(int xdepart, int ydepart, int xarrive, int yarrive, int[][] board) {

    this.depart = new MovePoint(xdepart, ydepart);
    this.arrive = new MovePoint(xarrive, yarrive);
    boardupdated = copy(board);
     applyMove(boardupdated, this);

  }

  public boolean estGagnant() {
    if (arrive.getY() == 7 || arrive.getY() == 0) {
      return true;
    }
    return false;
  }

  public int[][] getBoard() {
    return boardupdated;
  }

  public Point getDepart() {
    return depart;
  }

  public void setDepart(MovePoint depart) {
    this.depart = depart;
  }

  public Point getArrive() {
    return arrive;
  }

  public void setArrive(MovePoint arrive) {
    this.arrive = arrive;
  }

  /**
   * Méthode qui retourne le mouvement en format demandé par le programme de jeu
   * 
   * @return
   */
  @Override
  public String toString() {

    char lettredepart = (char) (this.depart.x + 65);
    char numerodepart = (char) (this.depart.y + 49);

    char lettrearrive = (char) (this.arrive.x + 65);
    char numeroarrive = (char) (this.arrive.y + 49);
    return lettredepart + "" + numerodepart + "-" + lettrearrive + "" + numeroarrive;
  }

  public String toCoordinate() {
    return "Depart  : [x=" + this.depart.x + ",y=" + this.depart.y + "]   Arrivé : [x=" + this.arrive.x + ",y="
        + this.arrive.y + "]";
  }

  /**
   * Applique le mouvement d'un object move dans le board
   * 
   * @param board
   * @param move
   */
  private static void applyMove(int[][] board, Move move) {
    int value = board[move.depart.x][move.depart.y];
    board[move.arrive.x][move.arrive.y] = value;
    board[move.depart.x][move.depart.y] = 0;
  }

  public void makeMove() {
    applyMove(this.boardupdated, this);
  }

  /**
   * copy un board afin de ne pas garder la référence pour le modifier dans chaque
   * move
   * 
   * @param src
   * @return une copy du board
   */
  public static int[][] copy(int[][] src) {
    int[][] dst = new int[src.length][];
    for (int i = 0; i < src.length; i++) {
      dst[i] = Arrays.copyOf(src[i], src[i].length);
    }
    return dst;
  }

}
