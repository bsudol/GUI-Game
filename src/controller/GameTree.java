package controller;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class GameTree {
	
	private Game g = new Game();
	private Board board = g.getBoard(); //each node will have its own copy of the board
	private int depth;
	private int score; //score associated with this copy of board
	public static int maxDepth = 0;
			
	private ArrayList<GameTree> children = new ArrayList<GameTree>();
	
	public GameTree(Player p, int d, int s) {
		depth = d;
		final List<Location> available = new ArrayList<Location>();
		
		score = s; //score associated with that board
	}
	public GameTree dfs(GameTree n, int d, Player p) {
		//create children of node
		n.children = CreateChildren(p);
		//depth first search through children
		for (GameTree each: n.children) {
			//start with children closest to non-null entries THIS REQUIRES RANKNIG

			if (depth > d) {
				return each; //return because you hit max depth
			}
			if (board.getState().HAS_WINNER) { //need to fix how this is accessed
				if (player.current == lost) {
					score = -1000000;
					//do i return a node here even though its not a good path to take?
				}
				else {
					score = 1000000;
					return each;
				}
			}
			dfs(n, d, p);
		}
		return null;
	}
	private ArrayList<GameTree> CreateChildren(Player p) {

		List<Location> available = new ArrayList<Location>();
		// find available moves
		for (Location loc : Board.LOCATIONS) {
			if (board.get(loc) == null) {
				available.add(loc);
			}
		}

		for (Location k : available) {
			g.submitMove(p, k);
			score = score(board, p); //get the score of this configuration
			GameTree child = new GameTree(p, depth, score); //this will overflow, need to move this into method
			children.add(child);
		}
		
		return children; //does this return the right thing
	}

	/** return the positive score for player p */
	public int score(Board b, Player p) {
		int result = 0;
		int count  = 0;
		for (Line s : Line.ALL_LINES) {
			int score = score(b,p,s);
			result += score;
		}
		return result;
	}
	
	/** Return the score for player p, for the line s. */
	public int score(Board b, Player p, Line s) {
		int result = 1;
		for (Location loc : s) {
			if (b.get(loc) == p)
				result *= 10;
			if (b.get(loc) == p.opponent())
				return 0;
		}
		return result/10;
	}
  }
