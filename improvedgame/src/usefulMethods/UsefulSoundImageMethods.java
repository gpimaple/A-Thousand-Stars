package usefulMethods;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

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

	
}
