package me.stupitdog.bhp.module.modules.client;

import java.awt.GraphicsEnvironment;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.event.events.SettingChangeEvent;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Fonts extends Module {
	
    public Setting<Boolean> antiAlias = this.register(new Setting<Boolean>("AntiAlias", Boolean.valueOf(true), "Smoother font."));
    public Setting<Boolean> fractionalMetrics = this.register(new Setting<Boolean>("Metrics", Boolean.valueOf(true), "Thinner font."));
    public Setting<Integer> fontSize = this.register(new Setting<Integer>("Size", Integer.valueOf(18), Integer.valueOf(12), Integer.valueOf(30), "Size of the font."));
    public Setting<Style> fontStyle = this.register(new Setting<Style>("Style",Style.PLAIN));
    public int fontNumber = 0;
    private boolean reloadFont = false;
	
	public Fonts() {
		super("Font", "", Category.CLIENT);
	}
	
    public static boolean checkFont(String font, boolean message) {
        String[] fonts;
        for (String s : fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            if (!message && s.equals(font)) {
                return true;
            }
        }
        return false;
    }
    
    @SubscribeEvent
    public void onChange(SettingChangeEvent event) {
    	if(event.getSetting().getModule() == this) {
    		reloadFont = true;
    	}
    }
    
    public void onUpdate() {
		if(fontStyle.getValue() == Style.PLAIN)
			fontNumber = 0;
		if(fontStyle.getValue() == Style.BOLD)
			fontNumber = 1;
		if(fontStyle.getValue() == Style.ITALIC)
			fontNumber = 2;
		if(fontStyle.getValue() == Style.BOLDITALIC)
			fontNumber = 3;
    	if(reloadFont) {
    		Bhp.instance.fontManager.init(false);
    		reloadFont = false;
    	}
    }
    
    public enum Style {
    	PLAIN,
    	BOLD,
    	ITALIC,
    	BOLDITALIC
    }
}
