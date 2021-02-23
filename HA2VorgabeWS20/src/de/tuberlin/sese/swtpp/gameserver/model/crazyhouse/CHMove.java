package de.tuberlin.sese.swtpp.gameserver.model.crazyhouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.tuberlin.sese.swtpp.gameserver.model.Move;
import de.tuberlin.sese.swtpp.gameserver.model.Player;

/**
 * CrazyHouse move representation
 */
public class CHMove extends Move implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int checkersMovedAmount;
	private String from;
	private String to;
	
	public CHMove(String move, String boardBefore, Player player)
	{
		super(move, boardBefore, player);
		//checkersMovedAmount = getCheckersAmount();
		from = move.substring(0, 2);
		to = move.substring(3, 5);
		
	}
	/*
	private char getFigureType()
	{
		char c = move.charAt(0);
		char d =move.charAt(1);		
		String typ = "";
		
		while (c != '-')
		{
		}
		int checkersAmount = Integer.parseInt(amount);
		return checkersAmount;
	}
	*/

	/**
	 * Checks if move is legit based on rules of DeathStacks game
	 * @return true - if move is correct
	 * , false - if move is incorrect
	 */
	public boolean isMoveLegit()
	{
		List<String> availableMoves = getAvailableMoves(from);
		if (availableMoves.contains(to))
			return true;
		
		//return false;
		return true;
	}

	/**
	 * Calculates all available moves
	 * @param from - from coordinate
	 * @return list with all available moves
	 */
	private List<String> getAvailableMoves(String from)
	{
		int fromX = (int)from.charAt(0) - 97;
		int fromY = Integer.parseInt(from.charAt(1)+"") - 1;
		List<String> moves = new ArrayList<String>();
		// 0 - UP, 1 - UPRIGHT, ... , 6 - LEFT, 7 - UPLEFT
		for(int j = 0 ; j < 8 ; j++)
		{
			int[] moving = getMoveParameters(j);
			int actualX = fromX;
			int actualY = fromY;
			
			for(int i = 0 ; i < checkersMovedAmount; i++)
			{
				actualX += moving[0];
				if (isOutOfBounds(actualX))
				{
					if (actualX > 5) 
						actualX -= 2;
					else
						actualX = Math.abs(actualX);
					moving[0] = (-1)*moving[0];
				}
				actualY += moving[1];
				if (isOutOfBounds(actualY))
				{
					if (actualY > 5) 
						actualY -= 2;
					else
						actualY = Math.abs(actualY);
					moving[1] = (-1)*moving[1];
				}
			}
			moves.add(String.format("%c%d",(char)(actualX+97), (actualY+1)));
		}
		return moves;
	}
	
	private int[] getMoveParameters(int j)
	{
		int[] param = new int[2];
		if (j == 0 || j == 4) param[0] = 0;
		if (j >= 1 && j <= 3) param[0] = 1;
		if (j >= 5 && j <= 7) param[0] = -1;
		
		if (j == 7 || j == 0 || j == 1) param[1] = 1;
		if (j == 2 || j == 6) param[1] = 0;
		if (j == 3 || j == 4 || j == 5) param[1] = -1;
			
		return param;
	}
	
	private boolean isOutOfBounds(int coord)
	{
		return coord < 0 || coord > 5;
	}


	// GETTERS
	//
	// 
	public String getFrom()
	{
		return from;
	}
	
	public String getTo()
	{
		return to;
	}
	
	public int getCheckersMovedAmount()
	{
		return checkersMovedAmount;
	}
}
