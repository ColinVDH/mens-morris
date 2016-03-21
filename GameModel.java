/*
GameModel provides information about the state of the game board and the red and blue pieces, 
gets errors of the game board state, 
selects first player.
*/
package com.aci.sixmensmorris;

import java.awt.Color;

import java.math.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.*;


public class GameModel extends Graph {


	private static Piece selectedpiece = null; 
	private static Integer selectedpiecenode = null; //the node from where the selected piece originates
	private static ArrayList<Piece> pieces = new ArrayList<Piece>(); //all unplayed pieces
	private static Color activeplayer; //current player
	private static String redstate; //state of the blue player ("place", "move", "fly", "remove", "win")
	private static String bluestate; //state of the red player  ("place", "move", "fly", "remove", "win")
	private static int redcountonboard; //the number of pieces that the red player has on the board
	private static int bluecountonboard; //the number of pieces that the blue player has on the board
	private static int redcountoffboard;
	private static int bluecountoffboard;
	private static ArrayList<Color[]> history =new ArrayList<Color[]>();



	/**
	 * The constructor picks a random first player, and sets each player's
	 * "state" and the number of pieces in play (0).
	 */
	public GameModel() {
		Color[] players = { Color.RED, Color.BLUE };
		activeplayer = players[new Random().nextInt(2)];
		redstate = "place";
		redcountonboard = 0;
		redcountoffboard=getPieceNumber();
		bluestate = "place";
		bluecountonboard = 0;
		bluecountoffboard = getPieceNumber();
	};

	/**
	 * Resets all the game variables to their start-of-game values
	 */
	public void modelReset() {
		Color[] players = { Color.RED, Color.BLUE };
		selectedpiece = null; 
		selectedpiecenode = null;
		activeplayer = players[new Random().nextInt(2)];
		redstate = "place";
		bluestate = "place";
		redcountonboard = 0;
		redcountoffboard=getPieceNumber();
		bluecountonboard = 0;
		bluecountoffboard = getPieceNumber();
		for (int i = 0; i < getGraphSize(); i++) {
			removeBoardPiece(i + 1);
		}

		pieces= new ArrayList<Piece>();
		history =new ArrayList<Color[]>();

	}

	public void changeActivePlayer() {
		if (activeplayer == Color.RED) {
			activeplayer = Color.BLUE;
		} else {
			activeplayer = Color.RED;
		}
	}

	public Color getActivePlayer() {
		return activeplayer;
	}

	/**
	 * Returns true if the player has finished placing all of his pieces on the
	 * board.
	 */
	public boolean allPiecesPlaced(Color c) {
		for (Piece p : pieces) {
			if (p.getColor() == c)
				return false;
		}
		return true;
	}

	public void setSelectedPiece(Piece p) {
		selectedpiece = p;
	}

	/**
	 * change x-coordinate of selected piece
	 */
	public void setSelectedPieceX(double x) {
		selectedpiece.x = x;
	}

	/**
	 * change y-coordinate of selected piece
	 */
	public void setSelectedPieceY(double y) {
		selectedpiece.y = y;
	}

	public Piece getSelectedPiece() {
		return selectedpiece;
	}

	/**
	 * get the number of the board location that the selected piece is on.
	 * Returns null if the selected piece is off the board.
	 */
	public Integer getSelectedPieceNode() {
		return selectedpiecenode;
	}

	public void setSelectedPieceNode(Integer i) {
		selectedpiecenode = i;
	}

	public ArrayList<Piece> getPieces() {
		return pieces;
	}
	public int getRedStack(){
		return redcountoffboard;
	}
	
	public int getBlueStack(){
		return bluecountoffboard;
	}
	/**
	 * Remove a piece from the array of unplayed pieces, at a particular index.
	 */
	public void removePiece(int index) {
		pieces.remove(index);
	}

	/**
	 * Add a piece to the array of unplayed pieces (used for board set-up in the
	 * view).
	 */
	public void addPiece(Piece p) {
		pieces.add(p);
	}

