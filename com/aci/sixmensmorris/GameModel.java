/*
GameModel provides information about the state of the game board and the red and blue pieces, 
gets errors of the game board state, 
selects first player.
*/
package com.aci.sixmensmorris;

import java.awt.Color;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.*;
import java.util.prefs.Preferences;
import java.io.InputStream;


public class GameModel {

	public static Graph graph;
	private static Piece selectedpiece = null; 
	private static Integer selectedpiecenode = null; //the node from where the selected piece originates
	private static ArrayList<Piece> pieces = new ArrayList<Piece>(); //all unplayed pieces
	private static Color activeplayer; //current player
	private static String redstate; //state of the blue player ("place", "move", "fly", "remove", "win")
	private static String bluestate; //state of the red player  ("place", "move", "fly", "remove", "win")
	public static int redcountonboard; //the number of pieces that the red player has on the board
	public static int bluecountonboard; //the number of pieces that the blue player has on the board
	public static int redcountoffboard;
	public static int bluecountoffboard;
	public static ArrayList<Color[]> history =new ArrayList<Color[]>();
	private static boolean computermode;
	private static Color computercolor;
	private int MCTS_ITERATIONS=10000;
	private static String mode;
	static String s="/com/aci/sixmensmorris";



	/**
	 * The constructor picks a random first player, and sets each player's
	 * "state" and the number of pieces in play (0).
	 */
	public GameModel() {
		Color[] players = { Color.RED, Color.BLUE };
		computercolor= players[new Random().nextInt(2)];
		activeplayer = players[new Random().nextInt(2)];
		redstate = "place";
		redcountonboard = 0;
		bluestate = "place";
		bluecountonboard = 0;
	};

	public void modelInit(){
		redcountoffboard = graph.getPieceNumber();
		bluecountoffboard = graph.getPieceNumber();
	}
	/**
	 * Resets all the game variables to their start-of-game values
	 */

	public void setGraph(String model){
		graph = new Graph(model);
	}
	
	public void modelReset() {
		Color[] players = { Color.RED, Color.BLUE };
		selectedpiece = null; 
		selectedpiecenode = null;
		activeplayer = players[new Random().nextInt(2)];
		computercolor= players[new Random().nextInt(2)];

		redstate = "place";
		bluestate = "place";
		redcountonboard = 0;
		redcountoffboard= graph.getPieceNumber();
		bluecountonboard = 0;
		bluecountoffboard = graph.getPieceNumber();
		for (int i = 0; i < graph.getGraphSize(); i++) {
			removeBoardPiece(i + 1);
		}

		pieces= new ArrayList<Piece>();
		history =new ArrayList<Color[]>();

	}
	/**
	 * Changes active player from red to blue, and vice versa
	 */
	public void changeActivePlayer() {
		if (activeplayer == Color.RED) {
			activeplayer = Color.BLUE;
		} else {
			activeplayer = Color.RED;
		}
	}
	/**
	 * Returns active player (red or blue). 
	 */
	public Color getActivePlayer() {
		return activeplayer;
	}
	
