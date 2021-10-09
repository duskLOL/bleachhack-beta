package me.stupitdog.bhp.module.modules.combat;

import me.stupitdog.bhp.manager.MessageManager;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import me.stupitdog.bhp.util.BurrowUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Burrow extends Module {

	private Setting<Mode> mode = this.register(new Setting<Mode>("Mode",Mode.OBSIDIAN));
	private Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate",false));
	private Setting<Double> offset = this.register(new Setting<Double>("Offset",5d,-20d,20d));
	
    private BlockPos originalPos;
    private int oldSlot = -1;
    private int stage;
	
	public Burrow() {
		super("Burrow","",Category.COMBAT);
	}
	
	public enum Mode {
		OBSIDIAN,
		ECHEST,
	}
	
	@Override
    public void onEnable() {
		if(fullNullCheck()) {
        super.onEnable();
        this.setBracket(mode.currentEnumName());
        stage = 0;

        // Save our original pos
        originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        // If we can't place in our actual post then toggle and return
        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) ||
                intersectsWithEntity(this.originalPos)) {
            toggle();
            return;
        }

        // Save our item slot
        oldSlot = mc.player.inventory.currentItem;
		}
    }

    public void onUpdate() {
		if(fullNullCheck()) {
    	if(mode.getValue() == Mode.OBSIDIAN) {
            if (BurrowUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            	MessageManager.sendChatErrorMessage("[Burrow] obsidian not found! disabling");
                toggle();
                return;
            }

            if(stage == 0) {
                // Change to obsidian slot
                BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockObsidian.class), false);

                // Fake jump
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));

                // Place block
                BurrowUtil.placeBlock(originalPos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);

                // Rubberband
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset.getValue(), mc.player.posZ, false));

                // SwitchBack
                BurrowUtil.switchToSlot(oldSlot, false);
                stage = 1;
                return;
            }
            
            if(stage == 1) {
            	if(mc.player.motionY > 0) {
            		toggle();
            		return;
            	}
            }
    	}
    	if(mode.getValue() == Mode.ECHEST) {
            if (BurrowUtil.findHotbarBlock(BlockEnderChest.class) == -1) {
            	MessageManager.sendChatErrorMessage("[Burrow] enderchest not found! disabling");
                toggle();
                return;
            }

            if(stage == 0) {
                // Change to obsidian slot
                BurrowUtil.switchToSlot(BurrowUtil.findHotbarBlock(BlockEnderChest.class), false);

                // Fake jump
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));

                // Place block
                BurrowUtil.placeBlock(originalPos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);

                // Rubberband
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset.getValue(), mc.player.posZ, false));

                // SwitchBack
                BurrowUtil.switchToSlot(oldSlot, false);
                stage = 1;
                return;
            }
            
            if(stage == 1) {
            	if(mc.player.motionY > 0) {
            		toggle();
            		return;
            	}
            }
    	}
            if(stage == 1) {
            	if(mc.player.motionY > 0) {
            		toggle();
            		return;
            	}
            }
		}
    }
    
	@SubscribeEvent
	public void listener(PlayerSPPushOutOfBlocksEvent event) {
		event.setCanceled(true);;
	}

    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }
}
