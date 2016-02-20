package com.aci.sixmensmorris;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;


import java.net.URL;
import javax.imageio.ImageIO;
import java.util.Observable;
import java.util.Observer;
import java.util.*;

public class GameView {

	static GameModel model;
	static final int GAMEWIDTH = 1000;
	static final int GAMEHEIGHT = 1000;
	static final double MULTIPLIER=0.7;
	static final int PIECERADIUS=50;
	static double[][] NodeCoordinates = new double[16][2];  //**

	public static JFrame frame;
	public static JFrame frame2;

	public GameView(GameModel model) {
//		super("Six Men's Morris");
		this.model = model;
//		setSize(GAMEWIDTH, GAMEHEIGHT);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setVisible(true);

		getNodeCoordinates(125, 125);  //**
		
		frame = new JFrame("Main Menu - 6 Men's Morris");
		frame.setVisible(true);
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

		JButton newGameButton = new JButton("New Game");
		c.gridx = 0;
		c.gridy = 9;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(newGameButton, c);

		JButton setPieces = new JButton("Set Pieces");
		c.gridx = 0;
		c.gridy = 10;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(setPieces, c);
		setPieces.addActionListener(new SetPiecesAction());
	}

	public void paint(Graphics g) {
//		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(10)); //**
			
			
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
	

	public static class DrawLines extends JPanel {

	
		Ellipse2D.Double[] redpieces = new Ellipse2D.Double[6];
		Ellipse2D.Double[] bluepieces = new Ellipse2D.Double[6];

		@Override
		public void paintComponent(Graphics g) {
			Dimension d = this.getSize();
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
				fillCenteredCircle(g2d, (int) NodeCoordinates[node-1][0], (int) NodeCoordinates[node-1][1], PIECERADIUS);
				g2d.setColor(Color.BLACK);
				drawCenteredCircle(g2d, (int) NodeCoordinates[node-1][0], (int) NodeCoordinates[node-1][1], PIECERADIUS);
			}

			// Code for pieces:
			// Red Pieces:
			g2d.setColor(Color.RED);
			for (int i = 0; i < redpieces.length; i++) {
				redpieces[i] = new Ellipse2D.Double(NodeCoordinates[1][0]+PIECERADIUS*(i-3),
						NodeCoordinates[0][0]-100, PIECERADIUS, PIECERADIUS);
				g2d.fill(redpieces[i]);
			}

			// Blue Pieces:
			g2d.setColor(Color.BLUE);
			for (int i = 0; i < bluepieces.length; i++) {
				bluepieces[i] = new Ellipse2D.Double(NodeCoordinates[1][0]+PIECERADIUS*(i-3),
						NodeCoordinates[15][0]+(100-PIECERADIUS), PIECERADIUS, PIECERADIUS);
				g2d.fill(bluepieces[i]);
			}



		}
		
		private void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
			  x = x-(r/2);
			  y = y-(r/2);
			  g.drawOval(x,y,r,r);
			}
		
		private void fillCenteredCircle(Graphics2D g, int x, int y, int r) {
			  x = x-(r/2);
			  y = y-(r/2);
			  g.fillOval(x,y,r,r);
			}
		
	}

	public static void SetPieces() {
		frame2 = new JFrame("Set Pieces");
		frame2.setVisible(true);
		frame2.setSize(GAMEWIDTH, GAMEHEIGHT);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new GridBagLayout());
		frame2.getContentPane().add(panel, BorderLayout.PAGE_START);
		GridBagConstraints c = new GridBagConstraints();

		DrawLines draw = new DrawLines();
		frame2.add(draw);
	}
		
	static class SetPiecesAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			GameView.SetPieces();
			frame.setVisible(false);
		}
	}

	static class CheckValidAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// View.SetPieces();
		}
	}

	public static void clickedPiece(MouseEvent e) {
	}

	public static void clickedPlacement(MouseEvent e) {

	}

	public static double distanceFromPoint(MouseEvent e, double x, double y) {
		return Math.sqrt((e.getX() - x) * (e.getX() - x) + (e.getY() - y) * (e.getY() - y));
	}
}