package com.aci.sixmensmorris;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		GameModel model = new GameModel();  //initialize all components of MVC
		GameView view = new GameView(model);
		GameController control = new GameController(model, view);
		view.registerListeners(control); //registers the controller as a listener to the model
	}
}
