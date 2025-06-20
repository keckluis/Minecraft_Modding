package net.luis.myfirstmod.event;

import net.luis.myfirstmod.MyFirstMod;
import net.luis.myfirstmod.entity.ModEntities;
import net.luis.myfirstmod.entity.custom.PenguinEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MyFirstMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.PENGUIN.get(), PenguinEntity.createAttributes().build());
    }
}
