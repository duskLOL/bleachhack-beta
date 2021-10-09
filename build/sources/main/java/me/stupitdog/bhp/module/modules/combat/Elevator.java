package me.stupitdog.bhp.module.modules.combat;

import java.util.List;

import me.stupitdog.bhp.manager.MessageManager;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import me.stupitdog.bhp.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Elevator extends Module {

	private BlockPos placeTarget;
	@SuppressWarnings("unused")
	private BlockPos newTarget;
	private BlockPos sidePos;
	private EnumFacing sideFacing;
	private EntityPlayer closestTarget;
	
	private int pistonSlot = -1;
	private int redstoneSlot = -1;
	private float rotVar;
	
	private boolean isSneaking;
	
	private int stage;
	
	private Setting<PlaceType> type = this.register(new Setting<PlaceType>("PlaceType",PlaceType.AUTO));
	private Setting<PrefType> preferred = this.register(new Setting<PrefType>("PrefType",PrefType.SIDE));
	
	public Elevator() {
		super("Elevator","",Category.COMBAT);
	}
	
	public void onEnable() {
		if(fullNullCheck()) {
		stage = 0;
		pistonSlot = -1;
		redstoneSlot = -1;
		
        for (int i = 0; i < 9; i++) {

            if (redstoneSlot != -1 && pistonSlot != -1) {
                break;
            }

            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) {
                continue;
            }

            Block block = ((ItemBlock) stack.getItem()).getBlock();

            if (block == Blocks.PISTON) {
            	pistonSlot = i;
            } else if (block == Blocks.REDSTONE_BLOCK) {
                redstoneSlot = i;
            }
        }
        
        if(redstoneSlot == -1) {
        	MessageManager.sendMultiLineMsg("[Elevator] missing redstone block! disabling...");
        	toggle();
        	return;
        }
        
        if(pistonSlot == -1) {
        	MessageManager.sendMultiLineMsg("[Elevator] missing piston block! disabling...");
        	toggle();
        	return;
        }
        
        if (mc.objectMouseOver == null || mc.objectMouseOver.getBlockPos() == null || mc.objectMouseOver.getBlockPos().up() == null) {
            MessageManager.sendMultiLineMsg("[Elevator] not looking at valid target! disabling...");
            toggle();
            return;
        }
        
        placeTarget = mc.objectMouseOver.getBlockPos().up();
        stage = 1;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void onUpdate() {
		if(fullNullCheck()) {
		if(stage == 1) {
			this.findClosestTarget();
			if(!(closestTarget == null)) {
				this.setBracket(closestTarget.getName());
				boolean hasRot = false;
				
				BlockPos tempTarget;
				
				tempTarget = new BlockPos(closestTarget.getPositionVector()).add(1, 1, 0);
				if(this.isSamePos(tempTarget, placeTarget) && hasRot == false) {
					rotVar = -90f;
					sidePos = placeTarget.add(1, 0, 0);
					sideFacing = EnumFacing.WEST;
					newTarget = tempTarget;
					hasRot = true;
				}
				tempTarget = new BlockPos(closestTarget.getPositionVector()).add(-1, 1, 0);
				if(this.isSamePos(tempTarget, placeTarget) && hasRot == false) {
					rotVar = 90f;
					sidePos = placeTarget.add(-1, 0, 0);
					sideFacing = EnumFacing.EAST;
					newTarget = tempTarget;
					hasRot = true;
				}
				tempTarget = new BlockPos(closestTarget.getPositionVector()).add(0, 1, 1);
				if(this.isSamePos(tempTarget, placeTarget) && hasRot == false) {
					rotVar = 0f;
					sidePos = placeTarget.add(0, 0, 1);
					sideFacing = EnumFacing.NORTH;
					newTarget = tempTarget;
					hasRot = true;
				}
				tempTarget = new BlockPos(closestTarget.getPositionVector()).add(0, 1, -1);
				if(this.isSamePos(tempTarget, placeTarget) && hasRot == false) {
					rotVar = 180f;
					sidePos = placeTarget.add(0, 0, -1);
					sideFacing = EnumFacing.SOUTH;
					newTarget = tempTarget;
					hasRot = true;
				}
				
	            mc.player.inventory.currentItem = pistonSlot;
	            mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
	            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(this.rotVar, 0.0f, mc.player.onGround));
	            placeBlock(new BlockPos(placeTarget), EnumFacing.DOWN);
	            mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
	            
	            mc.player.inventory.currentItem = redstoneSlot;
				if(type.getValue() == PlaceType.TOP) {
		            placeBlock(new BlockPos(placeTarget.add(0,1,0)), EnumFacing.DOWN);
				}
				if(type.getValue() == PlaceType.SIDE) {
		            placeBlock(new BlockPos(sidePos), sideFacing);
				}
				if(type.getValue() == PlaceType.AUTO) {
					if(preferred.getValue() == PrefType.TOP) {
						if(BlockUtil.canPlaceBlock(placeTarget.add(0, 1, 0)) && !this.intersectsWithEntity(placeTarget)) {
				            placeBlock(new BlockPos(placeTarget.add(0,1,0)), EnumFacing.DOWN);
						}
						else {
				            placeBlock(new BlockPos(sidePos), sideFacing);
						}
					}
					else {
						if(BlockUtil.canPlaceBlock(sidePos) && !this.intersectsWithEntity(sidePos)) {
				            placeBlock(new BlockPos(sidePos), sideFacing);
						}
						else {
				            placeBlock(new BlockPos(placeTarget.add(0,1,0)), EnumFacing.DOWN);
						}
					}
				}
	            stage = 2;
			}
		}
		}
	}
	
	private void placeBlock(final BlockPos pos, final EnumFacing side) {
		BlockUtil.placeBlock(pos);
    }
	
    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == mc.player) {
                continue;
            }
            if (!isLiving((Entity)target)) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            if (this.closestTarget == null) {
                this.closestTarget = target;
            }
            else {
                if (mc.player.getDistance((Entity)target) >= mc.player.getDistance((Entity)this.closestTarget)) {
                    continue;
                }
                this.closestTarget = target;
            }
        }
    }
    
    public boolean isSamePos(BlockPos newPos, BlockPos placePos) {
    	if(newPos.getX() == placePos.getX()) {
    		if(newPos.getY() == placePos.getY()) {
    			if(newPos.getZ() == placePos.getZ()) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    public static boolean isLiving(final Entity e) {
        return e instanceof EntityLivingBase;
    }
	
    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }
	
	public enum PlaceType {
		TOP,
		SIDE,
		AUTO
	}
	
	public enum PrefType {
		TOP,
		SIDE
	}
}
