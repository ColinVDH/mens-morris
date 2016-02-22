package com.aci.sixmensmorris;

import java.awt.Color;
import java.util.*;

public class GameModel extends Graph{
	

	Piece selectedpiece = null;
	Integer selectedpiecenode=null;
	Color selectedpiecepile=null;
	ArrayList<Piece> redpieces = new ArrayList<Piece>();
	ArrayList<Piece> bluepieces = new ArrayList<Piece>();

	
	
	public GameModel(){
		selectedpiece=null;
		
	};
	
	
	public void setSelectedPiece(Piece s){
		selectedpiece=s;
	}
	
	public Piece getSelectedPiece(){
		return selectedpiece;
	}
	
	
	public ArrayList<Piece> getPieces(Color color){
		if (color==Color.RED){
			return redpieces;
		}
		else if (color==Color.BLUE){
			return bluepieces;
		}
		return null;
	}


	public String getErrors() {
		String errorstring="";
		int errorcount=0;
		for (int i=0; i<getGraphSize();i++){
			if (getTokenStack(i+1).size()>1){
				errorcount+=1;
			}
		}
		if (errorcount==1) errorstring="There is 1 board location with multiple pieces. Only 1 piece per location is allowed.";
		if (errorcount>1) errorstring="There are "+errorcount+" board locations with multiple pieces. Only 1 piece per location is allowed.";
		return errorstring;
		
	}
	
	public String selectFirstPlayer(){
		String[] players={"Red", "Blue"};
		return players[new Random().nextInt(2)];
		
	}

	
}
