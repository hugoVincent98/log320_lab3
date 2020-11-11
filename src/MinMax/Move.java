
import java.awt.*;

public class Move {

  Point depart;
  Point arrive;

  public Move(int xdepart, int ydepart, int xarrive, int yarrive) {

    this.depart = new Point(xdepart, ydepart);

    this.arrive = new Point(xarrive, yarrive);
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

  public void setDepart(Point depart) {
    this.depart = depart;
  }

  public Point getArrive() {
    return arrive;
  }

  public void setArrive(Point arrive) {
    this.arrive = arrive;
  }

  @Override
  public String toString() {
    
    char lettredepart = (char)(this.depart.y + 65);
    int numerodepart = this.depart.x+1;

    char lettrearrive = (char)(this.arrive.y + 65);
    int numeroarrive = this.arrive.x + 1;
    return lettredepart+""+numerodepart+"-"+lettrearrive+""+numeroarrive;
  }

}
