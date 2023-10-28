package com.sammy.malum.data;

import com.sammy.malum.MalumMod;
import com.sammy.malum.registry.common.worldgen.BiomeTagRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MalumBiomeTags extends BiomeTagsProvider {

    public MalumBiomeTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, MalumMod.MALUM, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        //super.addTags(pProvider);
        tag(BiomeTagRegistry.HAS_SOULSTONE).addTag(BiomeTags.IS_OVERWORLD);
        tag(BiomeTagRegistry.HAS_BRILLIANT).addTag(BiomeTags.IS_OVERWORLD);
        tag(BiomeTagRegistry.HAS_BLAZING_QUARTZ).addTag(BiomeTags.IS_NETHER);
    }
}
