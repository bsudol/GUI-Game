//netid bas334
package controller;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.Game;
import model.Location;
import model.NotImplementedException;
import model.Player;
import model.Board.State;

/**
 * A DumbAI is a Controller that always chooses the blank space with the
 * smallest column number from the row with the smallest row number.
 */
public class DumbAI extends Controller {

	public DumbAI(Player me) {
		super(me);
		// TODO Auto-generated constructor stub
	}

	protected @Override Location nextMove(Game g) {
		// Note: Calling delay here will make the CLUI work a little more
		// nicely when competing different AIs against each other.
		
		List<Location> available = new ArrayList<Location>();
		
		// find available moves
		for (Location loc : Board.LOCATIONS)
			if (g.getBoard().get(loc) == null)
				available.add(loc);
		
		// wait a bit
		delay();

		// pick first entry in list of available moves
		Location loc = available.get(0); //assuming the first 'available' should be the 'lowest' one
		return loc;
	}
}
