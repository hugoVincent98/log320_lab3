package MinMax;

import java.awt.*;

public class MovePoint extends Point {

    public MovePoint(int x, int y) {
        super(x,y);
    }
    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }
}
