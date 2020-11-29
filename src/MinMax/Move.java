package MinMax;

import java.awt.*;

public class Move {

  MovePoint depart;
  MovePoint arrive;

  public Move(int xdepart, int ydepart, int xarrive, int yarrive) {

    this.depart = new MovePoint(xdepart, ydepart);

    this.arrive = new MovePoint(xarrive, yarrive);
  }

  public boolean estGagnant() {
    if (arrive.getY() == 7 || arrive.getY() == 0) {
      return true;
    }
    return false;
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

}
