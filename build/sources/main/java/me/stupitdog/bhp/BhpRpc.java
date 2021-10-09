package me.stupitdog.bhp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;

public class BhpRpc {

	private static String discordID = "789246960381067296";
    private static DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static List<String> images = new ArrayList<>();

    public static void startRPC(){
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));

        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

        images.add("1");
        images.add("2");
        images.add("3");
        images.add("4");
        images.add("5");
        images.add("6");
        images.add("7");
        images.add("8");
        images.add("9");
        images.add("10");
        images.add("11");
        images.add("12");
        images.add("13");
        images.add("14");
        images.add("15");
        images.add("16");
        images.add("17");
        images.add("18");
        images.add("19");
        images.add("20");
        
        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.details = "v2.0";
        discordRichPresence.largeImageKey = getRandomImage();
        discordRichPresence.largeImageText = "bleachhack+ ontop";
        
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String details = "";
                	
                    details = "main menu";
                    discordRichPresence.largeImageKey = getRandomImage();
                    if (mc.isIntegratedServerRunning()) {
                        details = "singleplayer | " + mc.getIntegratedServer().getWorldName();
                    } else if (mc.currentScreen instanceof GuiMultiplayer) {
                        details = "multiplayer menu";
                    } else if (mc.currentScreen instanceof GuiWorldSelection) {
                        details = "singleplayer menu";
                    } else if (mc.getCurrentServerData() != null) {
                        details = "server | " + mc.getCurrentServerData().serverIP.toLowerCase();
                    }
                    discordRichPresence.state = details;
                    
                    discordRPC.Discord_UpdatePresence(discordRichPresence);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }, "RPC-Callback-Handler").start();
        
    }

    public static void stopRPC(){
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
    
    public static String getRandomImage() {
    	Random rand = new Random();
    	return images.get(rand.nextInt(images.size()));
    }
    
}
