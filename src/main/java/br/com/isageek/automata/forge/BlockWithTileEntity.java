package br.com.isageek.automata.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BlockWithTileEntity extends BlockEntity {


    public BlockWithTileEntity(
            BlockEntityType<?> p_155228_,
            BlockPos p_155229_,
            BlockState p_155230_
    ) {
        super(p_155228_, p_155229_, p_155230_);
    }
}