	/**
	 * Set the piece that has been selected by user
	 */
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
	/**
	 * Get the currently selected piece
	 */
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
	/**
	 * store the node where the selected piece is located
	 */
	public void setSelectedPieceNode(Integer i) {
		selectedpiecenode = i;
	}
	/**
	 * get all unplayed pieces in an arraylist
	 */
	public ArrayList<Piece> getPieces() {
		return pieces;
	}
	/**
	 * get the number of unplayed red pieces
	 */
	public int getRedStack(){
		return redcountoffboard;
	}
	/**
	 * get the number of unplayed blue pieces
	 */
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
		if (!graph.getTokenStack(n).isEmpty())
			graph.removeToken(n);
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
			for (int i = 0; i < graph.getGraphSize(); i++) {
				if (!graph.getTokenStack(i + 1).isEmpty() && graph.getTokenStack(i + 1).get(0).getColor() == enemy) {
					validnodes.add(i + 1); // initially set all nodes to be
											// valid
				}
			}
			for (int i = 0; i < graph.getGraphSize(); i++) {
				if (!graph.getTokenStack(i + 1).isEmpty() && graph.getTokenStack(i + 1).get(0).getColor() == enemy) {
					for (int n : graph.getNeighbors(i + 1)) {
						if (!graph.getTokenStack(n).isEmpty() && graph.getTokenStack(n).get(0).getColor() == enemy) {
							for (int nn : graph.getNeighbors(n)) {
								if ((nn != i + 1)
										&& (Math.abs(graph.getNeighborangle(i + 1, n) - graph.getNeighborangle(n, nn)) == 180
												|| Math.abs(graph.getNeighborangle(i + 1, n) - graph.getNeighborangle(n, nn)) == 0)
										&& (!graph.getTokenStack(nn).isEmpty()
												&& graph.getTokenStack(nn).get(0).getColor() == enemy)) {
									for (int j = 0; j < validnodes.size(); j++) {
										if (validnodes.get(j) == nn || validnodes.get(j) == n
												|| validnodes.get(j) == i + 1) {
											validnodes.remove(j);
											
										}
									}
								}
							}
						}
					}
				}
			}
			if (validnodes.isEmpty()) {
				for (int i = 0; i < graph.getGraphSize(); i++) {
					validnodes.add(i + 1);
				}
			}
		}

		else {
			if ((getState().equals("place") && selectedpiecenode == null) || getState().equals("fly")) { // if out of play piece;
																											
																											
				for (int i = 0; i < graph.getGraphSize(); i++) {
					if (graph.getTokenStack(i + 1).isEmpty()) {
						validnodes.add(i + 1); 
					}
				}
			} else if (getState().equals("move")) { // piece on board
				int[] neighbors = graph.getNeighbors(selectedpiecenode);
				for (int node : neighbors) {
					if (graph.getTokenStack(node).isEmpty()) {
						validnodes.add(node);
					}
				}
			}
		}
		return validnodes;
	}
	/**
	 * Turn computer mode on or off.
	 */
	public void setComputerMode(boolean b){   
		computermode=b;
	}
	
	/**
	 * Checks if computer mode is active
	 */
	public boolean isComputerMode(){
		return computermode;
	}
	
	/**
	 * Get the color of the computer player
	 */
	public Color getComputerColor(){
		return computercolor;
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
	private void setState(String state) {
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
				bluecountonboard--; //decrement the blue pieces on board
			else
				redcountonboard--; //decrement the red pieces on board

			if ((redcountoffboard!=0 && activeplayer==Color.RED) || (bluecountoffboard!=0 && activeplayer==Color.BLUE))
				newstate = "place";
			else {
				newstate = "move";
				if ((activeplayer == Color.RED && bluecountonboard <= 2 && bluecountoffboard==0)
					|| (activeplayer == Color.BLUE && redcountonboard <= 2 && redcountoffboard==0))
					newstate = "win";
				 if (activeplayer==Color.RED && bluecountonboard==3 && !graph.MODE.equals("six")) bluestate="fly";
				 if (activeplayer==Color.BLUE && redcountonboard==3 && !graph.MODE.equals("six")) redstate="fly";
			}
		}

		else if (oldstate.equals("place")) {
			if (activeplayer == Color.RED){
				redcountonboard++;  //incremend red pieces on board
				redcountoffboard--;  //decrement red pieces off board
			}
			else {
				bluecountonboard++; //incremend blue pieces on board
				bluecountoffboard--; //decrement blue pieces off board
			}
			if ((redcountoffboard==0 && activeplayer==Color.RED) || (bluecountoffboard==0 && activeplayer==Color.BLUE))
				newstate = "move";
			else
				newstate = "place";
		}

		else if (oldstate.equals("move"))
			newstate = "move";

		else if (oldstate.equals("fly")) 
			newstate = "fly";

		if (oldstate.equals("place") || oldstate.equals("move") || oldstate.equals("fly")) {
			for (int n : graph.getNeighbors(clickednode)) {
				if (!graph.getTokenStack(n).isEmpty() && graph.getTokenStack(n).get(0).getColor() == activeplayer) {
					if (angles.isEmpty())
						angles.add(graph.getNeighborangle(clickednode, n));
					else {
						for (int angle : angles) {
							if (Math.abs(graph.getNeighborangle(clickednode, n) - angle) == 180
									|| Math.abs(graph.getNeighborangle(clickednode, n) - angle) == 0) {
								newstate = "remove";
							}
						}
					}
					for (int nn : graph.getNeighbors(n)) {
						if (nn != clickednode) {
							if (!graph.getTokenStack(nn).isEmpty() && graph.getTokenStack(nn).get(0).getColor() == activeplayer) {
								if (Math.abs(graph.getNeighborangle(clickednode, n) - graph.getNeighborangle(n, nn)) == 180
										|| Math.abs(graph.getNeighborangle(clickednode, n) - graph.getNeighborangle(n, nn)) == 0) {
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
	/**
	 * clears the history array to erase history of previous board arrangements.
	 */
	public void clearHistory(){
		history= new ArrayList<Color[]>();
	}
	/**
	 * add current board arrangement to the history array.
	 */
	public void updateHistory(){
		Color[] currentgraph=new Color[graph.getGraphSize()];
		for (int i=0;i<graph.getGraphSize();i++){
			if (graph.nodes[i].getTokenStack().isEmpty()) currentgraph[i]=null;
			else currentgraph[i] = graph.nodes[i].getTokenStack().get(0).getColor();
		}
		history.add(currentgraph);
		
	}
	/**
	 * check if the current board arrangement matches one in the history array
	 */
	private boolean repeatedPosition(){
		if (getState()!="place" && getState()!="remove"){
			for (int i=0;i<history.size();i++){
				int counter=0;
				Color[] oldgraph=history.get(i);
				for (int j=0;j<graph.getGraphSize(); j++){
					if ((oldgraph[j]==null && graph.nodes[j].getTokenStack().isEmpty())
							|| (oldgraph[j]!=null && !graph.nodes[j].getTokenStack().isEmpty() 
							&& oldgraph[j] == graph.nodes[j].getTokenStack().get(0).getColor())){
						counter+=1;
					}
					
				}
				if (counter==graph.getGraphSize()) return true;
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
	private boolean opponentmovesAvailable() {
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
			for (int i = 0; i < graph.getGraphSize(); i++) {
				if (!graph.getTokenStack(i + 1).isEmpty() && graph.getTokenStack(i + 1).get(0).getColor() != activeplayer) {
					for (int n : graph.getNeighbors(i + 1)) {
						if (graph.getTokenStack(n).isEmpty()) {
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
			
				FileOutputStream f= new FileOutputStream(Main.class.getResource("/save.txt").getFile());
				PrintStream save= new PrintStream(f);
				String boardstring="";
				for (int i=0; i<graph.getGraphSize();i++) {
					if (!graph.getTokenStack(i+1).isEmpty() && graph.getTokenStack(i+1).get(0).getColor()==Color.RED) boardstring=boardstring+"red"+",";
					else if (!graph.getTokenStack(i+1).isEmpty() && graph.getTokenStack(i+1).get(0).getColor()==Color.BLUE) boardstring=boardstring+"blue"+",";
					else boardstring=boardstring+"null"+",";
				}
				
				boardstring=boardstring.substring(0,boardstring.length()-1);
				
				save.println(boardstring); // close the PrintStreams
				if (activeplayer==Color.RED) save.println("red");
				else save.println("blue");  
				save.println(redstate);    // red state added
				save.println(redcountoffboard);  //red count off board
				save.println(bluestate); //blue state added
				save.println(bluecountoffboard); //blue count off board
				if (computermode){
					if (computercolor==Color.RED) save.println("true,red"); //computermode and computer color
					else save.println("true,blue");
				}
				else save.println("false,");
				for (Color[] carray: history) {  //complete history of moves also added to end of file
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
		InputStream in= this.getClass().getResourceAsStream("/save.txt");
		Scanner input = new Scanner(in);
		
		pieces= new ArrayList<Piece>();
		history =new ArrayList<Color[]>();
		for (int i = 0; i < graph.getGraphSize(); i++) {
			removeBoardPiece(i + 1);
		}
		redcountonboard=0;
		bluecountonboard=0;
		int line=0;
		while (input.hasNextLine()){
			
			if (line==0){
				String fileboardstring=input.nextLine();
				String [] fileboardarray = fileboardstring.split(",");
				for (int i=0;i<fileboardarray.length;i++){
					if (fileboardarray[i].equals("red")) {
						graph.addToken(new Piece(Color.RED, nodecoordinates.get(i)[0]-pieceradius/2 , nodecoordinates.get(i)[1]-pieceradius/2 , pieceradius), i+1); //add pieces back to board
						redcountonboard++;
		
					}
					else if (fileboardarray[i].equals("blue")){
						
						graph.addToken(new Piece(Color.BLUE, nodecoordinates.get(i)[0]-pieceradius/2 , nodecoordinates.get(i)[1]-pieceradius/2  , pieceradius), i+1); //pieces back to board
						bluecountonboard++;
				
					}
					
				}
				line++;
			}
			else if (line==1){  //active player
				String fileactiveplayer=input.nextLine();
				if (fileactiveplayer.equals("red")) activeplayer=Color.RED;
				else activeplayer=Color.BLUE;
				line++;
			}
			else if (line==2){  //red state
				redstate=input.nextLine();
				line++;

			}
			else if (line==3){ //red off-board count
				redcountoffboard=Integer.parseInt(input.nextLine());
				line++;
			}
			
			else if (line==4){ //blue state
				bluestate=input.nextLine();
				line++;

			}
			else if (line==5){ //blue off-board count
				bluecountoffboard=Integer.parseInt(input.nextLine());;
				line++;

			}
			else if (line==6){  //computermode and computer color
				String computerinfo=input.nextLine();
				String [] computerinfoarray = computerinfo.split(",");
				if (computerinfoarray[0].equals("true")){
					computermode=true;
					if (computerinfoarray[1].equals("red")) computercolor=Color.RED;
					else computercolor=Color.BLUE;
				}
				
				else computermode=false;
				line++;

			}
			else if (line==7){ //gamemode (six, nine, twelve)
				String gameMode = input.nextLine();
				if (gameMode.equals("six") || gameMode.equals("nine") || gameMode.equals("twelve"))
					setGraph(gameMode);
				line++;
			}
			else{
				String filehistorystring=input.nextLine();  //complete move history
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

	


	
	/*
	 * This function uses a Monte Carlo Tree Search algorithm to find the best move for the computer to make. 
	 */
	public int[] getComputerMove(){
		TreeNode rootNode=new TreeNode(new GameState(true)); //make the root node
		
		for (int i=0;i<MCTS_ITERATIONS;i++){ //repeat algorithm for the desired number of iterations
			TreeNode node=rootNode; //copy the root
			GameState state=node.state.Clone(); //clone the state of the root
			
			
			//**SELECT**
			while (node.untriedMoves.isEmpty() && !node.childNodes.isEmpty()){ //node is fully expanded and non-terminal
				node=node.UCTselectChild();
				state.doMove(node.move);
			}
			//**EXPAND**
			if (!node.untriedMoves.isEmpty()){ // if we can expand (i.e. state/node is non-terminal)
				int rnd=new Random().nextInt(node.untriedMoves.size());
				int[] randselection= node.untriedMoves.get(rnd);
				state.doMove(randselection);
				node=node.addChild(randselection, state); //add child and descend tree
			}
			//**ROLLOUT
			while (!state.getMoves().isEmpty()){ //while state is non-terminal
				ArrayList<int[]> allmoves=state.getMoves();
				int rnd2=new Random().nextInt(allmoves.size());
				int[] randselection2= allmoves.get(rnd2);
				state.doMove(randselection2);
			}
			//**BACKPROPAGATE
			while (node!=null){ //backpropagate from the expanded node and work back to the root node
				node.Update(state.getResult(node.playerMoved)); // state is terminal. Update node with result from POV of node.playerJustMoved
				node=node.parentNode;
			}
		}

		Collections.sort(rootNode.childNodes, (c1,c2) -> c1.visits.compareTo(c2.visits));  //sort nodes by number of visits
		int[] move=new int[3];
		for (int i=0;i<3;i++){
			move[i]=rootNode.childNodes.get(rootNode.childNodes.size()-1).move[i]+1; //construct move array from node with the most number of visits
		}
	
		return move;
		
		
		
	}
	
	
	

	
	/*
	 * A state of the game used in the AI, i.e. the game board.
        By convention the players are numbered 1 and 2. 
	 */
	public class GameState {
		int playerMoved; //last player to move
		int[] board;  //board array
		int[][] piecenumbers=new int[3][2];  //array to store the number of off and on-board pieces
		ArrayList<int[]> history2=new ArrayList<int[]>();  //array to store history
		
		
		//initialize all state values to the current state of the game
		public GameState(boolean first){
			this.board=new int[graph.getGraphSize()];
			for (int i = 0; i < graph.getGraphSize(); i++) {
				if (graph.getTokenStack(i + 1).isEmpty())
					this.board[i] = 0;
				else if (graph.getTokenStack(i + 1).get(0).getColor() == getComputerColor())
					this.board[i] = 1;
				else
					this.board[i] = 2;   
			}
			if (getComputerColor()==Color.RED){
				this.piecenumbers[1][0]=redcountoffboard;
				this.piecenumbers[1][1]=redcountonboard;
				this.piecenumbers[2][0]=bluecountoffboard;
				this.piecenumbers[2][1]=bluecountonboard;
			}
			else{
				this.piecenumbers[1][0]=bluecountoffboard;
				this.piecenumbers[1][1]=bluecountonboard;
				this.piecenumbers[2][0]=redcountoffboard;
				this.piecenumbers[2][1]=redcountonboard;
			}
			
			for (Color[] c : history) {
				int[] historyboard = new int[graph.getGraphSize()];
				for (int i = 0; i < graph.getGraphSize(); i++) {
					if (c[i] == null)
						historyboard[i] = 0;
					else if (c[i] == getComputerColor())
						historyboard[i] = 1;
					else
						historyboard[i] = 2;
				}
				this.history2.add(historyboard);
			}
			this.playerMoved=2; //At the root pretend the player just moved is player 2 - player 1 has the first move
		}
		
		
		
		public GameState(){
		}
		
		/*
		 * Clone this current state and return the clone.
		 */
		public GameState Clone(){
			GameState state=new GameState();
			
			int[] boardclone=this.board.clone();
			int [][] piecenumbersclone = new int[3][2];
			for(int i = 0; i < this.piecenumbers.length; i++)
				piecenumbersclone[i] = this.piecenumbers[i].clone();
			ArrayList<int[]> historyclone=new ArrayList<int[]>();
			for (int[] i: this.history2){
				historyclone.add(i);
			}
			
			state.playerMoved=this.playerMoved;
			state.board=boardclone;
			state.history2=historyclone;
			state.piecenumbers=piecenumbersclone;
			
			return state; //return the cloned state
		}
		
		/*
		 * Update a state by carrying out the given move.
            Must update playerJustMoved.
		 */
		public void doMove(int[] move){
			this.playerMoved = (3-this.playerMoved);
			if (move[0]==-1){ //off board piece picked up
				this.piecenumbers[this.playerMoved][0]-=1;
				this.piecenumbers[this.playerMoved][1]+=1;
			}
			else{
				this.board[move[0]]=0; //on board piece picked up
			}
			
			this.board[move[1]]=this.playerMoved; //piece placed
			if (move[2]!=-1){ //piece can be removed
				this.board[move[2]]=0; //piece is removed
				this.piecenumbers[3-this.playerMoved][1]-=1;  //on board piece numbers go down
				this.history2=new ArrayList<int[]>(); //erase history
			}
		}
		
		/*
		 * Get all possible moves from this state. 
		 */
		public ArrayList<int[]> getMoves(){
			return getMoves(false);
		}
		
		/*
		 * Get all possible moves from this state. if checkwin is true, you are using this function to check for a win. 
		 */
		public ArrayList<int[]> getMoves(boolean checkwin){
			ArrayList<int[]> allmoves=new ArrayList<int[]>();
			
			if (!checkwin){
				if ((this.piecenumbers[3-this.playerMoved][0]==0) && (this.piecenumbers[3-this.playerMoved][1]<3)){
					return allmoves;
				}
			}
			
			if (this.piecenumbers[3-this.playerMoved][0]>0){
				for (int i=0;i<this.board.length;i++){
					if (this.board[i]==0){
						if (this.makesMill(-1, i, (3-this.playerMoved))){
							for (int m: this.canRemove()){
								allmoves.add(new int[] {-1,i,m});
							}
						}
						else allmoves.add(new int[] {-1,i,-1});
					}
				}
			}
			else{
				for (int i=0;i<this.board.length;i++){
					if (this.board[i]==(3-this.playerMoved)){
						int[] neighbors = graph.getNeighbors(i+1);
						for (int n: neighbors){
							if (this.board[n-1]==0){
								if (this.makesMill(i, n-1, (3-this.playerMoved))){
									for (int m: this.canRemove()){
										allmoves.add(new int[] {i,n-1,m});
									}
								}
								else{
									allmoves.add(new int[] {i, n-1, -1});
								}
							}
						}
					}
				}
			}
			return allmoves; //return all possible moves
		}
		
		/*
		 * Get the game result from the viewpoint of plater p.
		 */
		public double getResult(int p){
			if (this.getMoves(true).isEmpty() && 3-this.playerMoved==p) return 0.0;
			else if (this.piecenumbers[p][1]<3) return 0.0;
			else if (this.getMoves(true).isEmpty() && 3-this.playerMoved!=p) return 1.0;
			else if (this.piecenumbers[3-p][1]<3) return 1.0;
//			else {
//				for (int[] i: this.history2){
//					if (Arrays.equals(i, this.board)){
//						return 0.5;
//					}
//				}
//			}
			assert false;
			return 0.0;
		}
	
		/*
		 * by removing a piece from old, and adding it to i, does player p make any mills?
		 */
		private boolean makesMill(int old, int i, int p){
			if (old!=-1) this.board[old]=0;
			if ((i==0 && ((this.board[1]==p && this.board[2]==p) || (this.board[6]==p && this.board[13]==p)))
					|| (i==1 && ((this.board[0]==p && this.board[2]==p)))
					|| (i==2 && ((this.board[0]==p && this.board[1]==p) || (this.board[9]==p && this.board[15]==p)))
					|| (i==3 && ((this.board[4]==p && this.board[5]==p) || (this.board[7]==p && this.board[10]==p)))
					|| (i==4 && ((this.board[3]==p && this.board[5]==p)))
					|| (i==5 && ((this.board[3]==p && this.board[4]==p) || (this.board[8]==p && this.board[12]==p)))
					|| (i==6 && ((this.board[0]==p && this.board[13]==p)))
					|| (i==7 && ((this.board[3]==p && this.board[10]==p)))
					|| (i==8 && ((this.board[5]==p && this.board[12]==p)))
					|| (i==9 && ((this.board[2]==p && this.board[15]==p)))
					|| (i==10 && ((this.board[3]==p && this.board[7]==p) || (this.board[11]==p && this.board[12]==p)))
					|| (i==11 && ((this.board[10]==p && this.board[12]==p)))
					|| (i==12 && ((this.board[5]==p && this.board[8]==p) || (this.board[10]==p && this.board[11]==p)))
					|| (i==13 && ((this.board[0]==p && this.board[6]==p) || (this.board[14]==p && this.board[15]==p)))
					|| (i==14 && ((this.board[13]==p && this.board[15]==p)))
					|| (i==15 && ((this.board[2]==p && this.board[9]==p) || (this.board[13]==p && this.board[14]==p)))){
						if (old!=-1) this.board[old]=p;
						return true;
					}
					else{
						if (old!=-1) this.board[old]=p;
						return false;
					}
		}
		/*
		 * return the index of all the pieces that the given player, who formed a mill, can remove
		 */
		private ArrayList<Integer> canRemove(){
			ArrayList<Integer> pieces= new ArrayList<Integer>();
			for (int i=0; i<this.board.length;i++){
				if (this.board[i]==this.playerMoved && !makesMill(-1, i, this.playerMoved)){
					pieces.add(i);
				}
			}
			if (pieces.isEmpty()){
				for (int i=0; i<this.board.length;i++){
					if (this.board[i]==this.playerMoved){
						pieces.add(i);
					}
				}
			}
			return pieces;
		}
	}

	
	
	
	

}
