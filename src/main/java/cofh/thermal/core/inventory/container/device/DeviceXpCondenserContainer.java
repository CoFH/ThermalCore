package cofh.thermal.core.inventory.container.device;

import cofh.core.inventory.container.TileCoFHContainer;
import cofh.lib.inventory.wrapper.InvWrapperCoFH;
import cofh.thermal.lib.block.entity.AugmentableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static cofh.thermal.core.init.TCoreContainers.DEVICE_XP_CONDENSER_CONTAINER;

public class DeviceXpCondenserContainer extends TileCoFHContainer {

    public final AugmentableBlockEntity tile;

    public DeviceXpCondenserContainer(int windowId, Level world, BlockPos pos, Inventory inventory, Player player) {

        super(DEVICE_XP_CONDENSER_CONTAINER.get(), windowId, world, pos, inventory, player);
        this.tile = (AugmentableBlockEntity) world.getBlockEntity(pos);
        InvWrapperCoFH tileInv = new InvWrapperCoFH(this.tile.getItemInv());

        bindAugmentSlots(tileInv, 0, this.tile.augSize());
        bindPlayerInventory(inventory);
    }

}
