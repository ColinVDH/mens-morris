/*
Piece allows representation and provides information about a particular game piece. 
*/

package com.aci.sixmensmorris;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class Piece extends Ellipse2D.Double {

	Color color;
	
	public Piece(Color C, double x, double y, double r){
		super(x, y, r, r);
		color=C;
	}
	
	public Color getColor(){
		return color;
	}

	
}
