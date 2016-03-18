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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Arrays;

/**
 * @author Asus
 *
 */
/**
 * @author Asus
 *
 */
public class GameModel extends Graph {

	private Piece selectedpiece = null;
	private Integer selectedpiecenode = null; // the node from where the
												// selected piece originates
	private ArrayList<Piece> pieces = new ArrayList<Piece>(); // all unplayed
																// pieces
	private Color activeplayer; // current player
	private String redstate; // state of the blue player ("place", "move",
								// "fly", "remove", "win")
	private String bluestate; // state of the red player ("place", "move",
								// "fly", "remove", "win")
	private int redcount; // the number of pieces that the red player has on the
							// board
	private int bluecount; // the number of pieces that the blue player has on
							// the board

	public String[] boardPlacement = new String[getGraphSize()];

	/**
	 * The constructor picks a random first player, and sets each player's
	 * "state" and the number of pieces in play (0).
	 */
	public GameModel() {
		Color[] players = { Color.RED, Color.BLUE };
		activeplayer = players[new Random().nextInt(2)];
		redstate = "place";
		redcount = 0;
		bluestate = "place";
		bluecount = 0;
	};

	/**
	 * Resets all the game variables to their start-of-game values
	 */
	public void modelReset() {
		Color[] players = { Color.RED, Color.BLUE };
		activeplayer = players[new Random().nextInt(2)];
		redstate = "place";
		bluestate = "place";
		redcount = 0;
		bluecount = 0;
		for (int i = 0; i < getGraphSize(); i++) {
			removeBoardPiece(i + 1);
		}
		pieces = new ArrayList<Piece>();
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

		if (oldstate == "remove") {
			if (activeplayer == Color.RED)
				bluecount--;
			else
				redcount--;

			if (!allPiecesPlaced(activeplayer))
				newstate = "place";
			else {
				newstate = "move";
				if (activeplayer == Color.RED && bluecount <= 2 || activeplayer == Color.BLUE && redcount <= 2)
					newstate = "win";
				// if (activeplayer==Color.RED && bluecount==3) bluestate="fly";
				// if (activeplayer==Color.BLUE && redcount==3) redstate="fly";
			}
		}

		else if (oldstate == "place") {
			if (activeplayer == Color.RED)
				redcount++;
			else
				bluecount++;
			if (allPiecesPlaced(activeplayer))
				newstate = "move";
			else
				newstate = "place";
		}

		else if (oldstate == "move")
			newstate = "move";

		else if (oldstate == "fly")
			newstate = "fly";

		if (oldstate == "place" || oldstate == "move" || oldstate == "fly") {
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
		if (opponentmovesAvailable() == false)
			newstate = "win";
		setState(newstate);
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

		if (enemystate == "move") {
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
	public void writeToSave(String saveState) {
		int[] stacks = checkStack();
		try {
			String filename = "save.txt";
			FileWriter fw = new FileWriter(filename);
			fw.write(saveState + System.lineSeparator());
			fw.write("Red: " + stacks[0] + System.lineSeparator());
			fw.write("Blue: " + stacks[1]);
			fw.close();
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		} catch (Exception e) {
			System.err.println("Exception Error: " + e.getMessage());
		}
	}

	/*
	 * If continue from save game button is pressed, it reads from the save file
	 * to place all the pieces back on the board as left before. Continues last
	 * saved game.
	 */
	public void readFromSave() {
		try {
			String filename = "save.txt";
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String[] positions = new String[getGraphSize()];
			String a = br.readLine();
			positions = a.substring(1, a.length() - 1).split(", ");
			String redSize = br.readLine().substring(5);
			String blueSize = br.readLine().substring(6);
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		} catch (Exception e) {
			System.err.println("Exception Error: " + e.getMessage());
		}
	}

	/*
	 * Sets inputed location in boardPlacement array
	 */
	public String[] setPosition(String containedPiece, int index) {
		if (containedPiece.equals("java.awt.Color[r=0,g=0,b=255]"))
			containedPiece = "blue";
		else if (containedPiece.equals("java.awt.Color[r=255,g=0,b=0]"))
			containedPiece = "red";
		boardPlacement[index - 1] = containedPiece;
		return boardPlacement;
	}

	/*
	 * Check number of red and blue pieces to be placed.
	 */
	public int[] checkStack() {
		int redStack = 0;
		int blueStack = 0;
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).toString().equals("com.aci.sixmensmorris.Piece@2c715800")) {
				blueStack++;
			} else if (pieces.get(i).toString().equals("com.aci.sixmensmorris.Piece@24867000")) {
				redStack++;
			}
		}
		int[] stacks = new int[2];
		stacks[0] = redStack;
		stacks[1] = blueStack;
		return stacks;
	}

}
