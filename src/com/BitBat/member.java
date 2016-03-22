package com.BitBat;

import java.io.*;

class Rat implements Runnable
{
	int x;
	int y;

	int ratStatus=1;
	int ratPointFlag;
  	int sleepTime=1000;
  	
  	boolean gamePanelLive;

  	public Rat(int ratPointFlag)
  	{
  		this.ratPointFlag=ratPointFlag;
  	}

  	public void flagChoosePoint()
  	{
  		switch(ratPointFlag)
  		{
  		case 0:
  			x=103;
  			y=220;
  			break;
  		case 1:
  			x=583;
  			y=220;
  			break;
  		case 2:
  			x=313;
  			y=280;
     		break;
  		case 3:
  			x=53;
  			y=355;
  			break;
  		case 4:
  			x=533;
  			y=353;
  			break;
  		case 5:
  			x=274;
  			y=482;
  			break;
  		case 6:
  			x=583;
  			y=537;
  			break;
  		case 7:
  			x=127;
  			y=618;
  		case 8:
  			x=1200;
  			y=1200;
  			ratStatus=2;
    	}
  	}
 	public int getSleepTime() {
 		return sleepTime;
 	}
 	public void setSleepTime(int sleepTime) {
 		this.sleepTime=sleepTime;
 	}

 	public void ratPointChangeSpeed()
 	{
 		if((Recorder.getYouBitRatNum()>5)&&(Recorder.getYouBitRatNum()<=10))
 		{
 			this.setSleepTime(900);
 		} else if((Recorder.getYouBitRatNum()>10)&&(Recorder.getYouBitRatNum()<=15)){
 			this.setSleepTime(800);
 		} else if((Recorder.getYouBitRatNum()>15)&&(Recorder.getYouBitRatNum()<=20)){
 			this.setSleepTime(700);
 		} else if((Recorder.getYouBitRatNum()>20)&&(Recorder.getYouBitRatNum()<=25)){
 			this.setSleepTime(600);
 		} else if (Recorder.getYouBitRatNum()>25){
 			this.setSleepTime(300);
    	}
 	 }

  	public void run()
  	{
  		while(true)
    	{
  			if(GamePanel.isLive==false&&gamePanelLive==true)
	    	{
  				gamePanelLive=false;
	    		break;
	    	}
  			
  			this.ratPointChangeSpeed();
  			switch(ratStatus)
  			{
  			case 0:
  				try{
  					Thread.sleep(800);
  				} catch (InterruptedException e) {
  					e.printStackTrace();
  				}
  				ratStatus=1;
  				break;
  			case 1:
  				try{
  					Thread.sleep(sleepTime);
  				}
  				catch (InterruptedException e) {
  					e.printStackTrace();
  				}
  				if((ratStatus==1)&&(GamePanel.isLive==true))
  				{
  					Recorder.youNotBitRatNumPlus();
  				}
  				
        		if(ratStatus==0)
        		{
        			Recorder.youBitRatNumPlus();
        		}
        		if(ratStatus==1)
        		{
        			ratStatus=0;
        		}
        		ratPointFlag=((int)(Math.random()*9));
        		this.flagChoosePoint();
  				break;
  			case 2:
  				try {
					Thread.sleep(sleepTime+800*2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
  				ratStatus=1;
  				ratPointFlag=((int)(Math.random()*8));
  				this.flagChoosePoint();
  				break;
  			default:
  				break;	
  			}
    	}
  	}
}
class Recorder
{
	private static int youBitRatNum=0;
	private static int bestYouBitRatNum=0;
  	private static int youNotBitRatNum=0;

  	private static FileWriter fw=null;
  	private static BufferedWriter bw=null;
  	private static FileReader fr=null;
  	private static BufferedReader br=null;

  	public static int getYouBitRatNum()
  	{
  		return youBitRatNum;
  	}

  	public static void setYouBitRatNum(int youBitRatNum) {
  		Recorder.youBitRatNum=youBitRatNum;
  	}

  	public static int getBestYouBitRatNum() {
  		return bestYouBitRatNum;
  	}

  	public static void setBestYouBitRatNum(int bestYouBitRatNum) {
  		Recorder.bestYouBitRatNum=bestYouBitRatNum;
  	}
  	public static int getYouNotBitRatNum() {
  		return youNotBitRatNum;
  	}

 	public static void setYouNotBitRatNum(int youNotBitRatNum) {
 		Recorder.youNotBitRatNum=youNotBitRatNum;
 	}

 	public static void youBitRatNumPlus()
 	{
 		youBitRatNum+=1;
 	}
 	
 	public static void youNotBitRatNumPlus() {
 		youNotBitRatNum+=1;
 	}

 	public static void writeData()
  	{
 		if(youBitRatNum>bestYouBitRatNum)
 		{
 			File file=new File("d:\\素材\\打地鼠\\BitRat.txt");
 			try{
 				fw=new FileWriter(file);
 				bw=new BufferedWriter(fw);
 				String s="";
 				s=youBitRatNum+"";
 				bw.write(s);
 			}catch (IOException e) {
 				e.printStackTrace();
 			}finally{
				try{
 					bw.close();
 					fw.close();
 				}catch (IOException e) {
 					e.printStackTrace();
 				}	
 			}

 		}
  	}

 	public static void readData()
 	{
 		File file=new File("D:\\AppLibs\\BitRat_Java\\BitRat.txt");
 		if(file.exists()==false)
			{
				try{
					file.createNewFile();
				}catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("文件已存在");
		}
 		try{
 			fr=new FileReader(file);
 			br=new BufferedReader(fr);
 			String s="";
 			try {
 				s=br.readLine();
 				if(s==null)
 				{
 					s="0";
 				}
 			} catch (IOException e){
 				e.printStackTrace();
 			}
 			bestYouBitRatNum=Integer.parseInt(s);
 		} catch (FileNotFoundException e) {
 			e.printStackTrace();
    	}finally{
    		try {
    			br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
 	}
}
class Blast
{
	int x;
	int y;
	int life=15;
	boolean isLive=true;

	public Blast(int x,int y)
  	{
		this.x=x;
		this.y=y;
  	}

	public int getLife() {
    	return life;
  	}
	public void setLife(int life) {
		this.life=life;
	}

	public void lifeReduce()
	{
		if(this.life>0)
		{
			life-=1;
		}else 
		{
			isLive=false;
		}
	}
}
class Bomb implements Runnable
{
	int x;
	int y;
	int bombStatus=2;
	int bombPointFlag;
	int ratSleepTime;
	int ratStatus;
	
	boolean gamePanelLive;
	
	public Bomb(int bombPointFlag)
	{
		this.bombPointFlag=bombPointFlag;
		this.flagChoosePoint();
	}
	public void flagChoosePoint()
	{
		switch(bombPointFlag)
  		{
  		case 0:
  			x=103;
  			y=220;
  			break;
  		case 1:
  			x=583;
  			y=220;
  			break;
  		case 2:
  			x=313;
  			y=280;
     		break;
  		case 3:
  			x=53;
  			y=355;
  			break;
  		case 4:
  			x=533;
  			y=353;
  			break;
  		case 5:
  			x=274;
  			y=482;
  			break;
  		case 6:
  			x=583;
  			y=537;
  			break;
  		case 7:
  			x=127;
  			y=618;
    	}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			if(GamePanel.isLive==false&&gamePanelLive==true)
	    	{
  				gamePanelLive=false;
	    		break;
	    	}
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bombStatus=1;
			try {
				Thread.sleep(ratSleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bombStatus=2;
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}
}