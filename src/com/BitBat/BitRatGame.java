package com.BitBat;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.util.Vector;

public class BitRatGame extends JFrame implements Runnable{
	
	GamePanel gamePanel=null;
	MenuPanel menuPanel=null;
	AboutGamePanel aboutGamePanel=null;
	StartPanel startPanel=null;
	EndPanel endPanel=null;

	Image imageRat=null;

	Rat rat=null;

	String menuCommand="";
	String endCommand="";

	boolean startPanelFlag=false;

	public static void main(String[] args){
		
		BitRatGame bitRatGame=new BitRatGame();
		Thread bitRatGameThread=new Thread(bitRatGame);
		bitRatGameThread.start();
    }
	public BitRatGame() 
	{
		imageRat=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\rat.png");
		this.setIconImage(imageRat);

		menuPanel=new MenuPanel();
		this.add(menuPanel);
		Thread menuPanelThread=new Thread(menuPanel);
		menuPanelThread.start();

		menuCommand=menuPanel.getMenuButtonCommand();
		aboutGamePanel=new AboutGamePanel();
		startPanel=new StartPanel();

		endPanel=new EndPanel();

		this.setSize(800, 788);
		this.setLocation(550, 100);
		this.setTitle("BitRat");
		this.setDefaultCloseOperation(3);
		this.setResizable(false);
		
		this.addWindowListener(new WindowAdapter(){
			   @Override
			   public void windowClosing(WindowEvent e) {
				  Recorder.writeData();
			      System.exit(0);
			  }
		});


		this.setVisible(true);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try{
				Thread.sleep(100L);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}

			if(menuPanel.isLive())
			{
				menuCommand=menuPanel.getMenuButtonCommand();
				if(menuCommand.equals("newGame"))
				{
					menuPanel.setLive(false);

					Recorder.readData();

					this.remove(menuPanel);

					this.add(startPanel);
					Thread startPanelThread=new Thread(startPanel);
					startPanelThread.start();

					this.setVisible(true);
				}
				else if(menuCommand.equals("aboutGame"))
				{
	        	
					menuPanel.setLive(false);

					this.remove(menuPanel);
					this.add(aboutGamePanel);
					Thread menuPanelThread=new Thread(aboutGamePanel);
					menuPanelThread.start();

					this.setVisible(true);
				}
			}
			if (!aboutGamePanel.isLive())
			{
				aboutGamePanel.setLive(true);

				this.remove(aboutGamePanel);
				menuPanel=new MenuPanel();
				this.add(menuPanel);
				menuPanel.setLive(true);
				Thread menuPanelThread=new Thread(menuPanel);
				menuPanelThread.start();

				this.setVisible(true);
			}
			if (!startPanel.isLive())
			{
				startPanel.setLive(true);
				this.remove(startPanel);

				gamePanel=new GamePanel();
				this.add(gamePanel);
				GamePanel.isLive=true;
				gamePanel.rat.gamePanelLive=true;
				gamePanel.bomb.gamePanelLive=true;
				
				Thread myPanelThread=new Thread(gamePanel);
				myPanelThread.start();

				this.setVisible(true);
			}
			if (!EndPanel.isLive())
			{
				endCommand=endPanel.getEndButtonCommand();
				if (endCommand.equals("restart"))
				{
					EndPanel.setLive(true);

					this.remove(endPanel);

					this.add(startPanel);
					Recorder.setYouNotBitRatNum(0);
					Recorder.setYouBitRatNum(0);
					Recorder.readData();

					Thread startPanelThread=new Thread(startPanel);
					startPanelThread.start();

					this.setVisible(true);
				}
			}
			if(Recorder.getYouNotBitRatNum()>=5)
			{
				Recorder.setYouNotBitRatNum(0);

				this.remove(gamePanel);
				GamePanel.isLive=false;

				this.add(endPanel);
				Thread endPanelThread = new Thread(endPanel);
				endPanelThread.start();

				this.setVisible(true);
			}if(gamePanel.isLive==true)
			{
				if(gamePanel.isYouBitBomb==true)
				{
					this.remove(gamePanel);
					GamePanel.isLive=false;

					this.add(endPanel);
					Thread endPanelThread = new Thread(endPanel);
					endPanelThread.start();

					this.setVisible(true);
				}
			}
	    }
	}
}

