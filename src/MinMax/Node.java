package MinMax;

import java.util.LinkedList;
import java.util.List;

public class Node {
    Move move;
    boolean isMax;
    int score;
    List<Node> enfant;

    public Node(Move move , boolean isMax) {
      this.move = move;
      this.isMax = isMax;
    }

    public void setEnfant(List<Node> enfant) {
      this.enfant = enfant;
    }

    public List<Node> getEnfant() {
      return enfant;
    }

    public boolean getIsMax(){
      return this.isMax;
    }

    public int getScore() {
      return score;
    }

    public void addEnfant(Node enfant){
      this.enfant.add(enfant);
    }
}
