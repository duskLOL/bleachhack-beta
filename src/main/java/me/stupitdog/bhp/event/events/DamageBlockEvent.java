package me.stupitdog.bhp.event.events;

import me.stupitdog.bhp.event.BhpEvent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DamageBlockEvent extends BhpEvent {
	
	private BlockPos blockPos;
	private EnumFacing enumFacing;

	public DamageBlockEvent(BlockPos blockPos, EnumFacing enumFacing) {
		this.blockPos = blockPos;
		this.enumFacing = enumFacing;
	}

	public BlockPos getBlockPos() {
		return this.blockPos;
	}

	public void setBlockPos(BlockPos blockPos) {
		this.blockPos = blockPos;
	}

	public EnumFacing getEnumFacing() {
		return this.enumFacing;
	}

	public void setEnumFacing(EnumFacing enumFacing) {
		this.enumFacing = enumFacing;
	}
}
