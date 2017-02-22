package usefulMethods;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import mainPackage.Entity;
import mainPackage.Item;
import mainPackage.Main;
import mainPackage.Particle;

public class UsefulSoundImageMethods{

	public static Clip CurrentBackgroundMusic = null;

	public static void PlayBackgroundMusic(String filename)//plays a clip over and over again
	{
		CurrentBackgroundMusic = null;//stops old background music
		Clip clip = GetSoundClip(filename);//gets new background music
		clip.loop(Clip.LOOP_CONTINUOUSLY);//plays background music
	}


	public static void PlaySound(String filename)
	{
		Clip clip = GetSoundClip(filename);
		clip.start();
	}

	public static Clip GetSoundClip(String filename)
	{
		Clip clip = null;
		try {
			File thing = new File("Resources\\Sound\\"+filename);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(thing);
			// Get a sound clip resource.
			clip = AudioSystem.getClip();
			clip.open(audioIn);

		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return clip;
	}

	//checks if button has been clicked
	public static boolean IsButtonClicked(int x, int y, int width, int height)
	{
		if(Main.mousex >= x && Main.mousex <= x+width && Main.mousey >= y && Main.mousey <=y+height)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	public static void DrawParticles(Graphics2D g2d)
	{
		//renders icons
		for(int i = 0; i < Main.ParticleList.size(); i++)
		{

			Particle displayed = Main.ParticleList.get(i);
			double screenx = displayed.X -Main.screenCenteredX + 350;
			double screeny = displayed.Y -Main.screenCenteredY + 350;

			if(displayed.active == true && screenx < 750 && screenx > -50 && screeny < 750 && screeny > -50)
			{
				if(displayed.Sprite == null)
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
						xPoints[q] = (int)Math.rint(xshift[q]/1 + screenx/1);
						yPoints[q] = (int)Math.rint(yshift[q]/1 + screeny/1);

					}

					g2d.setPaint(displayed.Fill);
					g2d.fillPolygon(xPoints, yPoints, xPoints.length);
					g2d.setPaint(displayed.Outline);
					g2d.drawPolygon(xPoints, yPoints, xPoints.length);	
				}
				else
				{
					Image img = displayed.Sprite;
					int imgwidth = img.getWidth(null);
					int imgheight = img.getHeight(null);
					g2d.drawImage(img, (int)Math.rint(screenx- imgwidth/2), (int)Math.rint(screeny- imgheight/2), null);
				}
				///*
				g2d.setPaint(Color.white);
				g2d.drawOval((int)(-displayed.Radius + screenx),
						(int)(-displayed.Radius + screeny),
						(int)displayed.Radius*2, (int)displayed.Radius*2);//*/
			}

		}
	}

	public static void DrawGameBar(Graphics2D g2d)
	{
		g2d.setFont(new Font("Courier New", Font.PLAIN, 11));
		g2d.setPaint(Color.GRAY);
		g2d.fillRect(700, 0, 2000, 2000);

		g2d.setPaint(Color.white);	
		g2d.fillRect(710, 20, 80, 20);//pause
		g2d.fillRect(800, 20, 80, 20);//
		g2d.fillRect(890, 20, 80, 20);
		g2d.fillRect(710, 50, 260, 20);
		g2d.setPaint(Color.black);
		if(Main.currentScreen == "game")
		{
			if(Main.gameActive == false) { g2d.drawString("PLAY", 713, 35); }
			else{ g2d.drawString("PAUSE", 713, 35); }
		}
		g2d.drawString("SAVE/LOAD", 893, 35);
		if(Main.currentScreen == "inventory")
		{
			g2d.drawString("CLOSE", 803, 35);
		}
		else
		{
			g2d.drawString("INVENTORY", 803, 35);
		}
		g2d.drawString("CHANGE_APPLICATION_SPEED", 713, 65);
		g2d.setPaint(Color.white);
		g2d.drawString("STATS:", 700, 100);
		if(UsefulParticleMethods.GetPlayerIndex() > -1)
		{
			Particle playerparticle = Main.ParticleList.get(UsefulParticleMethods.GetPlayerIndex());
			Entity playerentity = Main.EntityMap.get(playerparticle);
			g2d.drawString("YOUR_HEALTH: " + playerentity.CurrentHealth, 700, 120);


			String shields = "" +(int)(100*(playerentity.Shield.CurrentHitpoints/playerentity.Shield.MaxHitpoints))+"%";
			if(playerentity.Shield.Failed == true){shields = "FAILED";}
			g2d.drawString("YOUR_SHIELDS: " + shields,700,140);

			g2d.drawString("YOUR_RECHARGE: " + (int)(100*(playerentity.Weapon.CurrentWeaponReload/playerentity.Weapon.MaxWeaponReload)) +"%",700,160);
			g2d.drawString("YOUR_X_VELOCITY: " +(float)(playerparticle.Xvel), 700, 180);
			g2d.drawString("YOUR_Y_VELOCITY: " +(float)(playerparticle.Yvel), 700, 200);
			g2d.drawString("YOUR_ROTATION: " + (int)((180/Math.PI)*playerparticle.Rotation), 700, 220);
			g2d.drawString("RADAR:", 700, 240);
		}

		Main.PlayerIndex = UsefulParticleMethods.GetPlayerIndex();
		if(Main.PlayerIndex > -1)
		{ 
			Main.screenCenteredX = (int)(Main.ParticleList.get(Main.PlayerIndex).X);
			Main.screenCenteredY = (int)(Main.ParticleList.get(Main.PlayerIndex).Y);
		}
		if(Main.leftmousepressedrecently == true && Main.currentScreen == "game")//if mouse has been clicked
		{
			if(IsButtonClicked(710, 20,80,20))
			{
				Main.gameActive = !Main.gameActive;
			}
			if(IsButtonClicked(800, 20, 80, 20))
			{
				Main.currentScreen = "inventory";
				Main.gameActive = false;
			}
		}


	}

