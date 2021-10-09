package me.stupitdog.bhp.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.event.BhpEvent;
import me.stupitdog.bhp.event.BhpEvent.Era;
import me.stupitdog.bhp.event.events.PacketEvent;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import me.stupitdog.bhp.util.CrystalUtil;
import me.stupitdog.bhp.util.EntityUtil;
import me.stupitdog.bhp.util.MathUtil;
import me.stupitdog.bhp.util.RenderUtil;
import me.stupitdog.bhp.util.Timer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoCrystal extends Module {

	private Setting<Boolean> place = this.register(new Setting<Boolean>("Place",true));
	private Setting<Boolean> explode = this.register(new Setting<Boolean>("Break",true));
	private Setting<BreakMode> breakMode = this.register(new Setting<BreakMode>("BreakMode",BreakMode.ALL));
	private Setting<Boolean> predictBreak = this.register(new Setting<Boolean>("PredictBreak",true));
	private Setting<Integer> predictDelay = this.register(new Setting<Integer>("PredictTicks",12,0,500));
	private Setting<Double> yawStepThresh = this.register(new Setting<Double>("YawStepThreshold",55.0,0.0,100.0));
	private Setting<Integer> yawStepTicks = this.register(new Setting<Integer>("YawStepTicks",1,1,10));
	private Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate",true));
	private Setting<Integer> range = this.register(new Setting<Integer>("EnemyRange",15,0,20));
	private Setting<Double> placeDelay = this.register(new Setting<Double>("PlaceDelay",1d,0d,10d));
	private Setting<Double> placeRange = this.register(new Setting<Double>("PlaceRange",4.4,0d,10d));
	private Setting<Double> breakDelay = this.register(new Setting<Double>("BreakDelay",1d,0d,10d));
	private Setting<Double> breakRange = this.register(new Setting<Double>("BreakRange",4.4,0d,10d));
	private Setting<Boolean> inhibit = this.register(new Setting<Boolean>("Inhibit",true));
	private Setting<Boolean> sequential = this.register(new Setting<Boolean>("Sequential",true));
	private Setting<Boolean> multiplace = this.register(new Setting<Boolean>("Multiplace",false));
	private Setting<Integer> minDmg = this.register(new Setting<Integer>("MinDmg",4,0,36));
	private Setting<Integer> maxSelfDmg = this.register(new Setting<Integer>("MaxSelfDmg",8,0,36));
	private Setting<SwingMode> swingMode = this.register(new Setting<SwingMode>("SwingMode",SwingMode.OFFHAND));
	
	private Entity target;
	private EntityEnderCrystal crystal;
	private BlockPos pos;
	private float yaw = 0.0f;
	private float pitch = 0.0f;
	private boolean rotating = false;
	private int ticks = 0;
	
	private Timer placeTimer = new Timer();
	private Timer breakTimer = new Timer();
	private Timer predictTimer = new Timer();
	
	public AutoCrystal() {
		super("AutoCrystal","",Category.COMBAT);
	}
	
	public void onEnable() {
		if(fullNullCheck()) {
			this.target = null;
			this.crystal = null;
			this.pos = null;
			yaw = 0.0f;
			pitch = 0.0f;
			ticks = 0;
			rotating = false;
		}
	}
	
	@SubscribeEvent
	public void listener(TickEvent event) {
		if(fullNullCheck()) {
			doCrystal();
		}
	}
	
	public void doCrystal() {
		if(fullNullCheck()) {
			target = null;
			//place
			for(Entity p : mc.world.loadedEntityList) {
				if(p instanceof EntityPlayer && !(p == mc.player)) {
					if(mc.player.getDistanceSq(p) <= range.getValue()) {
						target = p;
					}
				}
			}
			if(target == null) {
				this.setBracket("");
				pos = null;
				return;
			}
			else {
				if(place.getValue() && placeTimer.hasReached(placeDelay.getValue().longValue() * 50)) {
					placeTimer.reset();
					float tempDmg = 0;
					BlockPos newPos = null;
					for(BlockPos blockPos : CrystalUtil.possiblePlacePositions(placeRange.getValue().floatValue(), false, !multiplace.getValue())) {
						float targetDmg = CrystalUtil.calculateDamage(blockPos.getX() + 0.5f, blockPos.getY() + 1, blockPos.getZ() + 0.5d, target);
						float selfDmg = CrystalUtil.calculateDamage(blockPos.getX() + 0.5f, blockPos.getY() + 1, blockPos.getZ() + 0.5d, mc.player);
						if(targetDmg > tempDmg && selfDmg <= maxSelfDmg.getValue().floatValue() && targetDmg >= minDmg.getValue().floatValue()) {
							newPos = blockPos;
							tempDmg = targetDmg;
						}
					}
					this.setBracket(target.getName());
					pos = newPos;
					if(!(pos == null)) {
						this.rotateToPos(this.pos);
		                AutoCrystal.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
					}
				}
				if(explode.getValue() && breakTimer.hasReached(breakDelay.getValue().longValue() * 50)) {
					breakTimer.reset();
					try {
						for(Entity crystal : mc.world.loadedEntityList) {
							if(mc.player.getDistance(crystal) <= breakRange.getValue()) {
								if(crystal instanceof EntityEnderCrystal) {
									if(swingMode.getValue() == SwingMode.MAINHAND) 
										mc.player.swingArm(EnumHand.MAIN_HAND);
									if(swingMode.getValue() == SwingMode.OFFHAND) 
										mc.player.swingArm(EnumHand.OFF_HAND);
									this.rotateTo(crystal);
									mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
									if(sequential.getValue() && !(pos == null)) {
											AutoCrystal.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
									}
							}
							}
						}
					}
					catch(Exception e) {}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void packetEvent(PacketEvent.Send event) {
		if(event.getEra() == Era.PRE && rotate.getValue() && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            packet.yaw = this.yaw;
            packet.pitch = this.pitch;
            this.rotating = false;
		}
	}
	
	@SubscribeEvent
	public void packetEvent(PacketEvent.Receive event) {
		if(!fullNullCheck()) {
			return;
		}
		if(predictBreak.getValue() && explode.getValue() && event.getPacket() instanceof SPacketSpawnObject) {
			SPacketSpawnObject sPacketSpawnObject = (SPacketSpawnObject) ((Object)event.getPacket());
            if (sPacketSpawnObject.getType() == 51) {
            	BlockPos predictBlock = new BlockPos(sPacketSpawnObject.getX(), sPacketSpawnObject.getY(), sPacketSpawnObject.getZ());
            	if(predictBlock != null) {
                	if(damageCheck(predictBlock) && mc.player.getDistanceSq(predictBlock) <= breakRange.getValue() && predictTimer.hasReached(predictDelay.getValue())) {
                		this.rotateToPos(predictBlock);
                        this.attackCrystalPredict(sPacketSpawnObject.getEntityID(), predictBlock);
                	}
            	}
            	
            }
		}
		if(inhibit.getValue()) {
			Packet packet = event.getPacket();
			if (packet instanceof SPacketSoundEffect && ((SPacketSoundEffect) packet).getCategory() == SoundCategory.BLOCKS && ((SPacketSoundEffect) packet).getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE)             for (Entity entity : mc.world.loadedEntityList) if (entity instanceof EntityEnderCrystal && entity.getDistance(((SPacketSoundEffect) packet).getX(), ((SPacketSoundEffect) packet).getY(), ((SPacketSoundEffect) packet).getZ()) <= 12) entity.setDead();
		}
	}
	
	public enum SwingMode {
		MAINHAND,
		OFFHAND,
		NONE
	}
	
	public enum BreakMode {
		ALL,
		SMART,
		OWN
	}
	
	@SubscribeEvent
	public void render(RenderWorldLastEvent event) {
		if(!(pos == null))
			RenderUtil.drawBox(RenderUtil.generateBB(pos.getX(), pos.getY(), pos.getZ()), 255 / 255f, 255 / 255f, 255 / 255f, 140 / 255f);
	}
	
    private void rotateTo(Entity entity) {
        if (this.rotate.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }

    private void rotateToPos(BlockPos pos) {
        if (this.rotate.getValue().booleanValue()) {
            float[] angle = MathUtil.calcAngle(AutoCrystal.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() - 0.5f, (float) pos.getZ() + 0.5f));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }
    
    private void attackCrystalPredict(int n, BlockPos blockPos) {
    	rotateToPos(blockPos);
        CPacketUseEntity cPacketUseEntity = new CPacketUseEntity();
        cPacketUseEntity.entityId = n;
        cPacketUseEntity.action = CPacketUseEntity.Action.ATTACK;
        AutoCrystal.mc.player.connection.sendPacket(cPacketUseEntity);
        this.predictTimer.reset();
    }
    
    private boolean damageCheck(BlockPos blockPos) {
    	if(blockPos != null) {
			float selfDmg = CrystalUtil.calculateDamage(blockPos.getX() + 0.5f, blockPos.getY() + 1, blockPos.getZ() + 0.5d, mc.player);
			if(selfDmg <= maxSelfDmg.getValue().floatValue()) {
				return true;
			}
    	}
		return false;
    }
}