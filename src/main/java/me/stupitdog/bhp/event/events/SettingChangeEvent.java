package me.stupitdog.bhp.event.events;

import me.stupitdog.bhp.event.BhpEvent;
import me.stupitdog.bhp.setting.Setting;

public class SettingChangeEvent extends BhpEvent {
	Setting setting;
	
	public SettingChangeEvent(Setting setting) {
		this.setting = setting;
	}
	
	public Setting getSetting() {
		return setting;
	}
}
