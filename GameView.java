package com.aci.sixmensmorris;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;




public class GameView{

	static GameModel model;
	static final int GAMEWIDTH = 1000;
	static final int GAMEHEIGHT = 1000;
	static final double MULTIPLIER=0.7;
	static final int PIECERADIUS=50;
	static double[][] NodeCoordinates = new double[16][2];  //**
	static Ellipse2D.Double[] boardnodes= new Ellipse2D.Double[16];

	public static JFrame frame;
	public static JFrame frame2;
	public static JButton newGameButton = new JButton("New Game");
	public static JButton setPieces = new JButton("Set Pieces");
	public static GameBoard board = new GameBoard();
	public static Pieces pieces = new Pieces();
	public static JButton validitybutton = new JButton("Check Validity");


	public GameView(GameModel model){

		this.model = model;
		getNodeCoordinates(125, 125);  //**

	}

	public void initMenu(){
		frame = new JFrame("Main Menu - 6 Men's Morris");
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(500, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new GridBagLayout());
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		GridBagConstraints c = new GridBagConstraints();

		JLabel label1 = new JLabel("Welcome to Six Men's Morris");
		c.gridx = 0;
		c.gridy = 8;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(label1, c);

		
		c.gridx = 0;
		c.gridy = 9;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(newGameButton, c);
		

		
		c.gridx = 0;
		c.gridy = 10;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(setPieces, c);
		
	}
	
	private void getNodeCoordinates(double NODE1_X, double NODE1_Y, int node){
		System.out.println("running " + node);
		
		int[] neighbors=model.getNeighbors(node);
		for (int i=0; i<neighbors.length;i++){
			int neighbor=neighbors[i];
			if (NodeCoordinates[neighbor-1][0]==0.0){
				int Y_CHANGE = (int)(MULTIPLIER*GAMEHEIGHT*model.getNeighborlength(node, neighbor)* Math.sin(Math.toRadians(model.getNeighborangle(node, neighbor))));
				int X_CHANGE = (int) (MULTIPLIER*GAMEWIDTH*model.getNeighborlength(node, neighbor)* Math.cos(Math.toRadians(model.getNeighborangle(node, neighbor))));
				NodeCoordinates[neighbor-1][0]=NODE1_X+X_CHANGE;
				NodeCoordinates[neighbor-1][1]=NODE1_Y-Y_CHANGE;
				getNodeCoordinates(NodeCoordinates[neighbor-1][0], NodeCoordinates[neighbor-1][1], neighbor);
			}	
		}
	}
	
	private void getNodeCoordinates(double NODE1_X, double NODE1_Y){
		getNodeCoordinates(NODE1_X, NODE1_Y, 1);
	}
	


	public static class GameBoard extends JPanel {
		
	
		@Override
		public void paintComponent(Graphics g) {
//			Dimension d = this.getSize();
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(3));

			
			//Draw Lines for Board:
			// DRAW GAME BOARD
			for (int i=0; i<NodeCoordinates.length;i++){
				int node = i+1;
				
				int[] neighbors = model.getNeighbors(node);
				for (int j = 0; j < neighbors.length; j++) {
					int neighbor = neighbors[j];
					Line2D line = new Line2D.Double(NodeCoordinates[node-1][0], NodeCoordinates[node-1][1] , NodeCoordinates[neighbor-1][0],NodeCoordinates[neighbor-1][1]);
					g2d.draw(line);
				}
				
			}
			
			
			
			// Draw Circles:
			for (int i=0; i<NodeCoordinates.length;i++){
				int node = i+1;
				g2d.setColor(Color.WHITE);
				Ellipse2D.Double boardnode= new Ellipse2D.Double(NodeCoordinates[node-1][0]-PIECERADIUS/2, NodeCoordinates[node-1][1]-PIECERADIUS/2, (double) PIECERADIUS, (double) PIECERADIUS);
				boardnodes[i]=boardnode;
				g2d.fill(boardnode);
				g2d.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(3));
				g2d.draw(boardnode);
			}
		}
	}
		
		
	
	
	
	public static class Pieces extends JLayeredPane{
			
			
		Pieces()
	    {
	        super() ;
	        this.setOpaque( false ) ; // this will make the JPanel transparent 
	                                  // but not its components (JLabel, TextField etc
	    
	    }
			public void paintComponent(Graphics g) {
				
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
			
			
			
			// Code for pieces:
			// Red Pieces:
				for (int i=0; i<model.getGraphSize(); i++){
					for (Piece j: model.getTokenStack(i+1)){
						g2d.setColor(j.getColor());
						g2d.fill(j);
						g2d.setStroke(new BasicStroke(3));
						g2d.setColor(Color.BLACK);
						g2d.draw(j);
					}
				}
				
			for (Piece i : model.redpieces) {
				g2d.setColor(Color.RED);
				g2d.fill(i);
				g2d.setStroke(new BasicStroke(3));
				g2d.setColor(Color.BLACK);
				g2d.draw(i);
				
			}
			
			
			for (Piece i : model.bluepieces) {
				g2d.setColor(Color.BLUE);
				g2d.fill(i);
				g2d.setStroke(new BasicStroke(3));
				g2d.setColor(Color.BLACK);
				g2d.draw(i);
			}
			
			if (model.getSelectedPiece()!=null){
				g2d.setStroke(new BasicStroke(4));
				g2d.setColor(Color.YELLOW);
				g2d.draw(model.getSelectedPiece());
			}
			
		}
	}
		


		
	

	public void SetPieces() {
		frame.setVisible(false);
		frame2 = new JFrame("Set Pieces");
		frame2.setVisible(true);
		frame2.setPreferredSize(new Dimension(GAMEWIDTH, GAMEHEIGHT));
		frame2.setLayout(new BorderLayout());
		
		pieces.setLayout(null);
		board.setLayout(null);
		validitybutton.setBounds(0,0,200,30);
		board.add(validitybutton);
		frame2.add(pieces);
		frame2.add(board);

		frame2.setResizable(false);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//		JPanel panel = new JPanel(new GridBagLayout());
//		frame2.getContentPane().add(panel, BorderLayout.PAGE_START);
//		GridBagConstraints c = new GridBagConstraints();
		board.setBounds(0, 0, GAMEWIDTH, GAMEHEIGHT);
		pieces.setBounds(0, 0, GAMEWIDTH, GAMEHEIGHT);
		board.setBackground(Color.LIGHT_GRAY);
		board.setOpaque(true);
		frame2.pack();	
	}
		
	public static double distanceFromPoint(MouseEvent e, double x, double y) {
		return Math.sqrt((e.getX() - x) * (e.getX() - x) + (e.getY() - y) * (e.getY() - y));
	}
	
	public void registerListeners(GameController controller) {
			board.addMouseListener(controller);
			validitybutton.addActionListener(controller);
			setPieces.addActionListener(controller);
			newGameButton.addActionListener(controller);
    }

	public void displayErrors(String errorstring) {
		JFrame frame3=new JFrame();
		String message;
			if (errorstring.isEmpty()){
				message="This arrangement is valid.";
			}
			else {
				message="ERROR:\n"+errorstring;
			}
		JOptionPane.showMessageDialog(frame3, message);
	}

	public void displayFirstPlayer(String firstplayer) {
		frame.setVisible(false);
		JFrame frame4=new JFrame();
		String message=firstplayer+ " goes first!";
		JOptionPane.showMessageDialog(frame4, message);
		frame.setVisible(true);
	}
	

}