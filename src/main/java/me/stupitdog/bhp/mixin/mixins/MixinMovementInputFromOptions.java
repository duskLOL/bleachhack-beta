package me.stupitdog.bhp.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.stupitdog.bhp.Bhp;
import me.stupitdog.bhp.module.modules.player.GuiMove;


@Mixin(value = MovementInputFromOptions.class, priority = 10000)
public abstract class MixinMovementInputFromOptions extends MovementInput {

	@Redirect(method = "updatePlayerMoveState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
	public boolean isKeyPressed(KeyBinding keyBinding) {
		if (Bhp.instance.moduleManager.getModuleByClass(GuiMove.class).isEnabled()
				&& Minecraft.getMinecraft().currentScreen != null
				&& !(Minecraft.getMinecraft().currentScreen instanceof GuiChat)
				&& Minecraft.getMinecraft().player != null) {
			return Keyboard.isKeyDown(keyBinding.getKeyCode());
		}
		return keyBinding.isKeyDown();
	}
}