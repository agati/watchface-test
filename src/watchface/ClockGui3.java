package watchface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

/**
 * Creates a GUI Watchface with a clean style.
 * <p>
 * It extends the JFrame class.
 * <p>
 * Exit the rendered JFrame
 *
 * 
 * 
 */

public class ClockGui3 extends JFrame {

	private static final long serialVersionUID = 1L;

	private static JFrame frame;
	private int status = 0;
	private static final int spacing = 35;
	private static final float radPerSecMin = (float) (Math.PI / 30.0);
	private static final float radPerNum = (float) (Math.PI / -6);
	private int size;
	private int centerX;
	private int centerY;

	SimpleDateFormat sf;

	Calendar cal;
	int hour;
	int minute;
	int second;
	Color colorSecond, colorMHour, colorMMinutes, colorNumber;

	Timer timer;
	TimeZone timeZone;

	/**
	 * Main Class.
	 * 
	 * 
	 */

	public static void main(String args[]) {

		frame = new ClockGui3();
		frame.setTitle("Watchface test");
		frame.setVisible(true);
	}

	/**
	 * Defines bounds, generates Content Pane and start listen to mouse
	 */
	public ClockGui3() {

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(700, 100, 400, 430);
		getContentPane().setBackground(new Color(0, 0, 0));

		timer = new Timer();
		timeZone = TimeZone.getDefault();
		timer.schedule(new TickTimerTask(), 0, 1000);
	}

	/**
	 * Get an instance of Calendar and repaints component.
	 * 
	 */

	class TickTimerTask extends TimerTask {

		@Override
		public void run() {
			cal = (Calendar) Calendar.getInstance(timeZone);
			repaint();
		}

	}

