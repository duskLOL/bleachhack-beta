package me.stupitdog.bhp;

import me.stupitdog.bhp.manager.ColorManager;
import me.stupitdog.bhp.manager.CommandManager;
import me.stupitdog.bhp.manager.ConfigManager;
import me.stupitdog.bhp.manager.FileManager;
import me.stupitdog.bhp.manager.FontManager;
import me.stupitdog.bhp.manager.ModuleManager;
import me.stupitdog.bhp.manager.SpeedManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "bhp", name = "bleachhack+", version = "v2.0")
public class Bhp {

	@Instance
	public static Bhp instance = new Bhp();
	
	public ModuleManager moduleManager;
	public CommandManager commandManager;
	public ColorManager colorManager;
	public FontManager fontManager;
	public ConfigManager configManager;
	public FileManager fileManager;
	public SpeedManager speedManager;
	
	public String name;
	public TextFormatting cmdColor;
	
	@EventHandler
	public void init(FMLPreInitializationEvent event) {
		moduleManager = new ModuleManager();
		commandManager = new CommandManager();
		colorManager = new ColorManager();
		fontManager = new FontManager();
		fileManager = new FileManager();
		configManager = new ConfigManager();
		speedManager = new SpeedManager();
		
		configManager.init();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
	}
}
