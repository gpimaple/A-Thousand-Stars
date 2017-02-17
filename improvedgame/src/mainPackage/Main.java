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

import usefulMethods.Sector;
import usefulMethods.UsefulParticleMethods;
import usefulMethods.UsefulSoundImageMethods;
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

	public static String currentScreen = "title";
	public static int screenCenteredX = 0;
	public static int screenCenteredY = 0;

	public static double mousex = 0;
	public static double mousey = 0;
	public static boolean mousedown = false;
	public static boolean mouseclickedrecently = true;

	public static boolean spacekeydown = false;
	public static boolean upkeydown = false;
	public static boolean downkeydown = false;
	public static boolean leftkeydown = false;
	public static boolean rightkeydown = false;

	public static boolean gameActive = false;
	
	public static final int MapSize = 30;
	public static final int SectorSize = 64;
	
	
	
	
	
	
	public static int PlayerIndex = -1;


	public static Sector[][] SectorList = new Sector[MapSize][MapSize];
	public static ArrayList<Particle> ParticleList  = new ArrayList<Particle>();
	public static HashMap<Particle, Entity> EntityMap = new HashMap<Particle, Entity>();
	public static HashMap<Entity, AI> AIMap = new HashMap<Entity, AI>();


	void initialize()
	{
		UsefulParticleMethods.CreatePlayer(500,100);
		///*
		for(int i = 0; i < 30; i++)
		{
			for(int q = 0; q < 30;q++)
			{
				if( i%8 < 8 && q%8 < 8)
				{
					UsefulParticleMethods.CreateAsteroid(200+i*6.4, 200+q*6.4, 10);
				}
			}
		}
		//*/
		int bob = UsefulParticleMethods.CreateAsteroid(100,100, 5000);
		//Particle thing = ParticleList.get(bob);
		//thing.Xvel = -1;
	}
	long initialdate = 0;
	long miliseconds = 0;
	long gamemiliseconds = 0;
	void gameloop() throws InterruptedException
	{
		Date date = new Date();
		
		miliseconds = date.getTime()-initialdate;
		initialdate = date.getTime();
		gamemiliseconds = 50;
		Thread.sleep(50);
		
		//System.out.println((float)((double)gamemiliseconds/(double)miliseconds));
		
		if(gameActive == true)
		{
			for(int a = 0; a < 10; a++)//10 steps
			{
				for(int i = ParticleList.size()-1; i >= 0 ; i--)//run it backward
				{
					UpdateParticle(i);//updates the particles, location, velocity, 
					UpdateEntity(i);//updates health
				}
				UsefulWorldGenMethods.ResetSectors();
				UsefulParticleMethods.CheckCollision();		
				KillParticles();//kills particles
				UpdatePriorities();// updates whether something is active or not.	
			}	
		}
		repaint();//paints the scene, and lets you can click on stuff
		
	}




	void UpdateEntity(int i)
	{
		Particle particle = ParticleList.get(i);
		Entity updated = EntityMap.get(particle);
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

			if(updated.Shield != null)
			{
				
				if(updated.Shield.CurrentHitpoints < updated.Shield.MaxHitpoints &&
						updated.Generator.CurrentPower >= updated.Shield.PowerConsumptionPerRegenTick)
				{
					updated.Shield.CurrentHitpoints += updated.Shield.RegenPerTick;
					if(updated.Shield.CurrentHitpoints > updated.Shield.MaxHitpoints)
					{
						updated.Shield.CurrentHitpoints = updated.Shield.MaxHitpoints;
					}
					updated.Generator.CurrentPower -= updated.Shield.PowerConsumptionPerRegenTick;
				}

				if(updated.Shield.CurrentHitpoints < 0)
				{
					updated.Shield.Failed = true;
					updated.Shield.CurrentFix = 0;
					updated.Shield.CurrentHitpoints = updated.Shield.MaxHitpoints;
				}

				if(updated.Shield.Failed == true)
				{
					updated.Shield.CurrentFix += 1;
					if(updated.Shield.CurrentFix > updated.Shield.FixTime)
					{
						updated.Shield.Failed = false;
					}
				}
			}
			if(updated.Weapon != null)
			{
				if(updated.Weapon.CurrentWeaponReload < updated.Weapon.MaxWeaponReload)
				{
					updated.Weapon.CurrentWeaponReload += updated.Weapon.WeaponRegeneration;
				}
				
				if(updated.Weapon.CurrentWeaponReload > updated.Weapon.MaxWeaponReload)
				{
					updated.Weapon.CurrentWeaponReload = updated.Weapon.MaxWeaponReload;
				}
			}
			
			if(updated.Generator != null)
			{
				if(updated.Generator.CurrentPower < updated.Generator.MaxPower)
				{
					updated.Generator.CurrentPower += updated.Generator.PowerGenerated;
					if(updated.Generator.CurrentPower > updated.Generator.MaxPower)
					{
						updated.Generator.CurrentPower = updated.Generator.MaxPower;
					}
				}
				System.out.println(updated.Generator.CurrentPower);
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
					UsefulParticleMethods.MoveEntity(particle);
				}
				if(spacekeydown == true)
				{
					UsefulParticleMethods.ShootEntity(particle);
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
				updated.Xvel = -updated.Xvel;
			}
			if(updated.Y < 0)
			{
				updated.Y = 0;
				updated.Yvel = -updated.Yvel;
			}
			if(updated.X > SectorSize*MapSize)
			{
				updated.X = SectorSize*MapSize;
				updated.Xvel = -updated.Xvel;
			}
			if(updated.Y > SectorSize*MapSize)
			{
				updated.Y = SectorSize*MapSize;
				updated.Yvel = -updated.Yvel;
			}
		}

		if(updated.countdown < 0)
		{
			updated.tobedestroyed = true;
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
						if(distance > updated.Priority*3)//if it is too far
						{
							if(Math.sqrt(updated.Xvel*updated.Xvel + updated.Yvel*updated.Yvel) < 0.2)
							updated.active = false;
						}
					}
					else
					{
						if(distance < updated.Priority/1.1)//if it is close enough that it will run
						{
							updated.active = true;
						}
					}
				}
			}
		}
	}




	public static Color ColorBlack = new Color(10,10,10,255); 
	@Override
	public void paint(Graphics g) 
	{
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setFont(new Font("Courier New", Font.PLAIN, 11));
		setBackground(Color.gray);
		g2d.setPaint(ColorBlack);
		g2d.fillRect(0, 0, 700, 700);
		
		if(currentScreen == "game")
		{
			UsefulSoundImageMethods.DrawParticles(g2d);
			UsefulSoundImageMethods.DrawGameBar(g2d);
		}
		else if(currentScreen == "inventory")
		{
			UsefulSoundImageMethods.DrawParticles(g2d);
			UsefulSoundImageMethods.DrawGameBar(g2d);
			UsefulSoundImageMethods.DrawInventoryInGame(g2d);
		}
		else if(currentScreen == "title")
		{
			UsefulSoundImageMethods.DrawTitleScreen(g2d);
		}
		mouseclickedrecently = false;
	}

	public static void main(String[] args)
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
			try
			{
				game.gameloop();		
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}

