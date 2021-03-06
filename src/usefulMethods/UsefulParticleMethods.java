package usefulMethods;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.sound.sampled.AudioInputStream;

import artificialIntelligence.DroppedItem;
import artificialIntelligence.Player;
import mainPackage.*;

public class UsefulParticleMethods 
{
	public static int GetPlayerIndex()
	{
		for(int i = 0; i < Main.ParticleList.size(); i++)
		{
			if(Main.EntityMap.get(Main.ParticleList.get(i)) != null &&
					(Main.EntityMap.get(Main.ParticleList.get(i)).Type.equals("player")))
			{
				return i;
			}
		}
		//System.out.println("Player is dead.");
		return -1;


		/*Iterator<HashMap.Entry<Particle, Entity>> iterator = Main.EntityMap.entrySet().iterator();
		while(iterator.hasNext())
		{
			HashMap.Entry<Particle, Entity> Entry = iterator.next();
			if(Entry.getValue().type.equals("player"))
			{
				return iterator.hashCode();
	        }
	       	//You can remove elements while iterating. //iterator.remove();
		}
		System.out.println("The player is dead");
		return -1;*/
	}

	public static void CheckCollision()//Wikipedia is awesome. this is an elastic collision.
	{
		for(int i = 0; i < Main.ParticleList.size(); i++)
		{
			Particle p1 = Main.ParticleList.get(i);//first particle in collision
			if(Main.EntityMap.get(p1) != null && p1.active == true)//check that it has an entity. Dont want to lag on checking nonentity
			{
				int sectorx = (int)(p1.X/Main.SectorSize);
				int sectory = (int)(p1.Y/Main.SectorSize);

				ArrayList<Particle> particlelist = new ArrayList<Particle>();

				double xmin = p1.X - p1.Radius;
				double xmax = p1.X + p1.Radius;

				double ymin = p1.Y - p1.Radius;
				double ymax = p1.Y + p1.Radius;

				int xminsector = (int)(xmin/Main.SectorSize);
				int yminsector = (int)(ymin/Main.SectorSize);
				int xmaxsector = (int)(xmax/Main.SectorSize);
				int ymaxsector = (int)(ymax/Main.SectorSize);


				for(int x = xminsector; x <= xmaxsector; x++)//iterate through all the sectors it is in
				{
					for(int y = yminsector; y <= ymaxsector; y++)
					{
						particlelist.addAll(UsefulParticleMethods.GetSectorParticles(x, y));
					}
				}
				double r1 = p1.Radius;
				for(int q = 0; q < particlelist.size(); q++)
				{
					Particle p2 = particlelist.get(q);
					if( !p1.equals(p2) && Main.EntityMap.get(p2)!=null && p2.active == true)
					{
						double r2 = p2.Radius;
						double distance = UsefulParticleMethods.GetDistance(p1, p2);
						if(distance < r1 + r2)
						{
							UsefulParticleMethods.CollideParticles(p1, p2);
						}
					}
				}
			}
		}
	}




	public static ArrayList<Particle> GetSectorParticles(int x, int y)
	{
		ArrayList<Particle> particlelist = new ArrayList<Particle>();
		try
		{
			Sector sector = Main.SectorList[x][y];
			if(x >= 0 && y >= 0 && x < Main.MapSize && y < Main.MapSize  && sector != null)
			{
				particlelist.addAll(sector.particlesIn);
			}
			//	particlelist.get(0).Fill = new Color(0,255,0);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			//	e.printStackTrace();
		}
		catch(java.lang.NullPointerException e)
		{
			//e.printStackTrace();
		}
		return particlelist;
	}


