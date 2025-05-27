import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener {
	
	//for any debugging code we add
	public static boolean debugging = true;
	
	//Timer related variables
	int waveTimer = 5; //each wave of enemies is 20s
	long ellapseTime = 0;
	Font timeFont = new Font("Courier", Font.BOLD, 70);
	int level = 0;
	int score = 0;
	boolean gameOver = false;
	
	Font myFont = new Font("Monospaced", Font.BOLD, 30);
	SimpleAudioPlayer backgroundMusic = new SimpleAudioPlayer("scifi.wav", true);
	SimpleAudioPlayer winner = new SimpleAudioPlayer("win.wav", false);
	SimpleAudioPlayer points = new SimpleAudioPlayer("points.wav", false);

//	Music soundBang = new Music("bang.wav", false);
//	Music soundHaha = new Music("haha.wav", false);
	
	//stays at the top so all other objects go on the bottom
	Background theBackground = new Background(0,0);
	allLivesGone liveBackground = new allLivesGone(0,0);
	gameWon gameWon = new gameWon(0,0);
	
	penguin myPenguin = new penguin(250, 670);
	Present present1 = new Present(250, 25);
	
	//penguin myPenguin2 = new penguin(100, 200);
	
	//a row of penguinScrolling objects
	PenguinScroller[] row1 = new PenguinScroller[10];
	
	//frame width/height
	int width = 600;
	int height = 800;	
	
	IceBlock myIce = new IceBlock(200, 400);
	IceScroller[] row2 = new IceScroller[5];
	IcedScroller[] row2i =  new IcedScroller[4];
	ArrayList<IcedScroller> row1List = new ArrayList<IcedScroller>();
	ArrayList<lives> lives = new ArrayList<lives>();
	
	JellyfishScroller[] row3 = new JellyfishScroller[4];
	
	PolarBearScroller[] row4 = new PolarBearScroller[4];
	
	PolarBearOtherScroller[] row4i = new PolarBearOtherScroller[4];
	SealScroller[] row5 = new SealScroller[4];
	
	DeerScroller[] row6 = new DeerScroller[4];
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		theBackground.paint(g);
		present1.paint(g);
		backgroundMusic.play();
		//ADD TEXT
		g.setFont(myFont);
		g.setColor(new Color(255,255,255));
		g.drawString("score:"+score, 450,30); 
		//these two need to be here because the penguin goes on top of these objects
		
		
		boolean riding = false;
		for(IcedScroller obj : row2i) {
			obj.paint(g);
			
		}
		for(IcedScroller obj : row2i) {
			if(obj.collided(myPenguin)) {
				myPenguin.setvx(obj.getvx());
				riding = true;
				break;
			}
		}
		for(IceScroller obj : row2) {
			if(obj.collided(myPenguin)) {

				myPenguin.setvx(obj.getvx());
				riding = true;
				break;
			}
		}
		//main character stops moving if not on a rideable object
		//but also lets limit it in the y
		
		if((!riding && myPenguin.getY() == 370) || (!riding && myPenguin.getY() == 220)) {//|| (!riding && myPenguin.getY() < 380 && !riding && myPenguin.getY() < 420)) {
			riding = false;
			resetPenguin();
			//if i riding any the duck is in the water area
			//reset back to starting 
			System.out.println("you fell into the water");
			JOptionPane.showMessageDialog(null,"You fell in the water and drowned!");


		}
		else if (!riding ) {
			myPenguin.setvx(0);
			//System.out.println("you fell into the water");

		}
		
		
		for(IceScroller obj : row2) {
			obj.paint(g);
		}
		//paint the other objects on the screen
		myPenguin.paint(g);

	

		//have the row 1 objects paint on the screen
		//or each obj in row 
		
		for(IcedScroller obj : row1List) {
			obj.paint(g);
		}
		if(present1.collided(myPenguin)) {
			//winningAudio = true;
			myPenguin.setvx(0);
			myPenguin.x = 250;
			myPenguin.y = 670;
			score++;
			points = new SimpleAudioPlayer("points.wav", false);
			points.play();
			
		
		}
		
		//jelly fish
		//myJellyFish.paint(g);
		for(JellyfishScroller obj : row3) {
			obj.paint(g);
			if(obj.collided(myPenguin)) {
				resetPenguin();
				JOptionPane.showMessageDialog(null,"The jellyfish stung you!");
				
			}
		}
		
		//polar bear obj
		//myPolarBear.paint(g);
		for(PolarBearScroller obj : row4) {
			obj.paint(g);
			if(obj.collided(myPenguin)) {
				resetPenguin();
				JOptionPane.showMessageDialog(null,"You were hit by the polar bear!");
				
			}
		}
		
		
		for(PolarBearOtherScroller obj : row4i) {
			obj.paint(g);
			if(obj.collided(myPenguin)) {
				resetPenguin();
				JOptionPane.showMessageDialog(null,"You were hit by the polar bear!");
				
			}
		}
		//mySeal.paint(g);
		for(SealScroller obj : row5) {
			obj.paint(g);
			if(obj.collided(myPenguin)) {
				resetPenguin();
				JOptionPane.showMessageDialog(null,"The seal made you drown");
				
			}
		}
		
		for(DeerScroller obj : row6) {
			obj.paint(g);
			if(obj.collided(myPenguin)) {
				resetPenguin();
				JOptionPane.showMessageDialog(null,"The deer knocked you over!");
				
			}
		}
		
		for(lives obj : lives){
			//draw the lives images
			obj.paint(g);
		}
		if(score >= 3) {
			gameWon.paint(g);
			winner = new SimpleAudioPlayer("win.wav", false);
			//score = 0;
			winner.play();
		
		}
		if(lives.size() <=0) {
			liveBackground.paint(g);
		}
		
		if(gameOver) {
			myPenguin.x = 250;
			myPenguin.y = 670;
			liveBackground.paint(g);
		}
		
		
	}
	public void resetPenguin() {
		myPenguin.setvx(0);
		myPenguin.x = 250;
		myPenguin.y = 670;
		if(lives.size() > 1) {
			lives.remove(lives.size()-1);
		}else  {
			gameOver = true;			
			
			}
		
	}

	public void resetGame() {
		gameOver = false;
		myPenguin.setvx(0);
		myPenguin.x = 250;
		myPenguin.y = 670;
		score = 0;
		resetLives();
		
	}
	
	public void resetLives(){
		for(int i = 0; i <6; i++) {
			this.lives.add(new lives(i*40, 730));
		}
	}
	
	//collision detection
	//for each ghost object in row array
	
	
	public static void main(String[] arg) {
		Frame f = new Frame();
		
	}
	
	public Frame() {
		JFrame f = new JFrame("Duck Hunt");
		f.setSize(new Dimension(width, height));
		f.setBackground(Color.white);
		f.add(this);
		f.setResizable(false);
 		f.addMouseListener(this);
		f.addKeyListener(this);
	
//		backgroundMusic.play();

		// setup any 1d array here - create objects that go in them;l
	// traverse the array
		for(int i = 0; i < row1.length; i++) {
			row1[i] = new PenguinScroller(i*200,300); 
		}
		
		
		//practice row for arraylist
		//for(int i = 0; i < 10; i++) {
		//	this.row1List.add(new IcedScroller(i*210, 345));
		//}
		
		for( int j = 0; j <row2.length; j++) {
			row2[j] = new IceScroller(j*200, 200);
		}
		
		for(int y =0; y < row2i.length; y++) {
			row2i[y] = new IcedScroller(y*200, 345);
		}
		
		for(int k = 0; k< row3.length; k++ ) {
			row3[k] = new JellyfishScroller(k*200,270);
		}
		
		for(int n = 0; n< row4.length; n++) {
			row4[n] = new PolarBearScroller(n*200, 450);
		}
		
		for(int r = 0; r <row4i.length; r++) {
			row4i[r] = new PolarBearOtherScroller(r*200, 570);
		}
		for(int p =0; p < row5.length; p++) {
			row5[p] = new SealScroller(p*200, 140);
		}
		
		for (int e =0; e <row6.length; e++) {
			row6[e] = new DeerScroller(e*250, 500);
		}
		
		
		//start with 6 attempts
		for(int i = 0; i <6; i++) {
			this.lives.add(new lives(i*40, 730));
			
		}
		//the cursor image must be outside of the src folder
		//you will need to import a couple of classes to make it fully 
		//functional! use eclipse quick-fixes
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon("torch.png").getImage(),
				new Point(0,0),"custom cursor"));	
		
		
		Timer t = new Timer(16, this);
		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent m) {
		
	
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getKeyCode());
		if(arg0.getKeyCode() ==87) {
			//move character up
			myPenguin.move(0);
		}else if (arg0.getKeyCode() ==83) {
			 myPenguin.move(1);
			//move character down
		}
		
		if(arg0.getKeyCode() ==65) {
			myPenguin.move(2);
		}else if (arg0.getKeyCode() ==68) {
			myPenguin.move(3);
		}
		
		if(arg0.getKeyCode() ==10) {
			if(score >= 3) {
				resetGame();
			}
			if(gameOver) {
				resetGame();
			}
			
		}
		if(arg0.getKeyCode() == 82) {
			resetGame();
		}
		
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
