package me.stupitdog.bhp.mixin.mixins;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.module.modules.render.Chams;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@Mixin(value={RenderEnderCrystal.class})
public class MixinRenderEnderCrystal {

	@Shadow
    @Final
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;

    @Redirect(method={"doRender"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    public void renderModelBaseHook(ModelBase model, Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    	if(Bhp.instance.moduleManager.getModuleByClass(Chams.class).isEnabled() && Bhp.instance.moduleManager.getModuleByClass(Chams.class).crystal.getValue()) {
            GL11.glPushAttrib((int)1048575);
            GL11.glDisable((int)3008);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glEnable((int)3042);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth(Bhp.instance.moduleManager.getModuleByClass(Chams.class).lineWidth.getValue());
            GL11.glEnable((int)2960);
            
            Color visibleColor;
            Color color = visibleColor = new Color(Bhp.instance.moduleManager.getModuleByClass(Chams.class).color.getRGB());
            float alpha = Bhp.instance.moduleManager.getModuleByClass(Chams.class).a.getValue();
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glEnable((int)10754);
            GL11.glColor4f((float)((float)visibleColor.getRed() / 255.0f), (float)((float)visibleColor.getGreen() / 255.0f), (float)((float)visibleColor.getBlue() / 255.0f), alpha / 255.0f);
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if(Bhp.instance.moduleManager.getModuleByClass(Chams.class).outline.getValue()) {
                GL11.glPolygonMode(1032, 6913);
                model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                GL11.glPolygonMode(1032, 6914);
            }
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2896);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)3008);
            GL11.glPopAttrib();
    	}
    	else {
    		model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    	}
    }
}
