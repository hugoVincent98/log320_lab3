package MinMax;

public class Pion {

    MovePoint positionActuelle;
    int couleur;

    public Pion(MovePoint positionActuelle, int couleur) {
        this.positionActuelle = positionActuelle;
        this.couleur = couleur;
    }

    public MovePoint getPositionActuelle() {
        return positionActuelle;
    }

    public void setPositionActuelle(MovePoint positionActuelle) {
        this.positionActuelle = positionActuelle;
    }

    public int getCouleur() {
        return couleur;
    }

    public void setCouleur(int couleur) {
        this.couleur = couleur;
    }

    
}
