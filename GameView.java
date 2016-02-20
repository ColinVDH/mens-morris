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

public class GameView extends JFrame {

	GameModel model;
	final int GAMEWIDTH = 800;
	final int GAMEHEIGHT = 800;
	final double MULTIPLIER=0.7;
	double[][] NodeCoordinates = new double[16][2];  //**
	JPanel p = new JPanel();

	public GameView(GameModel model) {
		super("Six Men's Morris");
		this.model = model;
		setSize(GAMEWIDTH, GAMEHEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		add(p);
		getNodeCoordinates(125, 125);  //**
	}

	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(10)); //**
		
		// DRAW GAME BOARD
			for (int i=0; i<NodeCoordinates.length;i++){
				int node = i+1;
				
				int[] neighbors = model.getNeighbors(node);
				for (int j = 0; j < neighbors.length; j++) {
					int neighbor = neighbors[j];
					Line2D line = new Line2D.Double(NodeCoordinates[node-1][0], NodeCoordinates[node-1][1] , NodeCoordinates[neighbor-1][0],NodeCoordinates[neighbor-1][1]);
					g2.draw(line);	
				}
				
			}
			
			for (int i=0; i<NodeCoordinates.length;i++){
				int node = i+1;
				g2.setColor(Color.WHITE);
				fillCenteredCircle(g2, (int) NodeCoordinates[node-1][0], (int) NodeCoordinates[node-1][1], 50);
				g2.setColor(Color.BLACK);
				drawCenteredCircle(g2, (int) NodeCoordinates[node-1][0], (int) NodeCoordinates[node-1][1], 50);
			}
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