class MenuPanel extends JPanel implements Runnable, MouseListener, MouseMotionListener
{
	Image imageMenuRat=null;
	Image imageMenuHammer1=null;
	Image imageMenuHammer2=null;
	Image iamgeMenuBlast1=null;
	Image iamgeMenuBlast2=null;
 	Image iamgeMenuBlast3=null;
 	int time=16;

 	int buttonFlag=0;

 	boolean isButton=false;
 	
 	boolean isStartButton=false;
 	boolean isExitButton=false;
 	boolean isAboutButton=false;

 	String menuButtonCommand="";

 	boolean isLive=true;

 	public boolean isLive() 
 	{
 		return isLive;
 	}

 	public void setLive(boolean isLive)
 	{
 		this.isLive=isLive;
 	}

 	public String getMenuButtonCommand() 
 	{
 		return menuButtonCommand;
 	}

 	public void setMenuButtonCommand(String menuButtonCommand) 
 	{
 		menuButtonCommand = menuButtonCommand;
 	}

 	public MenuPanel()
 	{
 		this.addMouseMotionListener(this);
 		this.addMouseListener(this);

 		imageMenuRat=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\rat.png");
 		imageMenuHammer1=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\hammer1.png");
 		imageMenuHammer2=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\hammer2.png");
 		iamgeMenuBlast1=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\blast_1.gif");
 		iamgeMenuBlast2=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\blast_2.gif");
 		iamgeMenuBlast3=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\blast_3.gif");

    	Font font=new Font("Î¢ÈíÑÅºÚ", 1, 35);
    	this.setFont(font);
 	}

 	public void paint(Graphics g)
 	{
 		super.paint(g);

 		this.menuCartoon(g);

 		this.paintMenuButton(g);
 	}

 	public void paintMenuButton(Graphics g)
 	{
 		if(isStartButton==true)
 		{
 			g.setColor(new Color(132,128,110));
 			g.drawString("Start Game", 335, 405);
 		}else{
 			g.setColor(Color.black);
 			g.drawString("Start Game", 335, 405);
 		}
 		if(isExitButton==true)
 		{
 			g.setColor(new Color(132,128,110));
 			g.drawString("Exit Game", 335, 505);
 		}else{
 			g.setColor(Color.black);
 			g.drawString("Exit Game", 335, 505);
 		}
 		if(isAboutButton==true)
 		{
 			g.setColor(new Color(132,128,110));
 			g.drawString("About Game", 335, 605);
 		}else{
 			g.setColor(Color.black);
 			g.drawString("About Game", 335, 605);
 		}
 		g.setColor(Color.black);
    
 		if (isButton)
 		{
 			switch(buttonFlag)
 			{
 			case 1:
 				g.drawRoundRect(303, 363, 200, 60, 20, 20);
 				break;
 			case 2:
 				g.drawRoundRect(303, 463, 200, 60, 20, 20);
 				break;
 			case 3:
 				g.drawRoundRect(303, 563, 200, 60, 20, 20);
 				break;
 			}
 		}
 	}
 	
