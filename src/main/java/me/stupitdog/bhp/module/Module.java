package me.stupitdog.bhp.module;

import java.util.ArrayList;
import java.util.List;

import me.stupitdog.bhp.gui.BhpGui;
import me.stupitdog.bhp.setting.Bind;
import me.stupitdog.bhp.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings({"rawtypes","unchecked"})
public class Module {

	public static Minecraft mc = Minecraft.getMinecraft();
	
	private String name;
	private String bracket;
	private Category category;
	public boolean hidden;
    public List<Setting> settings = new ArrayList<Setting>();
    public Setting<Boolean> enabled = this.register(new Setting<Boolean>("Enabled", false));
    public Setting<Boolean> drawn = this.register(new Setting<Boolean>("Drawn", true));
    public Setting<Bind> bind = this.register(new Setting<Bind>("Keybind", new Bind(-1)));
    public Setting<String> displayName;
	
	public Module(String name, String bracket, Category cat) {
		this.name = name;
		this.bracket = bracket;
		this.category = cat;
	}
	
	public void onUpdate() {}
	public void constUpdate() {}
	public void onTick() {}
	
	public void onEnable() {}
	public void onDisable() {}
	
	public void enable() {
		MinecraftForge.EVENT_BUS.register(this);
		onEnable();
	}
	
	public void disable() {
		MinecraftForge.EVENT_BUS.unregister(this);
		onDisable();
	}
	
	public void toggle() {
		enabled.setValue(!enabled.getValue());
		
		if(enabled.getValue())
			enable();
		else
			disable();
	}
	
	public Setting register(Setting setting) {
        setting.setModule(this);
        this.settings.add(setting);
        if (this instanceof Module && mc.currentScreen instanceof BhpGui) {
            BhpGui.getInstance().updateModule((Module) this);
        } 
        return setting;
    }
    
    public void unregister(Setting settingIn) {
        ArrayList<Setting> removeList = new ArrayList<Setting>();
        for (Setting setting : this.settings) {
            if (!setting.equals(settingIn)) continue;
            removeList.add(setting);
        }
        if (!removeList.isEmpty()) {
            this.settings.removeAll(removeList);
        }
        if (this instanceof Module && mc.currentScreen instanceof BhpGui) {
            BhpGui.getInstance().updateModule((Module) this);
        }
    }
    
    public Setting getSettingByName(String name) {
        for (Setting setting : this.settings) {
            if (!setting.getName().equalsIgnoreCase(name)) continue;
            return setting;
        }
        return null;
    }
    
    public void reset() {
        for (Setting setting : this.settings) {
            setting.setValue(setting.getDefaultValue());
        }
    }

    public void clearSettings() {
        this.settings = new ArrayList<Setting>();
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBracket() {
		return bracket;
	}

	public void setBracket(String bracket) {
		this.bracket = bracket;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

    public Bind getBind() {
        return this.bind.getValue();
    }
    
    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

	public boolean isEnabled() {
		return enabled.getValue();
	}

	public void setToggled(boolean toggled) {
		this.enabled.setValue(toggled);
	}

	public List<Setting> getSettings() {
		return settings;
	}
	
	public boolean fullNullCheck() {
		if(mc.player == null || mc.world == null) {
			return false;
		}
		return true;
	}
}
