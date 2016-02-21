package com.aci.sixmensmorris;

import java.util.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Graph {
	// The setup of a particular Men's Morris board is inputted as an array of
	// values associated with each node:
	// {node#}, {neighbor1#, neighbor2#, etc...}, {neighbor1boardline,
	// neighbor2boardline}
	private static final double[][][] SETUP={{{1},{2,7},{0,270},{0.5,0.5}},
											{{2},{1,3,5},{180,0,270},{0.5,0.5,0.25}},
											{{3},{2,10},{180,270},{0.5,0.5}},
											{{4},{5,8},{0,270},{0.25,0.25}},
											{{5},{2,4,6},{90,180,0},{0.25,0.25,0.25}},
											{{6},{5,9},{180,270},{0.25,0.25}},
											{{7},{1,8,14},{90,0,270},{0.5,0.25,0.5}},   //Six Men's Morris board
											{{8},{4,7,11},{90,180,270},{0.25,0.25,0.25}},
											{{9},{6,10,13},{90,0,270},{0.25,0.25,0.25}},
											{{10},{3,9,16},{90,180,270},{0.5,0.25,0.5}},
											{{11},{8,12},{90,0},{0.25,0.25}},
											{{12},{11,13,15},{180,0,270},{0.25,0.25,0.25}},
											{{13},{9,12},{90,180},{0.25,0.25}},
											{{14},{7,15},{90,0},{0.5,0.5}},
											{{15},{14,16,12},{180,0,90},{0.5,0.5,0.25}},
											{{16},{10,15},{90,180},{0.5,0.5}}};	
	private static Node[] nodes;

	public Graph() {
		this(SETUP);
	}

	public Graph(double[][][] SETUP) {
		nodes= new Node[SETUP.length];
		for (int i = 0; i < SETUP.length; i++) {
			if (SETUP[i][2].length!=SETUP[i][3].length || SETUP[i][1].length!=SETUP[i][2].length){
				throw new IllegalArgumentException("Invalid Setup Array");
			}
			nodes[i] = new Node(SETUP[i][1], SETUP[i][2], SETUP[i][3]);
		}
	}
	
	public int getGraphSize(){
		return nodes.length;
	}

	public int[] getNeighbors(int n) {
		if (n<1 || n>getGraphSize()) throw new IllegalArgumentException("Node"+ n +" does not exist");
		return nodes[n - 1].getNeighbors();
	}

	public void addToken(Piece C, int n) {
		nodes[n - 1].addToken(C);
	}
	
	public void removeToken(Piece C, int n){
		nodes[n-1].removeToken(C);
	}

	public ArrayList<Piece> getTokenStack(int n) {
		return nodes[n - 1].getTokenStack();
	}

	public double getNeighborlength(int n, int s) {
		if (n<1 || n>getGraphSize()) throw new IllegalArgumentException("Node"+ n +" does not exist");
		return nodes[n - 1].getNeighborlength(s);
	}
	
	public int getNeighborangle(int n, int s) {
		return nodes[n - 1].getNeighborangle(s);
	}
	
	public boolean areNeighbors(int n, int m){
		for (int i=0; i<getNeighbors(n).length;i++){
			if (getNeighbors(n)[i]==m){
				return true;
			}
		}
		return false;
	}

}