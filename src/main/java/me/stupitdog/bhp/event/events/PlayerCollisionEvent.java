package me.stupitdog.bhp.event.events;

import me.stupitdog.bhp.event.BhpEvent;
import net.minecraft.entity.Entity;

public class PlayerCollisionEvent extends BhpEvent {
	    public Entity entity;
	    
	    public PlayerCollisionEvent(Entity entity)
	    {
	        super();
	        
	        this.entity = entity;
	    }
}
