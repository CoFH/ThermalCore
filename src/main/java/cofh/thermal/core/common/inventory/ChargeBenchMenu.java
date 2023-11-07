package cofh.thermal.core.common.inventory;

import cofh.core.common.inventory.BlockEntityCoFHMenu;
import cofh.lib.common.inventory.SlotCoFH;
import cofh.lib.common.inventory.wrapper.InvWrapperCoFH;
import cofh.thermal.core.common.block.entity.ChargeBenchBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import static cofh.thermal.core.init.registries.TCoreContainers.CHARGE_BENCH_CONTAINER;

public class ChargeBenchMenu extends BlockEntityCoFHMenu {

    public final ChargeBenchBlockEntity tile;

    public ChargeBenchMenu(int windowId, Level world, BlockPos pos, Inventory inventory, Player player) {

        super(CHARGE_BENCH_CONTAINER.get(), windowId, world, pos, inventory, player);
        this.tile = (ChargeBenchBlockEntity) world.getBlockEntity(pos);
        InvWrapperCoFH tileInv = new InvWrapperCoFH(this.tile.getItemInv());

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlot(new SlotCoFH(tileInv, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
        addSlot(new SlotCoFH(tileInv, 9, 8, 53));

        bindAugmentSlots(tileInv, 10, this.tile.augSize());
        bindPlayerInventory(inventory);
    }

}
