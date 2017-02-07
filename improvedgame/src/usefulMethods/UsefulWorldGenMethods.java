package usefulMethods;

import java.awt.Point;
import java.util.ArrayList;

import mainPackage.Entity;
import mainPackage.Main;
import mainPackage.Particle;
import mainPackage.Sector;

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
					int sectorX = (int)(updated.X/Main.SectorSize);
					int sectorY = (int)(updated.Y/Main.SectorSize);
					
					Sector sector = Main.SectorList[sectorX][sectorY];
					if(sector == null)
					{
						sector = new Sector(new Point(sectorX, sectorY));
					}
					if(!sector.particlesIn.contains(updated))
					{
						sector.particlesIn.add(updated);
					}
					Main.SectorList[sectorX][sectorY] = sector;
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
