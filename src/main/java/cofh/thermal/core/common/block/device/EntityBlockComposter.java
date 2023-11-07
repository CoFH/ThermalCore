package cofh.thermal.core.common.block.device;

import cofh.core.common.block.EntityBlockActive;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.function.Supplier;

import static cofh.lib.util.constants.BlockStatePropertiesCoFH.ACTIVE;

public class EntityBlockComposter extends EntityBlockActive {

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_COMPOSTER;

    public EntityBlockComposter(Properties builder, Class<?> tileClass, Supplier<BlockEntityType<?>> blockEntityType) {

        super(builder, tileClass, blockEntityType);
        this.registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false).setValue(LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {

        super.createBlockStateDefinition(builder);
        builder.add(LEVEL);
    }

}
