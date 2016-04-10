/*
Graph represents the game board and gets features of particular board points. 
*/


package com.aci.sixmensmorris;

import java.util.*;

public class Graph {
	// The SIXSETUP of a particular Men's Morris board is inputted as an array of
	// values associated with each node: {node#}, {neighbor1#, neighbor2#, etc...}, {angle to neighbor 1, angle to neighbor 2,...}, {length to neighbor 1, length to neighbor 2,...}
	private static final double[][][] SIXSETUP=
		{{{1},{2,7},{0,270},{0.5,0.5}},
		{{2},{1,3,5},{180,0,270},{0.5,0.5,0.25}},
		{{3},{2,10},{180,270},{0.5,0.5}},
		{{4},{5,8},{0,270},{0.25,0.25}},
		{{5},{2,4,6},{90,180,0},{0.25,0.25,0.25}},
		{{6},{5,9},{180,270},{0.25,0.25}},
		{{7},{1,8,14},{90,0,270},{0.5,0.25,0.5}},   											//Six Men's Morris Board
		{{8},{4,7,11},{90,180,270},{0.25,0.25,0.25}},
		{{9},{6,10,13},{90,0,270},{0.25,0.25,0.25}},
		{{10},{3,9,16},{90,180,270},{0.5,0.25,0.5}},
		{{11},{8,12},{90,0},{0.25,0.25}},
		{{12},{11,13,15},{180,0,270},{0.25,0.25,0.25}},
		{{13},{9,12},{90,180},{0.25,0.25}},
		{{14},{7,15},{90,0},{0.5,0.5}},
		{{15},{14,16,12},{180,0,90},{0.5,0.5,0.25}},
		{{16},{10,15},{90,180},{0.5,0.5}}};	
	
	private static final double[][][] NINESETUP=	
	{{{1},{2,10},{0,270},{0.5,0.5}},
	{{2},{1,3,5},{180,0,270},{0.5,0.5,(double) 1/6}},
	{{3},{2,15},{180,270},{0.5,0.5}},
	{{4},{5,11},{0,270},{(double) 2/6,(double) 2/6}},
	{{5},{2,4,6,8},{90,180,0,270},{(double) 1/6,(double) 2/6,(double) 2/6,(double) 1/6}},
	{{6},{5,14},{180,270},{(double) 2/6,(double) 2/6}},
	{{7},{8,12},{0,270},{(double) 1/6,(double) 1/6}},   //Six Men's Morris board
	{{8},{5,7,9},{90,180,0},{(double) 1/6,(double) 1/6,(double) 1/6}},
	{{9},{8,13},{180,270},{(double) 1/6,(double) 1/6}},
	{{10},{1,11,22},{90,0,270},{0.5,(double) 1/6,0.5}},
	{{11},{4,10,12,19},{90,180,0,270},{(double) 2/6,(double) 1/6,(double) 1/6,(double) 2/6}},    						//Nine Mens Morris Board
	{{12},{7,11,16},{90,180,270},{(double) 1/6,(double) 1/6,(double) 1/6}},
	{{13},{9,14,18},{90,0,270},{(double) 1/6,(double) 1/6,(double) 1/6}},
	{{14},{6,13,15,21},{90,180,0,270},{(double) 2/6,(double) 1/6,(double) 1/6,(double) 2/6}},
	{{15},{3,14,24},{90,180,270},{0.5,(double) 1/6,0.5}},
	{{16},{12,17},{90,0},{(double) 1/6,(double) 1/6}},	
	{{17},{16,18,20},{180,0,270},{(double) 1/6,(double) 1/6,(double) 1/6}},	
	{{18},{13,17},{90,180},{(double) 1/6,(double) 1/6}},	
	{{19},{11,20},{90,0},{(double) 2/6,(double) 2/6}},
	{{20},{17,19,21,23},{90,180,0,270},{(double) 1/6,(double) 2/6,(double) 2/6,(double) 1/6}},	
	{{21},{14,20},{90,180},{(double) 2/6,(double) 2/6}},	
	{{22},{10,23},{90,0},{0.5,0.5}},
	{{23},{20,22,24},{90,180,0},{(double) 1/6,0.5,0.5}},		
	{{24},{15,23},{90,180},{0.5,0.5}}};

