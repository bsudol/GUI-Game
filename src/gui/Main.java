/* NetId(s): dnm54, bas334.*/

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
	JComboBox<String> cbPlayerX, cbPlayerO;
	Player p;
	//JLabel label;
	Controller playerX, playerO;
	JFrame gameFrame;
	Game guigame;
	Board guiBoard;
	ArrayList<Square> squares = new ArrayList<Square>();
	
	private class okClick implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (((String) cbPlayerX.getSelectedItem()) != "User") {
				playerX = createController((String) cbPlayerX.getSelectedItem()
						, Player.X); 
				guigame.addListener(playerX);
			}
			
			if (((String)cbPlayerO.getSelectedItem()) != "User") {	
				playerO = createController((String) cbPlayerO.getSelectedItem()
						, Player.O); 
				guigame.addListener(playerO);
			}
			
			gameFrame.setVisible(true);
			setVisible(false);
		}
		
	}
	
	public class BoardPrinter implements GameListener {

		@Override
		public void gameChanged(Game g) {
			// TODO Auto-generated method stub
			Board b = g.getBoard();
			Square s;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (b.get(i, j) != null) {
						s = squares.get(i*9 + j);
						s.repaint();
					}
						
				}
			}
		}
		
	}
	
	private class mouseOver implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			Square panelClicked = (Square) e.getSource();
			if (panelClicked.selected == false) {
				//panelClicked.paintImmediately(0,0,
					//	panelClicked.getWidth(), panelClicked.getHeight());
				panelClicked.selected = true;
			}
			
			
			guigame.submitMove(p, panelClicked.loc);
			
			p = guigame.nextTurn();
			
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
			
			Square panelClicked = (Square) e.getSource();
			panelClicked.inSquare = true;
			if (!panelClicked.selected)
				panelClicked.repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			Square panelClicked = (Square) e.getSource();
			panelClicked.inSquare = false;
			if (!panelClicked.selected)
				panelClicked.repaint();
		}
	}
	
	public class Square extends JButton {
		private boolean inSquare = false;
		private boolean selected = false;
		
		Location loc;
		
		public Square(int i, int j) {
			loc = new Location(i,j);
			setPreferredSize(new Dimension(10,10));
			setOpaque(true);
			setBorder(new LineBorder(Color.blue));
			addMouseListener(new mouseOver());
			squares.add(this);
		}
		
		public Square getSquare(int i, int j) {
			return squares.get(i*9 +j);
		}
		
		public ArrayList<Square> getSquareList() {
			
			return squares;
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			boolean select;
			select = guigame.getBoard().get(loc) != null;
			g.setFont(new Font("", Font.PLAIN, 50));
			if (inSquare && !select) {
				if (guigame.nextTurn() == Player.X)
					g.drawString("X", 40, 55);
				else 
					g.drawString("O", 40, 55);
			}
			if (select) {
				if (guigame.getBoard().get(loc) == Player.X)
					g.drawString("X", 40, 55);
				else 
					g.drawString("O", 40, 55);
			}
			else if (!select)
				g.dispose();
			
			if (guigame.getBoard().getState() == State.HAS_WINNER) {
				paintWinners();
			}
		}
		
		public void paintWinners() {
			Board b = guigame.getBoard();
			Line winners = b.getWinner().line;
			Square win;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (winners.contains(i, j)) {
						win = getSquare(i,j);
						win.setBackground(Color.GREEN);
					}
						
				}
			}
		}
	}
	
	
	public Main() {
		//start a new game
		guigame = new Game();
		//start up the JFrame for the game
	    gameFrame = new JFrame();
	    JPanel gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(9,9));
	
		
		//add all 81 buttons into the grid layout for the 'board'
		for (int i = 0; i < 9; i ++) {
			for (int j = 0; j < 9; j ++) {
				Square s = new Square(i, j);
				gamePanel.add(s);	
			}
		}
		
		//lay it out/make it go
		gameFrame.add(gamePanel);
		gameFrame.setPreferredSize(new Dimension(1000,1000));
		gameFrame.pack();
		gamePanel.setVisible(true);
		
		//gameFrame.setVisible(true);
		
		setLayout(new BorderLayout());
		setTitle("Choose Player Modes");
		
		
	    
	    cbPlayerX = new JComboBox<String>();
	    
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
	    //cbPlayerO.setPreferredSize(new Dimension(, 10));
	    
	    add(cbPlayerX, BorderLayout.WEST);
	    add(label, BorderLayout.CENTER);
	    add(cbPlayerO, BorderLayout.EAST);

	    JButton ok = new JButton("OK");
	    p = Player.X;
	    ok.addActionListener(new okClick());
	    add(ok, BorderLayout.SOUTH);
	   
		setPreferredSize(new Dimension(300,150));
		pack();
		setVisible(true);

		guigame.addListener(new BoardPrinter());
	}
	
	public Controller createController(String selection, Player p){
		switch(selection) {
		case "User":
			return null;
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
		new Main().setVisible(true);
	}
	
	
}
