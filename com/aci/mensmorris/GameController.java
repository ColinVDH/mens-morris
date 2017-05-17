/*
GameController registers as a listener to the view, 
responds to user input (mouse clicks, button presses) 
by modifying the data in the model if necessary, 
and then calls the view to update the GUI. 
*/

package com.aci.sixmensmorris;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import javax.swing.Timer;


public class GameController implements MouseListener, ActionListener {
	private static GameModel model;
	private GameView view;
	
	private Timer timer; 
	private Timer timer2;
	private Timer timer0;
	private int[] move;

	/**
	 * controller construbtor inializes the main menu and keeps reference to model and view. 
	 */
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
		if (command == "Save Game") {   //save button pressed
			model.writeToSave(); //write to save
			view.notification("Game progress saved"); //display notification informing user
		}
		else if (command == "Continue from Save") { //continue from save pressed (main menu)
			try{
				String gameMode = model.readLineFromFile("save.txt",0);
				if (gameMode.equals("six") || gameMode.equals("nine") || gameMode.equals("twelve")) {
					model.setGraph(gameMode);
				}
				view.setNodeCoordinates();
				model.readFromSave(view.NodeCoordinates, view.PIECERADIUS); //read from save
				view.viewReset();
				view.initGameWindow(false); //initialize game window (false-- no mode selection)
			}

			catch (FileNotFoundException e){
				e.printStackTrace();
				view.notification("Error: No save file available!");	
			}
			catch(NullPointerException e){
				e.printStackTrace();
				view.notification("Error: Unable to load save file!");
			}
			
		}

		else if (command == "Six Men's Morris"){
			model.setGraph("six");
			view.setNodeCoordinates();
			model.modelInit();
			view.viewInit();
			view.initGameWindow(true); // calls view to create Set Pieces window
		}

		else if (command == "Nine Men's Morris"){
			model.setGraph("nine");
			view.setNodeCoordinates();
			model.modelInit();
			view.viewInit();
			view.initGameWindow(true); // calls view to create Set Pieces window
		}

		else if (command == "Twelve Men's Morris"){
			model.setGraph("twelve");
			view.setNodeCoordinates();
			model.modelInit();
			view.viewInit();
			view.initGameWindow(true); // calls view to create Set Pieces window
		}

		else if (command=="Player vs. Computer"){
			model.setComputerMode(true);
			view.closeModeFrame();   //close the frame containing mode selection
			if (model.isComputerMode() && model.getActivePlayer()==model.getComputerColor()) mousePressed(null); //if the computer goes first, you have to "pretend" that there has been a click, 
																													//because the computer always does its moves in response to user action
		}
		
		else if (command=="Player vs. Player"){
			model.setComputerMode(false);
			view.closeModeFrame();//close the frame containing mode selection
		}

