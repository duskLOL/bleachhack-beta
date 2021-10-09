package me.stupitdog.bhp.module.modules.client;

import java.awt.Color;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.event.events.SettingChangeEvent;
import me.stupitdog.bhp.gui.BhpGui;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui extends Module {

    public Setting<Integer> hoverAlpha = this.register(new Setting<Integer>("Alpha", 180, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("HoverAlpha", 240, 0, 255));
    public Setting<CommandColor> cmdColor = this.register(new Setting<CommandColor>("CmdColor",CommandColor.DARKPURPLE));
    public Setting<Integer> toggleR = this.register(new Setting<Integer>("ToggleR",130,0,255));
    public Setting<Integer> toggleG = this.register(new Setting<Integer>("ToggleG",0,0,255));
    public Setting<Integer> toggleB = this.register(new Setting<Integer>("ToggleB",255,0,255));
    public Setting<Integer> fontR = this.register(new Setting<Integer>("FontR",255,0,255));
    public Setting<Integer> fontG = this.register(new Setting<Integer>("FontG",255,0,255));
    public Setting<Integer> fontB = this.register(new Setting<Integer>("FontB",255,0,255));
    public Setting<Integer> bgR = this.register(new Setting<Integer>("BgR",130,0,255));
    public Setting<Integer> bgG = this.register(new Setting<Integer>("BgG",0,0,255));
    public Setting<Integer> bgB = this.register(new Setting<Integer>("BgB",255,0,255));
    
    public Color toggleColor;
    public Color fontColor;
    public Color bgColor;
    
    public TextFormatting cmdColorTxt;
	
	public ClickGui() {
		super("ClickGui", "", Category.CLIENT);
		toggleColor = new Color(toggleR.getValue(),toggleG.getValue(),toggleB.getValue());
		fontColor = new Color(fontR.getValue(),fontG.getValue(),fontB.getValue());
		bgColor = new Color(bgR.getValue(),bgG.getValue(),bgB.getValue());
	}
	
	@SubscribeEvent
	public void onSetChange(SettingChangeEvent event) {
		if(event.getSetting() == cmdColor) {
		}
	}
	
	public void constUpdate() {
		toggleColor = new Color(toggleR.getValue(),toggleG.getValue(),toggleB.getValue());
		fontColor = new Color(fontR.getValue(),fontG.getValue(),fontB.getValue());
		bgColor = new Color(bgR.getValue(),bgG.getValue(),bgB.getValue());
		Bhp.instance.colorManager.setColor(toggleColor);
		Bhp.instance.colorManager.setRed(toggleR.getValue() / 255f);
		Bhp.instance.colorManager.setGreen(toggleG.getValue() / 255f);
		Bhp.instance.colorManager.setBlue(toggleB.getValue() / 255f);
		if(cmdColor.getValue() == CommandColor.AQUA) 
			cmdColorTxt = TextFormatting.AQUA;
		if(cmdColor.getValue() == CommandColor.BLACK) 
			cmdColorTxt = TextFormatting.BLACK;
		if(cmdColor.getValue() == CommandColor.BLUE) 
			cmdColorTxt = TextFormatting.BLUE;
		if(cmdColor.getValue() == CommandColor.DARKPURPLE) 
			cmdColorTxt = TextFormatting.DARK_PURPLE;
		if(cmdColor.getValue() == CommandColor.GOLD) 
			cmdColorTxt = TextFormatting.GOLD;
		if(cmdColor.getValue() == CommandColor.GREEN) 
			cmdColorTxt = TextFormatting.GREEN;
		if(cmdColor.getValue() == CommandColor.GRAY)
			cmdColorTxt = TextFormatting.GRAY;
		if(cmdColor.getValue() == CommandColor.DARKRED)
			cmdColorTxt = TextFormatting.DARK_RED;
		if(cmdColor.getValue() == CommandColor.RED) 
			cmdColorTxt = TextFormatting.RED;
		Bhp.instance.cmdColor = cmdColorTxt;
	}
	
	public void onEnable() {
		mc.displayGuiScreen(BhpGui.getClickGui());
	}
	
	public enum CommandColor {
		AQUA,
		BLACK,
		BLUE,
		GOLD,
		GRAY,
		GREEN,
		RED,
		DARKPURPLE,
		DARKRED
	}
}
