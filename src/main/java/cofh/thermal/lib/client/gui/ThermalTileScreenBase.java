package cofh.thermal.lib.client.gui;

import cofh.core.client.gui.ContainerScreenCoFH;
import cofh.core.client.gui.element.ElementTexture;
import cofh.core.client.gui.element.ElementXpStorage;
import cofh.core.client.gui.element.panel.AugmentPanel;
import cofh.core.client.gui.element.panel.RSControlPanel;
import cofh.core.client.gui.element.panel.SecurityPanel;
import cofh.core.content.inventory.container.ContainerCoFH;
import cofh.core.network.packet.server.FilterGuiOpenPacket;
import cofh.core.util.helpers.FilterHelper;
import cofh.lib.util.helpers.SecurityHelper;
import cofh.thermal.lib.tileentity.ThermalTileAugmentable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.Collections;

import static cofh.core.util.helpers.GuiHelper.*;

public class ThermalTileScreenBase<T extends ContainerCoFH> extends ContainerScreenCoFH<T> {

    protected ThermalTileAugmentable tile;

    public ThermalTileScreenBase(T container, Inventory inv, ThermalTileAugmentable tile, Component titleIn) {

        super(container, inv, titleIn);
        this.tile = tile;
    }

    @Override
    public void init() {

        super.init();

        // TODO: Enchantment Panel
        // addPanel(new PanelEnchantment(this, "This block can be enchanted."));
        addPanel(new SecurityPanel(this, tile, SecurityHelper.getID(player)));

        if (menu.getAugmentSlots().size() > 0) {
            addPanel(new AugmentPanel(this, menu::getNumAugmentSlots, menu.getAugmentSlots()));
        }
        addPanel(new RSControlPanel(this, tile));

        if (tile.getXpStorage() != null) {
            addElement(setClaimable((ElementXpStorage) createDefaultXpStorage(this, 152, 65, tile.getXpStorage()).setVisible(() -> tile.getXpStorage().getMaxXpStored() > 0), tile));
        }

        // Filter Tab
        addElement(new ElementTexture(this, 4, -21)
                .setUV(24, 0)
                .setSize(24, 21)
                .setTexture(TAB_TOP, 48, 32)
                .setVisible(() -> FilterHelper.hasFilter(tile, 0)));
        addElement(new ElementTexture(this, 8, -17) {

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {

                FilterGuiOpenPacket.openFilterGui(tile, (byte) 0);
                return true;
            }
        }
                .setSize(16, 16)
                .setTexture(NAV_FILTER, 16, 16)
                .setTooltipFactory((element, mouseX, mouseY) -> Collections.singletonList(tile.getFilter(0).getDisplayName()))
                .setVisible(() -> FilterHelper.hasFilter(tile, 0)));

        // TODO: Revisit ItemStack-based
        //        addElement(new ElementItemStack(this, 8, -17) {
        //
        //            @Override
        //            public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        //
        //                FilterGuiOpenPacket.openFilterGui(tile);
        //                return true;
        //            }
        //        }
        //                .setItem(() -> new ItemStack(Items.WHEAT))
        //                .setSize(16, 16)
        //                .setVisible(() -> FilterHelper.hasFilter(tile)));
    }

}
