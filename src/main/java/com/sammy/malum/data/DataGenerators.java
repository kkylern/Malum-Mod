package com.sammy.malum.data;

import com.sammy.malum.MalumMod;
import com.sammy.malum.data.block.MalumBlockLootTables;
import com.sammy.malum.data.block.MalumBlockStates;
import com.sammy.malum.data.block.MalumBlockTags;
import com.sammy.malum.data.item.MalumItemModels;
import com.sammy.malum.data.recipe.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = MalumMod.MALUM, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();


        MalumItemModels itemModelsProvider = new MalumItemModels(output, helper);
        MalumBlockTags blockTagsProvider = new MalumBlockTags(output, provider, helper);

        generator.addProvider(event.includeClient(), new MalumBlockStates(output, helper, itemModelsProvider));
        generator.addProvider(event.includeClient(), itemModelsProvider);


        //TODO this crashes generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new MalumBlockLootTables(output));
        //TODO generator.addProvider(event.includeServer(), new MalumItemTags(output, provider, blockTagsProvider.contentsGetter(), helper));

        generator.addProvider(event.includeServer(), new MalumRecipes(output));
        generator.addProvider(event.includeServer(), new MalumVanillaRecipeReplacements(output));
        generator.addProvider(event.includeServer(), new MalumSpiritInfusionRecipes(output));
        generator.addProvider(event.includeServer(), new MalumSpiritFocusingRecipes(output));
        generator.addProvider(event.includeServer(), new MalumSpiritTransmutationRecipes(output));
        generator.addProvider(event.includeServer(), new MalumVoidFavorRecipes(output));

        generator.addProvider(event.includeServer(), new MalumBiomeTags(output, provider, helper));

        generator.addProvider(event.includeServer(), new RegistryDataGenerator(output, provider));
        generator.addProvider(event.includeClient(), new MalumLang(output));
    }
}