 	public void menuCartoon(Graphics g)
 	{
 		if(this.time>11)
 		{
 			g.drawImage(imageMenuRat, 340, 150, 120, 120, this);
 			g.drawImage(imageMenuHammer1, 460, 90, 60, 60, this);
 		}else if(time>6&&time<=11)
 		{
 			g.drawImage(imageMenuRat, 340, 150, 120, 120, this);
 			g.drawImage(imageMenuHammer2, 460, 90, 60, 60, this);
 		}else if(time>4&&time<=6)
 		{
 			g.drawImage(iamgeMenuBlast1, 340, 150, 120, 120, this);
 			g.drawImage(imageMenuHammer1, 460, 90, 60, 60, this);
 		}else if(time>2&&time<=4)
 		{
 			g.drawImage(iamgeMenuBlast2, 340, 150, 120, 120, this);
 			g.drawImage(imageMenuHammer1, 460, 90, 60, 60, this);
 		}else if(time>0&&time<=2)
 		{
 			g.drawImage(iamgeMenuBlast3, 340, 150, 120, 120, this);
 			g.drawImage(imageMenuHammer1, 460, 90, 60, 60, this);
 		}
 	}
 	@Override
 	public void run()
 	{
 		while(true)
 		{
 			try{
 				Thread.sleep(50);
 			}
 			catch (InterruptedException e) {
 				e.printStackTrace();
 			}
 			
 			time-= 1;
 			
 			if(time <= 0)
 			{
 				time = 20;
 			}
 			this.repaint();
 		}
 	}

 	@Override
 	public void mouseDragged(MouseEvent e) {
 		// TODO Auto-generated method stub
	
 	}

 	@Override
 	public void mouseMoved(MouseEvent e) {
 		// TODO Auto-generated method stub
 		if(isLive)
 		{
 			if((e.getX()>303)&&(e.getX()<503)&&(e.getY()>363)&&(e.getY()<423))
 			{
 				buttonFlag = 1;
 				isButton = true;
 				isStartButton=true;
 			}else{
 				isStartButton=false;
 			}
		 	if((e.getX()>303)&&(e.getX()<503)&&(e.getY()>463)&&(e.getY()<523))
		  	{
		 		buttonFlag=2;
		 		isButton=true;
		 		isExitButton=true;
		  	}else{
		  		isExitButton=false;
		  	}
		 	if((e.getX()>303)&&(e.getX()<503)&&(e.getY()>563)&&(e.getY()<623))
		 	{
		 		buttonFlag=3;
		 		isButton=true;
		 		isAboutButton=true;
		  	}else{
		  		isAboutButton=false;
		  	}
		 	
		 	if(!(isStartButton||isAboutButton||isExitButton))
		 	{
		 		isButton=false;
		 	}
	  	}
 	}

 	@Override
 	public void mouseClicked(MouseEvent e) {
 		// TODO Auto-generated method stub
	
 	}

 	@Override
	public void mousePressed(MouseEvent e) {
 		// TODO Auto-generated method stub
 		if(isLive)
 		{
		 	if(isButton)
		 	{
		 		switch(buttonFlag)
		 		{
		 		case 1:
		 			menuButtonCommand = "newGame";
		 			break;
		 		case 2:
		 			System.exit(0);
		 			break;
		 		case 3:
		 			menuButtonCommand = "aboutGame";
		 			break;
		 		}
		 	}
 		}
 	}

 	@Override
 	public void mouseReleased(MouseEvent e) {
 		// TODO Auto-generated method stub
	
 	}

 	@Override
 	public void mouseEntered(MouseEvent e) {
 		// TODO Auto-generated method stub
	
 	}

 	@Override
 	public void mouseExited(MouseEvent e) {
 		// TODO Auto-generated method stub
	
 	}
}
class AboutGamePanel extends JPanel implements Runnable, MouseListener, MouseMotionListener
{
	boolean isLive=true;
	boolean isButton=false;
	String buttonCommand="";
	Font font=null;

