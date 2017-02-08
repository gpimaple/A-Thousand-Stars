package usefulMethods;

import java.awt.Point;
import java.util.ArrayList;

import mainPackage.Entity;
import mainPackage.Main;
import mainPackage.Particle;

public class UsefulWorldGenMethods {
	
	public static void ResetSectors()
	{
		Main.SectorList = new Sector[Main.MapSize][Main.MapSize];
		for(int i = 0; i < Main.ParticleList.size(); i++)
		{
			Particle updated = Main.ParticleList.get(i);
			if(updated.active)
			{
				Entity updatedEntity = Main.EntityMap.get(updated);
				if(updatedEntity != null)
				{
					double xmin = updated.X - updated.Radius;
					double xmax = updated.X + updated.Radius;
					
					double ymin = updated.Y - updated.Radius;
					double ymax = updated.Y + updated.Radius;
					
					int xminsector = (int)(xmin/Main.SectorSize);
					int yminsector = (int)(ymin/Main.SectorSize);
					int xmaxsector = (int)(xmax/Main.SectorSize);
					int ymaxsector = (int)(ymax/Main.SectorSize);
					
					
					for(int x = xminsector; x <= xmaxsector; x++)//iterate through all the sectors it is in
					{
						for(int y = yminsector; y <= ymaxsector; y++)
						{
							Sector sector = Main.SectorList[x][y];
							if(sector == null)//if the sector does not yet exist, add it
							{
								sector = new Sector(new Point(x, y));
							}
							if(!sector.particlesIn.contains(updated))//if the sector does not already hold it, add it
							{
								sector.particlesIn.add(updated);
							}
							Main.SectorList[x][y] = sector;
						}
					}
				}
			}	
		}
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
