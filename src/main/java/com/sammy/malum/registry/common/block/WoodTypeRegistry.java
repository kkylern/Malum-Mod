package com.sammy.malum.registry.common.block;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.ArrayList;
import java.util.List;

//@Mod.EventBusSubscriber(modid= MalumMod.MALUM, bus= Mod.EventBusSubscriber.Bus.MOD)
public class WoodTypeRegistry {
    public static List<WoodType> WOOD_TYPES = new ArrayList<>();
    //public static final WoodType RUNEWOOD = WoodType.register(new MalumWoodType("runewood"));
    //public static final WoodType SOULWOOD = WoodType.register(new MalumWoodType("soulwood"));

    public static final WoodType RUNEWOOD = WoodType.register(new WoodType("runewood", MalumBlockSetTypes.RUNEWOOD));
    public static final WoodType SOULWOOD = WoodType.register(new WoodType("soulwood", MalumBlockSetTypes.SOULWOOD));



}
