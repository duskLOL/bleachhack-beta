package me.stupitdog.bhp.module.modules.client;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import me.stupitdog.bhp.util.ColorUtil;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HUD extends Module {
	
	public HUD() {
		super("HUD", "", Category.CLIENT);
	}

	@SubscribeEvent
	public void render(RenderGameOverlayEvent event) {
		int color = ColorUtil.toRGBA(Bhp.instance.colorManager.getColor());
		if(event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
			int offset = 0;
			Bhp.instance.fontManager.drawStringWithShadow("bleachhack" + TextFormatting.WHITE + "+ v2.0-beta-0.1", 2, 2, color);
			offset += 10;
			
	    	ArrayList<Module> sortedMods = new ArrayList<Module>();
	    	sortedMods = Bhp.instance.moduleManager.modules;
			sortedMods.sort((a, b) -> Integer.compare(getWidth(b), getWidth(a)));
			
			for(Module m : sortedMods) {
				if(m.isEnabled() && m.drawn.getValue()) {
					if(!m.getBracket().equalsIgnoreCase(""))
						Bhp.instance.fontManager.drawStringWithShadow(m.getName() + TextFormatting.GRAY + " [" +TextFormatting.WHITE + m.getBracket() + TextFormatting.GRAY + "]", 2, 2 + offset, color);
					else
						Bhp.instance.fontManager.drawStringWithShadow(m.getName(), 2, 2 + offset, color);
				offset += 10;
				}
			}
			
			//Bhp.instance.speedManager.updateValues();
			//Bhp.instance.fontManager.drawStringWithShadow("Speed: " + TextFormatting.WHITE + Bhp.instance.speedManager.getSpeedKpH() + "km/h", 2, 2 + offset, color);
		}
	}
	
    public int getWidth(Module m) {
		int width = -1;
		
		if(m.getBracket().equalsIgnoreCase("")) {
			width = Bhp.instance.fontManager.getStringWidth(m.getName());
		}
		else if(!m.getBracket().equalsIgnoreCase("")) {
			width = Bhp.instance.fontManager.getStringWidth(m.getName() + " [" + m.getBracket() + "]");
		}
		return width;
    }
}
