package src.minmax;

public class Tree<T> {
    private Node root;

    public Tree() {
        this.root = new Node<T>(null, null);
    }

    public Node getRoot(){
        return this.root;
    }


    
}