	/*
	 * Paints the container. This forwards the paint to any lightweight components
	 * that are children of this container.
	 * 
	 */

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);

		/*
		 * clock border
		 */

		g.setColor(new Color(68, 238, 244));// CYAN clock border
		g.fillOval(25, spacing, 350, 350);
		g.setColor(Color.black);
		g.fillOval(25 + 2, spacing + 2, 345, 345);// the fills' intersection defines the clock contour thickness

		size = 400 - spacing;
		centerX = 400 / 2;
		centerY = 400 / 2 + 10;

		/*
		 * clock face
		 */
		drawClockFace(g);

		/*
		 * get system time
		 */
		cal = Calendar.getInstance();
		hour = cal.get(Calendar.HOUR);
		minute = cal.get(Calendar.MINUTE);
		second = cal.get(Calendar.SECOND);

		/*
		 * draw digital clock
		 */

		g.setColor(Color.white);
		g.setFont(new Font("Calibri", Font.PLAIN, 60));

		sf = new SimpleDateFormat("HH:mm:ss");
		String str = sf.format(cal.getTime());

		/*
		 * split string to get a more refined position placement and to avoid screen
		 * character shifting when using ":"
		 */

		String strSplit0 = str.substring(0, 1);
		String strSplit1 = str.substring(1, 2);
		String strSplit2 = str.substring(2, 3);
		String strSplit3 = str.substring(3, 4);
		String strSplit4 = str.substring(4, 5);
		String strSplit5 = str.substring(5, 6);
		String strSplit6 = str.substring(6, 7);
		String strSplit7 = str.substring(7, 8);

		g.drawString(strSplit0, centerX - 136, centerY);
		g.drawString(strSplit1, centerX - 136 + 38, centerY);

		if (second % 2 != 0)
			strSplit2 = "";

		g.drawString(strSplit2, centerX - 136 + 38 + 38, centerY);

		g.drawString(strSplit3, centerX - 136 + 38 + 38 + 24, centerY);
		g.drawString(strSplit4, centerX - 136 + 38 + 38 + 24 + 38, centerY);

		if (second % 2 != 0)
			strSplit5 = "";

		g.drawString(strSplit5, centerX - 136 + 38 + 38 + 24 + 38 + 38, centerY);

		g.drawString(strSplit6, centerX - 136 + 38 + 38 + 24 + 38 + 38 + 24, centerY);
		g.drawString(strSplit7, centerX - 136 + 38 + 38 + 24 + 38 + 38 + 24 + 38, centerY);

		// drawhands() - help to calibre hours and minutes vectors and present the
		// seconds vector
		//
		drawHands(g, hour, minute, second, colorSecond.GRAY, colorMHour.BLUE, colorMMinutes.RED);

		// draw a central point clock button - can be used to visually help
		// setting adjustments
		/*
		 * g.setColor(Color.BLUE); g.fillOval(centerX - 5, centerY - 5, 10, 10);
		 * g.setColor(Color.BLUE); g.fillOval(centerX - 3, centerY - 3, 6, 6);
		 */

		double rotationAngle;

		// minutes vector
		int xValues[] = { centerX - 2, centerX - 2, centerX + 2, centerX + 2 };
		int yValues[] = { centerY - 5, centerY - 5 + 120, centerY - 5 + 120, centerY - 5 };
		int nPoints = 4;
		rotationAngle = minute * (180 / 30) + 181;
		drawPoly2(g, centerX, centerY, xValues, yValues, rotationAngle, Color.RED);

		// hour vector
		int[] xValues2 = { centerX - 4, centerX - 4, centerX + 3, centerX + 3 };
		int[] yValues2 = { centerY, centerY + 100, centerY + 100, centerY };
		nPoints = 4;
		rotationAngle = hour * (180 / 6) + 180;
		drawPoly2(g, centerX , centerY, xValues2, yValues2, rotationAngle, Color.BLUE);

	}

	/*-------------Clock Face----------------*/
	/**
	 * Draw the clock face and calls numberclock() that is used to put the cyan
	 * markers on the watchface.
	 * 
	 * 
	 * @param g :graphics
	 */
	public void drawClockFace(Graphics g) {
		// TODO Auto-generated method stub

		// tick marks

		for (int sec = 0; sec < 60; sec++) {
			int ticStart;
			if (sec % 5 == 0) {
				ticStart = size / 2 - 10;
			} else {
				ticStart = size / 2 - 5;
			}

			g.setColor(Color.CYAN);
			drawNumberClock(g);
		}

	}

	/**
	 * Used as an auxiliary method to help calibrating the clock screen
	 * 
	 * @param g:          graphics
	 * @param x:          x coordinate
	 * @param y:          y coordinate
	 * @param angle  the angle of the radius being plotted
	 * @param minRadius the min radius size
	 * @param maxRadius the max radius size
	 * @param colorNumber the color of the radius being plotted
	 */
	// Maintained to calibrate the clock screen
	public void drawRadius(Graphics g, int x, int y, double angle, int minRadius, int maxRadius, Color colorNumber) {
		float sine = (float) Math.sin(angle);
		float cosine = (float) Math.cos(angle);
		int dxmin = (int) (minRadius * sine);
		int dymin = (int) (minRadius * cosine);
		int dxmax = (int) (maxRadius * sine);
		int dymax = (int) (maxRadius * cosine);
		g.setColor(colorNumber);
		g.drawLine(x + dxmin, y + dymin, x + dxmax, y + dymax);
	}
	/*---------------------------------------------*/

	/*----------------Clock Number-----------------*/
	/**
	 * Draws the clock numbers (1,2,..,12).
	 * 
	 * @param g: graphics
	 */
	// draw the clock numbers

	public void drawNumberClock(Graphics g) {
		// TODO Auto-generated method stub
		for (int num = 12; num > 0; num--) {
			drawnum(g, radPerNum * num, num);
		}
	}

	/**
	 * Used to fill the screen with the cyan markers representing hours.
	 * 
	 * @param g:graphics
	 * @param angle:the  angle that the vector form with the axis
	 * 
	 */

	public void drawnum(Graphics g, float angle, int n) {
		// TODO Auto-generated method stub
		float sine = (float) Math.sin(angle);
		float cosine = (float) Math.cos(angle);
		int dx = (int) ((size / 2 - 20 - 25) * -sine);
		int dy = (int) ((size / 2 - 20 - 25) * -cosine);
		g.fillOval(dx + centerX - 5, dy + centerY, 4, 4);
	}
	/*-----------------------------------------------*/

	/**
	 * Draws the hour, minutes and seconds vectors, with its respective colors,
	 * using drawLine graphics method.
	 * 
	 * @param g              : graphics
	 * @param hour:          clock hours number
	 * @param minute:        clock minutes number
	 * @param second:        clock seconds number
	 * @param colorSecond:   Seconds vector color
	 * @param colorMHour:    Hours vector color
	 * @param colorMMinutes: Minutes vector color
	 */
	/*----------------Clock Hands--------------------*/
	public void drawHands(Graphics g, double hour, double minute, double second, Color colorSecond, Color colorMHour,
			Color colorMMinutes) {
		// TODO Auto-generated method stub
		double rsecond = (second * 6) * (Math.PI) / 180;

		double rminute = ((minute + (second / 60)) * 6) * (Math.PI) / 180;

		double rhours = ((hour + (minute / 60)) * 30) * (Math.PI) / 180;

		// draw seconds vector
		g.setColor(colorSecond);
		g.drawLine(centerX, centerY, centerX + (int) (135 * Math.cos(rsecond - (Math.PI / 2))),
				centerY + (int) (135 * Math.sin(rsecond - (Math.PI / 2))));

		// draw hours vector as a single line
		/*
		 * g.setColor(colorMHour); g.drawLine(centerX, centerY, centerX + (int) (90 *
		 * Math.cos(rhours - (Math.PI / 2))) + 20, centerY + (int) (90 * Math.sin(rhours
		 * - (Math.PI / 2))) + 20);
		 * 
		 * Draws a minutes vector as a single line
		 * 
		 * g.setColor(colorMMinutes); g.drawLine(centerX, centerY, centerX + (int) (120
		 * * Math.cos(rminute - (Math.PI / 2))), centerY + (int) (120 * Math.sin(rminute
		 * - (Math.PI / 2))));
		 * 
		 */

	}

	/**
	 * Draws a Polygon with four points (x,y) coordinates and fills it with the last
	 * color selected.
	 * 
	 * @param g graphics
	 * @param xValues x coordinates of an array of points
	 * @param yValues y coordinates of an array of points
	 * @param nPoints number of polygon´s points
	 */
	/*----------------Draw a filled polygon--------------------*/

	public void drawPoly(Graphics g, int xValues[], int yValues[], int nPoints) {
		g.fillPolygon(xValues, yValues, nPoints);

	}

	/**
	 * Rotate a polygon provided an rotation angle ant its points coordinates.
	 * 
	 * @param centerX        : coordinate x
	 * @param centerY        : coordinate y
	 * @param xp:            x coordinates of a points array
	 * @param yp:            y coordinates of a points array
	 * @param rotationAngle: the rotation angle related origin (centerX, centerY)
	 * @return the polygon rotated
	 * @throws IllegalArgumentException
	 */

	/*----------------Rotate a polygon--------------------*/
	public Polygon buildPolygon(int centerX, int centerY, int[] xp, int[] yp, double rotationAngle)
			throws IllegalArgumentException {
		// copy the arrays to reuse them if necessary
		int[] xpoints = Arrays.copyOf(xp, xp.length);
		int[] ypoints = Arrays.copyOf(yp, yp.length);

		if (xpoints.length != ypoints.length) {
			throw new IllegalArgumentException(
					"The provided x points are not the same length as the provided y points.");
		}

		/**
		 * create a list of Point2D pairs
		 */
		ArrayList<Point2D> list = new ArrayList<Point2D>();

		for (int i = 0; i < ypoints.length; i++) {
			list.add(new Point2D.Double(xpoints[i], ypoints[i]));
		}

		/**
		 * creates an array which will hold the rotated points
		 */
		Point2D[] rotatedPoints = new Point2D[list.size()];

		/**
		 * rotates the points
		 */
		AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(rotationAngle), centerX, centerY);
		transform.transform(list.toArray(new Point2D[0]), 0, rotatedPoints, 0, rotatedPoints.length);

		// build the polygon using the rotated points and returns it
		int[] ixp = new int[list.size()];
		int[] iyp = new int[list.size()];

		for (int i = 0; i < ixp.length; i++) {
			ixp[i] = (int) rotatedPoints[i].getX();
			iyp[i] = (int) rotatedPoints[i].getY();
		}
		return new Polygon(ixp, iyp, ixp.length);
	}

	/*----------------Rotate a filled polygon - End --------------------*/

	/*----------------Draw a colored polygon with rotation --------------------*/
	/**
	 * Builds a polygon with a new rotation angle using buildPolygon() and draws it.
	 * 
	 * @param g
	 * @param centerX coordinate x
	 * @param centerY coordinate y
	 * @param xValues  x coordinates of a points array
	 * @param yValues  y coordinates of a points array
	 * @param rotationAngle the rotation angle related origin (centerX, centerY)
	 * @param color The color used to fill the polygon
	 */
	public void drawPoly2(Graphics g, int centerX, int centerY, int[] xValues, int[] yValues, double rotationAngle,
			Color color) {
		g.setColor(color);
		Polygon polygon = buildPolygon(centerX, centerY, xValues, yValues, rotationAngle);
		drawPoly(g, polygon.xpoints, polygon.ypoints, polygon.npoints);
	}
	/*----------------Draw a colored polygon with rotation - End --------------------*/

}
