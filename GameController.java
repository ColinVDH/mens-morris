/*
GameController registers as a listener to the view, 
responds to user input (mouse clicks, button presses) 
by modifying the data in the model if necessary, 
and then calls the view to update the GUI. 
*/

package com.aci.sixmensmorris;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class GameController implements MouseListener, ActionListener {
	private static GameModel model;
	private GameView view;

	// constructor
	public GameController(GameModel m, GameView v) {
		model = m;
		view = v;
		view.initMenu(); // initialize the view main menu
	}

	
	@Override
	/* (non-Javadoc)
	 * Action if a particular JButton is pressed.
	 */
	public void actionPerformed(ActionEvent ae) {
		String command = ae.getActionCommand();
		if (command == "Save Game") {
			model.writeToSave(Arrays.deepToString(model.boardPlacement));
		}
		else if (command == "Continue from Save") {
			model.readFromSave();    
		} 
		else if (command == "New Game") {
			view.initGameWindow(); // calls view to create Set Pieces window
		}
		else if (command=="Return to Main Menu"){
			view.closeGame();
			model.modelReset();
			view.viewReset();
			view.initMenu();
			
		}

	}

	
	/* (non-Javadoc)
	 * The procedure if a mouse has been clicked on the game screen
	 */
	public void mousePressed(MouseEvent me) {
		boolean unselect=false; //unselect the current piece
		boolean changestate=false; //change the state of the active player
		boolean changeplayer=false; //change the active player
		int clickednode=0; //the node clicked (default 0)
		for (int i = 0; i < model.getGraphSize(); i++) {
			if (view.boardnodes.get(i).contains(me.getPoint())) {
				clickednode=i+1;
			}
		}

	if (model.getSelectedPiece()!=null){ //if a piece is currently selected
		if (model.getSelectedPiece().contains(me.getPoint())){  //if the same piece is clicked again
			unselect=true; //unselect the current piece
			
		}
		else if (clickednode!=0){ //if a board location has been clicked
			if (model.getValidNodes().contains(clickednode)){  //If a valid location is clicked
				if (model.getState().equals("place")){  //if the state is "place"
					model.removePiece(model.getPieces().indexOf(model.getSelectedPiece())); //remove piece from the unplayed pieces array
					model.setSelectedPieceX(view.boardnodes.get(clickednode-1).x); //change selected piece coordinates
					model.setSelectedPieceY(view.boardnodes.get(clickednode-1).y);
					model.addToken(model.getSelectedPiece(), clickednode); //add piece to the board location
					changestate=true;  
					//SAVE STUFF
					model.setPosition(model.getActivePlayer().toString(), clickednode);

				
				}		
			
				if (model.getState().equals("move") || model.getState().equals("fly")){
					model.setSelectedPieceX(view.boardnodes.get(clickednode-1).x); //change selected piece coordinates
					model.setSelectedPieceY(view.boardnodes.get(clickednode-1).y); 
					model.addToken(model.getSelectedPiece(), clickednode); //add piece to the board location
					model.removeToken(model.getSelectedPieceNode()); //remove piece from previous board location
					changestate=true;
					//SAVE STUFF
					model.setPosition(model.getActivePlayer().toString(), clickednode);

				}
			}
			else view.notification("Illegal Move!"); //If an invalid board location has been clicked.
			unselect=true; //unselect current piece either way (valid or invalid node clicked). 
		}

		if (unselect){
			model.setSelectedPiece(null); //select piece is set to null.
			model.setSelectedPieceNode(null); //select piece node is set to null.
		}
		
	}
	
	else { //If there is currently no piece selected
		if (model.getState().equals("remove")){ //If the active player must remove an enemy piece.
			if (clickednode!=0 && !model.getTokenStack(clickednode).isEmpty() && model.getTokenStack(clickednode).get(0).getColor()!=model.getActivePlayer()){ //the clicked node is occupied by opponent.
				if (model.getValidNodes().contains(clickednode)){ //the clicked node is valid to remove
					model.removeToken(clickednode); //remove the opponent piece.
					changestate=true;
					//SAVE STUFF
					model.setPosition("null", clickednode);

				}
				else {
					view.notification("Illegal Move!"); //Will only be invalid in the case that it is part of a mill.
				}
			}
		}
		
		else if (clickednode==0){
			for (Piece piece : model.getPieces()) {
				if (piece.contains(me.getPoint()) && model.getActivePlayer() == piece.getColor()) { // clicked unplayed piece
					model.setSelectedPiece(piece); //set selected piece
					break;
				}	
			}
		}
		else if (!model.getTokenStack(clickednode).isEmpty() && model.getTokenStack(clickednode).get(0).getColor()==model.getActivePlayer()){ //selects one of his pieces on the board.
			model.setSelectedPiece(model.getTokenStack(clickednode).get(0)); //set selected piece
			model.setSelectedPieceNode(clickednode); //set selected piece node.
		}
	
		
	}
	if (changestate) model.changeState(clickednode); //change state if necessary
	if (changestate && model.getState()!="win" && model.getState()!="remove") model.changeActivePlayer(); //change active player if necessary
	view.updateState(); //update how the state is presented in the view.
	view.repaintPieces(); // repaint the pieces on the board.

}



	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
