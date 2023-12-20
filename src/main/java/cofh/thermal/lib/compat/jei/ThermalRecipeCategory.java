package cofh.thermal.lib.compat.jei;

import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.lib.util.recipes.ThermalRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static cofh.lib.util.helpers.StringHelper.getTextComponent;
import static cofh.lib.util.helpers.StringHelper.localize;

public abstract class ThermalRecipeCategory<T extends ThermalRecipe> implements IRecipeCategory<T> {

    protected static final int ENERGY_X = 2;
    protected static final int ENERGY_Y = 2;

    protected static final int XP_X = 1;
    protected static final int XP_Y = 46;

    protected final RecipeType<T> type;
    protected IDrawable background;
    protected IDrawable icon;
    protected Component name;

    protected IDrawableStatic energyBackground;
    protected IDrawableStatic progressBackground;
    protected IDrawableStatic progressFluidBackground;
    protected IDrawableStatic speedBackground;

    protected IDrawableAnimated energy;
    protected IDrawableAnimated progress;
    protected IDrawableAnimated progressFluid;
    protected IDrawableAnimated speed;

    protected IDrawableStatic xp;

    protected Supplier<Float> energyMod = () -> 1.0F;

    public ThermalRecipeCategory(IGuiHelper guiHelper, ItemStack icon, RecipeType<T> type) {

        this.type = type;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon);

        energyBackground = Drawables.getDrawables(guiHelper).getEnergyEmpty();
        energy = guiHelper.createAnimatedDrawable(Drawables.getDrawables(guiHelper).getEnergyFill(), 400, IDrawableAnimated.StartDirection.TOP, true);
        xp = Drawables.getDrawables(guiHelper).getXp();
    }

    // region IRecipeCategory
    @Override
    public Component getTitle() {

        return name;
    }

    @Override
    public IDrawable getBackground() {

        return background;
    }

    @Override
    public IDrawable getIcon() {

        return icon;
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        int energyY = recipe.getXp() > 0 ? ENERGY_Y : ENERGY_Y + 8;

        if (recipe.getEnergy() > 0) {
            energyBackground.draw(guiGraphics, ENERGY_X, energyY);
            energy.draw(guiGraphics, ENERGY_X, energyY);
        }
        if (recipe.getXp() > 0) {
            xp.draw(guiGraphics, XP_X, XP_Y);
        }
    }

    @Override
    public List<Component> getTooltipStrings(T recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {

        List<Component> tooltip = new ArrayList<>();

        int energyY = recipe.getXp() > 0 ? ENERGY_Y : ENERGY_Y + 8;

        if (recipe.getEnergy() > 0 && mouseX > ENERGY_X && mouseX < ENERGY_X + energy.getWidth() - 1 && mouseY > energyY && mouseY < energyY + energy.getHeight() - 1) {
            tooltip.add(getTextComponent("info.cofh.energy").append(": " + StringHelper.format((long) (recipe.getEnergy() * energyMod.get())) + " " + localize("info.cofh.unit_rf")));
        }
        if (recipe.getXp() > 0 && mouseX > XP_X && mouseX < XP_X + xp.getWidth() - 1 && mouseY > XP_Y && mouseY < XP_Y + xp.getHeight() - 1) {
            tooltip.add(Component.literal("" + recipe.getXp()).append(" " + localize("info.cofh.unit_xp")));
        }
        return tooltip;
    }
    // endregion
}
