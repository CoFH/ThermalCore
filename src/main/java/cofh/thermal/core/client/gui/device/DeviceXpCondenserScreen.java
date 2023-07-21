package cofh.thermal.core.client.gui.device;

import cofh.thermal.core.inventory.container.device.DeviceXpCondenserContainer;
import cofh.thermal.lib.client.gui.AugmentableTileScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import static cofh.core.util.helpers.GuiHelper.*;
import static cofh.lib.util.constants.ModIds.ID_THERMAL;

public class DeviceXpCondenserScreen extends AugmentableTileScreen<DeviceXpCondenserContainer> {

    public static final String TEX_PATH = ID_THERMAL + ":textures/gui/container/xp_condenser.png";
    public static final ResourceLocation TEXTURE = new ResourceLocation(TEX_PATH);

    public DeviceXpCondenserScreen(DeviceXpCondenserContainer container, Inventory inv, Component titleIn) {

        super(container, inv, container.tile, titleIn);
        texture = TEXTURE;
        info = generatePanelInfo("info.thermal.device_xp_condenser");
    }

    @Override
    public void init() {

        super.init();

        addElement(setClearable(createMediumFluidStorage(this, 80, 22, tile.getTank(0)), tile, 0));
    }

    //    @Override
    //    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
    //
    //        String radius = format(1 + 2L * menu.tile.getRadius());
    //
    //        fontRenderer().draw(matrixStack, localize("info.cofh.area") + ": " + radius + " x " + radius, 70, 39, 0x404040);
    //
    //        super.renderLabels(matrixStack, mouseX, mouseY);
    //    }

}
