package com.aci.sixmensmorris;



public class Main {

	public static void main(String[] args) {
		GameModel model= new GameModel();  //initialize all components of MVC
		GameView view= new GameView(model);
		GameController control=new GameController(model, view);
		view.registerListeners(control); //control registers as a listener to the model
	}
}
