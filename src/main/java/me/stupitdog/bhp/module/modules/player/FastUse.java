package me.stupitdog.bhp.module.modules.player;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.item.ItemExpBottle;

public class FastUse extends Module {

	private Setting<Boolean> xp = this.register(new Setting<Boolean>("Xp",true));
	private Setting<Boolean> endcrystal = this.register(new Setting<Boolean>("EndCrystal",true));
	private Setting<Boolean> all = this.register(new Setting<Boolean>("All",true));
	
	public FastUse() {
		super("FastUse","",Category.PLAYER);
	}
	
    public void onUpdate() {
    	try {
    		if(all.getValue()) {
                mc.rightClickDelayTimer = 0;
    		}
    		else {
	        	if(mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle && xp.getValue()) {
	                mc.rightClickDelayTimer = 0;
	        	}
	        	if(mc.player.getHeldItemMainhand().getItem() instanceof ItemEndCrystal && endcrystal.getValue()) {
	                mc.rightClickDelayTimer = 0;
	        	}
    		}
    	}
    	catch(Exception e) {
    		
    	}
    }
}
