package com.sammy.malum.common.item.food;

import com.sammy.malum.registry.common.*;
import com.sammy.malum.registry.common.item.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import team.lodestar.lodestone.helpers.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;

public class ConcentratedGluttonyItem extends BottledDrinkItem {
    public static final Collection<Supplier<Item>> ROTTEN_TRINKETS = new ArrayList<>(List.of(ItemRegistry.RING_OF_DESPERATE_VORACITY, ItemRegistry.GLUTTONOUS_BROOCH, ItemRegistry.BELT_OF_THE_STARVED));

    public ConcentratedGluttonyItem(Properties builder) {
        super(builder);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        MobEffectInstance effect = pEntityLiving.getEffect(MobEffectRegistry.GLUTTONY.get());
        if (effect == null) {
            pEntityLiving.addEffect(createGluttonyEffect(pEntityLiving));
        }
        pEntityLiving.playSound(SoundRegistry.CONCENTRATED_GLUTTONY_DRINK.get(), 1f, RandomHelper.randomBetween(pLevel.random, 1.5f, 2f));

        return super.finishUsingItem(pStack, pLevel, pEntityLiving);
    }

    public static MobEffectInstance createGluttonyEffect(@Nullable Entity source) {
        return createGluttonyEffect(source, 1);
    }
    public static MobEffectInstance createGluttonyEffect(@Nullable Entity source, float durationScalar) {
        int amplifier = 3;
        int duration = 20;

        if (source instanceof LivingEntity livingEntity) {
            if (CurioHelper.hasCurioEquipped(livingEntity, ItemRegistry.RING_OF_GRUESOME_CONCENTRATION.get())) {
                amplifier++;
                duration += 40;
            }
            for (Supplier<Item> rottenTrinket : ROTTEN_TRINKETS) {
                if (CurioHelper.hasCurioEquipped(livingEntity, rottenTrinket.get())) {
                    amplifier++;
                    duration += 10;
                }
            }
        }
        return new MobEffectInstance(MobEffectRegistry.GLUTTONY.get(), (int) (duration * 20 * durationScalar), amplifier);
    }
}