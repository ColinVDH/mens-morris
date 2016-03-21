/*
GameView presents the graphical user interface. 
When the application is first loaded, this module initializes a menu, 
presenting the user with “New Game” or “Set Pieces”. 
If “New Game” is selected, this module presents a standard dialog box that informs the user of the randomly selected first player. 
If “Set Pieces” is selected, this module initializes a window with a graphical representation of the Game Board, 
with the standard number of pieces of each color on each side of the board.  
Pieces are updated on the frame as the user modifies their position. 
*/
package com.aci.sixmensmorris;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

public class GameView {

	public static final int GAMEWIDTH = 800;
	public static final int GAMEHEIGHT = 800;
	public static final double SIZE_MULTIPLIER = 0.6; // size of board
	public static final int NODE1_X = (int) ((double) GAMEWIDTH*0.195); // top left node coordinates
	public static final int NODE1_Y = (int) ((double) GAMEHEIGHT*0.180);
	public static final int PIECERADIUS = 45; // size of piece & board locations
	public static ArrayList<Ellipse2D.Double> boardnodes = new ArrayList<Ellipse2D.Double>();

	private static GameModel model;
	public static ArrayList<double[]> NodeCoordinates = new ArrayList<double[]>();
	private static JFrame frame; // mainmenu frame
	private static JFrame frame2; // gameboard frame
	private static JFrame frame3; //gameover frame
	private static JButton newgame = new JButton("New Game");
	private static JButton mainmenu = new JButton("Return to Main Menu");
	private static JButton savebutton = new JButton("Save Game");
	private static JButton continuebutton = new JButton("Continue from Save");
	private static GameBoard board = new GameBoard();
	private static Pieces pieces = new Pieces();

	private static JLabel state = new JLabel(); //Text describing the current game state
	private static Dimension dim = Toolkit.getDefaultToolkit().getScreenSize(); 

	public GameView(GameModel model) {
		
		
		
		this.model = model;
		getNodeCoordinates(NODE1_X, NODE1_Y);

		for (int i = 0; i < model.getPieceNumber(); i++) {
//			 Piece redpiece = new Piece(Color.RED, (double) GAMEWIDTH / 2 +
//			 PIECERADIUS * (i - (double) model.getPieceNumber()/2), 85,
//			 PIECERADIUS);
//			 Piece bluepiece = new Piece(Color.BLUE,(double) GAMEWIDTH / 2 +
//			 PIECERADIUS * (i - (double) model.getPieceNumber()/2), GAMEHEIGHT
//			 - 165, PIECERADIUS);
			Piece redpiece = new Piece(Color.RED, (double) GAMEWIDTH / 2 - PIECERADIUS / 2 -3,  60 , PIECERADIUS);
			Piece bluepiece = new Piece(Color.BLUE, (double) GAMEWIDTH / 2 - PIECERADIUS / 2 -3 , GAMEHEIGHT-135, PIECERADIUS);
			model.addPiece(redpiece);
			model.addPiece(bluepiece);
		}
	}

	public void viewReset(){
		for (int i = 0; i < model.getRedStack(); i++) {
			Piece redpiece = new Piece(Color.RED, (double) GAMEWIDTH / 2 - PIECERADIUS / 2 -3,  60 , PIECERADIUS);
			model.addPiece(redpiece);
		}	
		for (int i = 0; i < model.getBlueStack(); i++) {
			Piece bluepiece = new Piece(Color.BLUE, (double) GAMEWIDTH / 2 - PIECERADIUS / 2 -3 , GAMEHEIGHT-135, PIECERADIUS);
			model.addPiece(bluepiece);
		}
	}
	/**
	 * Recursively generate all node coordinates.
	 */
	private void getNodeCoordinates(double NODE1_X, double NODE1_Y, int node) {
		int[] neighbors = model.getNeighbors(node);
		for (int i = 0; i < neighbors.length; i++) {
			int neighbor = neighbors[i];
			if (NodeCoordinates.get(neighbor - 1)[0] == 0.0) {
				int Y_CHANGE = (int) (SIZE_MULTIPLIER * GAMEHEIGHT * model.getNeighborlength(node, neighbor)
						* Math.sin(Math.toRadians(model.getNeighborangle(node, neighbor))));
				int X_CHANGE = (int) (SIZE_MULTIPLIER * GAMEWIDTH * model.getNeighborlength(node, neighbor)
						* Math.cos(Math.toRadians(model.getNeighborangle(node, neighbor))));
				NodeCoordinates.get(neighbor - 1)[0] = NODE1_X + X_CHANGE;
				NodeCoordinates.get(neighbor - 1)[1] = NODE1_Y - Y_CHANGE;
				getNodeCoordinates(NodeCoordinates.get(neighbor - 1)[0], NodeCoordinates.get(neighbor - 1)[1],
						neighbor);
			}
		}
	}

