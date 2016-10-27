/* NetId(s): bas334, dnm54. */

package gui;

import clui.BoardPrinter;
import clui.ConsoleController;
import controller.Controller;
import controller.DumbAI;
import controller.RandomAI;
import controller.SmartAI;
import model.Game;
import model.NotImplementedException;
import model.Player;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Main extends JFrame{
	JComboBox<String> cbPlayerX, cbPlayerO;
	Player p;
	//JLabel label;
	Controller playerX, playerO;
	JFrame gameFrame;
	
	private class okClick implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			playerX = createController((String) cbPlayerX.getSelectedItem(), Player.X);
			playerO = createController((String) cbPlayerO.getSelectedItem(), Player.O);
			gameFrame.setVisible(true);
			setVisible(false);
			
		}
		
	}
	
	
	private class mouseOver implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			Square labelClicked = (Square) e.getSource();
			labelClicked.selected = true;
			labelClicked.repaint();
			
			/*if (p == Player.X) p = Player.O;
			else p = Player.X;*/
			
			
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
			
			Square labelClicked = (Square) e.getSource();
			labelClicked.inSquare = true;
			if (!labelClicked.selected)
				labelClicked.repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			Square labelClicked = (Square) e.getSource();
			labelClicked.inSquare = false;
			if (!labelClicked.selected)
				labelClicked.repaint();
		}
		
	}
	
	public class Square extends JButton {
		private boolean inSquare = false;
		private boolean selected = false;
		public Square() {
			setPreferredSize(new Dimension(10,10));
			setOpaque(true);
			setBorder(new LineBorder(Color.blue));
			addMouseListener(new mouseOver());
			
		}
		
		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.setFont(new Font("", Font.PLAIN, 50));
			//g.drawString("X", 40, 55);
			if (inSquare || selected) {
				if (p == Player.O)
					g.drawString("O", 40, 55);
				else 
					g.drawString("X", 40, 55);
				//g.drawArc(20, 10, getWidth() - 41, getHeight() - 21, 0, 360);
			}
			else if (!selected)
				g.dispose();
			//else if (p == Player.O && inSquare)
				//g.drawString("O", 0, 0);
		}
	}
	
	public Main() {
		//start a new game
		Game guigame = new Game();
		//start up the JFrame for the game
	    gameFrame = new JFrame();
	    JPanel gamePanel = new JPanel();
	    //gameFrame.setLayout(new FlowLayout());
		gamePanel.setLayout(new GridLayout(9,9));

		//add all 81 buttons into the grid layout for the 'board'
		for (int count = 0; count < 81; count ++) {
			gamePanel.add(new Square());
			
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
		
		
	    //dispose();
		
		
		guigame.addListener(playerX);//i dont exactly know what these do
		guigame.addListener(playerO);
		
		//need some code here to print the new GUI picture when it changes	
	    
	}
	
	public Controller createController(String selection, Player p){
		switch(selection) {
		case "User":
			System.out.println("user");
			return new ConsoleController(p);
		case "Dumb Computer":
			return new DumbAI(p);
		case "Smart Computer":
			return new RandomAI(p);
		case "Random Computer":
			return new SmartAI(p);
		default:
			return new ConsoleController(p);
			
		}
		
		
	}
		
	
	public static void main(String[] args) {
		new Main().setVisible(true);
	}
	
	
}
