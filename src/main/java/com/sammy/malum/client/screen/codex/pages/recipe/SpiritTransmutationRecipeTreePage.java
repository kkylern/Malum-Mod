package com.sammy.malum.client.screen.codex.pages.recipe;

import com.sammy.malum.*;
import com.sammy.malum.client.screen.codex.pages.*;
import com.sammy.malum.client.screen.codex.screens.*;
import com.sammy.malum.common.recipe.*;
import com.sammy.malum.registry.common.*;
import com.sammy.malum.registry.common.item.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.data.loading.*;
import team.lodestar.lodestone.handlers.screenparticle.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.registry.common.particle.*;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.builder.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;
import team.lodestar.lodestone.systems.particle.screen.*;
import team.lodestar.lodestone.systems.recipe.*;

import java.util.*;

import static com.sammy.malum.client.screen.codex.ArcanaCodexHelper.*;

public class SpiritTransmutationRecipeTreePage extends BookPage {

    private static final ScreenParticleHolder TRANSMUTATION_PARTICLES = new ScreenParticleHolder();

    private final String headlineTranslationKey;
    private final List<WrappedIngredient> itemTree = new ArrayList<>();

    public SpiritTransmutationRecipeTreePage(String headlineTranslationKey, Item start) {
        super(MalumMod.malumPath("textures/gui/book/pages/transmutation_recipe_tree_page.png"));
        this.headlineTranslationKey = headlineTranslationKey;
        if (DatagenModLoader.isRunningDataGen()) {
            return;
        }
        SpiritTransmutationRecipe recipe = SpiritTransmutationRecipe.getRecipe(Minecraft.getInstance().level, start);
        while (true) {
            if (recipe == null) {
                itemTree.add(new WrappedIngredient(Ingredient.of(ItemRegistry.BLIGHTED_SOIL.get())));
                break;
            }
            itemTree.add(new WrappedIngredient(recipe.ingredient));
            ItemStack output = recipe.output;
            recipe = SpiritTransmutationRecipe.getRecipe(Minecraft.getInstance().level, s -> s.ingredient.test(output));
        }
    }

    public String headlineTranslationKey() {
        return "malum.gui.book.entry.page.headline." + headlineTranslationKey;
    }

    @Override
    public boolean isValid() {
        return !itemTree.isEmpty();
    }

    @Override
    public void render(EntryScreen screen, GuiGraphics guiGraphics, int left, int top, int mouseX, int mouseY, float partialTicks, boolean isRepeat) {
        Component component = Component.translatable(headlineTranslationKey());
        renderText(guiGraphics, component, left + 70 - Minecraft.getInstance().font.width(component.getString()) / 2, top + 5);
        if (!isRepeat) {
            if (ScreenParticleHandler.canSpawnParticles) {
                TRANSMUTATION_PARTICLES.tick();
            }
            ScreenParticleHandler.renderParticles(TRANSMUTATION_PARTICLES);
        }
        renderComponent(screen, guiGraphics, itemTree.get(0), left + 63, top + 38, mouseX, mouseY);
        renderComponent(screen, guiGraphics, itemTree.get(itemTree.size() - 1), left + 63, top + 120, mouseX, mouseY);

        int leftStart = left + 73 - (itemTree.size())*10;
        for (int i = 1; i < itemTree.size()-1; i++) {
            WrappedIngredient wrappedIngredient = itemTree.get(i);
            renderComponent(screen, guiGraphics, wrappedIngredient, leftStart+i*20, top + 79, mouseX, mouseY);
        }
        int particlesX = left + 25;
        int particlesY = top + 88;
        if (ScreenParticleHandler.canSpawnParticles) {
            for (int i = 0; i < 8; i++) {
                RandomSource rand = Minecraft.getInstance().level.random;
                float scale = RandomHelper.randomBetween(rand, 0.1f, 0.2f);
                float spin = RandomHelper.randomBetween(rand, 0.2f, 0.4f);
                final double xOffset = 92 * ((Minecraft.getInstance().level.getGameTime()+i*30) % 100) / 100f;
                final double yOffset = Math.sin(((Minecraft.getInstance().level.getGameTime()+i*16) % 80) / 80f * Math.PI * 2) * 6;
                ScreenParticleBuilder.create(LodestoneScreenParticleRegistry.WISP, TRANSMUTATION_PARTICLES)
                        .setTransparencyData(GenericParticleData.create(0.2f, 0.6f, 0f).build())
                        .setSpinData(SpinParticleData.create(spin).build())
                        .setScaleData(GenericParticleData.create(0, scale, 0).build())
                        .setColorData(SpiritTypeRegistry.ARCANE_SPIRIT.createColorData().setCoefficient(0.75f).build())
                        .setLifetime(i % 2 == 0 ? 20 : 40)
                        .setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE)
                        .setLifeDelay(i > 3 ? 0 : 15)
                        .spawn(particlesX + xOffset, particlesY + yOffset);
            }
        }
    }
}