package src.minmax;

import java.awt.*;

public class Move {

  Point depart;
  Point arrive;

  public Move(int xdepart, int ydepart, int xarrive, int yarrive) {

    this.depart = new Point(xdepart, ydepart);

    this.arrive = new Point(xarrive, yarrive);
  }
}
