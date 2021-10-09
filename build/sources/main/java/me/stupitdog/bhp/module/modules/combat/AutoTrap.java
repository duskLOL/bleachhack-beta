package me.stupitdog.bhp.module.modules.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import me.stupitdog.bhp.util.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AutoTrap extends Module {

	private Setting<Double> range = this.register(new Setting<Double>("Range",4.4,1.0,10.0));
	private Setting<Integer> blockspertick = this.register(new Setting<Integer>("BlocksPerTick",1,1,10));
	
	public AutoTrap() {
		super("AutoTrap","",Category.COMBAT);
	}

	private boolean hasPlaced;

    @Override
    public void onEnable() {
    	super.onEnable();
        hasPlaced = false;
    }

    public void onUpdate() {
		if(fullNullCheck()) {
        int blocksPlaced = 0;

        
        this.setBracket("");
        final EntityPlayer target = getClosestPlayer();
        if(!(target == null)) {
        	try {
                this.setBracket(target.getName());
                }
                catch(Exception e) {
                	e.printStackTrace();
                }
                for (Vec3d autoTrapBox : getTrapType(target)) {

                    if (target != null) {
                        BlockPos blockPos = new BlockPos(autoTrapBox.add(getClosestPlayer().getPositionVector()));

                        if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)) {
                            int oldInventorySlot = mc.player.inventory.currentItem;
                            
                            for (int i = 0; i < 9; i++) {
                                Item item = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();
                                if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(Blocks.OBSIDIAN)) {
                                    mc.player.inventory.currentItem = i;
                                }
                            }
                            
                            BlockUtil.placeBlock(blockPos);
                            mc.player.inventory.currentItem = oldInventorySlot;
                            blocksPlaced++;

                            if (blocksPlaced == blockspertick.getValue()) return;
                        }
                    }
                }
        }
        if (blocksPlaced == 0) {
            hasPlaced = true;
        }
		}
    }
	
    private EntityPlayer getClosestPlayer() {
        EntityPlayer closestPlayer = null;

        for (final EntityPlayer entityPlayer : mc.world.playerEntities) {
            if (!(entityPlayer == mc.player)) {
                final double distance = mc.player.getDistance(entityPlayer);

                if (distance < range.getValue()) {
                    closestPlayer = entityPlayer;
                }
            }
        }
        return closestPlayer;
    }
    
    private List<Vec3d> getTrapType(EntityPlayer target) {
    	BlockPos playerBlockPos = new BlockPos(target.getPositionVector());
        if(!isAir(playerBlockPos.add(1, 0, 0)) || !isAir(playerBlockPos.add(-1, 0, 0)) || !isAir(playerBlockPos.add(0, 0, 1)) || !isAir(playerBlockPos.add(0, 0, -1)))
        	return autoTrap;
        else
        	return autoTrapFeet;
    }

    private List<Vec3d> autoTrapFeet = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, 0),
            new Vec3d(0, -1, -1),
            new Vec3d(0, -1, 1),
            new Vec3d(-1, -1, 0),
            new Vec3d(1, -1, 0),
            new Vec3d(0, 0, -1),
            new Vec3d(0, 0, 1),
            new Vec3d(-1, 0, 0),
            new Vec3d(1, 0, 0),
            new Vec3d(0, 1, -1),
            new Vec3d(0, 1, 1),
            new Vec3d(-1, 1, 0),
            new Vec3d(1, 1, 0),
            new Vec3d(-1, 2, 0),
            new Vec3d(1, 2, 0),
            new Vec3d(0, 2, 0),
            new Vec3d(-1,1,-1),
            new Vec3d(1,1,-1),
            new Vec3d(-1,1,1),
            new Vec3d(1,1,1)
    ));
    
    private List<Vec3d> autoTrap = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, 0),
            new Vec3d(0, -1, -1),
            new Vec3d(0, -1, 1),
            new Vec3d(-1, -1, 0),
            new Vec3d(1, -1, 0),
            //new Vec3d(0, 0, -1),
            //new Vec3d(0, 0, 1),
            //new Vec3d(-1, 0, 0),
            //new Vec3d(1, 0, 0),
            new Vec3d(0, 1, -1),
            new Vec3d(0, 1, 1),
            new Vec3d(-1, 1, 0),
            new Vec3d(1, 1, 0),
            new Vec3d(-1, 2, 0),
            new Vec3d(1, 2, 0),
            new Vec3d(0, 2, 0),
            new Vec3d(-1,1,-1),
            new Vec3d(1,1,-1),
            new Vec3d(-1,1,1),
            new Vec3d(1,1,1)
    ));
	
	public boolean isAir(BlockPos blockPos) {
            return (mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR);
    }
}
