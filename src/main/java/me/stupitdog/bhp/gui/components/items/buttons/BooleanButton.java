package me.stupitdog.bhp.gui.components.items.buttons;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.gui.BhpGui;
import me.stupitdog.bhp.module.modules.client.ClickGui;
import me.stupitdog.bhp.setting.Setting;
import me.stupitdog.bhp.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class BooleanButton
extends Button {
private final Setting setting;

public BooleanButton(Setting setting) {
super(setting.getName());
this.setting = setting;
this.width = 15;
}

@Override
public void drawScreen(int mouseX, int mouseY, float partialTicks) {
RenderUtil.drawRect(this.x, this.y, this.x + (float) this.width + 7.4f, this.y + (float) this.height - 0.5f, this.getState() ? (!this.isHovering(mouseX, mouseY) ? Bhp.instance.colorManager.getColorWithAlpha(Bhp.instance.moduleManager.getModuleByClass(ClickGui.class).hoverAlpha.getValue()) : Bhp.instance.colorManager.getColorWithAlpha(Bhp.instance.moduleManager.getModuleByClass(ClickGui.class).alpha.getValue())) : (!this.isHovering(mouseX, mouseY) ? 0x11555555 : -2007673515));
Bhp.instance.fontManager.drawStringWithShadow(this.getName(), this.x + 2.3f, this.y - 1.7f - (float) BhpGui.getClickGui().getTextOffset(), this.getState() ? Bhp.instance.moduleManager.getModuleByClass(ClickGui.class).fontColor.getRGB() : Bhp.instance.moduleManager.getModuleByClass(ClickGui.class).fontColor.getRGB());
}

@Override
public void update() {
this.setHidden(!this.setting.isVisible());
}

@Override
public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
super.mouseClicked(mouseX, mouseY, mouseButton);
if (this.isHovering(mouseX, mouseY)) {
	Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
}
}

@Override
public int getHeight() {
return 14;
}

@Override
public void toggle() {
this.setting.setValue((Boolean) this.setting.getValue() == false);
}

@Override
public boolean getState() {
return (Boolean) this.setting.getValue();
}
}
