package com.aci.sixmensmorris;

import java.awt.Color;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
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

	
}