	/**
	 * Remove a piece from a particular board loaction.
	 */
	public void removeBoardPiece(int n) {
		if (!getTokenStack(n).isEmpty())
			removeToken(n);
	}

	/**
	 * This function returns an array of all the nodes that the active player is
	 * allowed to click, given its current state (for "remove", it is all nodes
	 * with opponent pieces that are allowed to be removed, for "place", "move",
	 * or "fly", it is all nodes that the selected piece can move to.
	 */
	public ArrayList<Integer> getValidNodes() {
		ArrayList<Integer> validnodes = new ArrayList<Integer>();
		Color enemy;
		if (activeplayer == Color.RED)
			enemy = Color.BLUE;
		else
			enemy = Color.RED;
		if (getState().equals("remove")) {
			for (int i = 0; i < getGraphSize(); i++) {
				if (!getTokenStack(i + 1).isEmpty() && getTokenStack(i + 1).get(0).getColor() == enemy) {
					validnodes.add(i + 1); // initially set all nodes to be
											// valid
				}
			}
			for (int i = 0; i < getGraphSize(); i++) {
				if (!getTokenStack(i + 1).isEmpty() && getTokenStack(i + 1).get(0).getColor() == enemy) {
					for (int n : getNeighbors(i + 1)) {
						if (!getTokenStack(n).isEmpty() && getTokenStack(n).get(0).getColor() == enemy) {
							for (int nn : getNeighbors(n)) {
								if ((nn != i + 1)
										&& (Math.abs(getNeighborangle(i + 1, n) - getNeighborangle(n, nn)) == 180
												|| Math.abs(getNeighborangle(i + 1, n) - getNeighborangle(n, nn)) == 0)
										&& (!getTokenStack(nn).isEmpty()
												&& getTokenStack(nn).get(0).getColor() == enemy)) {
									for (int j = 0; j < validnodes.size(); j++) {
										if (validnodes.get(j) == nn || validnodes.get(j) == n
												|| validnodes.get(j) == i + 1) {
											validnodes.remove(j);
											System.out.println("remove node");
										}
									}
								}
							}
						}
					}
				}
			}
			if (validnodes.isEmpty()) {
				for (int i = 0; i < getGraphSize(); i++) {
					validnodes.add(i + 1);
				}
			}
		}

		else {
			if ((getState().equals("place") && selectedpiecenode == null) || getState().equals("fly")) { // if
																											// out
																											// of
																											// play
																											// piece;
				for (int i = 0; i < getGraphSize(); i++) {
					if (getTokenStack(i + 1).isEmpty()) {
						validnodes.add(i + 1);
					}
				}
			} else if (getState().equals("move")) { // piece on board
				int[] neighbors = getNeighbors(selectedpiecenode);
				for (int node : neighbors) {
					if (getTokenStack(node).isEmpty()) {
						validnodes.add(node);
					}
				}
			}
		}
		return validnodes;
	}

	/**
	 * Returns the state of the active player
	 */
	public String getState() {
		if (activeplayer == Color.RED)
			return redstate;
		else
			return bluestate;
	}

	/**
	 * sets the state of the active player
	 */
	public void setState(String state) {
		if (activeplayer == Color.RED)
			redstate = state;
		else
			bluestate = state;
	}

