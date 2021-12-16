package com.sammy.malum.common.recipe;

import com.google.gson.JsonObject;
import com.sammy.malum.MalumMod;
import com.sammy.malum.core.registry.content.RecipeSerializerRegistry;
import com.sammy.malum.core.systems.recipe.IMalumRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.Level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTransmutationRecipe extends IMalumRecipe
{
    public static final String NAME = "block_transmutation";
    public static class Type implements IRecipeType<BlockTransmutationRecipe> {
        @Override
        public String toString () {
            return MalumMod.MODID + ":" + NAME;
        }

        public static final BlockTransmutationRecipe.Type INSTANCE = new BlockTransmutationRecipe.Type();
    }

    private final ResourceLocation id;

    public final Block input;
    public final Block output;

    public BlockTransmutationRecipe(ResourceLocation id, Block input, Block output)
    {
        this.id = id;
        this.input = input;
        this.output = output;
    }
    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return RecipeSerializerRegistry.BLOCK_TRANSMUTATION_RECIPE_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType()
    {
        return Type.INSTANCE;
    }

    @Override
    public ResourceLocation getId()
    {
        return id;
    }

    public boolean doesInputMatch(Block input)
    {
        return input.equals(this.input);
    }
    public boolean doesOutputMatch(Block output)
    {
        return output.equals(this.output);
    }

    public static List<BlockTransmutationRecipe> getRecipes(Level Level)
    {
        return Level.getRecipeManager().getAllRecipesFor(Type.INSTANCE);
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BlockTransmutationRecipe> {

        @Override
        public BlockTransmutationRecipe fromJson(ResourceLocation recipeId, JsonObject json)
        {
            String input = json.getAsJsonPrimitive("input").getAsString();
            String output = json.getAsJsonPrimitive("output").getAsString();

            return new BlockTransmutationRecipe(recipeId, Registry.BLOCK.get(new ResourceLocation(input)), Registry.BLOCK.get(new ResourceLocation(output)));
        }

        @Nullable
        @Override
        public BlockTransmutationRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer)
        {
            BlockState input = Block.stateById(buffer.readVarInt());
            BlockState output = Block.stateById(buffer.readVarInt());
            return new BlockTransmutationRecipe(recipeId, input.getBlock(), output.getBlock());
        }

        @Override
        public void toNetwork(PacketBuffer buffer, BlockTransmutationRecipe recipe)
        {
            buffer.writeVarInt(Block.getId(recipe.input.defaultBlockState()));
            buffer.writeVarInt(Block.getId(recipe.output.defaultBlockState()));
        }
    }
}