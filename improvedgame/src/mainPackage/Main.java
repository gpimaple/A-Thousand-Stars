package mainPackage;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;

import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import usefulMethods.UsefulParticleMethods;
import usefulMethods.UsefulWorldGenMethods;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;



@SuppressWarnings("serial")
public class Main extends JPanel implements KeyListener, MouseListener, MouseMotionListener
{

	public Main(JFrame frame)
	{		
		addMouseListener(this);
		addMouseMotionListener(this);
		frame.addKeyListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {} // don't need this stuff
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override

	public void mousePressed(MouseEvent e) 
	{
		mouseclickedrecently = true;
		mousedown = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		mousedown = false;
	}


	@Override
	public void mouseDragged(MouseEvent e) 
	{
		mousex = e.getX();
		mousey = e.getY();
	}


	@Override
	public void mouseMoved(MouseEvent e) 
	{	
		mousex = e.getX();
		mousey = e.getY();
	}

	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			spacekeydown = true;	
		}
		if(e.getKeyCode() == KeyEvent.VK_UP)
		{
			upkeydown = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			downkeydown = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			leftkeydown = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			rightkeydown = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
		{
			spacekeydown = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP)
		{
			upkeydown = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			downkeydown = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			leftkeydown = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			rightkeydown = false;
		}
	}
	@Override
	public void keyTyped(KeyEvent e) 
	{

	}

	String gameoutcome = "";
	int screenCenteredX = 0;
	int screenCenteredY = 0;

	public static double mousex = 0;
	public static double mousey = 0;
	public static boolean mousedown = false;
	public static boolean mouseclickedrecently = true;

	public static boolean spacekeydown = false;
	public static boolean upkeydown = false;
	public static boolean downkeydown = false;
	public static boolean leftkeydown = false;
	public static boolean rightkeydown = false;

	boolean gamepaused = true;
	
	public static final int MapSize = 1000;
	public static final int SectorSize = 64;
	
	
	
	
	
	
	int PlayerIndex = UsefulParticleMethods.GetPlayerIndex();


	public static Sector[][] SectorList = new Sector[MapSize][MapSize];
	public static ArrayList<Particle> ParticleList  = new ArrayList<Particle>();
	public static HashMap<Particle, Entity> EntityMap = new HashMap<Particle, Entity>();	
	

	void initialize()
	{
		UsefulParticleMethods.CreatePlayer(-100,100);
		
		///*
		for(int i = 0; i < 9; i++)
		{
			for(int q = 0; q < 9;q++)
			{
				UsefulParticleMethods.CreateAsteroid(1000+i*80, 1000+q*80, 500);
			}
		}
		//*/
		//int bob = UsefulParticleMethods.CreateAsteroid(100,100, 1000);
		//Particle thing = ParticleList.get(bob);
		//thing.Xvel = -1;
	}
	long initialdate = 0;
	long miliseconds = 0;
	long gamemiliseconds = 0;
	void functions()
	{
		GUI();
		Date date = new Date();
		
		miliseconds = date.getTime()-initialdate;
		initialdate = date.getTime();
		gamemiliseconds = 50;

		
		System.out.println((float)((double)gamemiliseconds/(double)miliseconds));
		
		if(gamepaused == false)
		{
			for( int a = 0; a < 10; a++)//10 steps
			{
				for(int i = ParticleList.size()-1; i >= 0 ; i--)//run it backward
				{
					UpdateParticle(i);//updates the particles, location, velocity, 
					UpdateEntity(i);//updates health
					UpdateSector(i);
					CheckCollision(i);		
				}
				KillParticles();//kills particles
			}
		}
		UpdatePriorities();// updates whether something is active or not.
		UsefulWorldGenMethods.ClearSectors();
		repaint();//paints the scene
	}