	public AboutGamePanel() 
	{
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.font=new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 35);
    	this.setFont(font);
	}
	public boolean isLive() 
	{
		return isLive;
	}
	public void setLive(boolean isLive)
	{
		this.isLive=isLive;
	}

	public void paint(Graphics g)
	{
		super.paint(g);

		g.drawString("Version£º", 100, 100);
		g.drawString("-----1.0", 200, 150);
		if(isButton==true)
		{
			g.setColor(new Color(132,128,110));
			g.drawString("Back", 550, 600);
		}else{
			g.drawString("Back", 550, 600);
		}
		g.setColor(Color.black);
    	if(this.isButton)
    	{
    		g.drawRoundRect(481, 558, 200, 60, 20, 20);
    	}
	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		if(isLive)
		{
			if(isButton)
			{
				this.buttonCommand="gaBack";
				this.isLive=false;
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e)
	{
	}
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}
	@Override
	public void mouseExited(MouseEvent e)
	{
	}
	@Override
	public void mouseDragged(MouseEvent e)
	{
	}
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if(isLive)
		{
			if((e.getX()>481)&&(e.getX()<681)&&(e.getY()>558)&&(e.getY()<618))
			{
				isButton = true;
			}else {
				isButton = false;
			}
		}
	}
	@Override
	public void run()
	{
		while (true)
		{
			try{
				Thread.sleep(50L);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.repaint();
		}
	}
}

class StartPanel extends JPanel implements Runnable
{
	int time=3;
	boolean flag=true;
	GamePanel gamePanel=null;
  	boolean isLive=true;

  	public boolean isLive() 
  	{
  		return this.isLive;
  	}
  	public void setLive(boolean isLive)
  	{
  		this.isLive=isLive;
  	}

  	public void paint(Graphics g)
  	{
  		super.paint(g);

  		Font font=new Font("Tahoma", Font.BOLD, 55);
  		g.setFont(font);

  		if(flag)
  		{
  			g.drawString(time+"", 380, 370);
  		}
  	}
  	@Override
  	public void run()
 	{
  		while(true)
  		{
  			try{
  				Thread.sleep(500L);
  			}
  			catch(InterruptedException e) {
  				e.printStackTrace();
  			}
  			if(flag)
  			{
  				time-=1;
  				flag=!flag;
  			}else {
  				flag=!flag;
  			}
  			if(time==0)
  			{
  				time=3;
  				isLive=false;
  				break;
  			}

  			this.repaint();
  		}
 	}
}
class GamePanel extends JPanel implements Runnable, MouseListener
{
	Image imageRat=null;
	Image imageBackground=null;

	Image imageBlast1=null;
	Image imageBlast2=null;
	Image imageBlast3=null;
  
	Vector<Blast> blasts=new Vector();

	Rat rat = null;
	int mouseX;
 	int mouseY;
 	Cursor cursor1=null;
 	Cursor cursor2=null;
 	Image imageHammer1=null;
 	Image imageHammer2=null;
 	
 	Image imageBomb=null;
 	Bomb bomb=null;

 	static boolean isLive=false;
 	
 	boolean isYouBitBomb=false;
 	
 	Thread bombThread=null;

 	int times=30;
 	public void paint(Graphics g)
 	{
 		super.paint(g);
 		g.drawImage(imageBackground, 0, 50, 794, 728, this);
 		RecordeInfo(g);

 		if (rat.ratStatus==1)
 		{
 			g.drawImage(imageRat, rat.x, rat.y, 120, 120, this);
    	}
 		if(bomb.bombStatus==1)
 		{
 			g.drawImage(imageBomb, bomb.x, bomb.y, 120, 120,this);
 		}
 		this.isPaintBlast(g);
 	}

 	public GamePanel()
 	{
 		imageRat=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\rat.png");
 		imageBackground=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\background.png");
 		try {
 			imageBlast1=ImageIO.read(new File("D:\\AppLibs\\BitRat_Java\\blast_1.gif"));
 			imageBlast2=ImageIO.read(new File("D:\\AppLibs\\BitRat_Java\\blast_2.gif"));
 			imageBlast3=ImageIO.read(new File("D:\\AppLibs\\BitRat_Java\\blast_3.gif"));
 		}
 		catch (IOException e) {
 			e.printStackTrace();
 		}
 		rat=new Rat((int)(Math.random()*8));
 		rat.flagChoosePoint();
 		Thread ratThread=new Thread(rat);
 		ratThread.start();
 		
 		bomb=new Bomb((int)(Math.random()*8));
// 		Thread bombThread=new Thread(bomb);
//		bombThread.start();
 		
 		
 		this.addMouseListener(this);

 		imageHammer1=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\hammer1.png");
 		cursor1=Toolkit.getDefaultToolkit().createCustomCursor(imageHammer1, new Point(16, 16), "hammer1");
 		imageBomb=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\bomb.png");
 		this.setCursor(cursor1);
 	}

