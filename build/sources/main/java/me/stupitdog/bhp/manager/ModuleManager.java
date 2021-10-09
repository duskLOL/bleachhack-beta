package me.stupitdog.bhp.manager;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.module.modules.client.*;
import me.stupitdog.bhp.module.modules.combat.*;
import me.stupitdog.bhp.module.modules.exploit.*;
import me.stupitdog.bhp.module.modules.movement.*;
import me.stupitdog.bhp.module.modules.player.*;
import me.stupitdog.bhp.module.modules.render.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ModuleManager {
	private Minecraft mc = Minecraft.getMinecraft();
	
	public ArrayList<Module> modules;
	
	public ModuleManager() {
		MinecraftForge.EVENT_BUS.register(this);
		modules = new ArrayList<>();
		init();
	}
	
	/*Suggestions
	 * -Tomb
	 * -AutoCity
	 * -AutoDonkeyDupe
	 * -Tracers
	 * -ChestESP
	 * -AntiHunger
	 * -AutoSelfAnvil
	 * */
	
	public void init() {
		//combat
		modules.add(new Aura()); //done
		modules.add(new AutoCrystal());
		modules.add(new AutoTrap()); //done
		modules.add(new Burrow()); //done
		modules.add(new Criticals()); //done
		modules.add(new Elevator()); //done
		modules.add(new HoleFill()); //done
		modules.add(new SelfTrap());
		modules.add(new Surround()); //done
		//movement
		modules.add(new NoSlowDown()); //done
		modules.add(new ReverseStep()); //done
		modules.add(new Speed()); //done
		modules.add(new Sprint()); //done
		modules.add(new Step()); //done
		modules.add(new Velocity()); //done
		//player
		modules.add(new FakePlayer()); //done
		modules.add(new FastUse()); //done
		modules.add(new GuiMove()); //done
		//exploit
		modules.add(new Blink()); //done
		modules.add(new BoatFly());
		modules.add(new GodModule()); //done
		modules.add(new Speedmine()); //done
		//render
		modules.add(new Chams());
		modules.add(new HoleESP());
		modules.add(new Nametags());
		//client
		modules.add(new ClickGui()); //done
		modules.add(new CSGOWatermark()); //done
		modules.add(new Fonts()); //done
		modules.add(new HUD()); //done
		modules.add(new RPC()); //done
	}
	
	public Module getModule(String name) {
		for(Module m : modules) {
			if(m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		return null;
	}
	
	public ArrayList<Module> getModulesInCategory(Category c) {
		ArrayList<Module> mods = new ArrayList<Module>();
		
		for(Module m : modules) {
			if(m.getCategory() == c) {
				mods.add(m);
			}
		}
		return mods;
	}
	
    public <T extends Module> T getModuleByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

	
	@SubscribeEvent
	public void onUpdate(ClientTickEvent event) {
		if(mc.player != null) {
			for(Module m : modules) {
				if(m.isEnabled()) {
					m.onUpdate();
				}
				m.constUpdate();
			}
		}
	}
	
	@SubscribeEvent
	public void onKey(KeyInputEvent event) {
		if(mc.player != null && mc.world != null) {
			if(Keyboard.getEventKeyState()) {
		        this.modules.forEach(module -> {
		            if (module.getBind().getKey() == Keyboard.getEventKey()) {
		                module.toggle();
		            }
		        });
			}
		}
	}
}
