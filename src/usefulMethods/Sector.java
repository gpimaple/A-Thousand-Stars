package usefulMethods;

import java.awt.Point;
import java.util.ArrayList;

import mainPackage.Particle;

public class Sector {
	public ArrayList<Particle> particlesIn = new ArrayList<Particle>();
	public Point Location;
	public Sector(Point location)
	{
		Location = location;
	}
}
