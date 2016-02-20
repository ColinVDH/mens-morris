import javax.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class View {

	public static JFrame frame;
	public static JFrame frame2;

	public static void main(String[] args) {
		frame = new JFrame("Main Menu - 6 Men's Morris");
		frame.setVisible(true);
		frame.setSize(500, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new GridBagLayout());
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		GridBagConstraints c = new GridBagConstraints();

		JLabel label1 = new JLabel("Welcome to Six Men's Morris");
		c.gridx = 0;
		c.gridy = 8;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(label1, c);

		JButton newGameButton = new JButton("New Game");
		c.gridx = 0;
		c.gridy = 9;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(newGameButton, c);

		JButton setPieces = new JButton("Set Pieces");
		c.gridx = 0;
		c.gridy = 10;
		c.insets = new Insets(10, 10, 10, 10);
		panel.add(setPieces, c);
		setPieces.addActionListener(new SetPiecesAction());
	}

	public static void SetPieces() {
		frame2 = new JFrame("Set Pieces");
		frame2.setVisible(true);
		frame2.setSize(1000, 1000);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new GridBagLayout());
		frame2.getContentPane().add(panel, BorderLayout.PAGE_START);
		GridBagConstraints c = new GridBagConstraints();

		DrawLines draw = new DrawLines();
		frame2.add(draw);

//		JLabel label1 = new JLabel("Set Game Pieces");
//		c.gridx = 0;
//		c.gridy = 0;
//		c.insets = new Insets(10, 10, 10, 10);
//		panel.add(label1, c);
//
//		JLabel chooseSpot = new JLabel("Select Coloured Piece");
//		c.gridx = 1;
//		c.gridy = 3;
//		panel.add(chooseSpot, c);

		// // Red Pieces:
		// JLabel[] redPiece = new JLabel[6];
		// JLabel redSpot = new JLabel();
		// c.gridx = 1;
		// c.gridy = 7;
		// panel.add(redSpot, c);
		// for (int i = 0; i < redPiece.length; i++) {
		// redPiece[i] = new JLabel();
		// redPiece[i].setBounds(0, 0, 5, 5);
		// c.gridx = i;
		// c.gridy = 1;
		// redPiece[i].setIcon(new ImageIcon("redPiece.png"));
		// panel.add(redPiece[i], c);
		// }
		// redPiece[0].addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// chooseSpot.setText("Click location to place piece");
		// frame2.addMouseListener(new MouseAdapter(){
		// @Override
		// public void mouseClicked(MouseEvent e2){
		// System.out.println("X: " + e2.getX() + " Y: " + e2.getY());
		//// redPiece[0].setAlignmentX(e2.getX());
		//// redPiece[0].setAlignmentY(e2.getY());
		// }
		// });
		// }
		// });
		//
		// // Blue Pieces:
		// JLabel[] bluePiece = new JLabel[6];
		// JLabel blueSpot = new JLabel();
		// c.gridx = 7;
		// c.gridy = 2;
		// panel.add(blueSpot, c);
		// for (int i = 0; i < bluePiece.length; i++) {
		// bluePiece[i] = new JLabel();
		// bluePiece[i].setBounds(0, 0, 5, 5);
		// c.gridx = i;
		// c.gridy = 2;
		// bluePiece[i].setIcon(new ImageIcon("bluePiece.png"));
		// panel.add(bluePiece[i], c);
		// }

		// Check if valid button
//		JButton checkValidButton = new JButton("Check Validity");
//		c.gridx = 0;
//		c.gridy = 3;
//		c.insets = new Insets(10, 10, 10, 10);
//		panel.add(checkValidButton, c);
//		checkValidButton.addActionListener(new SetPiecesAction());

		frame2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// clickedPiece(e);
				// clickedPlacement(e);
				System.out.println("MouseX = "+e.getX()+" MouseY = "+e.getY());
				System.out.println(distanceFromPoint(e, draw.redPiece[0].getCenterX(), draw.redPiece[0].getCenterY()));
				if(draw.redPiece[0].contains(e.getPoint())){
				//if (distanceFromPoint(e, draw.redPiece[0].getCenterX(), draw.redPiece[0].getCenterY()) < draw.pieceRadius) {
					System.out.println("X: "+ draw.redPiece[0].getCenterX() + " Y: " +draw.redPiece[0].getCenterY());
					frame2.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							if (draw.circle[0].contains(e.getPoint())){//(distanceFromPoint(e, draw.circle[0].getCenterX(), draw.circle[0].getCenterY()) < draw.radius) {
								draw.redPiece[0].x = draw.circle[0].x;
								draw.redPiece[0].y = draw.circle[0].y;
								System.out.println("X: "+ draw.redPiece[0].getCenterX() + " Y: " +draw.redPiece[0].getCenterY());
							}

						}
					});
				}

			}
		});
	}

	public static class DrawLines extends JPanel {

		int radius = 10;
		Ellipse2D.Double[] circle = new Ellipse2D.Double[16];
		
		int pieceRadius = radius * 4;
		Ellipse2D.Double[] redPiece = new Ellipse2D.Double[6];
		Ellipse2D.Double[] bluePiece = new Ellipse2D.Double[6];

		@Override
		public void paintComponent(Graphics g) {
			Dimension d = this.getSize();
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(3));

			// Draw Circles:
			circle[0] = new Ellipse2D.Double(d.width / 6 - radius, d.height / 6 - radius, radius * 2, radius * 2);
			g2d.fill(circle[0]);
			circle[1] = new Ellipse2D.Double(d.width * 5 / 6 - radius, d.height / 6 - radius, radius * 2, radius * 2);
			g2d.fill(circle[1]);
			circle[2] = new Ellipse2D.Double(d.width / 6 - radius, d.height * 5 / 6 - radius, radius * 2, radius * 2);
			g2d.fill(circle[2]);
			circle[3] = new Ellipse2D.Double(d.width * 5 / 6 - radius, d.height * 5 / 6 - radius, radius * 2,
					radius * 2);
			g2d.fill(circle[3]);
			circle[4] = new Ellipse2D.Double(d.width * 2 / 6 - radius, d.height * 2 / 6 - radius, radius * 2,
					radius * 2);
			g2d.fill(circle[4]);
			circle[5] = new Ellipse2D.Double(d.width * 4 / 6 - radius, d.height * 2 / 6 - radius, radius * 2,
					radius * 2);
			g2d.fill(circle[5]);
			circle[6] = new Ellipse2D.Double(d.width * 2 / 6 - radius, d.height * 4 / 6 - radius, radius * 2,
					radius * 2);
			g2d.fill(circle[6]);
			circle[7] = new Ellipse2D.Double(d.width * 4 / 6 - radius, d.height * 4 / 6 - radius, radius * 2,
					radius * 2);
			g2d.fill(circle[7]);
			circle[8] = new Ellipse2D.Double(d.width * 3 / 6 - radius, d.height / 6 - radius, radius * 2, radius * 2);
			g2d.fill(circle[8]);
			circle[9] = new Ellipse2D.Double(d.width / 6 - radius, d.height * 3 / 6 - radius, radius * 2, radius * 2);
			g2d.fill(circle[9]);
			circle[10] = new Ellipse2D.Double(d.width * 3 / 6 - radius, d.height * 5 / 6 - radius, radius * 2,
					radius * 2);
			g2d.fill(circle[10]);
			circle[11] = new Ellipse2D.Double(d.width * 5 / 6 - radius, d.height * 3 / 6 - radius, radius * 2,
					radius * 2);
			g2d.fill(circle[11]);
			circle[12] = new Ellipse2D.Double(d.width * 3 / 6 - radius, d.height * 2 / 6 - radius, radius * 2,
					radius * 2);
			g2d.fill(circle[12]);
			circle[13] = new Ellipse2D.Double(d.width * 2 / 6 - radius, d.height * 3 / 6 - radius, radius * 2,
					radius * 2);
			g2d.fill(circle[13]);
			circle[14] = new Ellipse2D.Double(d.width * 3 / 6 - radius, d.height * 4 / 6 - radius, radius * 2,
					radius * 2);
			g2d.fill(circle[14]);
			circle[15] = new Ellipse2D.Double(d.width * 4 / 6 - radius, d.height * 3 / 6 - radius, radius * 2,
					radius * 2);
			g2d.fill(circle[15]);

			// Code for pieces:
			// Red Pieces:
			g2d.setColor(Color.RED);
			for (int i = 0; i < redPiece.length; i++) {
				redPiece[i] = new Ellipse2D.Double(d.width / 6 - pieceRadius + pieceRadius * i,
						d.height / 6 - pieceRadius * 2, pieceRadius, pieceRadius);
				g2d.fill(redPiece[i]);
			}

			// Blue Pieces:
			g2d.setColor(Color.BLUE);
			for (int i = 0; i < bluePiece.length; i++) {
				bluePiece[i] = new Ellipse2D.Double(d.width * 3 / 6 + pieceRadius * i,
						d.height / 6 - pieceRadius * 2, pieceRadius, pieceRadius);
				g2d.fill(bluePiece[i]);
			}

			g2d.setColor(Color.BLACK);

			// Draw Lines for board
			g2d.drawLine(d.width / 6, d.height / 6, d.width / 6, d.height * 5 / 6);
			g2d.drawLine(d.width / 6, d.height / 6, d.width * 5 / 6, d.height / 6);
			g2d.drawLine(d.width * 5 / 6, d.height / 6, d.width * 5 / 6, d.height * 5 / 6);
			g2d.drawLine(d.width / 6, d.height * 5 / 6, d.width * 5 / 6, d.height * 5 / 6);

			g2d.drawLine(d.width * 2 / 6, d.height * 2 / 6, d.width * 4 / 6, d.height * 2 / 6);
			g2d.drawLine(d.width * 2 / 6, d.height * 2 / 6, d.width * 2 / 6, d.height * 4 / 6);
			g2d.drawLine(d.width * 2 / 6, d.height * 4 / 6, d.width * 4 / 6, d.height * 4 / 6);
			g2d.drawLine(d.width * 4 / 6, d.height * 2 / 6, d.width * 4 / 6, d.height * 4 / 6);

			g2d.drawLine(d.width * 3 / 6, d.height / 6, d.width * 3 / 6, d.height * 2 / 6);
			g2d.drawLine(d.width / 6, d.height * 3 / 6, d.width * 2 / 6, d.height * 3 / 6);
			g2d.drawLine(d.width * 4 / 6, d.height * 3 / 6, d.width * 5 / 6, d.height * 3 / 6);
			g2d.drawLine(d.width * 3 / 6, d.height * 4 / 6, d.width * 3 / 6, d.height * 5 / 6);
		}
	}

	static class SetPiecesAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			View.SetPieces();
			frame.setVisible(false);
		}
	}

	static class CheckValidAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// View.SetPieces();
		}
	}

	public static void clickedPiece(MouseEvent e) {
	}

	public static void clickedPlacement(MouseEvent e) {

	}

	public static double distanceFromPoint(MouseEvent e, double x, double y) {
		return Math.sqrt((e.getX() - x) * (e.getX() - x) + (e.getY() - y) * (e.getY() - y));
	}
}
