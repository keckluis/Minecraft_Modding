package net.luis.myfirstmod.entity.client;

import net.luis.myfirstmod.MyFirstMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {

    public static final ModelLayerLocation PENGUIN_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(MyFirstMod.MOD_ID, "penguin_layer"), "main");
}
