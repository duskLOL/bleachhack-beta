package me.stupitdog.bhp.module.modules.client;

import java.awt.Color;
import java.util.Objects;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.event.events.SettingChangeEvent;
import me.stupitdog.bhp.gui.BhpGui;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import me.stupitdog.bhp.util.ColorUtil;
import me.stupitdog.bhp.util.RenderUtil;
import me.stupitdog.bhp.util.Timer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CSGOWatermark extends Module {

	private String message = "";
	
    Timer delayTimer = new Timer();
    public Setting<Integer> X = this.register(new Setting("WatermarkX", 0, 0, 300));
    public Setting<Integer> Y = this.register(new Setting("WatermarkY", 0, 0, 300));
    public Setting<Integer> delay = this.register(new Setting<Object>("Delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600)));
    public Setting<Integer> saturation = this.register(new Setting<Object>("Saturation", 127, 1, 255));
    public Setting<Integer> brightness = this.register(new Setting<Object>("Brightness", 100, 0, 255));
    public float hue;
    public int red = 1;
    public int green = 1;
    public int blue = 1;

    public CSGOWatermark() {
        super("CSGOWatermark", "noat em cee actually makes something", Category.CLIENT);
    }

    @SubscribeEvent
    public void render ( RenderGameOverlayEvent event ) {
    	if(event.getType() == RenderGameOverlayEvent.ElementType.TEXT)
    		drawCsgoWatermark();
    }

    public void drawCsgoWatermark () {
        message = "bleachhack+ v2.0 | " + mc.player.getName() + " | " + this.getPing() + "ms";
        Integer textWidth = mc.fontRenderer.getStringWidth(message);
        Integer textHeight = mc.fontRenderer.FONT_HEIGHT;
        RenderUtil.drawRect(X.getValue() - 4, Y.getValue() - 4, textWidth + 16, textHeight + 12, ColorUtil.toRGBA(22, 22, 22, 255));
        RenderUtil.drawRect(X.getValue(), Y.getValue(), textWidth + 4, textHeight + 4, ColorUtil.toRGBA(0, 0, 0, 255));
        RenderUtil.drawRect(X.getValue(), Y.getValue(), textWidth + 8, textHeight + 4, ColorUtil.toRGBA(0, 0, 0, 255));
        RenderUtil.drawRect(X.getValue(), Y.getValue(), textWidth + 8,  1, rainbow(this.delay.getValue()).hashCode());
        mc.fontRenderer.drawString(message, X.getValue() + 3, Y.getValue() + 3, ColorUtil.toRGBA(255, 255, 255, 255), false);
    }

    public Color rainbow(int delay) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + (long) delay) / 20.0);
        return Color.getHSBColor((float) ((rainbowState %= 360.0) / 360.0), this.saturation.getValue().floatValue() / 255.0f, this.brightness.getValue().floatValue() / 255.0f);
    }

public int getPing() {
    if (fullNullCheck()) {
        return 0;
    }
    try {
        return Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.getConnection().getGameProfile().getId()).getResponseTime();
    } catch (Exception e) {
        return 0;
    }
}

}
