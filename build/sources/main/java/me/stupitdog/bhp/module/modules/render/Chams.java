package me.stupitdog.bhp.module.modules.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.event.events.PacketEvent;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import me.stupitdog.bhp.util.RenderUtil;
import me.stupitdog.bhp.util.TotemPopCham;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Chams extends Module {
	public Setting<Boolean> crystal = this.register(new Setting<Boolean>("Crystal",true));
	public Setting<Boolean> outline = this.register(new Setting<Boolean>("Outline",true));
	public Setting<Float> lineWidth = this.register(new Setting<Float>("LineWidth",1.5f,0f,5.0f));
	public Setting<Boolean> pops = this.register(new Setting<Boolean>("Pops",true));
	public Setting<Boolean> self = this.register(new Setting<Boolean>("Self",false));
	public Setting<Boolean> single = this.register(new Setting<Boolean>("Single",false));
	public Setting<Integer> fadeStart = this.register(new Setting<Integer>("FadeStart",200,0,300));
	public Setting<Double> fadeSpeed = this.register(new Setting<Double>("FadeSpeed",0.5d,0d,2d));
	private Setting<Boolean> global = this.register(new Setting<Boolean>("Global",true));
	private Setting<Integer> r = this.register(new Setting<Integer>("Red",255,0,255));
	private Setting<Integer> g = this.register(new Setting<Integer>("Green",255,0,255));
	private Setting<Integer> b = this.register(new Setting<Integer>("Blue",255,0,255));
	public Setting<Integer> a = this.register(new Setting<Integer>("Alpha",255,0,255));
	
	public Color color = new Color(r.getValue(),g.getValue(),b.getValue(),a.getValue());
	
	public Chams() {
		super("Chams","",Category.RENDER);
	}
	
	public void constUpdate() {
		if(global.getValue())
			color = Bhp.instance.colorManager.getColor();
		else
			color = new Color(r.getValue(),g.getValue(),b.getValue(),a.getValue());
	}
	
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double popAlpha;

	@SubscribeEvent
	public void packetListener(PacketEvent.Receive event) {
	       if (event.getPacket() instanceof SPacketEntityStatus) {
	            final SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
	            if (packet.getOpCode() == 35 && packet.getEntity(Chams.mc.world) != null && (self.getValue() || packet.getEntity(Chams.mc.world).getEntityId() != Chams.mc.player.getEntityId())) {
	                final GameProfile profile = new GameProfile(mc.player.getUniqueID(), "");
	                (this.player = new EntityOtherPlayerMP(mc.world, profile)).copyLocationAndAnglesFrom(packet.getEntity(mc.world));
	                this.playerModel = new ModelPlayer(0.0f, false);
	                this.startTime = System.currentTimeMillis();
	                playerModel.bipedHead.showModel = false;
	                playerModel.bipedBody.showModel = false;
	                playerModel.bipedLeftArmwear.showModel = false;
	                playerModel.bipedLeftLegwear.showModel = false;
	                playerModel.bipedRightArmwear.showModel = false;
	                playerModel.bipedRightLegwear.showModel = false;
	                if(!single.getValue()) {
	                	TotemPopCham p = new TotemPopCham(player, playerModel, startTime, a.getValue());
	                }
	            }
	        }
	}
	
    @SubscribeEvent
    public void onRenderWorld(final RenderWorldLastEvent event) {
        if (single.getValue()) {
            if (player == null || mc.world == null || mc.player == null) {
                return;
            }
            GL11.glLineWidth(1.0f);
            Color color = new Color(this.color.getRGB());
            int fillA = (color).getAlpha();
            final long time = System.currentTimeMillis() - this.startTime - ((Number) fadeStart.getValue()).longValue();
            if (System.currentTimeMillis() - this.startTime > ((Number) fadeStart.getValue()).longValue()) {
                double normal = this.normalize((double) time, 0.0, ((Number) fadeSpeed.getValue()).doubleValue());
                normal = MathHelper.clamp(normal, 0.0, 1.0);
                normal = -normal + 1.0;
                fillA *= (int) normal;
            }
            Color lineColor = newAlpha(color, fillA);
            Color fillColor = newAlpha(color, fillA);
            if (this.player != null && this.playerModel != null) {
                RenderUtil.glPrepare();
                GL11.glPushAttrib(1048575);
                GL11.glEnable(2881);
                GL11.glEnable(2848);
                if (popAlpha > 1) popAlpha -= fadeSpeed.getValue();
                Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int) popAlpha);

                if (popAlpha > 1) popAlpha -= fadeSpeed.getValue();
                Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int) popAlpha);
                glColor(fillFinal);
                GL11.glPolygonMode(1032, 6914);
                renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float) this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                glColor(outlineFinal);
                GL11.glPolygonMode(1032, 6913);
                renderEntity(this.player, this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, (float) this.player.ticksExisted, player.rotationYawHead, this.player.rotationPitch, 1);
                GL11.glPolygonMode(1032, 6914);
                GL11.glPopAttrib();
                RenderUtil.glRelease();
            }
        }
    }
    
    double normalize(final double value, final double min, final double max) {
        return (value - min) / (max - min);
    }

    public static void renderEntity(final EntityLivingBase entity, final ModelBase modelBase, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (mc.getRenderManager() == null) {
            return;
        }
        final float partialTicks = mc.getRenderPartialTicks();
        final double x = entity.posX - mc.getRenderManager().viewerPosX;
        double y = entity.posY - mc.getRenderManager().viewerPosY;
        final double z = entity.posZ - mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entity.isSneaking()) {
            y -= 0.125;
        }
        final float interpolateRotation = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        final float interpolateRotation2 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        final float rotationInterp = interpolateRotation2 - interpolateRotation;
        final float renderPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        renderLivingAt(x, y, z);
        final float f8 = handleRotationFloat(entity, partialTicks);
        prepareRotations(entity);
        final float f9 = prepareScale(entity, scale);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, f8, entity.rotationYawHead, entity.rotationPitch, f9, entity);
        modelBase.render(entity, limbSwing, limbSwingAmount, f8, entity.rotationYawHead, entity.rotationPitch, f9);
        GlStateManager.popMatrix();
    }

    public static void prepareTranslate(final EntityLivingBase entityIn, final double x, final double y, final double z) {
        renderLivingAt(x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY, z - mc.getRenderManager().viewerPosZ);
    }

    public static void renderLivingAt(final double x, final double y, final double z) {
        GlStateManager.translate((float)x, (float)y, (float)z);
    }

    public static float prepareScale(final EntityLivingBase entity, final float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        final double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        final double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;
        GlStateManager.scale(scale + widthX, scale * entity.height, scale + widthZ);
        final float f = 0.0625f;
        GlStateManager.translate(0.0f, -1.501f, 0.0f);
        return f;
    }

    public static void prepareRotations(final EntityLivingBase entityLivingBase) {
        GlStateManager.rotate(180.0f - entityLivingBase.rotationYaw, 0.0f, 1.0f, 0.0f);
    }

    public static float interpolateRotation(final float prevYawOffset, final float yawOffset, final float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevYawOffset + partialTicks * f;
    }

    public static Color newAlpha(final Color color, final int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void glColor(final Color color) {
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }

    public static float handleRotationFloat(final EntityLivingBase livingBase, final float partialTicks) {
        return 0.0f;
    }
}
