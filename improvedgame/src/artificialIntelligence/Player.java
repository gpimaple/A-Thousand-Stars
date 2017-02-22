package artificialIntelligence;

import mainPackage.Entity;
import mainPackage.Main;
import usefulMethods.UsefulParticleMethods;

public class Player implements AI{
	
	public Entity Host;
	public Player(Entity host)
	{
		Host = host;
	}
	
	
	@Override
	public void onClick() {
		
		
	}

	@Override
	public void onTick() {
		if(Main.leftkeydown == true)
		{
			Host.Host.Rotation -= 0.03;
			if(Host.Host.Rotation < -Math.PI)
			{
				Host.Host.Rotation += 2*Math.PI;
			}
		}
		if(Main.rightkeydown == true)
		{
			Host.Host.Rotation += 0.03;
			if(Host.Host.Rotation > Math.PI)
			{
				Host.Host.Rotation -= 2*Math.PI;
			}
		}
		if(Main.upkeydown == true)
		{
			UsefulParticleMethods.MoveEntity(Host);
		}
		if(Main.spacekeydown == true)
		{
			UsefulParticleMethods.ShootEntity(Host);
		}
		
	}

	@Override
	public void onDamagedbyEntity(Entity damager) {
		
		
	}


	@Override
	public void onDeath() {
		// TODO Auto-generated method stub
		
	}

}
