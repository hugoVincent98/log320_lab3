package MinMax;

public class tactics {
  
  public static int analyserNumero(boolean couleur, int [][] coteGauche, int [][] coteDroite){
    if(couleur){
      if(coteGauche.length == 0){
        return -1;
      }else{
        return (coteGauche.length * 10000);
      }
    }else{
      if(coteGauche.length == 0){
        return -1;
      }else{
        return (coteDroite.length * 10000);
      }
    }
  }


  
}
