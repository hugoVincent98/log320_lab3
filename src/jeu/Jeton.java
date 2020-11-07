package src.jeu;
import src.Couleur;

public class Jeton {
    private Couleur couleur;
    private boolean enVie;

    public Jeton(Couleur couleur){
        this.couleur = couleur;
        this.enVie = true;
    }

    public Couleur getCouleur(){
        return this.couleur;
    }
    
    public boolean getEnVie(){
        return this.enVie;
    }

    public void eleminer(){
        this.enVie = false;
    }
}