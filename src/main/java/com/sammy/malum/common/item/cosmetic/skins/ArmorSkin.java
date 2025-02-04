package com.sammy.malum.common.item.cosmetic.skins;

import com.sammy.malum.client.cosmetic.ArmorSkinRenderingData;
import com.sammy.malum.registry.common.item.ArmorSkinRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.lodestar.lodestone.systems.item.LodestoneArmorItem;

import java.util.Map;

public abstract class ArmorSkin {
    public static final String MALUM_SKIN_TAG = "malum:item_skin";

    public static int indexCounter;
    public final String id;
    public final Class<? extends LodestoneArmorItem> validArmorClass;
    public final Item weaveItem;
    public final int index;

    public ArmorSkin(String id, Class<? extends LodestoneArmorItem> validArmorClass, Item weaveItem) {
        this.id = id;
        this.validArmorClass = validArmorClass;
        this.weaveItem = weaveItem;
        this.index = indexCounter++;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract ArmorSkinRenderingData getRenderingData();

    public static String getApplicableItemSkinTag(ItemStack target, ItemStack weave) {
        for (Map.Entry<String, ArmorSkin> entry : ArmorSkinRegistry.SKINS.entrySet()) {
            if (entry.getValue().validArmorClass.isInstance(target.getItem()) && entry.getValue().weaveItem.equals(weave.getItem())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static ArmorSkin getAppliedItemSkin(ItemStack stack) {
        return stack.hasTag() ? ArmorSkinRegistry.SKINS.get(stack.getTag().getString(MALUM_SKIN_TAG)) : null;
    }

    public static class ArmorSkinDatagenData {
        public final String itemTexturePrefix;
        public final String itemModelPrefix;
        public final String helmetSuffix;
        public final String chestplateSuffix;
        public final String leggingsSuffix;
        public final String bootsSuffix;

        public ArmorSkinDatagenData(String itemTexturePrefix, String itemModelPrefix, String helmetSuffix, String chestplateSuffix, String leggingsSuffix, String bootsSuffix) {
            this.itemTexturePrefix = itemTexturePrefix;
            this.itemModelPrefix = itemModelPrefix;
            this.helmetSuffix = helmetSuffix;
            this.chestplateSuffix = chestplateSuffix;
            this.leggingsSuffix = leggingsSuffix;
            this.bootsSuffix = bootsSuffix;
        }

        public String getSuffix(LodestoneArmorItem item) {
            switch (item.getEquipmentSlot()) {
                case HEAD -> {
                    return helmetSuffix;
                }
                case CHEST -> {
                    return chestplateSuffix;
                }
                case LEGS -> {
                    return leggingsSuffix;
                }
                case FEET -> {
                    return bootsSuffix;
                }
            }
            return null;
        }
    }
}