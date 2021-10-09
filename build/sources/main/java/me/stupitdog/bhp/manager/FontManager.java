package me.stupitdog.bhp.manager;

import java.awt.Font;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.gui.font.CustomFont;
import me.stupitdog.bhp.module.modules.client.Fonts;
import me.stupitdog.bhp.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public class FontManager {
	
    private final Timer idleTimer = new Timer();
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    private CustomFont customFont = new CustomFont(new Font("Verdana", 0, 17), true, false);
    private boolean idling;

    public FontManager() {
        this.updateResolution();
    }

    public void init(boolean startup) {
        Fonts cFont = Bhp.instance.moduleManager.getModuleByClass(Fonts.class);
        try {
            this.setFontRenderer(new Font("Verdana", cFont.fontNumber, cFont.fontSize.getValue()), cFont.antiAlias.getValue(), cFont.fractionalMetrics.getValue());
        } catch (Exception exception) {
            // empty catch block
        }
    }

    public void drawStringWithShadow(String text, float x, float y, int color) {
        this.drawString(text, x, y, color, true);
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if (Bhp.instance.moduleManager.getModuleByClass(Fonts.class).isEnabled()) {
            if (shadow) {
                this.customFont.drawStringWithShadow(text, x, y, color);
            } else {
                this.customFont.drawString(text, x, y, color);
            }
            return;
        }
        Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, color, shadow);
    }

    public int getStringWidth(String text) {
        if (Bhp.instance.moduleManager.getModuleByClass(Fonts.class).isEnabled()) {
            return this.customFont.getStringWidth(text);
        }
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }

    public int getFontHeight() {
        if (Bhp.instance.moduleManager.getModuleByClass(Fonts.class).isEnabled()) {
            String text = "A";
            return this.customFont.getStringHeight(text);
        }
        return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
    }

    public void setFontRenderer(Font font, boolean antiAlias, boolean fractionalMetrics) {
        this.customFont = new CustomFont(font, antiAlias, fractionalMetrics);
    }

    public Font getCurrentFont() {
        return this.customFont.getFont();
    }

    public void updateResolution() {
        this.scaledWidth = Minecraft.getMinecraft().displayWidth;
        this.scaledHeight = Minecraft.getMinecraft().displayHeight;
        this.scaleFactor = 1;
        boolean flag = Minecraft.getMinecraft().isUnicode();
        int i = Minecraft.getMinecraft().gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        double scaledWidthD = this.scaledWidth / this.scaleFactor;
        double scaledHeightD = this.scaledHeight / this.scaleFactor;
        this.scaledWidth = MathHelper.ceil(scaledWidthD);
        this.scaledHeight = MathHelper.ceil(scaledHeightD);
    }

    public String getIdleSign() {
        if (this.idleTimer.hasReached(500L)) {
            this.idling = !this.idling;
            this.idleTimer.reset();
        }
        if (this.idling) {
            return "_";
        }
        return "";
    }
}