 	public void RecordeInfo(Graphics g)
 	{
 		Font font=new Font("Tahoma", 1, 25);
 		g.setColor(new Color(47, 178, 102));
 		g.fillRect(0, 0, 800, 50);

 		g.setColor(Color.black);
 		g.setFont(font);
 		g.drawString("Score:", 75, 32);
 		g.drawString(Recorder.getYouBitRatNum()+"", 175, 32);

 		g.drawString("NotBit:", 320, 32);
 		g.drawString(Recorder.getYouNotBitRatNum()+"", 420, 32);

 		g.drawString("Recorder:", 550, 32);
 		g.drawString(Recorder.getBestYouBitRatNum()+"", 700, 32);
 	}

 	public void isBitRat()
 	{
 		if((mouseX>=rat.x)&&(mouseX<=rat.x+120)&&(mouseY>=rat.y)&&(mouseY<=rat.y+120)&&(rat.ratStatus==1))
 		{
 			rat.ratStatus=0;
 			mouseX=0;
 			mouseY=0;

 			Blast blast=new Blast(rat.x,rat.x);
 			blasts.add(blast);
 		}
 	}
 	public void isBitBomb()
 	{
 		if((mouseX>=bomb.x)&&(mouseX<=bomb.x+120)&&(mouseY>=bomb.y)&&(mouseY<=bomb.y+120)&&(bomb.bombStatus==1))
 		{
 			bomb.bombStatus=0;
 			isYouBitBomb=true;
 			
 			mouseX=0;
 			mouseY=0;
 			
 			Blast blast=new Blast(rat.x,rat.y);
 			blasts.add(blast);
 		}
 	}

 	public void isPaintBlast(Graphics g)
 	{
 		for (int i=0; i<blasts.size(); i++)
 		{
 			Blast blast=blasts.get(i);
 			while(blast.getLife()>0)
 			{
 				if(blast.getLife()>10)
 				{
 					g.drawImage(imageBlast1, rat.x, rat.y, 120, 120, this);
 				}else if (blast.getLife() > 5)
 				{
 					g.drawImage(imageBlast2,rat.x,rat.y, 120, 120, this);
 				} else if (blast.getLife() > 0)
 				{
	        		g.drawImage(imageBlast3,rat.x,rat.y, 120, 120, this);
	        	}
 				blast.lifeReduce();
     		}
     		if (blast.getLife()==0)
     		{
     			blasts.remove(blast);
     		}
 		}
 	}

 	public void cursorCartoon()
 	{
 		imageHammer2=Toolkit.getDefaultToolkit().getImage("D:\\AppLibs\\BitRat_Java\\hammer2.png");
 		cursor2=Toolkit.getDefaultToolkit().createCustomCursor(imageHammer2, new Point(16, 16), "hammer2");
 		setCursor(cursor2);
 	}

 	public void run()
 	{
 		while (true)
 		{
 			try
 			{
 				Thread.sleep(10);
 			}
 			catch (InterruptedException e) {
 				e.printStackTrace();
 			}
			bomb.ratSleepTime=rat.getSleepTime();
 			bomb.ratStatus=rat.ratStatus;
 			if(rat.ratStatus==2&&bombThread==null)
 			{
 				bombThread=new Thread(bomb);
 				bombThread.start();
 			}
 			if(rat.ratStatus==1)
 			{
 				bombThread=null;
 			}
 			this.isBitRat();
 			this.isBitBomb();
 			
 			mouseX=0;
 			mouseY=0;
 			
 			this.repaint();
 		}
 	}

	public void mouseClicked(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		mouseX=e.getX();
		mouseY=e.getY();

		this.cursorCartoon();
	}

