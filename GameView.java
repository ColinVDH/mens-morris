import javax.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Arrays;

public class GameView {

	public static JFrame frame;
	public static JFrame frame2;

	public static double[][] redLoc = new double[6][2];
	public static double[][] blueLoc = new double[6][2];

	public static double[][] board = new double[16][2];

	public static int radius = 10;
	public static int pieceRadius = radius * 4;

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
		frame2.getContentPane().add(draw);
		Dimension d = new Dimension();
		d.width = 982;
		d.height = 953;
		// Set board Coordinates as is
		board[0][0] = d.width / 6 - radius;
		board[0][1] = d.height / 6 - radius;
		board[1][0] = d.width * 5 / 6 - radius;
		board[1][1] = d.height / 6 - radius;
		board[2][0] = d.width / 6 - radius;
		board[2][1] = d.height * 5 / 6 - radius;
		board[3][0] = d.width * 5 / 6 - radius;
		board[3][1] = d.height * 5 / 6 - radius;
		board[4][0] = d.width * 2 / 6 - radius;
		board[4][1] = d.height * 2 / 6 - radius;
		board[5][0] = d.width * 4 / 6 - radius;
		board[5][1] = d.height * 2 / 6 - radius;
		board[6][0] = d.width * 2 / 6 - radius;
		board[6][1] = d.height * 4 / 6 - radius;
		board[7][0] = d.width * 4 / 6 - radius;
		board[7][1] = d.height * 4 / 6 - radius;
		board[8][0] = d.width * 3 / 6 - radius;
		board[8][1] = d.height / 6 - radius;
		board[9][0] = d.width / 6 - radius;
		board[9][1] = d.height * 3 / 6 - radius;
		board[10][0] = d.width * 3 / 6 - radius;
		board[10][1] = d.height * 5 / 6 - radius;
		board[11][0] = d.width * 5 / 6 - radius;
		board[11][1] = d.height * 3 / 6 - radius;
		board[12][0] = d.width * 3 / 6 - radius;
		board[12][1] = d.height * 2 / 6 - radius;
		board[13][0] = d.width * 2 / 6 - radius;
		board[13][1] = d.height * 3 / 6 - radius;
		board[14][0] = d.width * 3 / 6 - radius;
		board[14][1] = d.height * 4 / 6 - radius;
		board[15][0] = d.width * 4 / 6 - radius;
		board[15][1] = d.height * 3 / 6 - radius;

		for (int i = 0; i < draw.redPiece.length; i++) {
			redLoc[i][0] = d.width / 6 - pieceRadius + pieceRadius * i;
			redLoc[i][1] = d.height / 6 - pieceRadius * 2;
			blueLoc[i][0] = d.width * 3 / 6 + pieceRadius * i;
			blueLoc[i][1] = d.height / 6 - pieceRadius * 2;
		}

		// DrawPieces drawP = new DrawPieces();
		// frame2.add(drawP);

		// JLabel label1 = new JLabel("Set Game Pieces");
		// c.gridx = 0;
		// c.gridy = 0;
		// c.insets = new Insets(10, 10, 10, 10);
		// panel.add(label1, c);
		//
		// JLabel chooseSpot = new JLabel("Select Coloured Piece");
		// c.gridx = 1;
		// c.gridy = 3;
		// panel.add(chooseSpot, c);

		// Check if valid button
		// JButton checkValidButton = new JButton("Check Validity");
		// c.gridx = 0;
		// c.gridy = 3;
		// c.insets = new Insets(10, 10, 10, 10);
		// panel.add(checkValidButton, c);
		// checkValidButton.addActionListener(new SetPiecesAction());

		frame2.getContentPane().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mouseClicked(MouseEvent e) {
				// clickedPiece(e);
				// clickedPlacement(e);
				// System.out.println("MouseX = "+e.getX()+" MouseY =
				// "+e.getY());
				// System.out.println(distanceFromPoint(e,
				// draw.redPiece[0].getCenterX(),
				// draw.redPiece[0].getCenterY()));
				for (int i = 0; i < draw.redPiece.length; i++) {
					if (draw.redPiece[i].contains(e.getPoint())) {
						// if (distanceFromPoint(e,
						// draw.redPiece[0].getCenterX(),
						// draw.redPiece[0].getCenterY()) < pieceRadius) {
						// System.out.println("X: "+
						// draw.redPiece[0].getCenterX() + " Y: "
						// +draw.redPiece[0].getCenterY());
						int k = i;
						frame2.getContentPane().addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent e) {
							}

							public void mouseReleased(MouseEvent e) {
							}

							public void mouseEntered(MouseEvent e) {
							}

							public void mouseExited(MouseEvent e) {
							}

							public void mouseClicked(MouseEvent e) {
								for (int j = 0; j < board.length; j++) {
									if (draw.circle[j ].contains(e.getPoint())) {
										redLoc[k][0] = board[j][0] - radius;
										redLoc[k][1] = board[j][1] - radius;
										draw.repaint();
										System.out.println(Arrays.deepToString(redLoc));
										// }
										// System.out.println("X2: "+
										// draw.redPiece[0].getCenterX() + " Y2:
										// "
										// +draw.redPiece[0].getCenterY());
									}
								}
							}
						});
					}
				}
			}
		});
	}

	public static class DrawLines extends JPanel {

		Ellipse2D.Double[] circle = new Ellipse2D.Double[16];

		Ellipse2D.Double[] redPiece = new Ellipse2D.Double[6];
		Ellipse2D.Double[] bluePiece = new Ellipse2D.Double[6];

		@Override
		protected void paintComponent(Graphics g) {
			Dimension d = this.getSize();
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(3));
			// System.out.println("dX: "+d.width+" dY: "+d.height);

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
				redPiece[i] = new Ellipse2D.Double(redLoc[i][0], redLoc[i][1], pieceRadius, pieceRadius);
				g2d.fill(redPiece[i]);
			}

			// Blue Pieces:
			g2d.setColor(Color.BLUE);
			for (int i = 0; i < bluePiece.length; i++) {
				bluePiece[i] = new Ellipse2D.Double(blueLoc[i][0], blueLoc[i][1], pieceRadius, pieceRadius);
				g2d.fill(bluePiece[i]);
			}

		}
	}

	static class SetPiecesAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			GameView.SetPieces();
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
		return Math.sqrt(((e.getX()) - x) * ((e.getX()) - x) + ((e.getY()) - y) * ((e.getY()) - y));
	}
}
