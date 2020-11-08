package src.minmax;

import java.util.*;

public class MinMaxTree {

  private class Node {
    int node;
    boolean isMax;
    int score;
    List<Node> enfant;

    public void setEnfant(List<Node> enfant) {
      this.enfant = enfant;
    }

    public List<Node> getEnfant() {
      return enfant;
    }

    public Node(int node, boolean isMax) {
      this.node = node;
      this.isMax = isMax;
    }

    public int getScore() {
      return score;
    }

  }

  private class Tree {
    Node root;

    public void setRoot(Node root) {
      this.root = root;
    }

    public Node getRoot() {
      return root;
    }
  }

  Tree tree;

  public void construireArbre(int noeud) {
    tree = new Tree();
    Node root = new Node(noeud, true);
    tree.setRoot(root);
    construireArbre(root.node);

  }

  private void construire(Node parent){
    //List<Integer> listePossible = 
  }

  // Ici, la méthode findBestChild trouve le nœud avec le score maximum si un
  // joueur est un maximiseur.
  // Sinon, il renvoie l'enfant avec le score minimum:

  public Node trouverMeilleurEnfant(boolean isMax, List<Node> enfant) {
    Comparator<Node> parScoreComparator = Comparator.comparing(Node::getScore);
    return enfant.stream().max(isMax ? parScoreComparator : parScoreComparator.reversed())
        .orElseThrow(NoSuchElementException::new);
  }
}
