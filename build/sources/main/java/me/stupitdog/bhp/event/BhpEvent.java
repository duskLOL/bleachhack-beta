package me.stupitdog.bhp.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.Event;

public class BhpEvent extends Event {

	private Era era = Era.PRE;
    private final float partialTicks;
    private boolean cancelled;
	
	public BhpEvent() {
        partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        cancelled = false;
	}
	
    public BhpEvent(Era p_Era) {
        partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        era = p_Era;
        cancelled = false;
    }
	
    public Era getEra() {
        return era;
    }
    
    public float getPartialTicks() {
        return partialTicks;
    }
    
    public boolean cancelled() {
    	return cancelled;
    }
    
    public void cancel() {
    	cancelled = true;
    }
	
    public enum Era {
        PRE,
        PERI,
        POST
    }
}
