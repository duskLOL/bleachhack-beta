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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class SelfTrap extends Module {

	private Setting<Integer> blockspertick = this.register(new Setting<Integer>("BlocksPerTick",1,1,10));
	
	public SelfTrap() {
		super("SelfTrap","",Category.COMBAT);
	}
	
	public void onUpdate() {
		if(fullNullCheck()) {
        int blocksPlaced = 0;
		
        for (Vec3d autoTrapBox : selfTrap) {
        	
                BlockPos blockPos = new BlockPos(autoTrapBox.add(mc.player.getPositionVector()));

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
	
    private List<Vec3d> selfTrap = new ArrayList<>(Arrays.asList(
    		new Vec3d(1,1,0),
    		new Vec3d(1,2,0),
    		new Vec3d(0,2,0)
    ));
	
	public boolean isAir(BlockPos blockPos) {
            return (mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR);
    }
}
