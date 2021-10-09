package me.stupitdog.bhp.event.events;

import me.stupitdog.bhp.event.BhpEvent;
import net.minecraft.network.Packet;

public class NetworkPacketEvent extends BhpEvent {

	public Packet m_Packet;
	
    
	public NetworkPacketEvent(Packet p_Packet) {
	    super();
	    m_Packet = p_Packet;
	}
	    
	public Packet GetPacket() {
	   return m_Packet;
	}
	    
	public Packet getPacket() {
	   return m_Packet;
	}
}
