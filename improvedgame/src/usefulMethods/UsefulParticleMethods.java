package usefulMethods;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
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
		System.out.println("Player is dead.");
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
			double maxhealth, double healthregen, //health
			double damageoncontact)//contactbehavior)
	{
		Entity newEntity = new Entity (host, type, info,
				maxhealth, healthregen, 
				damageoncontact);
		Main.EntityMap.put(host,newEntity);
		return newEntity;
	}

	//collides two particles together elastically
	public static void CollideParticles(int index1, int index2)
	{
		Particle p1 = Main.ParticleList.get(index1);
		Particle p2 = Main.ParticleList.get(index2);
		
		Entity e1 = Main.EntityMap.get(p1);
		Entity e2 = Main.EntityMap.get(p2);
		UsefulParticleMethods.DamageEntity(index1,e2.EntityDamageOnContact);
		UsefulParticleMethods.DamageEntity(index1,e1.EntityDamageSelfOnContact); //damage everybody
		UsefulParticleMethods.DamageEntity(index2, e1.EntityDamageOnContact);
		UsefulParticleMethods.DamageEntity(index2,e2.EntityDamageSelfOnContact);	
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

		p1.X+=p1.Xvel;
		p1.Y+=p1.Yvel;
		p2.X+=p2.Xvel;
		p2.Y+=p2.Yvel;
	}
	
	
	public static void DestroyParticle(int i)
	{
		Main.EntityMap.remove(Main.ParticleList.get(i));
		Main.ParticleList.remove(i);

	}

	public static void KillParticle(int i)
	{
		//explodes
		if(Main.EntityMap.get(Main.ParticleList.get(i)) != null)
		{
			UsefulParticleMethods.Explode((int)(Main.ParticleList.get(i).Mass/2),
					0.1,
					Main.ParticleList.get(i).X, Main.ParticleList.get(i).Y,
					Main.ParticleList.get(i).Xvel, Main.ParticleList.get(i).Yvel);
		}
		DestroyParticle(i);

	}

	public static void CreatePlayer(double x, double y)
	{
		//x = x
		//y = y
		double rotation = 0;
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
		Shield shield = new Shield("ZetaCorp Defense Shield", 0.5, 600, 0.003, 9000);
		Thruster thruster = new Thruster("Standard Galactic Rocketry Basic Thruster", 0.3);
		Weapon weapon = new Weapon("ZetaCorp Asteroid Harvester", 1, 0.0, 5, 1, 50, 1);
		double maxhealth = 500;
		double healthregen = 0.001;
		double damageoncontact = 10;
		Entity player = CreateEntity(host, type, info, maxhealth, healthregen, damageoncontact);
		player.EntityShield = shield;
		player.EntityThruster = thruster;
		player.EntityWeapon = weapon;
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
			directions[i] += (Math.random()-0.5)/2;
		}
		Color fill = new Color(100,100,100,255); //white
		Color outline  = new Color(100, 100, 100, 255);

		int hostnumber = CreateParticle(x, y, rotation, mass, radius, directions, magnitudes, fill, outline);
		Particle host = Main.ParticleList.get(hostnumber);
		host.GravityMultiplier = 0;
		//host.Xvel = 0.0001*Math.random();
		//host.Yvel = 0.0001*Math.random();
		String type = "asteroid";
		String info = "A large rock that may contain valuable metals";
		double damageoncontact = 1;
		double health = mass*50;
		double regen = 0.001;
		CreateEntity(host, type, info, health, regen, damageoncontact);
		return Main.ParticleList.size()-1;
	}

	
	
	
	

	public static void AddVelocity(int index, double theta, double force)
	{
		Particle particletoadd = Main.ParticleList.get(index);
		particletoadd.Xvel += force*Math.cos(theta);
		particletoadd.Yvel += force*Math.sin(theta);
	}

	public static void AddVector(int index, double theta, double force)
	{
		Particle particletoadd = Main.ParticleList.get(index);
		particletoadd.Xvel += (force*Math.cos(theta))/particletoadd.Mass;
		particletoadd.Yvel += (force*Math.sin(theta))/particletoadd.Mass;
	}

	public static void MoveEntity(int index)
	{
		Particle updated = Main.ParticleList.get(index);
		Entity updatede = Main.EntityMap.get(updated);
		if(updatede.EntityThruster != null)
		{
			double thrustforce = updatede.EntityThruster.Acceleration;
			AddVector(index, updated.Rotation,thrustforce);
		}
	}

	public static void RotateParticle(int index, double newrotation)
	{
		Main.ParticleList.get(index).Rotation = newrotation;
	}

	//checks if button has been clicked
	public static boolean IsButtonClicked(int x, int y, int width, int height)
	{
		if(Main.mousex >= x && Main.mousex <= x+width && Main.mousey >= y && Main.mousey <=y+height)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	

	public static int CreateElectronTorpedo(double x, double y, double xvel, double yvel)
	{
		//x = x
		//y = y
		double rotation = Math.atan2(yvel, xvel);
		double mass = 2;
		double radius = 0.5;
		double[] xs = new double[] {-1, 0};
		double[] ys = new double[] { 0, 0};
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
		double damageoncontact = 100000;
		CreateEntity(host, type, info, maxhealth, healthregen, damageoncontact);
		Main.EntityMap.get(host).EntityDamageSelfOnContact = 11;
		return hostnumber;
	}



	public static void ShootEntity(int i)
	{
		Particle shooter = Main.ParticleList.get(i);
		Entity shooterEntity = Main.EntityMap.get(shooter);
		if(shooterEntity.EntityWeapon != null && shooterEntity.EntityWeapon.CurrentReload >= shooterEntity.EntityWeapon.MaxReload)
		{
			for(int q = 0; q < shooterEntity.EntityWeapon.NumberAtOnce; q++)
			{
				double rotation = shooter.Rotation + (Math.random()-1) * shooterEntity.EntityWeapon.Spread;
				double setback = shooter.Radius + 2;
				double x = shooter.X + Math.cos(rotation)*setback*2;
				double y = shooter.Y + Math.sin(rotation)*setback*2;
				double velocity = shooterEntity.EntityWeapon.Velocity;
				double xvel = shooter.Xvel + Math.cos(rotation)*velocity;
				double yvel = shooter.Yvel + Math.sin(rotation)*velocity;
				int particlenum = CreateElectronTorpedo(x,y,xvel,yvel);
				Particle bullet = Main.ParticleList.get(particlenum);
				Entity bulletEntity = Main.EntityMap.get(bullet);
				bulletEntity.EntityDamageOnContact += shooterEntity.EntityWeapon.DamageBoost;
			}
			shooterEntity.EntityWeapon.CurrentReload = 0;
		}
	}

	public static void DamageEntity(int i, double damage)
	{
		Entity tobedamaged = Main.EntityMap.get(Main.ParticleList.get(i));
		if(tobedamaged.EntityShield != null && tobedamaged.EntityShield.Failed == false)
		{
			tobedamaged.EntityShield.CurrentHitpoints -= damage;
			tobedamaged.CurrentHealth -= tobedamaged.EntityShield.DamageMultiplier*damage;
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
			double[] ys = new double[] {0, 0};
			double[] magnitudes = GetMagnitudesForParticle(xs, ys);
			double[] directions = GetDirectionsForParticle(xs,ys);
			Color fillcolor = new Color(200,200,200,255);
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


