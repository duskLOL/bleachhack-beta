package me.stupitdog.bhp;

public class ShutdownHook extends Thread {

    @Override
    public void run() {
        saveConfig();
    }

    public static void saveConfig() {
    	Bhp.instance.configManager.saveConfig(Bhp.instance.configManager.config.replaceFirst("bleachhackplus/", ""));
    }
}
