package src.MinMax;

import java.util.LinkedList;

public class Node<T> {
    private T data;
    private LinkedList<Node> childrens;
    private Node parent;

    public Node(){
    }

    public Node(T data, Node parent){
        this.childrens = new LinkedList<>();
        this.data = data;
        this.parent = parent;
    }

    public T getdata(){
        return this.data;
    }

    public LinkedList<Node> getChildrens(){
        return this.childrens;
    }

    public Node getParent(){
        return this.parent;
    }
}