	void GUI()
	{
		PlayerIndex = UsefulParticleMethods.GetPlayerIndex();
		if(PlayerIndex > -1)
		{ 
			screenCenteredX = (int)(ParticleList.get(PlayerIndex).X);
			screenCenteredY = (int)(ParticleList.get(PlayerIndex).Y);
		}
		if(mouseclickedrecently == true)//if mouse has been clicked
		{
			if(UsefulParticleMethods.IsButtonClicked(710, 20,80,20))
			{
				gamepaused = !gamepaused;
			}
			mouseclickedrecently = false;
		}
	}

	void UpdateEntity(int i)
	{
		Entity updated = EntityMap.get(ParticleList.get(i));
		if(updated != null && updated.Host.active == true)
		{
			updated.Host.countdown += 1;
			updated.CurrentHealth += updated.HealthRegen;
			if(updated.CurrentHealth > updated.MaxHealth)
			{
				updated.CurrentHealth = updated.MaxHealth;
			}
			else if(updated.CurrentHealth < 0)
			{
				updated.Host.tobedestroyed = true;
			}

			if(updated.EntityShield != null)
			{
				updated.EntityShield.CurrentHitpoints += updated.EntityShield.Regeneration;


				if(updated.EntityShield.CurrentHitpoints > updated.EntityShield.MaxHitpoints)
				{
					updated.EntityShield.CurrentHitpoints = updated.EntityShield.MaxHitpoints;
				}

				if(updated.EntityShield.CurrentHitpoints < 0)
				{
					updated.EntityShield.Failed = true;
					updated.EntityShield.CurrentHitpoints = updated.EntityShield.MaxHitpoints;
				}

				if(updated.EntityShield.Failed == true)
				{
					updated.EntityShield.countdown--;
					if(updated.EntityShield.countdown < 0)
					{
						updated.EntityShield.Failed = false;
						updated.EntityShield.countdown = updated.EntityShield.SetupTime;
					}
				}
			}
			if(updated.EntityWeapon != null)
			{
				updated.EntityWeapon.CurrentReload += updated.EntityWeapon.Regeneration;
				if(updated.EntityWeapon.CurrentReload > updated.EntityWeapon.MaxReload)
				{
					updated.EntityWeapon.CurrentReload = updated.EntityWeapon.MaxReload;
				}
			}

			if(updated.Type == "player")
			{
				if(leftkeydown == true)
				{
					updated.Host.Rotation -= 0.03;
					if(updated.Host.Rotation < -Math.PI)
					{
						updated.Host.Rotation += 2*Math.PI;
					}
				}
				if(rightkeydown == true)
				{
					updated.Host.Rotation += 0.03;
					if(updated.Host.Rotation > Math.PI)
					{
						updated.Host.Rotation -= 2*Math.PI;
					}
				}
				if(upkeydown == true)
				{
					UsefulParticleMethods.MoveEntity(i);
				}
				if(spacekeydown == true)
				{
					UsefulParticleMethods.ShootEntity(i);
				}
			}


		}
	}

	void UpdateParticle(int i)
	{

		Particle updated = ParticleList.get(i);


		if(updated.active == true)
		{
			updated.countdown -=1;
			updated.X += updated.Xvel;
			updated.Y += updated.Yvel;
			if(updated.X < 0)
			{
				updated.X = 0;
			}
			if(updated.Y < 0)
			{
				updated.Y = 0;
			}
			if(updated.X > SectorSize*MapSize)
			{
				updated.X = SectorSize*MapSize;
			}
			if(updated.Y > SectorSize*MapSize)
			{
				updated.Y = SectorSize*MapSize;
			}
		}

		if(updated.countdown < 0)
		{
			updated.tobedestroyed = true;
		}
	}



	public void CheckCollision(int i)//Wikipedia is awesome. this is an elastic collision.
	{
		Particle p1 = ParticleList.get(i);//first particle in collision
		if(EntityMap.get(p1) != null && p1.active == true)//check that it has an entity. Dont want to lag on checking nonentity
		{
			double r1 = p1.Radius;
			for(int q = 0; q < ParticleList.size(); q++)
			{
				if(i != q && EntityMap.get(ParticleList.get(q))!=null && ParticleList.get(q).active == true)
				{
					Particle p2 = ParticleList.get(q);
					double r2 = p2.Radius;
					double distance = UsefulParticleMethods.GetDistance(p1, p2);
					if(distance < r1 + r2)
					{
						UsefulParticleMethods.CollideParticles(i, q);
					}
				}
			}
		}
	}

