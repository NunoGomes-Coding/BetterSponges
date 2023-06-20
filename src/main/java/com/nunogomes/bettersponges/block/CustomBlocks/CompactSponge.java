package com.nunogomes.bettersponges.block.CustomBlocks;

import com.google.common.collect.Lists;
import com.nunogomes.bettersponges.block.BlockInit;
import java.util.Queue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;

public class CompactSponge extends Block {
  public static int MAX_DEPTH = 6;
  public static int MAX_COUNT = 64;

  public CompactSponge(int scale) {
    super(BlockBehaviour.Properties.of(Material.SPONGE).strength(0.6F).sound(SoundType.GRASS));
    MAX_DEPTH *= scale;
    MAX_COUNT *= scale;
  }

  public void onPlace(BlockState p_56811_, Level p_56812_, BlockPos p_56813_, BlockState p_56814_,
      boolean p_56815_) {
    if (!p_56814_.is(p_56811_.getBlock())) {
      this.tryAbsorbWater(p_56812_, p_56813_);
    }
  }

  public void neighborChanged(BlockState p_56801_, Level p_56802_, BlockPos p_56803_,
    Block p_56804_, BlockPos p_56805_, boolean p_56806_) {
    this.tryAbsorbWater(p_56802_, p_56803_);
    // super.neighborChanged(p_56801_, p_56802_, p_56803_, p_56804_, p_56805_, p_56806_);
  }

  protected void tryAbsorbWater(Level p_56798_, BlockPos p_56799_) {
    if (this.removeWaterBreadthFirstSearch(p_56798_, p_56799_)) {
      p_56798_.setBlock(p_56799_, BlockInit.WET_COMPACT_SPONGE.get().defaultBlockState(), 2);
      p_56798_.levelEvent(2001, p_56799_, Block.getId(Blocks.WATER.defaultBlockState()));
    }

  }

  private boolean removeWaterBreadthFirstSearch(Level p_56808_, BlockPos p_56809_) {
    Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
    queue.add(new Tuple<>(p_56809_, 0));
    int i = 0;
    BlockState state = p_56808_.getBlockState(p_56809_);

    while (!queue.isEmpty()) {
      Tuple<BlockPos, Integer> tuple = queue.poll();
      BlockPos blockpos = tuple.getA();
      int j = tuple.getB();

      for (Direction direction : Direction.values()) {
        BlockPos blockpos1 = blockpos.relative(direction);
        BlockState blockstate = p_56808_.getBlockState(blockpos1);
        FluidState fluidstate = p_56808_.getFluidState(blockpos1);
        Material material = blockstate.getMaterial();
        if (state.canBeHydrated(p_56808_, p_56809_, fluidstate, blockpos1)) {
          if (blockstate.getBlock() instanceof BucketPickup
              && !((BucketPickup) blockstate.getBlock())
                  .pickupBlock(p_56808_, blockpos1, blockstate).isEmpty()) {
            ++i;
            if (j < MAX_DEPTH) {
              queue.add(new Tuple<>(blockpos1, j + 1));
            }
          } else if (blockstate.getBlock() instanceof LiquidBlock) {
            p_56808_.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3);
            ++i;
            if (j < MAX_DEPTH) {
              queue.add(new Tuple<>(blockpos1, j + 1));
            }
          } else if (material == Material.WATER_PLANT
              || material == Material.REPLACEABLE_WATER_PLANT) {
            BlockEntity blockentity =
                blockstate.hasBlockEntity() ? p_56808_.getBlockEntity(blockpos1) : null;
            dropResources(blockstate, p_56808_, blockpos1, blockentity);
            p_56808_.setBlock(blockpos1, Blocks.AIR.defaultBlockState(), 3);
            ++i;
            if (j < MAX_DEPTH) {
              queue.add(new Tuple<>(blockpos1, j + 1));
            }
          }
        }
      }

      if (i > MAX_COUNT) {
        break;
      }
    }

    return i > 0;
  }

}
