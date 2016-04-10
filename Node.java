/*
Node provide information about a particular point, 
like the neighboring points, and the distance or angle to each neighboring point.  
*/


package com.aci.sixmensmorris;

import java.util.*;

public class Node {
	private ArrayList<Piece> tokenstack= new ArrayList<Piece>();
	private int[] neighbors;
	private double[] neighborlengths;
	private int[] neighborangles;
	

<<<<<<< HEAD
	/**
	 * node constructor inputs the neighbors, neighbor's angles and neighbors lengths into the appropriate arrays. 
	 */
=======
	//Constructor for node inputs neighbours, neighbour angles and lengths.
>>>>>>> origin/master
	public Node(double[] neighbors,  double[] neighborangles, double[] neighborlengths) {
		
		this.neighborlengths=neighborlengths;
		
		int[] neighbors_int = new int[neighbors.length];
		int[] neighborangles_int= new int[neighborangles.length];
		for (int i = 0; i<neighbors.length;i++){
			neighbors_int[i]=(int)neighbors[i];
			neighborangles_int[i]=(int)neighborangles[i];
		}
		this.neighborangles=neighborangles_int;
		this.neighbors=neighbors_int;
	}
<<<<<<< HEAD
	/**
	 * get all neighbors
	 */
	public int[] getNeighbors() {
		return this.neighbors;
	}
	/**
	 * get the lengths to all neighbors
	 */
	public double getNeighborlength(int n) {
		return neighborlengths[getIndexOf(n, neighbors)];
	}
	/**
	 * get the angles to all neighbors
	 */
	public int getNeighborangle(int n){
		return neighborangles[getIndexOf(n, neighbors)];
	}
	/**
	 * add a piece to this node
	 */
	public void addToken(Piece C) {
		tokenstack.add(C);
	}
	/**
	 * remove a piece from this node 
	 */
	public void removeToken() {
		tokenstack.remove(tokenstack.size()-1);
	}
	/**
	 * return the stack of all pieces on this node
	 */
	public ArrayList<Piece> getTokenStack(){
		return tokenstack;
	}
	/**
	 * get the index of a particular integer (i.e. node number) in an array.
	 */
=======

	//Returns the neighbours of node
	public int[] getNeighbors() {
		return this.neighbors;
	}

	//returns neighbour n of node's length
	public double getNeighborlength(int n) {
		return neighborlengths[getIndexOf(n, neighbors)];
	}
	
	//returns angle for neighbour n of node
	public int getNeighborangle(int n){
		return neighborangles[getIndexOf(n, neighbors)];
	}
	
	//Adds token for input piece
	public void addToken(Piece C) {
		tokenstack.add(C);
	}
	
	//Removes token for input piece
	public void removeToken() {
		tokenstack.remove(tokenstack.size()-1);
	}
	
	//returns token stack
	public ArrayList<Piece> getTokenStack(){
		return tokenstack;
	}
	
	//finds index of integer in array tab
>>>>>>> origin/master
	private int getIndexOf( int toSearch, int[] tab )
		{
		  for( int i=0; i< tab.length ; i ++ )
		    if( tab[ i ] == toSearch)
		     return i;

		  return -1;
		}
	

}
