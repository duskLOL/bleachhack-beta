package me.stupitdog.bhp.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import me.stupitdog.bhp.event.events.PlayerCollisionEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

@Mixin(value = EntityPlayer.class, priority = Integer.MAX_VALUE)
public abstract class MixinEntityPlayer extends MixinEntityLivingBase
{
    public MixinEntityPlayer()
    {
        super();
    }

    @Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
    public void applyEntityCollision(Entity p_Entity, CallbackInfo info)
    {
        PlayerCollisionEvent event = new PlayerCollisionEvent(p_Entity);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.cancelled())
            info.cancel();
    }
}