	/**
	 * This function changes the state of the active player, and according to
	 * its current state and the clicked node.
	 */
	public void changeState(Integer clickednode) {
		String oldstate = getState();
		String newstate = "";
		ArrayList<Integer> angles = new ArrayList<Integer>();

		if (oldstate.equals("remove")) {
			if (activeplayer == Color.RED)
				bluecountonboard--;
			else
				redcountonboard--;

			if (!allPiecesPlaced(activeplayer))
				newstate = "place";
			else {
				newstate = "move";
				if (activeplayer == Color.RED && bluecountonboard <= 2 || activeplayer == Color.BLUE && redcountonboard <= 2)
					newstate = "win";
				// if (activeplayer==Color.RED && bluecount==3) bluestate="fly";
				// if (activeplayer==Color.BLUE && redcount==3) redstate="fly";
			}
		}

		else if (oldstate.equals("place")) {
			if (activeplayer == Color.RED){
				redcountonboard++;
				redcountoffboard--;
			}
			else {
				bluecountonboard++;
				bluecountoffboard--;
			}
			if (allPiecesPlaced(activeplayer))
				newstate = "move";
			else
				newstate = "place";
		}

		else if (oldstate.equals("move"))
			newstate = "move";

		else if (oldstate.equals("fly"))
			newstate = "fly";

		if (oldstate.equals("place") || oldstate.equals("move") || oldstate.equals("fly")) {
			for (int n : getNeighbors(clickednode)) {
				if (!getTokenStack(n).isEmpty() && getTokenStack(n).get(0).getColor() == activeplayer) {
					if (angles.isEmpty())
						angles.add(getNeighborangle(clickednode, n));
					else {
						for (int angle : angles) {
							if (Math.abs(getNeighborangle(clickednode, n) - angle) == 180
									|| Math.abs(getNeighborangle(clickednode, n) - angle) == 0) {
								newstate = "remove";
							}
						}
					}
					for (int nn : getNeighbors(n)) {
						if (nn != clickednode) {
							if (!getTokenStack(nn).isEmpty() && getTokenStack(nn).get(0).getColor() == activeplayer) {
								if (Math.abs(getNeighborangle(clickednode, n) - getNeighborangle(n, nn)) == 180
										|| Math.abs(getNeighborangle(clickednode, n) - getNeighborangle(n, nn)) == 0) {
									newstate = "remove";
								}
							}
						}
					}
				}
			}
		}
		if (opponentmovesAvailable()==false) newstate="win";
		else if (repeatedPosition()) newstate="draw";
		setState(newstate);
	}

	public void clearHistory(){
		history= new ArrayList<Color[]>();
	}
	
	public void updateHistory(){
		Color[] currentgraph=new Color[getGraphSize()];
		for (int i=0;i<getGraphSize();i++){
			if (nodes[i].getTokenStack().isEmpty()) currentgraph[i]=null;
			else currentgraph[i]=nodes[i].getTokenStack().get(0).getColor();
		}
		history.add(currentgraph);
		System.out.println("ADDED");
	}
	
	public boolean repeatedPosition(){
		if (getState()!="place" && getState()!="remove"){
			for (int i=0;i<history.size();i++){
				int counter=0;
				Color[] oldgraph=history.get(i);
				for (int j=0;j<getGraphSize(); j++){
					if ((oldgraph[j]==null && nodes[j].getTokenStack().isEmpty())
							|| (oldgraph[j]!=null && !nodes[j].getTokenStack().isEmpty() 
							&& oldgraph[j]==nodes[j].getTokenStack().get(0).getColor())){
						counter+=1;
					}
					System.out.println(counter);
				}
				if (counter==getGraphSize()) return true;
			}
		}
		return false;
	}
	




