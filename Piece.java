package com.aci.sixmensmorris;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class Piece extends Ellipse2D.Double {

	Color color;
	
	public Piece(Color C, double w, double h, double x, double y){
		super(w, h, x, y);
		color=C;
	}
	
	public Color getColor(){
		return color;
	}
}
