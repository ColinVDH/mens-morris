package com.aci.sixmensmorris;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class GameController implements MouseListener, ActionListener{
	private static GameModel model;
	private GameView view;
	
	public GameController(GameModel m, GameView v) {
		model=m;
		view=v;
	    view.initMenu();
	}

	
	@Override
	public void actionPerformed(ActionEvent ae) {
		String command= ae.getActionCommand();
		if (command == "Check Validity"){
			view.displayErrors(model.getErrors());
		}
		else if (command=="New Game"){
			view.displayFirstPlayer(model.selectFirstPlayer());
		}
		else if (command=="Set Pieces"){
			view.SetPieces();
		}

	}
	
	public void mousePressed(MouseEvent e) {
		
		if (model.getSelectedPiece()!=null){
			boolean found=false;
			for (int i=0; i<16;i++){
				if (view.boardnodes[i].contains(e.getPoint())){
					System.out.println("ll");
					model.setSelectedPieceX(view.boardnodes[i].x);
					model.setSelectedPieceY(view.boardnodes[i].y);
					model.addToken(model.getSelectedPiece(), i+1);
					
					//remove from red piece collection if it originated there
					if (model.selectedPieceUnplayed() && model.getSelectedPiece().getColor()==Color.RED){
						model.removeRedPiece(model.getRedPieces().indexOf(model.getSelectedPiece()));
					}
					//remove from blue pile if it originated there
					if (model.selectedPieceUnplayed() && model.getSelectedPiece().getColor()==Color.BLUE){
						model.removeBluePiece(model.getBluePieces().indexOf(model.getSelectedPiece()));
					}
					
					//remove from another node if it originated there
					else if (model.getSelectedPieceNode()!=null){
						model.removeToken(model.getSelectedPiece(), model.getSelectedPieceNode());
					}

					
					found=true;
					break;
			}
		}
			if (found==false){
			
				model.setSelectedPieceX(e.getX()-view.PIECERADIUS/2);
				model.setSelectedPieceY(e.getY()-view.PIECERADIUS/2);
				
				//remove from another node if it originated there, also must re-add to the appropriate color pile
				if (model.getSelectedPieceNode()!=null){
					model.removeToken(model.getSelectedPiece(), model.getSelectedPieceNode());
					if (model.getSelectedPiece().getColor()==Color.RED){
						model.addRedPiece(model.getSelectedPiece());
					}
					else model.addBluePiece(model.getSelectedPiece());
				}
				
			}
			
			
			
			//remove selected piece and related information
			model.setSelectedPiece(null);
			model.selectedPieceUnplayed(false);
			model.setSelectedPieceNode(null);
			
	
			
	}
		
	else{
		for (Piece redpiece: model.getRedPieces()){
			if (redpiece.contains(e.getPoint())){
				System.out.println("click");
				model.setSelectedPiece(redpiece);
				model.selectedPieceUnplayed(true);
			}
		}
		
		for (Piece bluepiece: model.getBluePieces()){
			if (bluepiece.contains(e.getPoint())){
				model.setSelectedPiece(bluepiece);
				model.selectedPieceUnplayed(true);
			}
		}
		
		for (int i=0;i<16;i++){
				ArrayList<Piece> stack= model.getTokenStack(i+1);
				if (!stack.isEmpty() && stack.get(stack.size() - 1).contains(e.getPoint())){
					model.setSelectedPiece(stack.get(stack.size() - 1));
					model.setSelectedPieceNode(i+1);
				}
			

		}
		
	}
		
	view.repaintPieces();
}

	

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}



}
	
	
	
	