	/**
	 * Checks to see if the opponent has any legal moves available. A player can
	 * only run out of moves if his state is "move" since he is limited to
	 * adjacent locations. If his state is "place" or "fly" there will always be
	 * a valid empty location to place a piece.
	 */
	public boolean opponentmovesAvailable() {
		Color enemy;
		String enemystate;
		if (activeplayer == Color.RED) {
			enemy = Color.BLUE;
			enemystate = bluestate;
		} else {
			enemy = Color.RED;
			enemystate = redstate;
		}

		if (enemystate.equals("move")) {
			for (int i = 0; i < getGraphSize(); i++) {
				if (!getTokenStack(i + 1).isEmpty() && getTokenStack(i + 1).get(0).getColor() != activeplayer) {
					for (int n : getNeighbors(i + 1)) {
						if (getTokenStack(n).isEmpty()) {
							return true;
						}
					}
				}
			}
			return false;
		}
		return true;
	}

	
	/*
	 * If save button is pressed in game, this function is called. It writes all
	 * the current location of all pieces to the save file. Only allows one save
	 * game state at a time.
	 */
	public void writeToSave() {
		try {
			// opens a PrintStream for each of the files
			PrintStream save = new PrintStream(new File("save.txt"));
			String boardstring="";
			for (int i=0; i<getGraphSize();i++) {
				if (!getTokenStack(i+1).isEmpty() && getTokenStack(i+1).get(0).getColor()==Color.RED) boardstring=boardstring+"red"+",";
				else if (!getTokenStack(i+1).isEmpty() && getTokenStack(i+1).get(0).getColor()==Color.BLUE) boardstring=boardstring+"blue"+",";
				else boardstring=boardstring+"null"+",";
			}
			
			boardstring=boardstring.substring(0,boardstring.length()-1);
			
			save.println(boardstring); // close the PrintStreams
			if (activeplayer==Color.RED) save.println("red");
			else save.println("blue");  
			save.println(redstate);
			save.println(redcountoffboard);
			save.println(bluestate);
			save.println(bluecountoffboard);
			for (Color[] carray: history) {
				String historystring="";
				for (Color c: carray){
					if (c==Color.RED) historystring=historystring+"red"+",";
					else if (c==Color.BLUE) historystring=historystring+"blue"+",";
					else historystring=historystring+"null"+",";
				}
				historystring=historystring.substring(0,historystring.length()-1);
				save.println(historystring);
			}
			save.close();
		} catch (FileNotFoundException e) {// This block should not be reached unless an access error has occured
				e.printStackTrace();
		} 
	}



	
	
	/*
	 * If continue from save game button is pressed, it reads from the save file
	 * to place all the pieces back on the board as left before. Continues last
	 * saved game.
	 */
	public void readFromSave(ArrayList<double[]> nodecoordinates, int pieceradius) throws FileNotFoundException{
		
		Scanner input = new Scanner(new FileReader("save.txt"));
		pieces= new ArrayList<Piece>();
		history =new ArrayList<Color[]>();
		for (int i = 0; i < getGraphSize(); i++) {
			removeBoardPiece(i + 1);
		}
		redcountonboard=0;
		bluecountonboard=0;
			int line=0;
			while (input.hasNextLine()){
				System.out.println(line);
				if (line==0){
					String fileboardstring=input.nextLine();
					String [] fileboardarray = fileboardstring.split(",");
					for (int i=0;i<fileboardarray.length;i++){
						if (fileboardarray[i].equals("red")) {
							addToken(new Piece(Color.RED, nodecoordinates.get(i)[0]-pieceradius/2 , nodecoordinates.get(i)[1]-pieceradius/2 , pieceradius), i+1);
							redcountonboard++;
						}
						else if (fileboardarray[i].equals("blue")){
							System.out.println(i);
							addToken(new Piece(Color.BLUE, nodecoordinates.get(i)[0]-pieceradius/2 , nodecoordinates.get(i)[1]-pieceradius/2  , pieceradius), i+1);
							bluecountonboard++;
						}
						
					}
					line++;
				}
				else if (line==1){
					String fileactiveplayer=input.nextLine();
					if (fileactiveplayer.equals("red")) activeplayer=Color.RED;
					else activeplayer=Color.BLUE;
					line++;
				}
				else if (line==2){
					redstate=input.nextLine();
					line++;
				}
				else if (line==3){
					redcountoffboard=Integer.parseInt(input.nextLine());
					line++;
				}
				
				else if (line==4){
					bluestate=input.nextLine();
					line++;
				}
				else if (line==5){
					bluecountoffboard=Integer.parseInt(input.nextLine());;
					line++;
				}
				else{
					String filehistorystring=input.nextLine();
					String [] filehistoryarray = filehistorystring.split(",");
					Color[] historyarray=new Color[filehistoryarray.length];
				
					for (int i=0;i<filehistoryarray.length;i++){
						if (filehistoryarray[i].equals("red")) {
							historyarray[i]=Color.RED;
						}
						else if (filehistoryarray[i].equals("blue")){
							historyarray[i]=Color.BLUE;
						}
						else historyarray[i]=null;
					}
					history.add(historyarray);
				}
		}
		input.close();// close the scanner at the end
	}

}
