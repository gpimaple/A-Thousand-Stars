package mainPackage;
import java.awt.Color;
//remember, all these classes are just wrappers meant to hold the values.
/* The particle is meant to display and move according to its velocity. 
 * It will disappear eventually. It is controlled by entity.
 */

public class Particle
{
	public double Priority = 1000; // this measures how far away (in pixels) it can be from the ship and still be rendered. Low for asteroids, (~3000) medium for enemies, (~6000), and very high for stars (~10000
	public double X;
	public double Y;
	public double Mass;
	public double Radius;
	public double GravityMultiplier = 1;
	public double Xvel = 0;
	public double Yvel = 0; //xvel, yvel, and rotation are optional. They're there if you want them. 
	public double Rotation = 0;//If you don't define them, they default to zero.
	public double[] Magnitudes;
	public double[] Directions;
	public Color Fill;// fill color
	public Color Outline;//outline color
	public int countdown = 1000;// This decreases by 1 each tick. If it reaches 0, then the particle removes itself. Not needed if it has an AI
	public boolean active = true;
	public boolean tobedestroyed = false;
	public Particle(double x, double y, double rotation, double mass, double radius, double[] directions, double[] magnitudes, Color fill, Color outline)
	{
		X = x;
		Y = y;
		Rotation = rotation;
		Mass = mass;
		Radius = radius;
		Magnitudes = magnitudes;
		Directions = directions;
		Fill = fill;
		Outline = outline;
	}
	double u = 0;
	double t = 0;
}
