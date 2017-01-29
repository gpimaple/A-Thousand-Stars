package mainPackage;

import java.awt.Point;
import java.util.ArrayList;

public class Sector {
	ArrayList<Particle> particlesIn = new ArrayList<Particle>();
	Point Location;
	public Sector(Point location)
	{
		Location = location;
	}
}
