package me.stupitdog.bhp.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.EntityLivingBase;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends MixinEntity
{
    public MixinEntityLivingBase()
    {
        super();
    }

    @Shadow
    public void jump()
    {
    }
}
