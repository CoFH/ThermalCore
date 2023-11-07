package cofh.thermal.core.common.inventory.device;

import cofh.core.common.inventory.TileCoFHContainer;
import cofh.lib.common.inventory.SlotCoFH;
import cofh.lib.common.inventory.wrapper.InvWrapperCoFH;
import cofh.thermal.core.common.block.entity.device.DeviceSoilInfuserTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static cofh.thermal.core.init.registries.TCoreContainers.DEVICE_SOIL_INFUSER_CONTAINER;

public class DeviceSoilInfuserContainer extends TileCoFHContainer {

    public final DeviceSoilInfuserTile tile;

    public DeviceSoilInfuserContainer(int windowId, Level world, BlockPos pos, Inventory inventory, Player player) {

        super(DEVICE_SOIL_INFUSER_CONTAINER.get(), windowId, world, pos, inventory, player);
        this.tile = (DeviceSoilInfuserTile) world.getBlockEntity(pos);
        InvWrapperCoFH tileInv = new InvWrapperCoFH(this.tile.getItemInv());

        addSlot(new SlotCoFH(tileInv, 0, 8, 53));

        bindAugmentSlots(tileInv, 1, this.tile.augSize());
        bindPlayerInventory(inventory);
    }

}