	public void mouseReleased(MouseEvent e)
	{
		this.setCursor(cursor1);
	}

	public void mouseEntered(MouseEvent e)
	{
  	}

	public void mouseExited(MouseEvent e)
	{
  	}
}
class EndPanel extends JPanel implements Runnable, MouseListener, MouseMotionListener
{
	static boolean isLive=true;
	int buttonFlag=0;
	boolean isButton=false;
	boolean btnIsRestart=false;
	boolean btnIsExit=false;
	String endButtonCommand="";

	public String getEndButtonCommand() 
	{
		return endButtonCommand;
	}	
	public void setEndButtonCommand(String endButtonCommand)
	{
		this.endButtonCommand=endButtonCommand;
  	}
	public static boolean isLive() 
	{
		return isLive;
	}
	public static void setLive(boolean isLive) 
	{
		EndPanel.isLive=isLive;
	}
	public EndPanel()
 	{
		this.addMouseMotionListener(this);
		this.addMouseListener(this);

		Font font=new Font("Î¢ÈíÑÅºÚ", Font.BOLD, 35);
		setFont(font);
 	}

	public void paint(Graphics g)
  	{
		super.paint(g);

		if(btnIsRestart==true)
	 	{
	 		g.setColor(new Color(215,202,153));
	 		g.drawString("ReStart", 335, 405);
	 	}else{
	 		g.setColor(Color.black);
	 		g.drawString("Play", 335, 405);
	 	}
		if(btnIsExit==true)
	 	{
	 		g.setColor(new Color(215,202,153));
	 		g.drawString("Exit", 335, 505);
	 	}else{
	 		g.setColor(Color.black);
	 		g.drawString("Exit", 335, 505);
	 	}		
		g.setColor(Color.black);
		Font font=new Font("Tahoma", Font.BOLD, 55);
		g.setFont(font);
		g.drawString("You Lose", 290, 275);

		if(isLive)
		{
			if(this.isButton)
			{
				switch(this.buttonFlag)
				{
				case 1:
					g.drawRoundRect(303, 363, 200, 60, 20, 20);
					break;
				case 2:
					g.drawRoundRect(303, 463, 200, 60, 20, 20);
					break;
				}
			}
		}
  	}
	@Override
	public void run()
	{
		while(true)
		{
			try{
				Thread.sleep(200L);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!isLive)
			{
				break;
			}
			
			this.repaint();
		}
 	 }
  	@Override
	public void mouseDragged(MouseEvent e)
	{
	}
  	@Override
	public void mouseMoved(MouseEvent e)
	{
		if(isLive)
		{
			if((e.getX()>303)&&(e.getX()<503)&&(e.getY()>363)&&(e.getY()<423))
			{
				buttonFlag=1;
				btnIsRestart=true;
				isButton=true;
			}else{
				btnIsRestart=false;
			}
			if((e.getX()>303)&&(e.getX()<503)&&(e.getY()>463)&&(e.getY()<523))
			{
				buttonFlag=2;
				btnIsExit=true;
				isButton=true;
			}else {
				btnIsExit=false;
			}
			if(!(btnIsRestart||btnIsExit))
			{
				isButton=false;
			}
		}
  	}
  	@Override
	public void mouseClicked(MouseEvent e)
	{
	}
  	@Override
	public void mousePressed(MouseEvent e)
	{
   	 	if(isLive)
   	 	{
   	 		if(isButton)
   	 		{
   	 			switch(buttonFlag)
   	 			{
   	 			case 1:
   	 				endButtonCommand="restart";
   	 				Recorder.writeData();
   	 				isLive=false;
   	 				break;
   	 			case 2:
   	 				Recorder.writeData();
   	 				System.exit(0);
   	 				break;
   	 			}
   	 		}
   	 	}
	}
  	@Override
	public void mouseReleased(MouseEvent e)
	{
  	}
  	@Override
 	public void mouseEntered(MouseEvent e)
 	{
  	}
  	@Override
 	public void mouseExited(MouseEvent e)
 	{
  	}
}