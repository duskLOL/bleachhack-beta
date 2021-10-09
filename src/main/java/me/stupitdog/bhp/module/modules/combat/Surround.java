package me.stupitdog.bhp.module.modules.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import me.stupitdog.bhp.util.BlockUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Surround extends Module {

	private Setting<Boolean> centre = this.register(new Setting<Boolean>("Centre", true));
	private Setting<Integer> blocksPerTick = this.register(new Setting<Integer>("BlocksPerTick",4,1,10));
	
	private int oldSlot;
	
	public Surround() {
		super("Surround","",Category.COMBAT);
	}
	
	private Vec3d center = Vec3d.ZERO;
    private int y;
    
    public void onEnable() {
		if(fullNullCheck()) {
    	
    	y = (int) mc.player.posY;
    	
        int oldSlot = mc.player.inventory.currentItem;
    	
    	if(centre.getValue()) {
		    for (Vec3d placePositions : getSurroundType()) {
		        BlockPos blockPos = new BlockPos(placePositions.add(mc.player.getPositionVector()));
		        
		        if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)) {
		            center = getCenter(mc.player.posX, mc.player.posY, mc.player.posZ);
		            
		            mc.player.motionX = 0;
		            mc.player.motionZ = 0;
		            
		            mc.player.connection.sendPacket(new CPacketPlayer.Position(center.x, center.y, center.z, true));
		            mc.player.setPosition(center.x, center.y, center.z);
		        }
		    }
    	}
		}
    }
	
	public void onUpdate() {
		if(fullNullCheck()) {
		if(mc.player.posY != y) {
			toggle();
		}
		
        int blocksPlaced = 0;
		oldSlot = mc.player.inventory.currentItem;
        
        for (Vec3d placePositions : getSurroundType()) {
            BlockPos blockPos = new BlockPos(placePositions.add(mc.player.getPositionVector()));
            
            for (int i = 0; i < 9; i++) {
                Item item = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();
                if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(Blocks.OBSIDIAN)) {
                    mc.player.inventory.currentItem = i;
                }
            }
            
            if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR)) {
                BlockUtil.placeBlock(blockPos);
                blocksPlaced++;
                if (blocksPlaced == blocksPerTick.getValue()) return;
            }
        }
		mc.player.inventory.currentItem = oldSlot;
		}
	}
	
    private List<Vec3d> getSurroundType() {
        return standardSurround;
    }
	
    private final List<Vec3d> standardSurround = new ArrayList<>(Arrays.asList(
            new Vec3d(0, -1, 0),
            new Vec3d(1, -1, 0),
            new Vec3d(0, -1, 1),
            new Vec3d(-1, -1, 0),
            new Vec3d(0, -1, -1),
            new Vec3d(1, 0, 0),
            new Vec3d(0, 0, 1),
            new Vec3d(-1, 0, 0),
            new Vec3d(0, 0, -1)
    ));
    
    public Vec3d getCenter(double posX, double posY, double posZ) {
        double x = Math.floor(posX) + 0.5D;
        double y = Math.floor(posY);
        double z = Math.floor(posZ) + 0.5D;

        return new Vec3d(x, y, z);
    }
}
