package io.github.linkle.valleycraft.blocks.entity;

import org.jetbrains.annotations.Nullable;

import io.github.linkle.valleycraft.utils.Util;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.MapColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class CrabTrap extends BlockWithEntity implements Waterloggable {
    protected static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final VoxelShape SHAPE = VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 16, 3, 16),
            //Top
            Block.createCuboidShape(0, 13, 0, 16, 16, 16),
            //Northwest corner
            Block.createCuboidShape(0, 0, 0, 2, 16, 1),
            Block.createCuboidShape(0, 0, 0, 1, 16, 2),
            //North side
            Block.createCuboidShape(4, 0, 0, 7, 16, 1),
            Block.createCuboidShape(9, 0, 0, 12, 16, 1),
            //Northeast corner
            Block.createCuboidShape(14, 0, 0, 16, 16, 1),
            Block.createCuboidShape(15, 0, 0, 16, 16, 2),
            //East side
            Block.createCuboidShape(15, 0, 4, 16, 16, 7),
            Block.createCuboidShape(15, 0, 9, 16, 16, 12),
            //Southeast corner
            Block.createCuboidShape(15, 0, 14, 16, 16, 16),
            Block.createCuboidShape(14, 0, 15, 16, 16, 16),
            //South side
            Block.createCuboidShape(4, 0, 15, 7, 16, 16),
            Block.createCuboidShape(9, 0, 15, 12, 16, 16),
            //Southwest corner
            Block.createCuboidShape(0, 0, 15, 2, 16, 16),
            Block.createCuboidShape(0, 0, 14, 1, 16, 16),
            //West side
            Block.createCuboidShape(0, 0, 4, 1, 16, 7),
            Block.createCuboidShape(0, 0, 9, 1, 16, 12));

    public static BlockEntityType<CrabTrapEntity> BLOCK_ENTITY;

    public CrabTrap() {
        super(Settings.of(new FabricMaterialBuilder(MapColor.ORANGE).blocksPistons().build())
                .strength(1.5f, 2.0f)
                .sounds(BlockSoundGroup.WOOD)
                .nonOpaque());
        setDefaultState(stateManager.getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    //Enables comparators to read from crap traps
    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }
    //Determine what signal strength comparators should output based on how full the trap is
    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        Inventory inventory = (Inventory)world.getBlockEntity(pos);
        int i = 0;
        float f = 0.0f;
        //This starts a loop. We're gonna check every slot for how full it is
        for (int j = 0; j < inventory.size(); ++j) {
            ItemStack itemStack = inventory.getStack(j);
            //If the slot is empty, check the next slot
            if (itemStack.isEmpty()) continue;
            //If we're currently testing the bait slot, we have to see how full the bait stack is,
            //then add a number between 0 and 1 to f representing how full the bait slot is
            if (j == 0) {
                f += (float)itemStack.getCount() / (float)Math.min(inventory.getMaxCountPerStack(), itemStack.getMaxCount());
            }
            //If we're checking an output slot, we treat the slot as full if it contains anything at all.
            //Because we checked for empty earlier, the slot is full, so add one to f.
            else {
                f += 1;
            }
            ++i;
        }
        //Return comparator strength based on how full the crab trap is overall.
        return MathHelper.floor((f /= inventory.size()) * 14.0f) + (i > 0 ? 1 : 0);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrabTrapEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : Util.checkType(type, BLOCK_ENTITY, CrabTrapEntity::tick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CrabTrapEntity entity) {
            player.openHandledScreen(entity);
        }
        return ActionResult.CONSUME;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (world.getBlockEntity(pos) instanceof CrabTrapEntity entity) {
            entity.checkValidation();
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            if (world.getBlockEntity(pos) instanceof CrabTrapEntity entity) {
                entity.checkValidation();
            }
            return;
        }
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CrabTrapEntity) {
            ItemScatterer.spawn(world, pos, (Inventory)blockEntity);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(WATERLOGGED, Util.inWater(ctx));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState,
            WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.get(WATERLOGGED)) {
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }
}
