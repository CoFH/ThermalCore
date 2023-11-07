package cofh.thermal.core.common.inventory.storage;

import cofh.core.common.inventory.BlockEntityCoFHMenu;
import cofh.lib.common.inventory.wrapper.InvWrapperCoFH;
import cofh.thermal.core.common.block.entity.storage.FluidCellBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static cofh.thermal.core.init.registries.TCoreContainers.FLUID_CELL_CONTAINER;

public class FluidCellMenu extends BlockEntityCoFHMenu {

    public final FluidCellBlockEntity tile;

    public FluidCellMenu(int windowId, Level world, BlockPos pos, Inventory inventory, Player player) {

        super(FLUID_CELL_CONTAINER.get(), windowId, world, pos, inventory, player);
        this.tile = (FluidCellBlockEntity) world.getBlockEntity(pos);
        InvWrapperCoFH tileInv = new InvWrapperCoFH(this.tile.getItemInv());

        bindAugmentSlots(tileInv, 0, this.tile.augSize());
        bindPlayerInventory(inventory);
    }

}
