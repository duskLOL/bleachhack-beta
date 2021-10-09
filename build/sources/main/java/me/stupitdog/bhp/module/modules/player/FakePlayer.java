package me.stupitdog.bhp.module.modules.player;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.GameType;

public class FakePlayer extends Module {

	public FakePlayer() {
		super("FakePlayer","shitass",Category.PLAYER);
	}
	
	private EntityOtherPlayerMP clonedPlayer;
		
	public void onEnable() { 


      if (mc.player == null || mc.player.isDead){
          toggle();
          return;
      }

  	clonedPlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("031311f6-25dd-4f05-b472-2af7d516401c"), "shitass"));
      clonedPlayer.copyLocationAndAnglesFrom(mc.player);
      clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
      clonedPlayer.rotationYaw = mc.player.rotationYaw;
      clonedPlayer.rotationPitch = mc.player.rotationPitch;
      clonedPlayer.setGameType(GameType.SURVIVAL);
      clonedPlayer.setHealth(20);
      mc.world.addEntityToWorld(-1234, clonedPlayer);
      clonedPlayer.onLivingUpdate();
  }

  public void onDisable() {
      if (mc.world != null) {
          mc.world.removeEntityFromWorld(-1234);
      }
  }
}
