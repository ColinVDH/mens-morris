package com.aci.sixmensmorris;

import java.util.*;

public class Main {

	public static void main(String[] args) {
		GameModel model= new GameModel();
		GameView view= new GameView(model);
		GameController control=new GameController(model, view);
		
	}
}
