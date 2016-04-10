package com.aci.sixmensmorris;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A node in the game tree. Note wins is always from the viewpoint of playerJustMoved.
**/
public class TreeNode {
	int[] move; //move to get to this node
	TreeNode parentNode; //parent to this node
	ArrayList<TreeNode> childNodes; //all children of this node 
	GameModel.GameState state; //the game state associated with this ndoe
	Double wins; //wins at this node
	Integer visits; //visits to this node
	ArrayList<int[]> untriedMoves; //all moves that haven't been explored / future child nodes
	int playerMoved; //the player that just moved.
	
	public TreeNode(){
		this.move=null;
		this.parentNode=null;
		this.state=null;
		this.wins=0.0;
		this.visits=0;
		this.playerMoved=this.state.playerMoved;
		this.untriedMoves=this.state.getMoves();
		this.childNodes=new ArrayList<TreeNode>();
		
	}
	//this is called for the root node
	public TreeNode(GameModel.GameState state){
		this.move=null;
		this.parentNode=null;
		this.state=state;
		this.wins=0.0;
		this.visits=0;
		this.playerMoved=this.state.playerMoved;
		this.untriedMoves=this.state.getMoves();
		this.childNodes=new ArrayList<TreeNode>();
	
	}
	//this is called for a regular child node
	public TreeNode(int[] move, TreeNode parent, GameModel.GameState state){
		this.move=move;
		this.parentNode=parent;
		this.state=state;
		this.wins=0.0;
		this.visits=0;
		this.playerMoved=this.state.playerMoved;
		this.untriedMoves=this.state.getMoves();
		this.childNodes=new ArrayList<TreeNode>();
		
	}
	//get the "value" of a particular node using the UCT formula
	private Double getUCTvalue(TreeNode tn){
		return tn.wins/tn.visits + Math.sqrt((2*Math.log((this.visits)/tn.visits)));
	}
	//select the optimal child node by their value using UCT.
	public TreeNode UCTselectChild(){
		Collections.sort(this.childNodes, (c1,c2) -> getUCTvalue(c1).compareTo(getUCTvalue(c2)));
		return childNodes.get(childNodes.size()-1);
		
	}
	/**
	 * Add a child node to this node
	**/
	public TreeNode addChild(int[] move, GameModel.GameState state){
		TreeNode node= new TreeNode(move, this, state);
		this.untriedMoves.remove(move);
		this.childNodes.add(node);
		return node;
	}
	/**
	 * during backpropagation, you update the node by adding another visit, and then adding the propagated result to the win total. 
	**/
	public void Update(double result){
		this.visits+=1;
		this.wins+=result;
	}
	
	
}
