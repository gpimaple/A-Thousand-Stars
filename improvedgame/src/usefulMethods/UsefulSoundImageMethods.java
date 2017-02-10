package usefulMethods;

import java.awt.Color;
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
				/*
				g2d.setPaint(Color.white);
				g2d.drawOval((int)(-displayed.Radius + screenx),
						(int)(-displayed.Radius + screeny),
						(int)displayed.Radius*2, (int)displayed.Radius*2);//*/
			}

		}
	}
	
	public static void DrawGameBar(Graphics2D g2d)
	{
		g2d.setPaint(Color.GRAY);
		g2d.fillRect(700, 0, 2000, 2000);

		g2d.setPaint(Color.white);	
		g2d.fillRect(710, 20, 80, 20);//pause
		g2d.fillRect(800, 20, 80, 20);//
		g2d.fillRect(890, 20, 80, 20);
		g2d.fillRect(710, 50, 260, 20);
		g2d.setPaint(Color.black);
		if(Main.gameActive == false) { g2d.drawString("PLAY", 713, 35); }
		else{ g2d.drawString("PAUSE", 713, 35); }
		g2d.drawString("TRACING", 893, 35);
		g2d.drawString("ADD_OBJECT", 803, 35);
		g2d.drawString("CHANGE_APPLICATION_SPEED", 713, 65);
		g2d.setPaint(Color.white);
		g2d.drawString("STATS:", 700, 100);
		if(UsefulParticleMethods.GetPlayerIndex() > -1)
		{
			Particle playerparticle = Main.ParticleList.get(UsefulParticleMethods.GetPlayerIndex());
			Entity playerentity = Main.EntityMap.get(playerparticle);
			g2d.drawString("YOUR_HEALTH: " + playerentity.CurrentHealth, 700, 120);


			String shields = "" +(int)(100*(playerentity.EntityShield.CurrentHitpoints/playerentity.EntityShield.MaxHitpoints))+"%";
			if(playerentity.EntityShield.Failed == true){shields = "FAILED";}
			g2d.drawString("YOUR_SHIELDS: " + shields,700,140);

			g2d.drawString("YOUR_RECHARGE: " + (int)(100*(playerentity.EntityWeapon.CurrentReload/playerentity.EntityWeapon.MaxReload)) +"%",700,160);//What weapon the entity has. Every entry will have a name, velocity, spread, damage boost, amount fired at once, how long to reload, and current reload
			g2d.drawString("YOUR_X_VELOCITY: " +(float)(playerparticle.Xvel), 700, 180);
			g2d.drawString("YOUR_Y_VELOCITY: " +(float)(playerparticle.Yvel), 700, 200);
			g2d.drawString("YOUR_ROTATION: " + (int)((180/Math.PI)*playerparticle.Rotation), 700, 220);
			g2d.drawString("RADAR:", 700, 240);
		}
	}
	
	public static Image titlebackground =  Toolkit.getDefaultToolkit().getImage("Resources/Images/TitleScreen.png");
	public static void DrawTitleScreen(Graphics2D g2d)
	{
		g2d.drawImage(titlebackground, 0, 0, null );
	}
}
