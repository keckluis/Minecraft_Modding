package net.luis.myfirstmod.event;

import net.luis.myfirstmod.MyFirstMod;
import net.luis.myfirstmod.entity.client.ModModelLayers;
import net.luis.myfirstmod.entity.client.PenguinModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MyFirstMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.PENGUIN_LAYER, PenguinModel::createBodyLayer);
    }
}
