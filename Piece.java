package com.aci.sixmensmorris;

import java.awt.Color;
import java.awt.geom.Ellipse2D;

public class Piece extends Ellipse2D.Double {

	Color color;
	
	public Piece(Color C, double x, double y, double w, double h){
		super(x, y, w, h);
		color=C;
	}
	
	public Color getColor(){
		return color;
	}
}
