package controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;

import controller.SmartAI;
import model.Line;
import model.Board;
import model.Board.State;
import model.Game;
import model.Location;
import model.NotImplementedException;
import model.Player;

/**
 * A MinMaxAI is a controller that uses the minimax algorithm to select the next
 * move. The minimax algorithm searches for the best possible next move, under
 * the assumption that your opponent will also always select the best possible
 * move.
 *
 * <p>The minimax algorithm assigns a score to each possible game configuration
 * g.  The score is assigned recursively as follows: if the game g is over and
 * the player has won, then the score is infinity.  If the game g is over and
 * the player has lost, then the score is negative infinity.  If the game is a
 * draw, then the score is 0.
 * 
 * <p>If the game is not over, then there are many possible moves that could be
 * made; each of these leads to a new game configuration g'.  We can
 * recursively find the score for each of these configurations.
 * 
 * <p>If it is the player's turn, then they will choose the action that
 * maximizes their score, so the score for g is the maximum of all the scores
 * of the g's.  However, if it is the opponent's turn, then the opponent will
 * try to minimize the score for the player, so the score for g is the
 * <em>minimum</em> of all of the scores of the g'.
 * 
 * <p>You can think of the game as defining a tree, where each node in the tree
 * represents a game configuration, and the children of g are all of the g'
 * reachable from g by taking a turn.  The minimax algorithm is then a
 * particular traversal of this tree.
 * 
 * <p>In practice, game trees can become very large, so we apply a few
 * strategies to narrow the set of paths that we search.  First, we can decide
 * to only consider certain kinds of moves.  For five-in-a-row, there are
 * typically at least 70 moves available at each step; but it's (usually) not
 * sensible to go on the opposite side of the board from where all of the other
 * pieces are; by restricting our search to only part of the board, we can
 * reduce the space considerably.
 * 
 * <p>A second strategy is that we can look only a few moves ahead instead of
 * planning all the way to the end of the game.  This requires us to be able to
 * estimate how "good" a given board looks for a player.
 * 
 * <p>This class implements the minimax algorithm with support for these two
 * strategies for reducing the search space.  The abstract method {@link
 * #moves(Board)} is used to list all of the moves that the AI is willing to
 * consider, while the abstract method {@link #estimate(Board)} returns
 * the estimation of how good the board is for the given player.
 */
public abstract class MinMaxAI extends Controller {
	
	protected int depth = 0;

	/**
	 * Return an estimate of how good the given board is for me.
	 * A result of infinity means I have won.  A result of negative infinity
	 * means that I have lost.
	 */
	protected abstract int estimate(Board b);
	
	/**
	 * Return the set of moves that the AI will consider when planning ahead.
	 * Must contain at least one move if there are any valid moves to make.
	 */
	protected abstract Iterable<Location> moves(Board b);
	
	/**
	 * Create an AI that will recursively search for the next move using the
	 * minimax algorithm.  When searching for a move, the algorithm will look
	 * depth moves into the future.
	 *
	 * <p>choosing a higher value for depth makes the AI smarter, but requires
	 * more time to select moves.
	 */
	protected MinMaxAI(Player me, int depth) {
		super(me);
		int d = depth;
	}
	
	/**
	 * Return the move that maximizes the score according to the minimax
	 * algorithm described above.
	 */
	protected @Override Location nextMove(Game g) {
		
		return minimax(g.getBoard(), depth, me);
	}
	
	
	public class Tuple {
		public Location move;
		public int score;
		
		public Tuple(Location l, int i) {
			move = l;
			score = i;
		}
		
		public Tuple minimax(Board b, int currentdepth, Player p) {
			if (b.getState() == State.DRAW) {
				return new Tuple(null,0);
			}
			if (b.getState() == State.HAS_WINNER) {
				if (p == me) { 	//how do we get access to me
					return new Tuple(null, Integer.MAX_VALUE); //represents infinity
				}
				else {
					return new Tuple(null, Integer.MIN_VALUE);
				}
			}
			if (currentdepth == 0) {
				return new Tuple(null, estimate(b)); //how do we get access to estimate
			}

			/**go through each possible move recursively, figure out what the best 
			 * move is based on p and return the Tuple of that move and score. */
			 Tuple best = null; //update this whenever we find new best
			 for (Location each : moves(b)) {
				 Tuple current = minimax(b.update(p, each), currentdepth-1, p.opponent());
				 current.move = move;
				 //we want to minimize best for opponent and maximize best for us
				 if ((best == null) || (p == me)) { //and current score > best score , OR player is opponent and score is smaller than best
					 best = current;
				 }
				 return best;
			 }
			 return null;
		} //we also have to check every node at some step. and some cases you don't need to...
	}
}
