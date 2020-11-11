package MinMax;
import java.util.List;

public class MinMax {
    static int MAX = 100000;
    static int MIN = -100000;
    static int END = 6666666;

    int toMax;
    int toMin;
    int[][] board;
    GenerateurMove gen;
    GenerateurMove counterGen;

    public MinMax(int toMin, int toMax, int alpha, int beta, int[][] board){;
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
        int[][] nboard = this.board;

        for(int i = 0; i < myMoves.size(); i++){
            int value = nboard[(int)myMoves.get(i).depart.getX()][(int)myMoves.get(i).depart.getY()];
            nboard[(int)myMoves.get(i).arrive.getX()][(int)myMoves.get(i).arrive.getY()] = value;
            nboard[(int)myMoves.get(i).depart.getX()][(int)myMoves.get(i).depart.getY()] = 0;

            int score = miniMax(nboard, 0, false);

            if (score > meilleurScore){
                meilleurScore = score;
                meilleurMove = myMoves.get(i);

            }
        }

        board = nboard;
        return meilleurMove;
    }


    public int miniMax(int [][] board ,int depth, boolean isMax ){
        if (checkWinner() != 0){
            return END;
        }

        if(isMax){
            int meilleurScore = MIN;
            List<Move> myMoves = gen.obtenirListeMoves();
            Move meilleurMove = null;
            int[][] nboard = this.board;

            for(int i = 0; i < myMoves.size(); i++){
                int value = nboard[(int)myMoves.get(i).depart.getX()][(int)myMoves.get(i).depart.getY()];
                nboard[(int)myMoves.get(i).arrive.getX()][(int)myMoves.get(i).arrive.getY()] = value;
                nboard[(int)myMoves.get(i).depart.getX()][(int)myMoves.get(i).depart.getY()] = 0;
    
                int score = miniMax(nboard, 0, false);

                if (score > meilleurScore){
                    meilleurScore = score;
                    meilleurMove = myMoves.get(i);

                }
            }
            return meilleurScore;
        }else{
            int meilleurScore = MAX;
            List<Move> myMoves = counterGen.obtenirListeMoves();
            Move meilleurMove = null;
            int[][] nboard = this.board;

            for(int i = 0; i < myMoves.size(); i++){
                int value = nboard[(int)myMoves.get(i).depart.getX()][(int)myMoves.get(i).depart.getY()];
                nboard[(int)myMoves.get(i).arrive.getX()][(int)myMoves.get(i).arrive.getY()] = value;
                nboard[(int)myMoves.get(i).depart.getX()][(int)myMoves.get(i).depart.getY()] = 0;
    
                int score = miniMax(nboard, 0, true);

                if (score < meilleurScore){
                    meilleurScore = score;
                    meilleurMove = myMoves.get(i);

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




    
}