	private static final double[][][] TWELVESETUP=	
	{{{1},{2,4,10},{0,315,270},	{0.5,(double) Math.sqrt(2)/6,0.5}},
	{{2},{1,3,5},{180,0,270},	{0.5,0.5,(double) 1/6}},
	{{3},{2,6,15},{180,225,270},{0.5,(double) Math.sqrt(2)/6,0.5}},
	{{4},{1,5,7,11},{135,0,315,270},{(double) Math.sqrt(2)/6,(double) 2/6,(double) Math.sqrt(2)/6,(double) 2/6}},
	{{5},{2,4,6,8},{90,180,0,270},	{(double) 1/6,(double) 2/6,(double) 2/6,(double) 1/6}},
	{{6},{3,5,9,14},{45,180,225,270},{(double) Math.sqrt(2)/6,(double) 2/6,(double) Math.sqrt(2)/6,(double) 2/6}},
	{{7},{4,8,12},{135,0,270},{(double) Math.sqrt(2)/6,(double) 1/6,(double) 1/6}},  									 //12 Men's Morris Board 
	{{8},{5,7,9},{90,180,0},{(double) 1/6,(double) 1/6,(double) 1/6}},
	{{9},{6,8,13},{45,180,270},{(double) Math.sqrt(2)/6,(double) 1/6,(double) 1/6}},
	{{10},{1,11,22},{90,0,270},{0.5,(double) 1/6,0.5}},
	{{11},{4,10,12,19},{90,180,0,270},{(double) 2/6,(double) 1/6,(double) 1/6,(double) 2/6}},
	{{12},{7,11,16},{90,180,270},{(double) 1/6,(double) 1/6,(double) 1/6}},
	{{13},{9,14,18},{90,0,270},{(double) 1/6,(double) 1/6,(double) 1/6}},
	{{14},{6,13,15,21},{90,180,0,270},{(double) 2/6,(double) 1/6,(double) 1/6,(double) 2/6}},
	{{15},{3,14,24},{90,180,270},{0.5,(double) 1/6,0.5}},
	{{16},{12,17,19},{90,0,225},{(double) 1/6,(double) 1/6,(double) Math.sqrt(2)/6}},	
	{{17},{16,18,20},{180,0,270},{(double) 1/6,(double) 1/6,(double) 1/6}},	
	{{18},{13,17,21},{90,180,315},{(double) 1/6,(double) 1/6,(double) Math.sqrt(2)/6}},	
	{{19},{11,16,20,22},{90,45,0,225},{(double) 2/6,(double) Math.sqrt(2)/6,(double) 2/6,(double) Math.sqrt(2)/6}},
	{{20},{17,19,21,23},{90,180,0,270},{(double) 1/6,(double) 2/6,(double) 2/6,(double) 1/6}},	
	{{21},{14,18,20,24},{90,135,180,315},{(double) 2/6,(double) Math.sqrt(2)/6,(double) 2/6,(double) Math.sqrt(2)/6}},	
	{{22},{10,19,23},{90,45,0},{0.5,(double) Math.sqrt(2)/6,0.5}},
	{{23},{20,22,24},{90,180,0},{(double) 1/6,0.5,0.5}},		
	{{24},{15,21,23},{90,135,180},{0.5,(double) Math.sqrt(2)/6,0.5}}};

	private static int PIECENUMBER;
	private static String NAME;
	
	protected static Node[] nodes;
	public static ArrayList<double[]> NodeCoordinates = new ArrayList<double[]>();

	// Constructor for graph, set up for six mens morris
	public Graph() {
		this(SIXSETUP);
		PIECENUMBER=6;
		NAME="Six Men's Morris";
	}
	
	// Constructor with inputted setup size for "x" mens morris
	public Graph(double[][][] SETUP) {
		nodes= new Node[SETUP.length];
		for (int i = 0; i < SETUP.length; i++) {
			if (SETUP[i][2].length!=SETUP[i][3].length || SETUP[i][1].length!=SETUP[i][2].length){
				throw new IllegalArgumentException("Invalid Setup Array");
			}
			nodes[i] = new Node(SETUP[i][1], SETUP[i][2], SETUP[i][3]);
		}
	}
<<<<<<< HEAD
	/**
	 * get the size of the graph (16 in the case of six men's morris)
	 */
	public static int getGraphSize(){
		return nodes.length;
	}
	/**
	 * get the number of pieces allotted to each player (6 in the case of six men's morris)
	 */
	public int getPieceNumber(){
		return PIECENUMBER;
	}
	/**
	 * get the name of this game (eg. "Six Men's Morris")
	 */
	public String getName(){
		return NAME;
	}
	/**
	 * Get the array with the node numbers of all neighbors to the inputted node
	 */
=======
	
	//returns size of board
	public static int getGraphSize(){
		return nodes.length;
	}
	
	//returns number of pieces
	public int getPieceNumber(){
		return PIECENUMBER;
	}
	
	//returns title for game
	public String getName(){
		return NAME;
	}

	//returns neighbours for inputted node n
>>>>>>> origin/master
	public int[] getNeighbors(int n) {
		if (n<1 || n>getGraphSize()) throw new IllegalArgumentException("Node"+ n +" does not exist");
		return nodes[n - 1].getNeighbors();
	}
<<<<<<< HEAD
	/**
	 * Add a piece to the inputted node
	 */
	public static void addToken(Piece C, int n) {
		nodes[n - 1].addToken(C);
	}
	/**
	 * Remove a piece from the inputted node
	 */
	public void removeToken(int n){
		nodes[n-1].removeToken();
	}
	/**
	 * get the stack of all pieces on a particular node
	 */
	public ArrayList<Piece> getTokenStack(int n) {
		return nodes[n - 1].getTokenStack();
	}
	/**
	 * get the length between this node n and its neighbor s.
	 */
=======
	
	// adds token for input piece C on node n
	public static void addToken(Piece C, int n) {
		nodes[n - 1].addToken(C);
	}
	
	// removes token for piece on node n
	public void removeToken(int n){
		nodes[n-1].removeToken();
	}

	//returns token stack of input node n
	public ArrayList<Piece> getTokenStack(int n) {
		return nodes[n - 1].getTokenStack();
	}

	//returns neighbour length of input node n and element s
>>>>>>> origin/master
	public double getNeighborlength(int n, int s) {
		if (n<1 || n>getGraphSize()) throw new IllegalArgumentException("Node"+ n +" does not exist");
		return nodes[n - 1].getNeighborlength(s);
	}
<<<<<<< HEAD
	/**
	 * get the angle between this node n and its neighbor s.
	 */
=======
	
	//returns neighbour angle of input node n and element s 
>>>>>>> origin/master
	public int getNeighborangle(int n, int s) {
		return nodes[n - 1].getNeighborangle(s);
	}

}