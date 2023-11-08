package cofh.thermal.core.common.inventory.device;

import cofh.core.common.inventory.BlockEntityCoFHMenu;
import cofh.lib.common.inventory.wrapper.InvWrapperCoFH;
import cofh.thermal.lib.common.block.entity.AugmentableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static cofh.thermal.core.init.registries.TCoreMenus.DEVICE_XP_CONDENSER_CONTAINER;

public class DeviceXpCondenserMenu extends BlockEntityCoFHMenu {

    public final AugmentableBlockEntity tile;

    public DeviceXpCondenserMenu(int windowId, Level world, BlockPos pos, Inventory inventory, Player player) {

        super(DEVICE_XP_CONDENSER_CONTAINER.get(), windowId, world, pos, inventory, player);
        this.tile = (AugmentableBlockEntity) world.getBlockEntity(pos);
        InvWrapperCoFH tileInv = new InvWrapperCoFH(this.tile.getItemInv());

        bindAugmentSlots(tileInv, 0, this.tile.augSize());
        bindPlayerInventory(inventory);
    }

}
