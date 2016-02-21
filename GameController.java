package com.aci.sixmensmorris;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class GameController implements MouseListener{
	private static GameModel model;
	private GameView view;
	private static String gamestate;
	
	public GameController(GameModel m, GameView v) {
		model=m;
		view=v;
		
		
	    for (int i = 0; i < 6; i++) {
					Piece redpiece=new Piece(Color.RED, view.NodeCoordinates[1][0]+view.PIECERADIUS*(i-3),
							view.NodeCoordinates[0][0]-100, view.PIECERADIUS, view.PIECERADIUS);
					Piece bluepiece=new Piece(Color.BLUE, view.NodeCoordinates[1][0]+view.PIECERADIUS*(i-3),
							view.NodeCoordinates[15][0]+(100-view.PIECERADIUS), view.PIECERADIUS, view.PIECERADIUS);
					model.redpieces.add(redpiece);
					model.bluepieces.add(bluepiece);
		        }
	}

	public void mouseClicked(MouseEvent e) {

		view.checkforValidityEvent();
		
		if (model.getSelectedPiece()!=null){
			boolean found=false;
			for (int i=0; i<16;i++){
				if (view.boardnodes[i].contains(e.getPoint())){
					System.out.println("ll");
					model.selectedpiece.x=view.boardnodes[i].x;
					model.selectedpiece.y=view.boardnodes[i].y;
					model.addToken(model.getSelectedPiece(), i+1);
					
					//remove from red piece collection if it originated there
					if (model.selectedpiecepile==Color.RED){
						model.redpieces.remove(model.redpieces.indexOf(model.getSelectedPiece()));
					}
					//remove from blue pile if it originated there
					if (model.selectedpiecepile==Color.BLUE){
						model.bluepieces.remove(model.bluepieces.indexOf(model.getSelectedPiece()));
					}
					
					//remove from another node if it originated there
					else if (model.selectedpiecenode!=null){
						model.removeToken(model.getSelectedPiece(), model.selectedpiecenode);
					}

					
					found=true;
					break;
			}
		}
			if (found==false){
				model.selectedpiece.x=e.getX()-view.PIECERADIUS/2;
				model.selectedpiece.y=e.getY()-view.PIECERADIUS/2;
				
				//remove from another node if it originated there, also must re-add to the appropriate color pile
				if (model.selectedpiecenode!=null){
					model.removeToken(model.getSelectedPiece(), model.selectedpiecenode);
					if (model.getSelectedPiece().getColor()==Color.RED){
						model.redpieces.add(model.getSelectedPiece());
					}
					else model.bluepieces.add(model.getSelectedPiece());
				}
				
			}
			
			
			
			//remove selected piece and related information
			model.setSelectedPiece(null);
			model.selectedpiecepile=null;
			model.selectedpiecenode=null;
			
	
			
	}
		
	else{
		for (Piece redpiece: model.getPieces(Color.RED)){
			if (redpiece.contains(e.getPoint())){
				System.out.println("click");
				model.setSelectedPiece(redpiece);
				model.selectedpiecepile=Color.RED;
			}
		}
		
		for (Piece bluepiece: model.getPieces(Color.BLUE)){
			if (bluepiece.contains(e.getPoint())){
				model.setSelectedPiece(bluepiece);
				model.selectedpiecepile = Color.BLUE;
			}
		}
		
		for (int i=0;i<16;i++){
				ArrayList<Piece> stack= model.getTokenStack(i+1);
				if (!stack.isEmpty() && stack.get(stack.size() - 1).contains(e.getPoint())){
					model.setSelectedPiece(stack.get(stack.size() - 1));
					model.selectedpiecenode=i+1;
				}
			

		}
		
	}
		
	view.pieces.repaint();
}

	

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	
	
	

}