		else if (command=="Return to Main Menu"){
			view.closeGame(); //close game window / game over frame
			model.modelReset(); //reset model variables to start-of-game state
			view.initMenu();//open up the main menu
			
			
		}
	}

	



	/* (non-Javadoc)
	 * The procedure if a mouse has been clicked on the game screen
	 */
	public void mousePressed(MouseEvent me) {
		
		
		boolean unselect=false; //unselect the current piece
		boolean changestate=false; //change the state of the active player
		int clickednode=0; //the node clicked (default 0)
		if (!model.isComputerMode() || (model.isComputerMode() && model.getActivePlayer()!=model.getComputerColor())){
		for (int i = 0; i < model.graph.getGraphSize(); i++) {
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
					model.setSelectedPieceX(view.boardnodes.get(clickednode-1).getX()); //change selected piece coordinates
					model.setSelectedPieceY(view.boardnodes.get(clickednode-1).getY());
					model.graph.addToken(model.getSelectedPiece(), clickednode); //add piece to the board location
					changestate=true;  

				
				}		
			
				if (model.getState().equals("move") || model.getState().equals("fly")){
					model.setSelectedPieceX(view.boardnodes.get(clickednode-1).getX()); //change selected piece coordinates
					model.setSelectedPieceY(view.boardnodes.get(clickednode-1).getY()); 
					model.graph.addToken(model.getSelectedPiece(), clickednode); //add piece to the board location
					model.graph.removeToken(model.getSelectedPieceNode()); //remove piece from previous board location
					changestate=true;
				

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
			if (clickednode!=0 && !model.graph.getTokenStack(clickednode).isEmpty() && model.graph.getTokenStack(clickednode).get(0).getColor()!=model.getActivePlayer()){ //the clicked node is occupied by opponent.
				if (model.getValidNodes().contains(clickednode)){ //the clicked node is valid to remove
					model.graph.removeToken(clickednode); //remove the opponent piece.
					changestate=true;
					model.clearHistory(); //the can clear the history each time the number of pieces on the board changes (eg. piece removed).

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
		else if (!model.graph.getTokenStack(clickednode).isEmpty() && model.graph.getTokenStack(clickednode).get(0).getColor()==model.getActivePlayer()){ //selects one of his pieces on the board.
			model.setSelectedPiece(model.graph.getTokenStack(clickednode).get(0)); //set selected piece
			model.setSelectedPieceNode(clickednode); //set selected piece node.
		}
	
		
		}
	}
		
	if (changestate) model.changeState(clickednode); //change state if necessary
	if (changestate && (model.getState().equals("move")  || model.getState().equals("fly"))) model.updateHistory(); //add a snapshot of the current board. This is used to check for a "draw"
	if (changestate && (model.getState().equals("place") || model.getState().equals("move")  || model.getState().equals("fly"))) model.changeActivePlayer(); //change active player if necessary
	view.updateState(); //update how the state is presented in the view.
	view.repaintPieces(); // repaint the pieces on the board.
	
	
	
	
	
	
	timer0=new Timer(1, new ActionListener(){  
		public void actionPerformed(ActionEvent e) { //when the timer is activated (BELOW), the following block is executed
			
			move = model.getComputerMove(); //*****get computer move*******
			view.showThinking(false); //once move is selected, remove "thinking" text
			if (move[0]!=0){ //If first integer of move is not 0, the selected piece is coming from an on-board location.
				model.setSelectedPiece(model.graph.getTokenStack(move[0]).get(0)); //set selected piece
				model.setSelectedPieceNode(move[0]); //set node where the selected piece originates.
			}
			else{ //selected piece coming from off board
				for (Piece p: model.getPieces()){
					if (p.getColor().equals(model.getComputerColor())){ //pick any off-board piece that is your color
						model.setSelectedPiece(p);//set selected piece
						break;
					}
				}
			}
			view.updateState(); 
			view.repaintPieces();

			timer.setRepeats(false);
			timer.start(); //start the next timer 

		}
	});
	
	timer = new Timer(1000, new ActionListener() {   //timer goes off 1 second after start()
            public void actionPerformed(ActionEvent e) {
        		
            	int clickednode2=move[1];  //store the node that the selected piece is moving to. 
            	if (move[0]==0){ //piece coming from off board
    				model.removePiece(model.getPieces().indexOf(model.getSelectedPiece())); //remove piece from the unplayed pieces array
    			}
    			else{//piece coming from on-board
    				model.graph.removeToken(model.getSelectedPieceNode()); //remove piece from previous board location
    			}
            	model.setSelectedPieceX(view.boardnodes.get(clickednode2-1).getX()); //change selected piece coordinates
    			model.setSelectedPieceY(view.boardnodes.get(clickednode2-1).getY());
    			model.graph.addToken(model.getSelectedPiece(), clickednode2); //add piece to the board location
    			boolean changestate2=true;  
    			
    			model.setSelectedPiece(null);   //"unselect"
    			model.setSelectedPieceNode(null);  //"unselect"
    			
    			if (changestate2) model.changeState(clickednode2); //change state if necessary
    			if (changestate2 && (model.getState().equals("move")  || model.getState().equals("fly"))) model.updateHistory(); //add a snapshot of the current board. This is used to check for a "draw"
    			if (changestate2 && (model.getState().equals("place") || model.getState().equals("move")  || model.getState().equals("fly"))) model.changeActivePlayer(); //change active player if necessary
    			view.updateState(); //update how the state is presented in the view.
    			view.repaintPieces(); // repaint the pieces on the board.
    			
    			if (move[2]!=0){   //this moves involves a piece removal 
    				timer2.setRepeats(false);  
    				timer2.start(); //start last timer
    			}
    			else view.frame2.setEnabled(true); //make the board clickable again
    		
    			
            }
         });
	
	timer2 = new Timer(1000, new ActionListener() {   //timer goes off after 1 second
        public void actionPerformed(ActionEvent e) {
        	int clickednode3=move[2];
        	model.graph.removeToken(clickednode3); //remove the opponent piece.
			boolean changestate3=true;
			model.clearHistory(); //the can clear the history each time the number of pieces on the board changes (eg. piece removed).

			
			if (changestate3) model.changeState(clickednode3); //change state if necessary
			if (changestate3 && (model.getState().equals("move")  || model.getState().equals("fly"))) model.updateHistory(); //add a snapshot of the current board. This is used to check for a "draw"
			if (changestate3 && (model.getState().equals("place") || model.getState().equals("move")  || model.getState().equals("fly"))) model.changeActivePlayer(); //change active player if necessary
			view.updateState(); //update how the state is presented in the view.
			view.repaintPieces(); // repaint the pieces on the board.
			view.frame2.setEnabled(true);
			
			
        }
     });

	
	
	if (model.isComputerMode() && model.getActivePlayer()==model.getComputerColor() && !model.getState().equals("draw") && !model.getState().equals("win")){ //if its the computer's turn to play and the game isn't over
		view.showThinking(true); //show "thinking" text 
		view.frame2.setEnabled(false); //make the board unclickable
		timer0.setRepeats(false); //timer only goes once
		timer0.start(); //start timer. All computer moves are performed with timers to coordinate when certain processes take effect. 
	}
		
		
		
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
