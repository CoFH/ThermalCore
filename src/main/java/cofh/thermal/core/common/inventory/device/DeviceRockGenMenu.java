package cofh.thermal.core.common.inventory.device;

import cofh.core.common.inventory.BlockEntityCoFHMenu;
import cofh.lib.common.inventory.SlotRemoveOnly;
import cofh.lib.common.inventory.wrapper.InvWrapperCoFH;
import cofh.thermal.lib.common.block.entity.AugmentableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static cofh.thermal.core.init.registries.TCoreMenus.DEVICE_ROCK_GEN_CONTAINER;

public class DeviceRockGenMenu extends BlockEntityCoFHMenu {

    public final AugmentableBlockEntity tile;

    public DeviceRockGenMenu(int windowId, Level world, BlockPos pos, Inventory inventory, Player player) {

        super(DEVICE_ROCK_GEN_CONTAINER.get(), windowId, world, pos, inventory, player);
        this.tile = (AugmentableBlockEntity) world.getBlockEntity(pos);
        InvWrapperCoFH tileInv = new InvWrapperCoFH(this.tile.getItemInv());

        addSlot(new SlotRemoveOnly(tileInv, 0, 125, 35));

        bindAugmentSlots(tileInv, 1, this.tile.augSize());
        bindPlayerInventory(inventory);
    }

}