	/**
	 * Recursively generate all node coordinates.
	 */
	private void getNodeCoordinates(double NODE1_X, double NODE1_Y) {
		for (int j = 0; j < model.getGraphSize(); j++) {
			NodeCoordinates.add(new double[] { 0.0, 0.0 });
		}
		getNodeCoordinates(NODE1_X, NODE1_Y, 1);
	}
	
	
	
	// Initialize main menu
	public void initMenu() {
		
		frame = new JFrame("Main Menu");
		
		frame.setResizable(false);
		frame.setSize(500, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new GridBagLayout());
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		GridBagConstraints c = new GridBagConstraints();

		JLabel label1 = new JLabel("Welcome to " + model.getName());
		label1.setFont(new Font("Calibri", Font.BOLD, 35));
		c.gridx = 0;
		c.gridy = 8;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(label1, c);

		c.gridx = 0;
		c.gridy = 9;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(newgame, c);
		
		c.gridx = 0;
		c.gridy = 10;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(continuebutton, c);
		
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.setVisible(true);
		

	}

	

	// Setup the Game Board panel
	public static class GameBoard extends JPanel {

		@Override
		public void paintComponent(Graphics g) {
			// Dimension d = this.getSize();
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(3));

			// Draw Lines for Board:
			// DRAW GAME BOARD
			for (int i = 0; i < NodeCoordinates.size(); i++) {
				int node = i + 1;
				int[] neighbors = model.getNeighbors(node);
				for (int j = 0; j < neighbors.length; j++) {
					int neighbor = neighbors[j];
					Line2D line = new Line2D.Double(NodeCoordinates.get(node - 1)[0], NodeCoordinates.get(node - 1)[1],
							NodeCoordinates.get(neighbor - 1)[0], NodeCoordinates.get(neighbor - 1)[1]);
					g2d.draw(line);
				}

			}

