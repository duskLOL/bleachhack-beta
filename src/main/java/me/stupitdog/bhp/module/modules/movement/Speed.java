package me.stupitdog.bhp.module.modules.movement;

import java.util.Objects;

import me.stupitdog.bhp.event.events.PlayerMoveEvent;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Speed extends Module {

	/*Basically a nerfed version of xulu's longjump*/
	
	private Setting<Double> multiplier = this.register(new Setting<Double>("Multiplier",1.9d,0d,4d));
	private Setting<Boolean> accelerationTimer = this.register(new Setting<Boolean>("AccelerationTimer",false));
	private Setting<Integer> timerSpeed = this.register(new Setting<Integer>("TimerSpeed",1,0,10));
	private Setting<Boolean> speedDetect = this.register(new Setting<Boolean>("SpeedDetect",true));
	private Setting<Boolean> jumpDetect = this.register(new Setting<Boolean>("JumpDetect",true));
	private Setting<Double> extraY = this.register(new Setting<Double>("ExtraY",0d,0d,1d));
	
	public Speed() {
		super("Speed","Strafe",Category.MOVEMENT);
	}
	
	private static int[] llIllII;
    private int currentState;
	private double prevDist;
	private double motionSpeed;
	private boolean attempting;
	private boolean doToggle;
	
	public void onEnable() {
		super.onEnable();
		
		this.attempting = false;
		
		lIIlIIIII();
	}
	
	public void onDisable() {
		super.onDisable();
		
		this.currentState = 0;
		this.motionSpeed = 0;
	}
	
    private static int lIIlIIIIl(final float var0, final float var1) {
        final float var2;
        return ((var2 = var0 - var1) == 0.0f) ? 0 : ((var2 < 0.0f) ? -1 : 1);
    }
	
	public void update() {
        this.prevDist = Math.sqrt((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX) + (mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ));
        
        if (lIIlIIlII(((boolean)this.accelerationTimer.getValue()) ? 1 : 0)) {
            mc.timer.tickLength = (float) (50.0f / this.timerSpeed.getValue());
        }
        else if (lIIlIIlII(lIIlIIIIl(mc.timer.tickLength, 50.0f))) {
            mc.timer.tickLength = 50.0f;
        }
	}
	
    private static void lIIlIIIII() {
        (llIllII = new int[10])[0] = 0;
        llIllII[1] = " ".length();
        llIllII[2] = "  ".length();
        llIllII[3] = "   ".length();
        llIllII[4] = 10;
        llIllII[5] = 4;
        llIllII[6] = 5;
        llIllII[7] = 6;
        llIllII[8] = 7;
        llIllII[9] = 8;
    }
    
    private static int lIIlIIllI(final float var0, final float var1) {
        final float var2;
        return ((var2 = var0 - var1) == 0.0f) ? 0 : ((var2 < 0.0f) ? -1 : 1);
    }
    
    private static boolean lIIlIlIII(final int lllIIlIIlIIIlll) {
        return lllIIlIIlIIIlll <= 0;
    }
    
    private static boolean lIIlIlIIl(final int lllIIlIIlIIIlIl) {
        return lllIIlIIlIIIlIl > 0;
    }
    
	@SubscribeEvent
	public void listener(PlayerMoveEvent event) {
		
        final float currentTps = mc.timer.tickLength / 1000.0f;
        switch (this.currentState) {
            case 0: {
                this.currentState += llIllII[1];
                this.prevDist = 0.0;
                break;
            }
            case 2: {
                double lllIIlIIlllIlII = 0.40123128 + this.extraY.getValue();
                if ((!lIIlIIlIl(lIIlIIllI(mc.player.moveForward, 0.0f)) || lIIlIIlII(lIIlIIllI(mc.player.moveStrafing, 0.0f))) && lIIlIIlII(mc.player.onGround ? 1 : 0)) {
                    if (lIIlIIlII(mc.player.isPotionActive(MobEffects.JUMP_BOOST) ? 1 : 0) && lIIlIIlII(((boolean)this.jumpDetect.getValue()) ? 1 : 0)) {
                        lllIIlIIlllIlII += (mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + llIllII[1]) * 0.1f;
                    }
                    event.setY(mc.player.motionY = lllIIlIIlllIlII);
                    this.motionSpeed *= 2.149;
                    break;
                }
                break;
            }
            case 3: {
                this.motionSpeed = this.prevDist - 0.76 * (this.prevDist - this.getBaseMotionSpeed());
                break;
            }
            default: {
                if ((!lIIlIlIII(mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(0.0, mc.player.motionY, 0.0)).size()) || lIIlIIlII(mc.player.collidedVertically ? 1 : 0)) && lIIlIlIIl(this.currentState)) {
                    if (lIIlIIlIl(lIIlIIllI(mc.player.moveForward, 0.0f)) && lIIlIIlIl(lIIlIIllI(mc.player.moveStrafing, 0.0f))) {
                        this.currentState = llIllII[0];
                    }
                    else {
                        this.currentState = llIllII[1];
                    }
                }
                this.motionSpeed = this.prevDist - this.prevDist / 159.0;
                break;
            }
        }
        this.motionSpeed = Math.max(this.motionSpeed, this.getBaseMotionSpeed());
        double lllIIlIIlllIIIl = mc.player.movementInput.moveForward;
        double lllIIlIIlllIIII = mc.player.movementInput.moveStrafe;
        final double lllIIlIIllIllll = mc.player.rotationYaw;
        if (lIIlIIlIl(lIIlIIlll(lllIIlIIlllIIIl, 0.0)) && lIIlIIlIl(lIIlIIlll(lllIIlIIlllIIII, 0.0))) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        if (lIIlIIlII(lIIlIIlll(lllIIlIIlllIIIl, 0.0)) && lIIlIIlII(lIIlIIlll(lllIIlIIlllIIII, 0.0))) {
            lllIIlIIlllIIIl *= Math.sin(0.7853981633974483);
            lllIIlIIlllIIII *= Math.cos(0.7853981633974483);
        }
        event.setX((lllIIlIIlllIIIl * this.motionSpeed * -Math.sin(Math.toRadians(lllIIlIIllIllll)) + lllIIlIIlllIIII * this.motionSpeed * Math.cos(Math.toRadians(lllIIlIIllIllll))) * 0.99);
        event.setZ((lllIIlIIlllIIIl * this.motionSpeed * Math.cos(Math.toRadians(lllIIlIIllIllll)) - lllIIlIIlllIIII * this.motionSpeed * -Math.sin(Math.toRadians(lllIIlIIllIllll))) * 0.99);
        this.attempting = true;
        this.currentState += llIllII[1];
	}
	
    private double getBaseMotionSpeed() {
        double lllIIlIIllllllI = 0.272 * this.multiplier.getValue() / 2;
        if (lIIlIIlII(mc.player.isPotionActive(MobEffects.SPEED) ? 1 : 0) && lIIlIIlII(((boolean)this.speedDetect.getValue()) ? 1 : 0)) {
            final int lllIIlIlIIIIIII = Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier();
            lllIIlIIllllllI *= 1.0 + 0.2 * lllIIlIlIIIIIII;
        }
        return lllIIlIIllllllI;
    }
	
    private static int lIIlIIlll(final double var0, final double var2) {
        final double var3;
        return ((var3 = var0 - var2) == 0.0) ? 0 : ((var3 < 0.0) ? -1 : 1);
    }
    
    private static boolean lIIlIIlII(final int lllIIlIIlIIlIll) {
        return lllIIlIIlIIlIll != 0;
    }
    
    private static boolean lIIlIIlIl(final int lllIIlIIlIIlIIl) {
        return lllIIlIIlIIlIIl == 0;
    }
}
