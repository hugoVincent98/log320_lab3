import java.util.*;


public class MinMaxTree {

  int[][] board = new int[8][8];
  GenerateurMove generateurMove;
  Tree tree;
  
  public MinMaxTree(int [][] board, GenerateurMove generateurMove){
    this.board = board;
    this.generateurMove = generateurMove;
  }

  public void construireArbre(Move move) {
    tree = new Tree();
    Node root = new Node(move, true);
    tree.setRoot(root);
    construireArbre(root);
  }

  private void construireArbre(Node parent){
    List<Move> listeMovePossible = generateurMove.obtenirListeMoves();

    boolean enfantMax = ! parent.getIsMax();

    for(int i = 0 ; i < listeMovePossible.size(); i++){
      
      Node enfant = new Node(listeMovePossible.get(i), enfantMax);
      parent.addEnfant(enfant);

      /*if(!listeMovePossible.get(i).estGagnant()){
        construireArbre(enfant);
      }*/
    }
  }

  // Ici, la méthode trouverMeilleurEnfant trouve le nœud avec le score maximum si
  // un joueur est un maximiseur. Sinon, il renvoie l'enfant avec le score minimum

  public Node trouverMeilleurEnfant(boolean isMax, List<Node> enfant) {
    Comparator<Node> parScoreComparator = Comparator.comparing(Node::getScore);
    return enfant.stream().max(isMax ? parScoreComparator : parScoreComparator.reversed())
        .orElseThrow(NoSuchElementException::new);
  }
}
