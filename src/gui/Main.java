/* NetId(s): dnm54, bas334. Time spent: 12 hours, 00 minutes. */

package gui;

import controller.*;
import model.*;
import model.Board.State;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Main extends JFrame{
	JComboBox<String> cbPlayerX, cbPlayerO;	//combo boxes for each player
	Player p;	//contains the player
	Controller playerX, playerO;  //holds the AI's/null if player is user
	JFrame gameFrame;	//the frame that has the game 
	Game guigame;	//the game being played
	Board guiBoard;		//the board for this game
	ArrayList<Square> squares = new ArrayList<Square>(); //arraylist of Squares
	JOptionPane winDisplay;		//display window for when someone wins
	
	/**
	 * Actionlistener that creates the controllers when OK is clicked on this.
	 * Also makes this no longer visible and makes the game visible
	 */
	private class okClick implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//if player X is not a user, create the controller for that player
			//and add it as a listener
			if (((String) cbPlayerX.getSelectedItem()) != "User") {
				playerX = createController((String) cbPlayerX.getSelectedItem()
						, Player.X); 
				guigame.addListener(playerX);
			}
			
			
			//if player Y is not a user, create the controller for that player
			//and add it as a listener
			if (((String)cbPlayerO.getSelectedItem()) != "User") {	
				playerO = createController((String) cbPlayerO.getSelectedItem()
						, Player.O); 
				guigame.addListener(playerO);
			}
			
			//make this invisible and make the game board visible
			gameFrame.setVisible(true);
			setVisible(false);
		}
		
	}
	
	/**
	 * Allows the user to select the moves on the board and submits
	 * them to the game. Also makes the player's character appear
	 * in the square that they hover over
	 */
	private class mouseOver implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			//if there is no winner
			if (guigame.getBoard().getState() != State.HAS_WINNER) {
				Square panelClicked = (Square) e.getSource(); //get the source
				
				//if it is unselected, make it selected, submit, and adjust p
				if (panelClicked.selected == false) {
					panelClicked.selected = true;
					guigame.submitMove(p, panelClicked.loc);
					p = guigame.nextTurn();
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			//if there is no winner, draw the player's icon in the 
			//square that the mouse is hovering over
			if (guigame.getBoard().getState() != State.HAS_WINNER) {
				Square panelClicked = (Square) e.getSource();
				panelClicked.inSquare = true;
				if (!panelClicked.selected)
					panelClicked.repaint();
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			//when the mouse leaves, remove the icon
			if (guigame.getBoard().getState() != State.HAS_WINNER) {
				Square panelClicked = (Square) e.getSource();
				panelClicked.inSquare = false;
				if (!panelClicked.selected)
					panelClicked.repaint();
			}
		}
	}
	
	/**
	 * A Square object is one tile in the game, which has a location
	 * and indicates whether or not the mouse in the square and if it has been
	 * selected. 
	 */
	public class Square extends JButton implements GameListener {
		private boolean inSquare = false;
		private boolean selected = false;
		
		Location loc;
		
		//constructor
		public Square(int i, int j) {
			//set the location
			loc = new Location(i,j);
			//set the size
			setPreferredSize(new Dimension(10,10));
			setOpaque(true);	//make it opaque and set the border
			setBorder(new LineBorder(Color.blue));
			//add mouseOver() as a listener
			addMouseListener(new mouseOver());
			//add this to the arraylist of squares
			squares.add(this);
		}
		
		/**
		 * Returns the square at location (i, j) where both are ints
		 */
		public Square getSquare(int i, int j) {
			return squares.get(i*9 +j);
		}
		
		
		@Override
		public void paint(Graphics g) {
			super.paint(g); //call the version defined in JPanel
			boolean select;//holds whether or not the location has been selected
			select = guigame.getBoard().get(loc) != null;
			//sets the font to 50
			g.setFont(new Font("", Font.PLAIN, 50));
			//if the mouse is in the square and it has not been selected
			if (inSquare && !select) {
				//paint the square based on which player's turn it is
				if (guigame.nextTurn() == Player.X)
					g.drawString("X", 40, 55);
				else 
					g.drawString("O", 40, 55);
			}
			//if the square has been selected, draw the corresponding letter
			//in it
			if (select) {
				if (guigame.getBoard().get(loc) == Player.X)
					g.drawString("X", 40, 55);
				else 
					g.drawString("O", 40, 55);
			}
			//if it is not selected, dispose of it
			else if (!select)
				g.dispose();
			
			//if there is a winner, call paintWinners()
			if (guigame.getBoard().getState() == State.HAS_WINNER)
				paintWinners();
		}
		
		/**
		 * Paints the squares in the winning line green
		 */
		public void paintWinners() {
			Board b = guigame.getBoard();  //store board
			Line winners = b.getWinner().line;	//get the winners
			Square win;
			//loop through all of the squares and fill the winners green
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					//if the square is a winner, get the square and paint it
					if (winners.contains(i, j)) {
						win = getSquare(i,j);
						win.setBackground(Color.GREEN);
					}
						
				}
			}
			
			//if the winning display has not been posted, call it
			if (winDisplay == null) {
				//create a JOptionPane()
				winDisplay = new JOptionPane();
				//set the winner string to be the player X or Y
				String winner = b.getWinner().winner == Player.X ? "X" : "Y";
				gameFrame.setTitle("Game Over!");  //change the title of the game
				//display a dialog which tells the winner
				winDisplay.setMessage("Player " + winner + " wins!");
				winDisplay.createDialog("Game Over!" ).show();
				
				//if the user presses anything, close the game window
				if (winDisplay.getValue() != null) {
					gameFrame.dispose();
				}
			}
			
		}

		@Override
		public void gameChanged(Game g) {
			// TODO Auto-generated method stub
			//paint this immediately (doesn't work)
			this.paintImmediately(0, 0, getWidth(), getHeight());
			
			//change the title of the game board to have it tell whose turn it is
			gameFrame.setTitle("Player " + guigame.nextTurn() + "'s Turn");
		}
	}
	
	
	public Main() {
		//start a new game
		guigame = new Game();
		//start up the JFrame for the game
	    gameFrame = new JFrame();
	    winDisplay = null;
	    JPanel gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(9,9));
	
		
		//add all 81 buttons into the grid layout for the 'board'
		for (int i = 0; i < 9; i ++) {
			for (int j = 0; j < 9; j ++) {
				Square s = new Square(i, j);
				gamePanel.add(s);	
				guigame.addListener(s);
			}
		}
		
		//lay it out/make it go
		gameFrame.add(gamePanel);
		gameFrame.setPreferredSize(new Dimension(1000,1000));
		gameFrame.pack();
		gamePanel.setVisible(true);
		
		//make this display two combo boxes and an OK button
		setLayout(new BorderLayout());
		setTitle("Choose Player Modes");
		
	    cbPlayerX = new JComboBox<String>();
	    
	    //create the choices
	    cbPlayerX.addItem("User");
	    cbPlayerX.addItem("Dumb AI");
	    cbPlayerX.addItem("Smart AI");
	    cbPlayerX.addItem("Random AI");
	    
	    JLabel label = new JLabel("Versus");
	    
	    cbPlayerO = new JComboBox<String>();
	    cbPlayerO.addItem("User");
	    cbPlayerO.addItem("Dumb AI");
	    cbPlayerO.addItem("Smart AI");
	    cbPlayerO.addItem("Random AI");
	    
	    //add them to different parts of the border layout
	    add(cbPlayerX, BorderLayout.WEST);
	    add(label, BorderLayout.CENTER);
	    add(cbPlayerO, BorderLayout.EAST);
	    
	    //create the new button and add okClick() as a winner
	    JButton ok = new JButton("OK");
	    p = Player.X;
	    ok.addActionListener(new okClick());
	    add(ok, BorderLayout.SOUTH);
	   
	    //set the size and pack
		setPreferredSize(new Dimension(300,150));
		pack();
		setVisible(true);
	}
	
	/**
	 * Return the controller based on the selection in the combo box
	 * for the given player
	 */
	public Controller createController(String selection, Player p){
		switch(selection) {
		case "User":
			return null; //return null for the user
		case "Dumb AI":
			return new DumbAI(p);
		case "Random AI":
			return new RandomAI(p);
		case "Smart AI":
			return new SmartAI(p);
		default:
			return null;
			
		}
		
		
	}
		
	
	public static void main(String[] args) {
		//create the JFrame and make it visible
		new Main().setVisible(true);
	}
	
	
}
