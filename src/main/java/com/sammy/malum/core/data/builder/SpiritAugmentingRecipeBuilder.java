package com.sammy.malum.core.data.builder;

import com.google.gson.JsonObject;
import com.sammy.malum.MalumMod;
import com.sammy.malum.core.setup.content.RecipeSerializerRegistry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class SpiritAugmentingRecipeBuilder {

    public final Ingredient targetItem;
    public final Ingredient augment;
    public final CompoundTag tagAugment;

    public SpiritAugmentingRecipeBuilder(Ingredient targetItem, Ingredient augment, CompoundTag tagAugment) {
        this.targetItem = targetItem;
        this.augment = augment;
        this.tagAugment = tagAugment;
    }

    public void build(Consumer<FinishedRecipe> consumerIn, String recipeName) {
        build(consumerIn, MalumMod.malumPath("augmenting/" + recipeName));
    }

    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
        consumerIn.accept(new SpiritAugmentingRecipeBuilder.Result(id, targetItem, augment, tagAugment));
    }

    public void build(Consumer<FinishedRecipe> consumerIn) {
        build(consumerIn, "augment_with_"+ augment.getItems()[0].getItem().getRegistryName().getPath());
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;

        public final Ingredient targetItem;
        public final Ingredient augment;
        public final CompoundTag tagAugment;

        public Result(ResourceLocation id, Ingredient targetItem, Ingredient augment, CompoundTag tagAugment) {
            this.id = id;
            this.targetItem = targetItem;
            this.augment = augment;
            this.tagAugment = tagAugment;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("targetItem", targetItem.toJson());
            json.add("augment", augment.toJson());
            json.addProperty("tagAugment", tagAugment.toString());
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RecipeSerializerRegistry.AUGMENTING_RECIPE_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}