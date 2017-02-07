package mainPackage;

import java.awt.Point;
import java.util.ArrayList;

public class Sector {
	public ArrayList<Particle> particlesIn = new ArrayList<Particle>();
	public Point Location;
	public Sector(Point location)
	{
		Location = location;
	}
}
