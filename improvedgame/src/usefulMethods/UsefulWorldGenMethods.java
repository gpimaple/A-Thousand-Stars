package usefulMethods;

import java.awt.Point;
import java.util.ArrayList;

import mainPackage.Main;
import mainPackage.Particle;
import mainPackage.Sector;

public class UsefulWorldGenMethods {
	
	public static void ClearSectors()
	{
		Main.SectorList = new Sector[Main.MapSize][Main.MapSize];
	}
	
	public static void InitializeSectors()
	{
		int mapsize = Main.MapSize;
		Sector[][] list = new Sector[mapsize][mapsize];
		for(int x = 0; x < mapsize; x++)
		{
			for(int y = 0; y < mapsize; y++)
			{
				Point location = new Point(x,y);
				list[x][y] = new Sector(location);
			}
		}
		
		Main.SectorList = list;
	}
}
