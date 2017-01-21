package mainPackage;

import java.awt.Color;

//effects are purely visible things. They are used for explosions 
public class Effect {
	Particle host = null;
	double X;
	double Y;
	double Rotation;
	double[] Directions;
	double[] Magnitudes;
	Color Fill;
	Color Outline;
	int countdown = 500;
	
	public Effect(double x, double y, double rotation, double[] directions, double[] magnitudes, Color fill, Color outline)
	{
		X = x;
		Y = y;
		
		Rotation = rotation;
		
		Directions = directions;
		Magnitudes = magnitudes;
		
	}
}
