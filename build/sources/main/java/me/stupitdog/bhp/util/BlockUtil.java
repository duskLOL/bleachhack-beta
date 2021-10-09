package me.stupitdog.bhp.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

@SuppressWarnings("rawtypes")
public class BlockUtil {
	
	public static final List blackList;
	public static final List shulkerList;
	private static final Minecraft mc = Minecraft.getMinecraft();
	
    public static List<Block> emptyBlocks;
    public static List<Block> rightclickableBlocks;

    static
    {
        emptyBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.VINE, Blocks.SNOW_LAYER, Blocks.TALLGRASS, Blocks.FIRE);
        rightclickableBlocks = Arrays.asList(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.ENDER_CHEST, Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.ANVIL, Blocks.WOODEN_BUTTON, Blocks.STONE_BUTTON, Blocks.UNPOWERED_COMPARATOR, Blocks.UNPOWERED_REPEATER, Blocks.POWERED_REPEATER, Blocks.POWERED_COMPARATOR, Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.BREWING_STAND, Blocks.DISPENSER, Blocks.DROPPER, Blocks.LEVER, Blocks.NOTEBLOCK, Blocks.JUKEBOX, Blocks.BEACON, Blocks.BED, Blocks.FURNACE, Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.BIRCH_DOOR, Blocks.JUNGLE_DOOR, Blocks.ACACIA_DOOR, Blocks.DARK_OAK_DOOR, Blocks.CAKE, Blocks.ENCHANTING_TABLE, Blocks.DRAGON_EGG, Blocks.HOPPER, Blocks.REPEATING_COMMAND_BLOCK, Blocks.COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, Blocks.CRAFTING_TABLE);
    }

	public static IBlockState getState(BlockPos pos) {
		return mc.world.getBlockState(pos);
	}
	
    public static void placeBlock(BlockPos pos) {
        for (EnumFacing enumFacing : EnumFacing.values()) {

            if (!Minecraft.getMinecraft().world.getBlockState(pos.offset(enumFacing)).getBlock().equals(Blocks.AIR) && !isIntercepted(pos)) {

                //Vec3d vec = new Vec3d(pos.getX() + 0.5D + (double) enumFacing.getFrontOffsetX() * 0.5D, pos.getY() + 0.5D + (double) enumFacing.getFrontOffsetY() * 0.5D, pos.getZ() + 0.5D + (double) enumFacing.getFrontOffsetZ() * 0.5D);

                float[] old = new float[]{Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.rotationPitch};

                //Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Rotation((float) Math.toDegrees(Math.atan2((vec.z - Minecraft.getMinecraft().player.posZ), (vec.x - Minecraft.getMinecraft().player.posX))) - 90.0F, (float) (-Math.toDegrees(Math.atan2((vec.y - (Minecraft.getMinecraft().player.posY + (double) Minecraft.getMinecraft().player.getEyeHeight())), (Math.sqrt((vec.x - Minecraft.getMinecraft().player.posX) * (vec.x - Minecraft.getMinecraft().player.posX) + (vec.z - Minecraft.getMinecraft().player.posZ) * (vec.z - Minecraft.getMinecraft().player.posZ)))))), Minecraft.getMinecraft().player.onGround));
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketEntityAction(Minecraft.getMinecraft().player, CPacketEntityAction.Action.START_SNEAKING));
                Minecraft.getMinecraft().playerController.processRightClickBlock(Minecraft.getMinecraft().player, Minecraft.getMinecraft().world, pos.offset(enumFacing), enumFacing.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);
                Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketEntityAction(Minecraft.getMinecraft().player, CPacketEntityAction.Action.STOP_SNEAKING));
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Rotation(old[0], old[1], Minecraft.getMinecraft().player.onGround));

                return;
            }
        }
    }
    
    public static void placePacketBlock(final BlockPos pos, final EnumFacing side) {
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!mc.player.isSneaking()) {
            mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).addVector(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, side, EnumHand.MAIN_HAND, (float)hitVec.x - pos.getX(), (float)hitVec.y - pos.getY(), (float)hitVec.z - pos.getZ()));
        
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }
    
    public static void openBlock(BlockPos pos)
    {
        EnumFacing[] facings = EnumFacing.values();

        for (EnumFacing f : facings)
        {
            Block neighborBlock = mc.world.getBlockState(pos.offset(f)).getBlock();

            if (emptyBlocks.contains(neighborBlock))
            {
                mc.playerController.processRightClickBlock(mc.player, mc.world, pos, f.getOpposite(), new Vec3d(pos), EnumHand.MAIN_HAND);

                return;
            }
        }
    }
    
    public static boolean isIntercepted(BlockPos pos) {
        for (Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) {
                return true;
            }
        }
        return false;
    }

	public static boolean checkForNeighbours(BlockPos blockPos) {
		if (!hasNeighbour(blockPos)) {

			for (EnumFacing side : EnumFacing.values()) {
				BlockPos neighbour = blockPos.offset(side);
				if (hasNeighbour(neighbour)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
    public static boolean canPlaceBlock(BlockPos pos)
    {
        if (isBlockEmpty(pos)) {
            EnumFacing[] facings = EnumFacing.values();

            for (EnumFacing f : facings) {
                if (!emptyBlocks.contains(mc.world.getBlockState(pos.offset(f)).getBlock()) && mc.player.getPositionEyes(mc.getRenderPartialTicks()).distanceTo(new Vec3d(pos.getX() + 0.5D + (double) f.getFrontOffsetX() * 0.5D, pos.getY() + 0.5D + (double) f.getFrontOffsetY() * 0.5D, pos.getZ() + 0.5D + (double) f.getFrontOffsetZ() * 0.5D)) <= 4.25D) {
                    return true;
                }
            }

        }
        return false;
    }

	private static boolean hasNeighbour(BlockPos blockPos) {
		for (EnumFacing side : EnumFacing.values()) {
			BlockPos neighbour = blockPos.offset(side);
			if (!mc.world.getBlockState(neighbour).getMaterial().isReplaceable()) {
				return true;
			}
		}
		return false;
	}


	public static Block getBlock(BlockPos pos) {
		return getState(pos).getBlock();
	}

	public static boolean canBeClicked(BlockPos pos) {
		return getBlock(pos).canCollideCheck(getState(pos), false);
	}

	public static void faceVectorPacketInstant(Vec3d vec) {
		float[] rotations = getNeededRotations2(vec);

		mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
	}

	private static float[] getNeededRotations2(Vec3d vec) {
		Vec3d eyesPos = getEyesPos();

		double diffX = vec.x - eyesPos.x;
		double diffY = vec.y - eyesPos.y;
		double diffZ = vec.z - eyesPos.z;

		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
		float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));

		return new float[] {
				mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw),
				mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)
		};
	}

	public static Vec3d getEyesPos() {
		return new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
	}

	public static List<BlockPos> getCircle(final BlockPos loc, final int y, final float r, final boolean hollow) {
		final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
		final int cx = loc.getX();
		final int cz = loc.getZ();
		for (int x = cx - (int)r; x <= cx + r; x++) {
			for (int z = cz - (int)r; z <= cz + r; z++) {
				final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z);
				if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
					final BlockPos l = new BlockPos(x, y, z);
					circleblocks.add(l);
				}
			}
		}
		return circleblocks;
	}

	static {
		blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER);
		shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
	}

	public static EnumFacing getPlaceableSide(BlockPos pos) {

		for (EnumFacing side : EnumFacing.values()) {

			BlockPos neighbour = pos.offset(side);

			if (!mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
				continue;
			}

			IBlockState blockState = mc.world.getBlockState(neighbour);
			if (!blockState.getMaterial().isReplaceable()) {
				return side;
			}
		}

		return null;
	}
	
    public static boolean isBlockEmpty(BlockPos pos) {
        try {
            if (emptyBlocks.contains(mc.world.getBlockState(pos).getBlock())) {
                AxisAlignedBB box = new AxisAlignedBB(pos);
                Iterator entityIter = mc.world.loadedEntityList.iterator();

                Entity e;

                do {
                    if (!entityIter.hasNext()) {
                        return true;
                    }

                    e = (Entity) entityIter.next();
                } while (!(e instanceof EntityLivingBase) || !box.intersects(e.getEntityBoundingBox()));

            }
        } catch (Exception ignored) { }
        return false;
    }
    
    public static void rotatePacket(double x, double y, double z)
    {
        double diffX = x - mc.player.posX;
        double diffY = y - (mc.player.posY + (double) mc.player.getEyeHeight());
        double diffZ = z - mc.player.posZ;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, diffXZ)));

        mc.player.connection.sendPacket(new Rotation(yaw, pitch, mc.player.onGround));
    }
}


