package me.stupitdog.bhp.module.modules.combat;

import java.util.Comparator;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import me.stupitdog.bhp.util.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

public class Aura extends Module {

	public Timer timer = new Timer();
	
	private Setting<Double> range = this.register(new Setting<Double>("Range",4d,1d,6d));
	private Setting<Boolean> swordOnly = this.register(new Setting<Boolean>("SwordOnly",true));
	private Setting<Boolean> player = this.register(new Setting<Boolean>("Player",true));
	private Setting<Boolean> animals = this.register(new Setting<Boolean>("Animal",false));
	private Setting<Boolean> mobs = this.register(new Setting<Boolean>("Mob",false));
	
	public Aura() {
		super("Aura","",Category.COMBAT);
	}
	
public void onUpdate() {
	if(fullNullCheck()) {
    	try {
            Entity target = mc.world.loadedEntityList.stream()
                    .filter(entity -> entity instanceof EntityLivingBase)
                    .filter(e -> mc.player.getDistanceSq(e) <= range.getValue() * 2)
                    .filter(entity -> !(entity == mc.player))
                    .map(entity -> (EntityLivingBase) entity)
                    .min(Comparator.comparing(c -> mc.player.getDistance(c)))
                    .orElse(null);

        	if(swordOnly.getValue()) {
        		if(mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD) {
            		doKa(target);
        		}
        	}
        	else {
        		doKa(target);
        	}
    	}
    	catch(Exception e) {}
	}
    }
    
    public void doKa(Entity target) {
        
        if(!(target == null)) {
        	this.setBracket(target.getName());
        		double cps = mc.player.getCooldownPeriod() * 45;
        		
                if(target instanceof EntityPlayer && player.getValue()) {
                    if(timer.hasReached((long) (cps))) {
                        mc.player.connection.sendPacket(new CPacketUseEntity(target));
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.resetCooldown();
                        timer.reset();
                    }
                }

                if(target instanceof EntityAnimal && animals.getValue()) {
                    if(timer.hasReached((long) (cps))) {
                        mc.player.connection.sendPacket(new CPacketUseEntity(target));
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.resetCooldown();
                        timer.reset();
                    }
                }

                if(target instanceof EntityMob && mobs.getValue()) {
                    if(timer.hasReached((long) (cps))) {
                        mc.player.connection.sendPacket(new CPacketUseEntity(target));
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.resetCooldown();
                        timer.reset();
                    }
                }
        	}
        else {
        	this.setBracket("");
        }
    }
}
