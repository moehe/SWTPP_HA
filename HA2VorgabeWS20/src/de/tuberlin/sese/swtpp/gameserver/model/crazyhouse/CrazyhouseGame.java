package de.tuberlin.sese.swtpp.gameserver.model.crazyhouse;

import java.io.Serializable;
import java.util.Arrays;

import de.tuberlin.sese.swtpp.gameserver.model.Game;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.Move;


public class CrazyhouseGame extends Game implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 5424778147226994452L;

	/************************
	 * member
	 ***********************/

	// just for better comprehensibility of the code: assign white and black player
	private Player blackPlayer;
	private Player whitePlayer;

	// internal representation of the game state
	// TODO: insert additional game data here
	private String state;
	private String[][] gameBoard = new String [][]{{"R", "N", "B", "Q", "K", "B", "N", "R"}, 
        {"P", "P", "P", "P", "P", "P", "P", "P"},
        {"-", "-", "-", "-", "-", "-", "-", "-"},
        {"-", "-", "-", "-", "-", "-", "-", "-"},
        {"-", "-", "-", "-", "-", "-", "-", "-"},
        {"-", "-", "-", "-", "-", "-", "-", "-"},
        {"p", "p", "p", "p", "p", "p", "p", "p"},
        {"r", "n", "b", "q", "k", "b", "n", "r"}};;



	/************************
	 * constructors
	 ***********************/

	public CrazyhouseGame() throws Exception{
		super();	
		

		// TODO: initialize internal model if necessary 
	}

	public String getType() {
		return "crazyhouse";
	}

	/*******************************************
	 * Game class functions already implemented
	 ******************************************/

	@Override
	public boolean addPlayer(Player player) {
		if (!started) {
			players.add(player);

			// game starts with two players
			if (players.size() == 2) {
				started = true;
				this.whitePlayer = players.get(0);
				this.blackPlayer= players.get(1);
				nextPlayer = whitePlayer;
			}
			return true;
		}

		return false;
	}

	@Override
	public String getStatus() {
		if (error)
			return "Error";
		if (!started)
			return "Wait";
		if (!finished)
			return "Started";
		if (surrendered)
			return "Surrendered";
		if (draw)
			return "Draw";

		return "Finished";
	}

	@Override
	public String gameInfo() {
		String gameInfo = "";

		if (started) {
			if (blackGaveUp())
				gameInfo = "black gave up";
			else if (whiteGaveUp())
				gameInfo = "white gave up";
			else if (didWhiteDraw() && !didBlackDraw())
				gameInfo = "white called draw";
			else if (!didWhiteDraw() && didBlackDraw())
				gameInfo = "black called draw";
			else if (draw)
				gameInfo = "draw game";
			else if (finished)
				gameInfo = blackPlayer.isWinner() ? "black won" : "white won";
		}

		return gameInfo;
	}

	@Override
	public String nextPlayerString() {
		return isWhiteNext() ? "w" : "b";
	}

	@Override
	public int getMinPlayers() {
		return 2;
	}

	@Override
	public int getMaxPlayers() {
		return 2;
	}

	@Override
	public boolean callDraw(Player player) {

		// save to status: player wants to call draw
		if (this.started && !this.finished) {
			player.requestDraw();
		} else {
			return false;
		}

		// if both agreed on draw:
		// game is over
		if (players.stream().allMatch(Player::requestedDraw)) {
			this.draw = true;
			finish();
		}
		return true;
	}

	@Override
	public boolean giveUp(Player player) {
		if (started && !finished) {
			if (this.whitePlayer == player) {
				whitePlayer.surrender();
				blackPlayer.setWinner();
			}
			if (this.blackPlayer == player) {
				blackPlayer.surrender();
				whitePlayer.setWinner();
			}
			surrendered = true;
			finish();

			return true;
		}

		return false;
	}

	/* ******************************************
	 * Helpful stuff
	 ***************************************** */

	/**
	 *
	 * @return True if it's white player's turn
	 */
	public boolean isWhiteNext() {
		return nextPlayer == whitePlayer;
	}

	/**
	 * Ends game after regular move (save winner, finish up game state,
	 * histories...)
	 *
	 * @param winner player who won the game
	 * @return true if game was indeed finished
	 */
	public boolean regularGameEnd(Player winner) {
		// public for tests
		if (finish()) {
			winner.setWinner();
			winner.getUser().updateStatistics();
			return true;
		}
		return false;
	}

	public boolean didWhiteDraw() {
		return whitePlayer.requestedDraw();
	}

	public boolean didBlackDraw() {
		return blackPlayer.requestedDraw();
	}

	public boolean whiteGaveUp() {
		return whitePlayer.surrendered();
	}

	public boolean blackGaveUp() {
		return blackPlayer.surrendered();
	}

	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 ******************************************/

	@Override
	public void setBoard(String state) {
		// Note: This method is for automatic testing. A regular game would not start at some artificial state.
		//       It can be assumed that the state supplied is a regular board that can be reached during a game.
		// TODO: implement
        String temp3[] = state.split("/");       
        for(int i=0;i<8;i++){
            String str = temp3[i];
            int k = 0;
            for(int j=0;j<str.length();j++){                   
                char code = str.charAt(j);  
                String cod = String.valueOf(code);
                boolean numeric = true;
                
                try {
                    int num = Integer.parseInt(cod);
                } catch (NumberFormatException e) {
                    numeric = false;
                }
               
                if(numeric){
                    int num = Integer.parseInt(cod);
                      for(int count=0;count<num;count++){
                        gameBoard[7-i][k] = "-";
                        k++;
                      }                          
                }                                       
                else{                        
                    gameBoard[7-i][k] = cod;
                    k++;
                }             
        }
        }	
	}

	@Override
	//returns String reprasentation of state 
	public String getBoard() {
		// TODO: implement
		
		// replace with real implementation
		//return "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/";
		updateState();
		return state;
	}

	@Override
	public boolean tryMove(String moveString, Player player) {
		// TODO: implement
		CHMove m = new CHMove(moveString, state, player);
		//boolean obeyTooTallRule = isTooTallRule(m);
		//if (m.isMoveLegit() && isPlayerCheckerOnTop(m.getFrom()) && obeyTooTallRule)
		if (m.isMoveLegit())
	
			
		{
			System.out.println(moveString);
			updateBoard(m);
			updateState();
			System.out.println(state);
			/*
			if (checkIfWon(isWhiteNext()))
			{
				endGame();
			}
			*/

			if(isWhiteNext())
				setNextPlayer(blackPlayer);
			else
				setNextPlayer(whitePlayer);
			
			return true;
		}
				
		
		// replace with real implementation
		return false;
	}
	
	
	//
	
	//need to work on it, sometimes produces rows of "1" instead of sum of empty boxes
	
	private void updateState() {
		state = "";
		for(int j = 7 ; j >= 0 ; j--){
			int counter=0;
			for(int i = 0 ; i < 8 ; i++)
				
			{
				String temp ="";
				String temp2 = "";
				temp=gameBoard[j][i].toString();
				if(i<7) {
				temp2=gameBoard[j][i+1].toString();
				}
				if(temp.equals("-")){
					counter++;
					if(i==7||temp2!="-") {
					state+=counter;
					counter=0;
					}
				}
				else {
		                state += gameBoard[j][i];
		                
		            }
		
					
			}
			state += "/";
		}
	}
	
	
	
	private void updateCells(int fromCol, int fromSeg, int toCol, int toSeg) {
		// remove
					String toAdd = "";
					
					toAdd += gameBoard[fromSeg-1][fromCol].charAt(0);
					
					StringBuilder sb = new StringBuilder(gameBoard[fromCol][fromSeg-1]);
					sb.deleteCharAt(0);
					sb.insert(0,"-");
					
					
					

					gameBoard[fromSeg-1][fromCol] = sb.toString();
					//add
					StringBuilder sb1 = new StringBuilder(gameBoard[toSeg-1][toCol]);
					// put on top
					sb1.deleteCharAt(0);
					sb1.insert(0, toAdd);

					gameBoard[toSeg-1][toCol] = sb1.toString();
					
	
	}
	
	
	public void updateBoard(CHMove m) {
		
		
		// get cells info (column from 0, row from 1)
		int fromColumnNumber = (int)m.getFrom().charAt(0) - 97;

		int fromRowSegment = Integer.parseInt(m.getFrom().charAt(1)+"");


		
		int toColumnNumber = (int)m.getTo().charAt(0) - 97;

		int toRowSegment = Integer.parseInt(m.getTo().charAt(1)+"");

		
		// update cells on board
		updateCells(fromColumnNumber, fromRowSegment, toColumnNumber, toRowSegment);
	}

	
	/*
	private void initGameBoard()
	{
		gameBoard = new String [][]{{"R", "N", "B", "Q", "K", "B", "N", "R"}, 
		             {"P", "P", "P", "P", "P", "P", "P", "P"},
		             {"-", "-", "-", "-", "-", "-", "-", "-"},
		             {"-", "-", "-", "-", "-", "-", "-", "-"},
		             {"-", "-", "-", "-", "-", "-", "-", "-"},
		             {"-", "-", "-", "-", "-", "-", "-", "-"},
		             {"p", "p", "p", "p", "p", "p", "p", "p"},
		             {"r", "n", "b", "q", "k", "b", "n", "r"}};
		
	
	}
*/
	
	

}
