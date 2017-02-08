package usefulMethods;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class UsefulSoundImageMethods {
	public static void PlaySound(String filename)
	{
		 try {
			 File thing = new File("Resources\\Sound\\"+filename);
	         AudioInputStream audioIn = AudioSystem.getAudioInputStream(thing);
	         // Get a sound clip resource.
	         Clip clip = AudioSystem.getClip();
	         clip.open(audioIn);
	         clip.start();
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	}
}
