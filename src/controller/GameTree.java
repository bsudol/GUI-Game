package controller;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class GameTree {
	
	private Game g = new Game();
	private Board board = g.getBoard();
	
	private int score;
	
	private int depth;
	public static int maxDepth = 0;
	
			
	private ArrayList<GameTree> children = new ArrayList<GameTree>();
	
	public GameTree(Player p, int d) {
		depth = d;
		final List<Location> available = new ArrayList<Location>();
		Game game1 = null;
		Board board1 = null;
		
		score = 0;
		
		// find available moves
		for (Location loc : Board.LOCATIONS) {
			if (board.get(loc) == null) {
				available.add(loc);
			}
		}
				
		//head = empty board
		for (Location k : available) {
			game1 = new Game();
			board1 = game1.getBoard();
			
			game1.submitMove(p, k);
	
			GameTree child = new GameTree(p); //this will overflow
			children.add(child);
		}
	}
	public int dfs(GameTree n, int d) {
		//create children of node
		n.children = CreateChildren();
		//depth first search through chilrden
		for (GameTree n: available) {
			//start with children closest to non-null entries
			//
			if (depth > d) {
				// return because you exceeded max depth
			}
			if (board.getState().HAS_WINNER) {
				// return because there's a winner, set g = infinity
				if (player.current == lost) {
					//score = -infinity
				}
				else {
					//score = infinity
				}
			}
			dfs(n);
		}
		return null;
	}
	private ArrayList<GameTree> CreateChildren() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
		
	}
  }
}