package me.stupitdog.bhp.module.modules.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import me.stupitdog.bhp.util.BlockUtil;
import me.stupitdog.bhp.util.HoleUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.Packet;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class HoleFill extends Module {

	private Setting<Double> range = this.register(new Setting<Double>("Range",4d,0d,10d));
	private Setting<Boolean> autoDisable = this.register(new Setting<Boolean>("Disable",false));
	
	private HoleUtil holeUtil = new HoleUtil();
	private int originalItem;
	
	public HoleFill() {
		super("HoleFill","",Category.COMBAT);
	}
	
	public void onEnable() {
		if(fullNullCheck()) {
		originalItem = mc.player.inventory.currentItem;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void onUpdate() {
		if(fullNullCheck()) {
        List<BlockPos> obsidianHoles = holeUtil.findObsidianHoles();
        List<BlockPos> bedrockHoles = holeUtil.findBedrockHoles();
        
        originalItem = mc.player.inventory.currentItem;
        for (int i = 0; i < 9; i++) {
            Item item = Minecraft.getMinecraft().player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(Blocks.OBSIDIAN)) {
                mc.player.inventory.currentItem = i;
            }
        }
        
		List<Entity> entities = new ArrayList<>();
		
		entities.addAll(mc.world.playerEntities.stream().collect(Collectors.toList()));
        
        for(BlockPos hole : obsidianHoles) {
        	if(mc.player.getDistance(hole.getX(), hole.getY(), hole.getZ()) < range.getValue() && !this.intersectsWithEntity(hole)) {
                BlockUtil.placeBlock(hole);
        	}
        }
        
        for(BlockPos hole : bedrockHoles) {
        	if(mc.player.getDistance(hole.getX(), hole.getY(), hole.getZ()) < range.getValue() && !this.intersectsWithEntity(hole)) {
                BlockUtil.placeBlock(hole);
        	}
        }
        
        mc.player.inventory.currentItem = originalItem;
        if(autoDisable.getValue()) {
	        this.toggle();
	        return;
        }
		}
	}
	
    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }
}
