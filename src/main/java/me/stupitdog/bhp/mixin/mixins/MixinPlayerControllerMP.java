package me.stupitdog.bhp.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.stupitdog.bhp.event.events.DamageBlockEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

@Mixin(PlayerControllerMP.class)
public abstract class MixinPlayerControllerMP {
	
	@Inject(method = "onPlayerDamageBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", at = @At("HEAD"), cancellable = true)
	private void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.cancelled()) {
			callbackInfoReturnable.setReturnValue(false);
		}
	}
}
