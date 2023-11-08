package cofh.thermal.core.common.inventory.device;

import cofh.core.common.inventory.BlockEntityCoFHMenu;
import cofh.lib.common.inventory.SlotRemoveOnly;
import cofh.lib.common.inventory.wrapper.InvWrapperCoFH;
import cofh.thermal.lib.common.block.entity.AugmentableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static cofh.thermal.core.init.registries.TCoreMenus.DEVICE_COLLECTOR_CONTAINER;

public class DeviceCollectorMenu extends BlockEntityCoFHMenu {

    public final AugmentableBlockEntity tile;

    public DeviceCollectorMenu(int windowId, Level world, BlockPos pos, Inventory inventory, Player player) {

        super(DEVICE_COLLECTOR_CONTAINER.get(), windowId, world, pos, inventory, player);
        this.tile = (AugmentableBlockEntity) world.getBlockEntity(pos);
        InvWrapperCoFH tileInv = new InvWrapperCoFH(this.tile.getItemInv());

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 5; ++j) {
                addSlot(new SlotRemoveOnly(tileInv, i * 5 + j, 44 + j * 18, 17 + i * 18));
            }
        }
        bindAugmentSlots(tileInv, 15, this.tile.augSize());
        bindPlayerInventory(inventory);
    }

}
