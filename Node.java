package com.aci.sixmensmorris;

import java.util.*;
import java.awt.*;

public class Node {
	private ArrayList<Color> tokenstack;
	private int[] neighbors;
	private double[] neighborlengths;
	private int[] neighborangles;
	

	
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

	public int[] getNeighbors() {
		return this.neighbors;
	}

	public double getNeighborlength(int n) {
		return neighborlengths[getIndexOf(n, neighbors)];
	}
	
	public int getNeighborangle(int n){
		System.out.println(n);
		System.out.println(Arrays.toString(neighbors));
		return neighborangles[getIndexOf(n, neighbors)];
	}

	public void addToken(Color C) {
		tokenstack.add(C);
	}
	
	public ArrayList<Color> getTokenStack(){
		return tokenstack;
	}
	
	private int getIndexOf( int toSearch, int[] tab )
		{
		  for( int i=0; i< tab.length ; i ++ )
		    if( tab[ i ] == toSearch)
		     return i;

		  return -1;
		}//met
	

}
