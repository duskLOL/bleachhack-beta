package me.stupitdog.bhp.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.input.Mouse;

import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.gui.components.Component;
import me.stupitdog.bhp.gui.components.items.Item;
import me.stupitdog.bhp.gui.components.items.buttons.ModuleButton;
import me.stupitdog.bhp.module.Category;
import me.stupitdog.bhp.module.Module;
import me.stupitdog.bhp.module.modules.client.ClickGui;
import net.minecraft.client.gui.GuiScreen;

public class BhpGui extends GuiScreen {

	 private static BhpGui oyveyGui;
	    private static BhpGui INSTANCE;

	    static {
	        INSTANCE = new BhpGui();
	    }

	    private final ArrayList<Component> components = new ArrayList();

	    public BhpGui() {
	        this.setInstance();
	        this.load();
	    }

	    public static BhpGui getInstance() {
	        if (INSTANCE == null) {
	            INSTANCE = new BhpGui();
	        }
	        return INSTANCE;
	    }

	    public static BhpGui getClickGui() {
	        return BhpGui.getInstance();
	    }

	    private void setInstance() {
	        INSTANCE = this;
	    }

	    private void load() {
	        int x = -84;
	        for (final Category category : Category.values()) {
	            this.components.add(new Component(category.name().substring(0, 1) + category.name().substring(1).toLowerCase(), x += 90, 4, true) {

	                @Override
	                public void setupItems() {
	                    counter1 = new int[]{1};
	                    Bhp.instance.moduleManager.getModulesInCategory(category).forEach(module -> {
	                        if (!module.hidden) {
	                            this.addButton(new ModuleButton(module));
	                        }
	                    });
	                }
	            });
	        }
	        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(Item::getName)));
	    }

	    public void updateModule(Module module) {
	        for (Component component : this.components) {
	            for (Item item : component.getItems()) {
	                if (!(item instanceof ModuleButton)) continue;
	                ModuleButton button = (ModuleButton) item;
	                Module mod = button.getModule();
	                if (module == null || !module.equals(mod)) continue;
	                button.initSettings();
	            }
	        }
	    }

	    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
	        this.checkMouseWheel();
	        //this.drawDefaultBackground();
	        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
	    }

	    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
	        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
	    }

	    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
	        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
	    }

	    public boolean doesGuiPauseGame() {
	        return false;
	    }

	    public final ArrayList<Component> getComponents() {
	        return this.components;
	    }

	    public void checkMouseWheel() {
	        int dWheel = Mouse.getDWheel();
	        if (dWheel < 0) {
	            this.components.forEach(component -> component.setY(component.getY() - 10));
	        } else if (dWheel > 0) {
	            this.components.forEach(component -> component.setY(component.getY() + 10));
	        }
	    }

	    public int getTextOffset() {
	        return -6;
	    }

	    public Component getComponentByName(String name) {
	        for (Component component : this.components) {
	            if (!component.name.equalsIgnoreCase(name)) continue;
	            return component;
	        }
	        return null;
	    }

	    public void keyTyped(char typedChar, int keyCode) throws IOException {
	        if (keyCode == 1)
	        {
	        	Bhp.instance.moduleManager.getModuleByClass(ClickGui.class).toggle();
	            this.mc.displayGuiScreen((GuiScreen)null);

	            if (this.mc.currentScreen == null)
	            {
	                this.mc.setIngameFocus();
	            }
	        }
	        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
	    }
}
