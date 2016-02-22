package com.aci.sixmensmorris;

import java.awt.Color;
import java.util.*;

public class GameModel extends Graph{
	

	private Piece selectedpiece = null;
	private Integer selectedpiecenode=null;
	private Boolean selectedpieceunplayed=null;
	private ArrayList<Piece> redpieces = new ArrayList<Piece>();
	private ArrayList<Piece> bluepieces = new ArrayList<Piece>();

	
	
	public GameModel(){
	};
	
	
	public void setSelectedPiece(Piece p){
		selectedpiece=p;
	}
	
	public void setSelectedPieceX(double x){
		selectedpiece.x=x;
	}
	
	public void setSelectedPieceY(double y){
		selectedpiece.y=y;
	}
	
	public Piece getSelectedPiece(){
		return selectedpiece;
	}
	
	public Integer getSelectedPieceNode(){
		return selectedpiecenode;
	}
	
	public void setSelectedPieceNode(Integer i){
		selectedpiecenode=i;
	}
	
	public boolean selectedPieceUnplayed(){
		return selectedpieceunplayed;
	}
	
	public void selectedPieceUnplayed(boolean b){
		selectedpieceunplayed=b;
	}
	
	public ArrayList<Piece> getRedPieces(){
			return redpieces;
	}
	
	public void removeRedPiece(int index){
		redpieces.remove(index);
	}
	
	public void addRedPiece(Piece p){
		redpieces.add(p);
	}
			
	public ArrayList<Piece> getBluePieces(){
		return bluepieces;
	}
	
	public void removeBluePiece(int index){
		bluepieces.remove(index);
	}
	
	public void addBluePiece(Piece p){
		bluepieces.add(p);
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
