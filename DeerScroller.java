import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class DeerScroller{
	private Image forward, backward, left, right; 	
	private AffineTransform tx;
	
	//attributes of the class
	int dir = 0; 					//0-forward, 1-backward, 2-left, 3-right
	int width, height;				//collision detection 
	int x, y;						//position of the object
	int vx, vy;						//movement variables
	double scaleWidth = 1.5;		//change to scale image
	double scaleHeight = 1.5; 		//change to scale image

	public DeerScroller() {
		forward 	= getImage("/imgs/"+"deer.png"); //load the image for Tree
		//backward 	= getImage("/imgs/"+"backward.png"); //load the image for Tree
		//left 		= getImage("/imgs/"+"left.png"); //load the image for Tree
		//right 		= getImage("/imgs/"+"right.png"); //load the image for Tree

		//width and height or hit box
		width  = 50;
		height = 50;
		
		//used for placement
		x = -width; //of screen for now
		y = 300;
		
		//if your movement will not be hopping base
		vx = -3;
		vy = 0;
		
		tx = AffineTransform.getTranslateInstance(0, 0);
		
		init(x, y); 				//initialize the location of the image
									//use your variables
		
	}
public boolean collided(penguin character) {
		
		
		//represent each object as a rectangle
		//then check if they re intersecting
		Rectangle main = new Rectangle(
				character.getX(),
				character.getY(),
				character.getWidth(),
				character.getHeight()
				);
		
		Rectangle thisObject = new Rectangle(x, y, width, height);
		
		//user built-in method to check intersection(COllISION)
		return main.intersects(thisObject);
	}
	//second constructor - allow setting x and y during construction
	public DeerScroller(int x, int y) {
		//call the default constructor for all the normal stuff
		this();//invokes original constructor
		
		//do the specific task for this constructor
		//this.x 
		this.x = x;
		this.y = y;
	}
	
	
	
	
	public void paint(Graphics g) {
		//these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;
		
		//update x and y if using vx,, vy variables
		
		x+=vx;
		y+=vy;	
		
		//for infinite scrolling - teleport to the other side
		//once it leave the other side
		if(x < -150) {
			x = 650;
		}
		
		init(x,y);
		
		g2.drawImage(forward, tx, null);
		//draw hit box based on x, y , width, height
		//for collison detection
		if(Frame.debugging) {
			g.setColor(Color.gray);
			g.drawRect(x, y, width, height);
		}

	}
	
	private void init(double a, double b) {
		tx.setToTranslation(a, b);
		tx.scale(scaleWidth, scaleHeight);
	}

	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = DeerScroller.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

}