			// Draw Circles:
			for (int i = 0; i < NodeCoordinates.size(); i++) {
				int node = i + 1;
				g2d.setColor(new Color(139, 69, 19));
				Ellipse2D.Double boardnode = new Ellipse2D.Double(NodeCoordinates.get(node - 1)[0] - PIECERADIUS / 2,
						NodeCoordinates.get(node - 1)[1] - PIECERADIUS / 2, (double) PIECERADIUS, (double) PIECERADIUS);
				boardnodes.add(boardnode);
				g2d.fill(boardnode);
				g2d.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(3));
				g2d.draw(boardnode);
			}
		}
	}
	
	
	/**
	 * Setup up the JLayeredPane so that pieces are overlayed over the game board. 
	 */
	public static class Pieces extends JLayeredPane {

		Pieces() {
			super();
			this.setOpaque(false); // this will make the JPanel transparent but not its components (JLabel, TextField etc

		}

		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			for (Piece p : model.getPieces()) {
				if (p.getColor() == Color.RED)
					g2d.setColor(Color.RED);
				else
					g2d.setColor(Color.BLUE);
				g2d.fill(p);
				g2d.setStroke(new BasicStroke(3));
				g2d.setColor(Color.BLACK);
				g2d.draw(p);
			}

			for (int i = 0; i < model.getGraphSize(); i++) {
				if (!model.getTokenStack(i + 1).isEmpty()) {
					Piece p = model.getTokenStack(i + 1).get(0);
					g2d.setColor(p.getColor());
					g2d.fill(p);
					g2d.setStroke(new BasicStroke(3));
					g2d.setColor(Color.BLACK);
					g2d.draw(p);
				}

			}

			if (model.getSelectedPiece() != null) {
				g2d.setColor(Color.YELLOW);
				g2d.setStroke(new BasicStroke(3));
				g2d.draw(model.getSelectedPiece());
			}

		}
	}
	
	// Initialize the frame containing the game board.
	public void initGameWindow() {
		frame.dispose();
		frame2 = new JFrame(model.getName());
		
		frame2.setPreferredSize(new Dimension(GAMEWIDTH, GAMEHEIGHT));
		ImageIcon ii = new ImageIcon(getClass().getResource("background.jpg"));
		JLabel background = new JLabel(ii);

		pieces.setLayout(null);
		board.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weighty = 1;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 0;
		savebutton.setEnabled(true);
		board.add(savebutton, c);

		
		c.anchor = GridBagConstraints.NORTH;
		board.add(state, c);
		state.setFont(new Font("Calibri", Font.BOLD, 25));
		frame2.add(pieces);
		frame2.add(board);
		frame2.add(background);
	
		frame2.getRootPane().setGlassPane(new JComponent() {
		    public void paintComponent(Graphics g) {
		        g.setColor(new Color(0, 0, 0, 100));
		        g.fillRect(0, 0, getWidth(), getHeight());
		        super.paintComponent(g);
		    }
		});
		
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		board.setBounds(0, 0, GAMEWIDTH, GAMEHEIGHT);
		pieces.setBounds(0, 0, GAMEWIDTH, GAMEHEIGHT);
		board.setOpaque(false);
		frame2.pack();
		frame2.setResizable(false);
		frame2.setLocation(dim.width/2-frame2.getSize().width/2, dim.height/2-frame2.getSize().height/2);
		frame2.setVisible(true);
		updateState();
	}
	
	// Repaint the pieces.
	public void repaintPieces() {
		pieces.repaint();
	}

	// Update the state text. If the game is won, it also opens the Game Over frame, allowing the player to return to the Main Menu.
	public void updateState() {
		String color; String color2;
		if (model.getActivePlayer() == Color.RED) color = "Red";
		else color = "Blue";
		
		if (model.getState().equals("draw") || model.getState().equals("win")){
			JLabel label1;
			if (model.getState().equals("draw")){
				label1 = new JLabel("It's a draw");
			}
			else label1 = new JLabel(color+" wins!");
			state.setText("Game Over.");
			
			frame2.getRootPane().getGlassPane().setVisible(true);
			savebutton.setEnabled(false);
			frame3 = new JFrame("");
			frame3.setResizable(false);
			frame3.setSize(200, 150);
			frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			

			JPanel panel = new JPanel(new GridBagLayout());
			frame3.getContentPane().add(panel, BorderLayout.NORTH);
			GridBagConstraints c = new GridBagConstraints();

			
			label1.setFont(new Font("Calibri", Font.BOLD, 25));
			c.gridx = 0;
			c.gridy = 8;
			c.insets = new Insets(10, 10, 10, 10);
			panel.add(label1, c);

			c.gridx = 0;
			c.gridy = 9;
			c.insets = new Insets(10, 10, 10, 10);
			panel.add(mainmenu, c);
			frame3.setLocationRelativeTo(frame2);
			frame3.setVisible(true);
		}
		else if (model.getState().equals("remove")) state.setText(color + "'s turn." + " Remove opponent piece.");
		else if (model.getState().equals("place")) state.setText(color + "'s turn." + " Place piece.");
		else if (model.getState().equals("move")) state.setText(color + "'s turn." + " Move piece.");
		else if (model.getState().equals("fly")) state.setText(color + "'s turn." + " Move piece. Flying is permitted.");
		
	}
		
	
	
	
	// Display notifications (Used to inform player of illegal moves)
	public void notification(String string) {
		JFrame frame3 = new JFrame();
		JOptionPane.showMessageDialog(frame3, string);
	}
	
	//close the game / game-over frame. 
	public void closeGame(){
		frame2.dispose();
		frame3.dispose();
	}

	// register the controller as a listener to all the buttons and the game board.
	public void registerListeners(GameController controller) {
		board.addMouseListener(controller);
		savebutton.addActionListener(controller);
		newgame.addActionListener(controller);
		mainmenu.addActionListener(controller);
		continuebutton.addActionListener(controller);
	}


	

}