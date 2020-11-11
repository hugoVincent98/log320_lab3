
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Node {
  Move move;
  boolean isMax;
  int score;
  ArrayList<Node> enfant;

  public Node(Move move , boolean isMax) {
    this.move = move;
    this.isMax = isMax;
    this.enfant = new ArrayList<Node>();
  }

  /*public void setEnfant(List<Node> enfant) {
    this.enfant = enfant;
  }*/

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

