package me.stupitdog.bhp.mixin.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;
import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.event.events.NetworkPacketEvent;
import me.stupitdog.bhp.event.events.PacketEvent;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void onSendPacket(Packet<?> p_Packet, CallbackInfo callbackInfo) {
	    NetworkPacketEvent event = new NetworkPacketEvent(p_Packet);
	    MinecraftForge.EVENT_BUS.post(event);

	    if (event.cancelled()) {
	        callbackInfo.cancel();
	     }
	}

	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void preSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
		PacketEvent.Send event = new PacketEvent.Send(packet);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.cancelled()) {
			callbackInfo.cancel();
		}
	}

	@Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
	private void preChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
		PacketEvent.Receive event = new PacketEvent.Receive(packet);
		MinecraftForge.EVENT_BUS.post(event);
		if (event.cancelled()) {
			callbackInfo.cancel();
		}
	}

	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("TAIL"), cancellable = true)
	private void postSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
		PacketEvent.PostSend event = new PacketEvent.PostSend(packet);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.cancelled()) {
			callbackInfo.cancel();
		}
	}

	@Inject(method = "channelRead0", at = @At("TAIL"), cancellable = true)
	private void postChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
		PacketEvent.PostReceive event = new PacketEvent.PostReceive(packet);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.cancelled()) {
			callbackInfo.cancel();
		}
	}
}
