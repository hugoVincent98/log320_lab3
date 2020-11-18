package MinMax;
import java.util.List;
import java.io.Console;
import java.util.Arrays;

//fonctionnel
public class MinMax {
    static int MAX = 100000;
    static int MIN = -100000;
    static int END = 6666666;

    int toMax;
    int toMin;
    int[][] board;
    GenerateurMove gen;
    GenerateurMove counterGen;


    public MinMax(int toMin, int toMax, int[][] board){;
        this.board = board;
        this.toMax = toMax;
        this.toMin = toMin;

        if(toMax == 2){
            gen = new GenerateurMoveRouge(this.board);
            counterGen = new GenerateurMoveNoir(this.board);
        }
        if (toMax == 4){
            gen = new GenerateurMoveNoir(board);
            counterGen = new GenerateurMoveRouge(this.board);
        }
    }

    public void updateBoard(int[][]board){
        this.board = board;
        if(toMax == 2){
            gen = new GenerateurMoveRouge(this.board);
            counterGen = new GenerateurMoveNoir(this.board);
        }
        if (toMax == 4){
            gen = new GenerateurMoveNoir(board);
            counterGen = new GenerateurMoveRouge(this.board);
        }
    }

    public Move getBestMove(){
        int meilleurScore = MIN;
        List<Move> myMoves = gen.obtenirListeMoves();
        Move meilleurMove = null;
        int[][] nboard = copy(this.board);

        for (Move m : myMoves) {
            int value = nboard[(int)m.depart.getX()][(int)m.depart.getY()];
            nboard[(int)m.arrive.getX()][(int)m.arrive.getY()] = value;
            nboard[(int)m.depart.getX()][(int)m.depart.getY()] = 0;
            
            System.out.println(" myMoves "+ m.toCoordinate());
            
            int score = miniMax(nboard, 0, false,MIN,MAX);

            if (score > meilleurScore){
                meilleurScore = score;
                meilleurMove = m;
            }
            System.out.println("meilleur score: "+ score);
        }

        board = nboard;
        return meilleurMove;
    }


    public int miniMax(int [][] board ,int depth, boolean isMax, int alpha, int beta ){

        if (checkWinner() != 0||depth > 3){
            return END;
        }

        if(isMax){
            int meilleurScore = MIN;
            List<Move> myMoves = gen.obtenirListeMoves();
            Move meilleurMove = null;
            int[][] nboard = copy(board);

            for(int i = 0; i < myMoves.size(); i++){
                int value = nboard[(int)myMoves.get(i).depart.getX()][(int)myMoves.get(i).depart.getY()];
                nboard[(int)myMoves.get(i).arrive.getX()][(int)myMoves.get(i).arrive.getY()] = value;
                nboard[(int)myMoves.get(i).depart.getX()][(int)myMoves.get(i).depart.getY()] = 0;
              
    
                int score = miniMax(nboard, depth +1, false,alpha ,beta);

                if (score > alpha){
                    alpha = score;
                    meilleurMove = myMoves.get(i);
                }
                if (alpha >= beta) {
                    break;
                }
            }
            return meilleurScore;
        }else{
            int meilleurScore = MAX;
            List<Move> myMoves = counterGen.obtenirListeMoves();
            Move meilleurMove = null;
            int[][] nboard = copy(board);

            for(int i = 0; i < myMoves.size(); i++){
                int value = nboard[(int)myMoves.get(i).depart.getX()][(int)myMoves.get(i).depart.getY()];
                nboard[(int)myMoves.get(i).arrive.getX()][(int)myMoves.get(i).arrive.getY()] = value;
                nboard[(int)myMoves.get(i).depart.getX()][(int)myMoves.get(i).depart.getY()] = 0;
    
                int score = miniMax(nboard, depth +1, true,alpha,beta);

                if (score < beta){
                    beta = score;
                    meilleurMove = myMoves.get(i);
                }
                if (alpha >= beta) {
                    break;
                }
            }
            return meilleurScore;
        }
    }

    public int checkWinner(){
        if(toMax == 2){
            for(int j = 0; j < 8; j++ ){
                if(board[0][j]==4){
                    return -1;
                }
                if(board[7][j]==2){
                    return 1;
                } 
            }
        }

        if(toMax == 4){
            for(int j = 0; j < 8; j++ ){
                if(board[7][j]==2){
                    return -1;
                }
                if(board[0][j]==4){
                    return 1;
                }
            }
        }
        return 0;
    }


    public static int[][] copy(int[][] src) {
        int[][] dst = new int[src.length][];
        for (int i = 0; i < src.length; i++) {
            dst[i] = Arrays.copyOf(src[i], src[i].length);
        }
        return dst;
    }

    
}