	//returns distance from 1 particle to another
	public static double GetDistance(Particle i, Particle a)
	{
		return Math.sqrt(Math.pow(i.X-a.X, 2) + Math.pow(i.Y-a.Y, 2));
	}
	//returns distance from 1 point to another
	public static double GetDistance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1- y2, 2));
	}

	public static double GetRelativeVelocity(Particle i, Particle a)
	{
		return Math.sqrt(Math.pow(i.Xvel - a.Xvel,2) + Math.pow(i.Yvel-a.Yvel, 2));
	}

	//returns direction from i to a
	public static double GetDirection(Particle i, Particle a)
	{
		return  Math.atan2(a.Y-i.Y, a.X-i.X);
	}

	//this adds velocity to the specified particle
	public static void AddVelocity(Particle modifiedParticle, double theta, double magnitude)
	{
		modifiedParticle.Xvel += magnitude * Math.cos(theta);
		modifiedParticle.Yvel += magnitude * Math.sin(theta);
	}

	//changes 2 arrays of x and y into directions. The size of x and y should be the same.
	public static double[] GetDirectionsForParticle(double[] x, double[] y)
	{
		double[] directions = new double[x.length];
		for(int i = 0; i < x.length; i++)
		{
			directions[i] = Math.atan2(y[i], x[i]);
		}
		return directions; 
	}
	//changes 2 arrays of x and y into magnitudes. The size of x and y should be the same.
	public static double[] GetMagnitudesForParticle(double[] x, double[] y)
	{
		double[] magnitudes = new double[x.length];
		for(int i = 0; i < x.length; i++)
		{
			magnitudes[i] = Math.sqrt(y[i]*y[i] + x[i]*x[i]);
		}
		return magnitudes;
	}


	//creates a particle and adds it to the array. 
	public static int CreateParticle(double x, double y, double rotation, double mass, double radius, 
			double[] directions, double[] magnitudes,
			Color fill, Color outline)
	{
		Particle newParticle = new Particle (x, y, rotation, mass, radius, directions, magnitudes, fill, outline);
		Main.ParticleList.add(newParticle);
		return Main.ParticleList.size()-1;
	}
	// creates an entity and adds it to the hashmap
	public static Entity CreateEntity(Particle host, String type, String info,
			Item[] items,
			double maxhealth, double healthregen, //health
			double damageoncontact)//contactbehavior)
	{
		Entity newEntity = new Entity (host, type, info,
				items,
				maxhealth, healthregen, 
				damageoncontact);
		Main.EntityMap.put(host,newEntity);
		return newEntity;
	}


	
	//collides two particles together elastically
	public static void CollideParticles(Particle p1, Particle p2)
	{	
		Entity e1 = Main.EntityMap.get(p1);
		Entity e2 = Main.EntityMap.get(p2);
		UsefulParticleMethods.DamageEntity(e1,e2.EntityDamageOnContact);
		UsefulParticleMethods.DamageEntity(e1,e1.EntityDamageSelfOnContact); //damage everybody
		UsefulParticleMethods.DamageEntity(e2, e1.EntityDamageOnContact);
		UsefulParticleMethods.DamageEntity(e2,e2.EntityDamageSelfOnContact);	
		double colAng = UsefulParticleMethods.GetDirection(p1, p2);
		double mag1 = Math.sqrt(p1.Xvel*p1.Xvel+p1.Yvel*p1.Yvel);
		double mag2 = Math.sqrt(p2.Xvel*p2.Xvel+p2.Yvel*p2.Yvel);
		double dir1 = Math.atan2(p1.Yvel, p1.Xvel);
		double dir2 = Math.atan2(p2.Yvel, p2.Yvel);
		double nXvel_1 = mag1*Math.cos(dir1-colAng);
		double nYvel_1 = mag1*Math.sin(dir1-colAng);
		double nXvel_2 = mag2*Math.cos(dir2-colAng);
		double nYvel_2 = mag2*Math.sin(dir2-colAng);
		double final_Xvel_1 = ((p1.Mass-p2.Mass)*nXvel_1+(p2.Mass+p2.Mass)*nXvel_2)/(p1.Mass+p2.Mass);
		double final_Xvel_2 = ((p1.Mass+p1.Mass)*nXvel_1+(p2.Mass-p1.Mass)*nXvel_2)/(p1.Mass+p2.Mass);
		double final_Yvel_1 = nYvel_1;
		double final_Yvel_2 = nYvel_2;
		p1.Xvel = Math.cos(colAng)*final_Xvel_1+Math.cos(colAng+Math.PI/2)*final_Yvel_1;
		p1.Yvel = Math.sin(colAng)*final_Xvel_1+Math.sin(colAng+Math.PI/2)*final_Yvel_1;
		p2.Xvel = Math.cos(colAng)*final_Xvel_2+Math.cos(colAng+Math.PI/2)*final_Yvel_2;
		p2.Yvel = Math.sin(colAng)*final_Xvel_2+Math.sin(colAng+Math.PI/2)*final_Yvel_2;
/*
		p1.X += p1.Xvel;
		p1.Y += p1.Yvel;
		p2.X += p2.Xvel;
		p2.Y += p2.Yvel;
	*/	
		
		double direction = GetDirection(p2, p1);
		double magnitude = 1;
		p1.X += magnitude*Math.cos(direction);
		p1.Y += magnitude*Math.sin(direction);

		direction = GetDirection(p1, p2);
		magnitude = 1;
		p2.X += magnitude*Math.cos(direction);
		p2.Y += magnitude*Math.sin(direction);


	}


	public static void DestroyParticle(int i)
	{
		Main.AIMap.remove(Main.EntityMap.get(Main.ParticleList.get(i)));
		Main.EntityMap.remove(Main.ParticleList.get(i));
		Main.ParticleList.remove(i);

	}

	public static void KillParticle(int i)
	{
		//explodes
		if(Main.EntityMap.get(Main.ParticleList.get(i)) != null)
		{
			Entity entity = Main.EntityMap.get(Main.ParticleList.get(i));
			UsefulParticleMethods.Explode((int)(Main.ParticleList.get(i).Mass/2),
					0.1,
					Main.ParticleList.get(i).X, Main.ParticleList.get(i).Y,
					Main.ParticleList.get(i).Xvel, Main.ParticleList.get(i).Yvel);
			if(Main.AIMap.get(entity) != null)
			{
				Object ai = Main.AIMap.get(entity);
				UsefulAIMethods.CallMethod(ai, "onDeath", null);
			}
			for(int q = 0; q < entity.Inventory.length; q++)
			{
				if(entity.Inventory[q] != null)
				{
					int itemint = CreateItem(entity.Inventory[q]);
					Particle itemparticle = Main.ParticleList.get(itemint);
					itemparticle.Xvel += (Math.random() -0.5)/2;
					itemparticle.Yvel += (Math.random() -0.5)/2;
					itemparticle.Xvel += entity.Host.Xvel;
					itemparticle.Yvel += entity.Host.Yvel;
					itemparticle.X = entity.Host.X;
					itemparticle.Y = entity.Host.Y;
				}
			}
		}
		DestroyParticle(i);

	}

	public static void CreatePlayer(double x, double y)
	{
		//x = x
		//y = y
		double rotation = Math.PI*0.85;
		double mass = 100;
		double radius = 5;
		double[] xs = new double[] {-5.0,10.0,-5.0};
		double[] ys = new double[] {-5.0,0.0,5.0};
		double[] magnitudes = GetMagnitudesForParticle(xs, ys);
		double[] directions = GetDirectionsForParticle(xs,ys);
		Color fillcolor = new Color(255,255,255,100);
		Color outlinecolor  = new Color(255, 255, 255, 100);
		int hostnumber = CreateParticle(x,y, rotation, mass, radius, directions, magnitudes, fillcolor, outlinecolor);
		Particle host = Main.ParticleList.get(hostnumber);
		String type = "player";
		String info = "You";
		double maxhealth = 500;
		double healthregen = 0.001;
		double damageoncontact = 10;
		Item generator = UsefulItemMethods.GlorbroxNuclearReactor();
		Item shield = UsefulItemMethods.ZetaCorpDefenseShield();
		Item thruster = UsefulItemMethods.StandardGalacticRocketryBasicThruster();
		Item weapon = UsefulItemMethods.ZetaCorpAsteroidHarvester();
		Item[] items = new Item[] {generator, shield, thruster, weapon,null,null,null};
		Entity player = CreateEntity(host, type, info, items, maxhealth, healthregen, damageoncontact);
		Player ai = new Player(player);
		Main.AIMap.put(player, ai);
	}
	//creates an asteroid. Asteroids are floating rocks not affected by gravity. They 
	// can be damaged when a player shoots them and will eventually explode, producing metals.
	public static int CreateAsteroid(double x, double y, double mass)
	{	
		//x = x
		//y = y
		//mass = mass
		double rotation = Math.random();
		double xvel = 0.0;
		double yvel = 0.0;
		double radius = Math.sqrt(mass);
		double[] magnitudes = new double[]{10,10,10,10,10,10};
		double[] directions = new double[]{0,1,2,3,4,5};
		for(int i = 0; i < 6; i++)
		{
			magnitudes[i]= radius/2 + Math.random()*radius;
			if(magnitudes[i] < 1)
			{
				magnitudes[i] = 1;
			}
			directions[i] += (Math.random()-0.5)/2;
		}
		Color fill = new Color(100,100,100,255); //white
		Color outline  = new Color(100, 100, 100, 255);

		int hostnumber = CreateParticle(x, y, rotation, mass, radius, directions, magnitudes, fill, outline);
		Particle host = Main.ParticleList.get(hostnumber);
		host.GravityMultiplier = 0;
		host.Xvel = 0*Math.random();
		host.Yvel = 0*Math.random();
		String type = "asteroid";
		String info = "A large rock that may contain valuable metals";
		double damageoncontact = 1;
		double health = mass*5;
		double regen = 0.001;
		Item[] items = new Item[] {};
		for(int i = 0; i < (int)mass; i++)
		{
			if(Math.random() > 0.999)
			{
				items = new Item[items.length +1];
				for(int q = 0; q < items.length; q++)
				{
					items[q] = UsefulItemMethods.Ore();
				}
			}
		}
		CreateEntity(host, type, info, items, health, regen, damageoncontact);
		return hostnumber;
	}

	public static int CreateItem(Item item)
	{
		double rotation = 0;
		double radius = 16;
		double[] magnitudes = new double[]{0};
		double[] directions = new double[]{0};
		Color fill = Color.WHITE;
		Color outline = Color.WHITE;
		
		int hostnumber = 
				CreateParticle(
						0,//x
						0,//y
						rotation,
						20,//mass
						radius,
						directions,
						magnitudes,
						fill,
						outline
						);
		Particle host = Main.ParticleList.get(hostnumber);
		host.Sprite = item.Sprite;
		String type = "item";
		String info = item.Description;
		double damageoncontact = 0;
		double health = 500;
		double regen = 0.001;
		Item[] items = new Item[] {};
		Entity entityhost = CreateEntity(host, type, info, items, health, regen, damageoncontact);
		DroppedItem ai = new DroppedItem(item, entityhost);
		Main.AIMap.put(entityhost, ai);
		return hostnumber;
		
	}


	public static void AddVector(Particle particletoadd, double theta, double force)
	{
		particletoadd.Xvel += (force*Math.cos(theta))/particletoadd.Mass;
		particletoadd.Yvel += (force*Math.sin(theta))/particletoadd.Mass;
	}

	public static void MoveEntity(Entity updated)
	{
		if(updated.Thruster != null)
		{
			if(updated.Generator != null
					&& updated.Generator.CurrentPower >= updated.Thruster.PowerConsumptionPerThrust)
			{
				double thrustforce = updated.Thruster.Acceleration;
				AddVector(updated.Host, updated.Host.Rotation,thrustforce);
				updated.Generator.CurrentPower -= updated.Thruster.PowerConsumptionPerThrust;
			}
		}
	}

	public static void RotateParticle(Particle rotated, double newrotation)
	{
		rotated.Rotation = newrotation;
	}




	public static int CreateElectronTorpedo(double x, double y, double xvel, double yvel)
	{
		//x = x
		//y = y
		double rotation = Math.atan2(yvel, xvel);
		double mass = 0.5;
		double radius = 0.5;
		//double[] xs = new double[] {Math.random()*4, -Math.random()*4, Math.random()*4, -Math.random()*4};
		//double[] ys = new double[] {-Math.random()*4, Math.random()*4, Math.random()*4, -Math.random()*4};
		double[] xs = new double[] {1,0};
		double[] ys = new double[] {0,0};		

		double[] magnitudes = GetMagnitudesForParticle(xs, ys);
		double[] directions = GetDirectionsForParticle(xs,ys);
		Color fillcolor = new Color(255,255,255,255);
		Color outlinecolor  = new Color(255, 255, 255, 255);
		int hostnumber = CreateParticle(x,y, rotation, mass, radius, directions, magnitudes, fillcolor, outlinecolor);
		Particle host = Main.ParticleList.get(hostnumber);
		host.Priority = 100000;
		host.Xvel = xvel;
		host.Yvel = yvel;
		String type = "electron_torpedo";
		String info = "A common missile used mainly to harvest asteroids, but can also be used to kill.";
		double maxhealth = 10;
		double healthregen = -0.03;
		double damageoncontact = 1000000;
		Item[] items = new Item[]{};
		CreateEntity(host, type, info, items, maxhealth, healthregen, damageoncontact);
		Main.EntityMap.get(host).EntityDamageSelfOnContact = 11;
		return hostnumber;
	}



	public static void ShootEntity(Entity shooterEntity)
	{
		if(shooterEntity.Weapon != null 
				&& shooterEntity.Weapon.CurrentWeaponReload >= shooterEntity.Weapon.MaxWeaponReload
				&& shooterEntity.Generator.CurrentPower >= shooterEntity.Weapon.PowerConsumptionWhenFired)
		{
			shooterEntity.Generator.CurrentPower -= shooterEntity.Weapon.PowerConsumptionWhenFired;
			for(int q = 0; q < shooterEntity.Weapon.NumberFiredAtOnce; q++)
			{
				double rotation = shooterEntity.Host.Rotation + (Math.random()-0.5) * shooterEntity.Weapon.Spread;
				double setback = shooterEntity.Host.Radius + 2;
				double x = shooterEntity.Host.X + Math.cos(rotation)*setback*2;
				double y = shooterEntity.Host.Y + Math.sin(rotation)*setback*2;
				double velocity = shooterEntity.Weapon.Velocity;
				double xvel = shooterEntity.Host.Xvel + Math.cos(rotation)*velocity;
				double yvel = shooterEntity.Host.Yvel + Math.sin(rotation)*velocity;
				// TODO differentiate for bullets
				int particlenum = CreateElectronTorpedo(x,y,xvel,yvel);
				Particle bullet = Main.ParticleList.get(particlenum);
				Entity bulletEntity = Main.EntityMap.get(bullet);
				bulletEntity.EntityDamageOnContact += shooterEntity.Weapon.DamageBoost;
			}
			shooterEntity.Weapon.CurrentWeaponReload = 0;
		}
	}

	public static void DamageEntity(Entity tobedamaged, double damage)
	{
		if(tobedamaged.Shield != null && tobedamaged.Shield.Failed == false)
		{
			tobedamaged.Shield.CurrentHitpoints -= damage;
			tobedamaged.CurrentHealth -= tobedamaged.Shield.DamageMultiplier*damage;
		}
		else
		{
			tobedamaged.CurrentHealth -= damage;
		}
	}

	public static void Explode(int particles, double force, double startx, double starty, double startxvel, double startyvel)
	{
		for(int i = 0; i < particles; i++)
		{
			double x = startx+Math.random()*force;
			double y = starty+Math.random()*force;
			double rotation = 0;
			double mass = 0.001;
			double radius = -100;
			double[] xs = new double[] {Math.random()*1, -Math.random()*2};
			double[] ys = new double[] {Math.random()*0, -Math.random()*0};

			//xs = new double[] {Math.random()*4, -Math.random()*4, Math.random()*4, -Math.random()*4};
			//ys = new double[] {-Math.random()*4, Math.random()*4, Math.random()*4, -Math.random()*4};

			double[] magnitudes = GetMagnitudesForParticle(xs, ys);
			double[] directions = GetDirectionsForParticle(xs,ys);
			Color fillcolor = new Color(200,200,200,100);
			Color outlinecolor  = new Color(200, 200, 200, 30);
			int hostnumber = CreateParticle(x,y, rotation, mass, radius, directions, magnitudes, fillcolor, outlinecolor);
			Particle host = Main.ParticleList.get(hostnumber);

			double direction = Math.random()*Math.PI*2;
			double newforce = force*(Math.random()*2);
			host.countdown = (int)Math.sqrt(particles*(1000*Math.random()));
			host.Priority = 1000000;
			host.Xvel = startxvel + Math.cos(direction)*newforce;
			host.Yvel = startyvel + Math.sin(direction)*newforce;
			host.Rotation = Math.atan2(host.Yvel, host.Xvel);
		}
	}

}