	public void KillParticles()
	{
		for(int i = ParticleList.size()-1; i >= 0 ; i--)//run it backward
		{
			if(ParticleList.get(i).tobedestroyed == true)
			{
				UsefulParticleMethods.KillParticle(i);
			}
		}
	}
	
	
	public void UpdateSector(int i)
	{
		Particle updated = ParticleList.get(i);
		if(updated.active)
		{
			Entity updatedEntity = EntityMap.get(updated);
			if(updatedEntity != null)
			{
				int sectorX = (int)(updated.X/SectorSize);
				int sectorY = (int)(updated.Y/SectorSize);
				
				Sector sector = SectorList[sectorX][sectorY];
				if(sector == null)
				{
					sector = new Sector(new Point(sectorX, sectorY));
				}
				if(!sector.particlesIn.contains(updated))
				{
					sector.particlesIn.add(updated);
				}
			}
		}	
	}
	
	
	
	public void UpdatePriorities()
	{
		PlayerIndex = UsefulParticleMethods.GetPlayerIndex();
		if(PlayerIndex != -1)
		{

			Particle PlayerParticle = ParticleList.get(PlayerIndex);
			for(int i = 0; i < ParticleList.size(); i++)
			{
				Particle updated = ParticleList.get(i);
				if(EntityMap.get(updated)!=null)//if we want only things with entities can be active or not, to cut down on lag from explosions
				{
					double distance = UsefulParticleMethods.GetDistance(updated, PlayerParticle);//gets distance
					if(updated.active == true)
					{
						if(distance > updated.Priority)//if it is too far
						{
							updated.active = false;
						}
					}
					else
					{
						if(distance < updated.Priority/2)//if it is close enough that it will run
						{
							updated.active = true;
						}
					}
				}
			}
		}
	}





	@Override
	public void paint(Graphics g) 
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(new Font("Courier New", Font.PLAIN, 11));
		setBackground(Color.gray);
		Color ColorBlack = new Color(10,10,10,255);
		g2d.setPaint(ColorBlack);
		g2d.fillRect(0, 0, 700, 700);
		
		///*
		g2d.setPaint(Color.WHITE);
		for(int i = 0; i < 30; i++)
		{
			for(int q = 0; q < 30; q++)
			{
				g2d.drawRect(i*SectorSize+350-screenCenteredX, q*SectorSize+350-screenCenteredY, SectorSize, SectorSize);
			}
		}//*/
	//renders icons
		for(int i = 0; i < ParticleList.size(); i++)
		{

			Particle displayed = ParticleList.get(i);
			double screenx = displayed.X -screenCenteredX + 350;
			double screeny = displayed.Y -screenCenteredY + 350;

			if(displayed.active == true && screenx < 700 && screenx > 0 && screeny < 700 && screeny > 0)
			{

				double[] finalrotations = new double[displayed.Directions.length];
				System.arraycopy(displayed.Directions, 0, finalrotations, 0, displayed.Directions.length);
				for(int q = 0; q < finalrotations.length; q++)
				{
					finalrotations[q] += displayed.Rotation; //rotates
				}
				double[] xshift = new double[finalrotations.length];
				double[] yshift = new double[finalrotations.length];//creates an empty array for now
				double[] magnitudes = displayed.Magnitudes;
				for(int q = 0; q < finalrotations.length; q++)
				{
					xshift[q] = magnitudes[q] * (Math.cos(finalrotations[q])); // changes into x and y 
					yshift[q] = magnitudes[q] * (Math.sin(finalrotations[q]));
				}

				int[] xPoints = new int[finalrotations.length];
				int[] yPoints = new int[finalrotations.length];
				for(int q = 0; q < finalrotations.length; q++)
				{
					xPoints[q] = (int)Math.rint(xshift[q] + screenx);
					yPoints[q] = (int)Math.rint(yshift[q] + screeny);

				}

				g2d.setPaint(displayed.Fill);
				g2d.fillPolygon(xPoints, yPoints, xPoints.length);
				g2d.setPaint(displayed.Outline);
				g2d.drawPolygon(xPoints, yPoints, xPoints.length);	
				/*
				g2d.setPaint(Color.white);
				g2d.drawOval((int)(displayed.X-displayed.Radius)-screenCenteredX+350,
						(int)(displayed.Y-displayed.Radius)-screenCenteredY+350,
						(int)displayed.Radius*2, (int)displayed.Radius*2);//*/
			}

		}

