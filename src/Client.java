
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import src.MinMax.*;
class Client {

	static final int ROUGE = 2;
	static final int NOIR = 4;

	public static void main(String[] args) {

		// Socket MyClient;
		BufferedInputStream input;
		BufferedOutputStream output;
		int[][] board = new int[8][8];
		GenerateurMove generateurMouvement;

		try (Socket myClient = new Socket("localhost", 8888)) {

			input = new BufferedInputStream(myClient.getInputStream());
			output = new BufferedOutputStream(myClient.getOutputStream());
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			while (1 == 1) {
				char cmd = 0;

				cmd = (char) input.read();
				System.out.println(cmd);
				// Debut de la partie en joueur rouge
				if (cmd == '1') {
					byte[] aBuffer = new byte[1024];

					int size = input.available();
					// System.out.println("size " + size);
					input.read(aBuffer, 0, size);
					String s = new String(aBuffer).trim();
					System.out.println(s);
					String[] boardValues;
					boardValues = s.split(" ");
					int x = 0, y = 0;
					for (int i = 0; i < boardValues.length; i++) {
						board[x][y] = Integer.parseInt(boardValues[i]);
						x++;
						if (x == 8) {
							x = 0;
							y++;
						}
					}

					System.out.println("Nouvelle partie! Vous jouer blanc, entrez votre premier coup : ");
					String move = null;
					
					printboard(board);

					MinMax minmax = new MinMax(4, 4, 0, 0, board);
					Move bestMove = minmax.getBestMove();
					System.out.println("myMoveD: x:"+bestMove.getDepart().getX()+" y: "+bestMove.getDepart().getY());
					System.out.println("myMoveF: x:"+bestMove.getArrive().getX()+" y: "+bestMove.getArrive().getY());

					move = console.readLine();
					output.write(move.getBytes(), 0, move.length());
					output.flush();
				}
				// Debut de la partie en joueur Noir
				if (cmd == '2') {
					System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
					byte[] aBuffer = new byte[1024];

					int size = input.available();
					// System.out.println("size " + size);
					input.read(aBuffer, 0, size);
					String s = new String(aBuffer).trim();
					System.out.println(s);
					String[] boardValues;
					boardValues = s.split(" ");
					int x = 0, y = 0;
					for (int i = 0; i < boardValues.length; i++) {
						board[x][y] = Integer.parseInt(boardValues[i]);
						x++;
						if (x == 8) {
							x = 0;
							y++;
						}
					}

					printboard(board);
				}

				// Le serveur demande le prochain coup
				// Le message contient aussi le dernier coup joue.
				if (cmd == '3') {
					byte[] aBuffer = new byte[16];

					int size = input.available();
					System.out.println("size :" + size);
					input.read(aBuffer, 0, size);

					String s = new String(aBuffer);
					System.out.println("Dernier coup :" + s); // B2 - C3 [1][1]-[2][2]

					char xOldChar = s.charAt(1);
					int xOld = xOldChar - 65;// commence a 0

					char yOldChar = s.charAt(2);
					int yOld = yOldChar - 49;// commence a 0

					char xNewChar = s.charAt(6);
					int xNew = xNewChar - 65;

					char yNewChar = s.charAt(7);
					int yNew = yNewChar - 49;

					int value = board[xOld][yOld];
					board[xOld][yOld] = 0;
					board[xNew][yNew] = value;

					printboard(board);

					System.out.println("Entrez votre coup : ");
					String move = null;
					
					MinMax minmax = new MinMax(4, 4, 0, 0, board);
					Move bestMove = minmax.getBestMove();
					System.out.println("myMoveD: x:"+bestMove.getDepart().getX()+" y: "+bestMove.getDepart().getY());
					System.out.println("myMoveF: x:"+bestMove.getArrive().getX()+" y: "+bestMove.getArrive().getY());
					
					System.out.println("le move : " + bestMove.toString());
					// update le board local
					
					value = board[bestMove.getDepart().x][bestMove.getDepart().y];
					board[bestMove.getDepart().x][bestMove.getDepart().y] = 0;
					board[bestMove.getArrive().x][bestMove.getArrive().y] = value;
					
					printboard(board);
					
					
					move = bestMove.toString();
					//move = console.readLine();
					output.write(move.getBytes(), 0, move.length());
					output.flush();

				}
				// Le dernier coup est invalide
				if (cmd == '4') {
					System.out.println("Coup invalide, entrez un nouveau coup : ");
					String move = null;
					move = console.readLine();
					output.write(move.getBytes(), 0, move.length());
					output.flush();

				}
				// La partie est terminée
				if (cmd == '5') {
					byte[] aBuffer = new byte[16];
					int size = input.available();
					input.read(aBuffer, 0, size);
					String s = new String(aBuffer);
					System.out.println("Partie Terminé. Le dernier coup joué est: " + s);
					String move = null;
					move = console.readLine();
					output.write(move.getBytes(), 0, move.length());
					output.flush();

				}
			}
			
		} catch (IOException e) {
			System.out.println(e);
		}

		

	}

	static void printboard(int[][] b) {
		for(int i = 0 ; i < 8; i++){
			for(int j = 0 ; j < 8 ; j++){
				System.out.print(b[i][j]+" ");
			}
			System.out.println("");
		}
	}
}
