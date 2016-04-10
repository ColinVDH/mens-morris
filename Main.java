package com.aci.sixmensmorris;


public class Main {

	// Initialize model, view and controller to start game.
	public static void main(String[] args) {
		GameModel model= new GameModel();  //initialize all components of MVC
		GameView view= new GameView(model);
		GameController control=new GameController(model, view);
		view.registerListeners(control); //registers the controller as a listener to the model.
		
	
	}
}