		g2d.setPaint(Color.GRAY);
		g2d.fillRect(700, 0, 2000, 2000);

		g2d.setPaint(Color.white);	
		g2d.fillRect(710, 20, 80, 20);//pause
		g2d.fillRect(800, 20, 80, 20);//
		g2d.fillRect(890, 20, 80, 20);
		g2d.fillRect(710, 50, 260, 20);
		g2d.setPaint(Color.black);
		if(gamepaused == true) { g2d.drawString("PLAY", 713, 35); }
		else{ g2d.drawString("PAUSE", 713, 35); }
		g2d.drawString("TRACING", 893, 35);
		g2d.drawString("ADD_OBJECT", 803, 35);
		g2d.drawString("CHANGE_APPLICATION_SPEED", 713, 65);
		g2d.setPaint(Color.white);
		g2d.drawString("STATS:", 700, 100);
		if(UsefulParticleMethods.GetPlayerIndex() > -1)
		{
			Particle playerparticle = ParticleList.get(UsefulParticleMethods.GetPlayerIndex());
			Entity playerentity = EntityMap.get(playerparticle);
			g2d.drawString("YOUR_HEALTH: " + playerentity.CurrentHealth, 700, 120);


			String shields = "" +(int)(100*(playerentity.EntityShield.CurrentHitpoints/playerentity.EntityShield.MaxHitpoints))+"%";
			if(playerentity.EntityShield.Failed == true){shields = "FAILED";}
			g2d.drawString("YOUR_SHIELDS: " + shields,700,140);

			g2d.drawString("YOUR_RECHARGE: " + (int)(100*(playerentity.EntityWeapon.CurrentReload/playerentity.EntityWeapon.MaxReload)) +"%",700,160);//What weapon the entity has. Every entry will have a name, velocity, spread, damage boost, amount fired at once, how long to reload, and current reload
			g2d.drawString("YOUR_X_VELOCITY: " +(float)(playerparticle.Xvel), 700, 180);
			g2d.drawString("YOUR_Y_VELOCITY: " +(float)(playerparticle.Yvel), 700, 200);
			g2d.drawString("YOUR_ROTATION: " + (int)((180/Math.PI)*playerparticle.Rotation), 700, 220);
			g2d.drawString("RADAR:", 700, 240);
			g2d.setPaint(ColorBlack);
			g2d.fillRect(700, 350, 280, 280);
			g2d.setPaint(Color.green);
			g2d.drawRect(700, 350, 280, 280);
			g2d.drawOval(700, 350, 280, 280);
			g2d.drawOval(700+40, 350+40, 200, 200);
			g2d.drawOval(700+80, 350+80, 120, 120);
			g2d.drawOval(700+120, 350+120, 40, 40);
			g2d.fillOval(700+138, 350+138, 5, 5);
		}


	}

	public static void main(String[] args) throws InterruptedException 
	{
		JFrame frame = new JFrame("A Thousand Stars");
		Main game = new Main(frame);
		frame.add(game);
		frame.setSize(1000, 700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.initialize();
		while (true) 
		{
			game.functions();
			Thread.sleep(50);		
		}
	}
}