	public static int draggedindex = -1;
	public static int selectedindex = -1;
	//Also handles inventory stuff
	public static void DrawInventory(Graphics2D g2d)
	{                              
		Color overlay = new Color(70,50,50, 100);
		g2d.setPaint(overlay);
		g2d.fillRect(0,0, 700, 700);
		g2d.setPaint(Color.RED);
		g2d.setFont(new Font("Courier New", Font.BOLD, 20));
		g2d.drawString("INVENTORY", 300, 50);
		g2d.setPaint(Color.RED);
		if(Main.PlayerIndex > -1)
		{                     
			Entity playerentity = Main.EntityMap.get(Main.ParticleList.get(Main.PlayerIndex));
			int inventorysize = playerentity.Inventory.length;
			for(int i = 0; i < 10; i++)
			{          
				for(int a = 0; a < 10; a++)
				{            
					int index = a*10 + i;
					if(index < inventorysize)
					{	
						int squarestartx = 70+(i)*50;
						int squarestarty = 70+(a)*50;
						
						g2d.drawRect(squarestartx, squarestarty, 50,50);
						
						if(index == selectedindex)
						{
							g2d.setPaint(Color.GREEN);
							g2d.drawRect(squarestartx+1,squarestarty+1, 48,48);
							g2d.setPaint(Color.RED);
						}
						
						if(playerentity.Inventory[index] != null//if there is something in the slot
								&& draggedindex != index)//if the sprite is not being dragged
						{
							Image sprite = playerentity.Inventory[index].Sprite;
							g2d.drawImage(sprite, squarestartx+10, squarestarty+10, null);
						}
						
						
						//handles selecting
						if(Main.rightmousepressedrecently == true 
								&& IsButtonClicked(squarestartx+10, squarestarty+10,32,32))//if mouse has been clicked
						{
							selectedindex = index;
						}
						
						if(index == selectedindex &&  playerentity.Inventory[selectedindex] != null)
						{
							Item selecteditem = playerentity.Inventory[selectedindex];
							g2d.setPaint(Main.ColorBlack);
							g2d.fillRect(60, 580, 2000, 70);
							g2d.setPaint(Color.GREEN);
							g2d.setFont(new Font("Courier New", Font.PLAIN, 11));
							g2d.drawString("NAME:"+selecteditem.Name, 70, 600);
							g2d.drawString("TYPE:"+selecteditem.Type, 400, 600);
							g2d.drawString("VALUE:"+selecteditem.Value, 530, 600);
							if(selecteditem.IsGenerator || selecteditem.IsShield || selecteditem.IsThruster || selecteditem.IsWeapon)
							{
								g2d.setPaint(Color.WHITE);
								g2d.fillRect(650, 590, 220, 20);
								g2d.setPaint(Color.BLACK);
								if(		selecteditem == playerentity.Generator || 
										selecteditem == playerentity.Shield ||
										selecteditem == playerentity.Thruster ||
										selecteditem == playerentity.Weapon)
								{
									g2d.drawString("ALREADY_EQUIPPED_AS_" + selecteditem.Type, 660, 600);
								}
								else
								{
									g2d.drawString("EQUIP_AS_" + selecteditem.Type, 660, 600);
								}								
								g2d.setPaint(Color.GREEN);
							}
							g2d.drawString("DESCRIPTION:"+selecteditem.Description, 70, 620);
							g2d.drawString("STATS:", 70, 640);
							if(selecteditem.IsGenerator)
							{
								g2d.drawString("POWER_PER_TICK:" + (float)selecteditem.PowerGenerated,  120, 640);
								g2d.drawString("MAX_POWER_HELD:" + (float)selecteditem.MaxPower, 260, 640);
							}
							else if(selecteditem.IsThruster)
							{
								g2d.drawString("ACCELERATION:" + (float)selecteditem.Acceleration,  120, 640);
								g2d.drawString("POWER_CONSUMPTION:" + (float)selecteditem.PowerConsumptionPerThrust, 260, 640);
							}
							else if(selecteditem.IsShield)
							{
								g2d.drawString("DAMAGE_MULTIPLIER:" + (float)selecteditem.DamageMultiplier,  120, 640);
								g2d.drawString("HITPOINTS:" + (float)selecteditem.MaxHitpoints,  280, 640);
								g2d.drawString("FIX_TIME:" + (float)selecteditem.FixTime,  400, 640);
								g2d.drawString("POWER_CONSUMPTION:" + (float)selecteditem.PowerConsumptionPerRegenTick,  520, 640);
							}
							else if(selecteditem.IsWeapon)
							{
								g2d.drawString("OBJECT_FIRED:" + selecteditem.EntityFired,  120, 640);
								g2d.drawString("OBJECT_VELOCITY:" + (float)selecteditem.Velocity,  340, 640);
								g2d.drawString("RELOAD:" + (float)Math.rint(selecteditem.MaxWeaponReload/selecteditem.WeaponRegeneration),  500, 640);
								g2d.drawString("SPREAD:" + Math.rint(1000*selecteditem.Spread*(180/Math.PI))/1000 + "°",  590, 640);
								g2d.drawString("BULLETS_FIRED:" + selecteditem.NumberFiredAtOnce,  680, 640);
								g2d.drawString("POWER_CONSUMPTION:" +(float)selecteditem.PowerConsumptionWhenFired,  800, 640);
							}
							g2d.setPaint(Color.RED);
						}
						
						
						//handles switching
						if(Main.leftmousepressedrecently == true 
								&& IsButtonClicked(squarestartx+10, squarestarty+10,32,32)
								&& playerentity.Inventory[index] != null)//if mouse has been clicked
						{
							draggedindex = index;
						}
						if(Main.leftmousereleasedrecently == true
								&& IsButtonClicked(squarestartx+10, squarestarty+10,32,32)
								&& draggedindex != -1)
						{
							Item item1 = playerentity.Inventory[draggedindex];
							Item item2 = playerentity.Inventory[index];

							playerentity.Inventory[index] = item1;
							playerentity.Inventory[draggedindex] = item2;
							draggedindex = -1;
						}
						
					}
				}
			}
			
			if(selectedindex != -1 && IsButtonClicked(650, 590, 220, 20))
			{
				Item selecteditem = playerentity.Inventory[selectedindex]; 
				if(selecteditem.IsGenerator)
				{
					playerentity.Generator = selecteditem;
				}
				if(selecteditem.IsThruster)
				{
					playerentity.Thruster = selecteditem;
				}
				if(selecteditem.IsShield)
				{
					playerentity.Shield = selecteditem;
				}
				if(selecteditem.IsWeapon)
				{
					playerentity.Weapon = selecteditem;
				}
			}
			
			if(Main.leftmousedown == true
					&& draggedindex != -1)
			{
				Image img = playerentity.Inventory[draggedindex].Sprite;
				int imgxoffset = Math.round(img.getWidth(null)/2);
				int imgyoffset = Math.round(img.getHeight(null)/2);
				g2d.drawImage(img, (int)Math.rint(Main.mousex-imgxoffset), (int)Math.rint(Main.mousey-imgyoffset), null);
			}
			else
			{
				draggedindex = -1;
			}
			
		}
		if(Main.leftmousepressedrecently == true)//if mouse has been clicked
		{
			if(IsButtonClicked(800, 20,80,20))
			{
				Main.currentScreen = "game";
			}
		}
	}


	public static Image titlebackground =  Toolkit.getDefaultToolkit().getImage("Resources/Images/TitleScreen.png");
	public static void DrawTitleScreen(Graphics2D g2d)
	{
		g2d.drawImage(titlebackground, 0, 0, null );
		g2d.setFont(new Font("Courier New", Font.PLAIN, 40));
		g2d.setPaint(Color.white);
		if(IsButtonClicked(280, 250, 420, 70))
		{
			g2d.fillRect(280, 250, 420, 70);
			g2d.setPaint(Color.black);
			if(Main.leftmousepressedrecently)
			{
				Main.currentScreen = "game";
			}
		}
		g2d.drawString("A Thousand Stars", 300, 300);
	}
}
