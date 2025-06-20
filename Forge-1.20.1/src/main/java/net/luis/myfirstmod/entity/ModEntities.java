package net.luis.myfirstmod.entity;

import net.luis.myfirstmod.MyFirstMod;
import net.luis.myfirstmod.entity.custom.PenguinEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MyFirstMod.MOD_ID);

    public static final RegistryObject<EntityType<PenguinEntity>> PENGUIN =
            ENTITY_TYPES.register(
                    "penguin",
                    () -> EntityType.Builder.of(
                            PenguinEntity::new, MobCategory.CREATURE).sized(0.5f, 1.2f).build("penguin"
                    )
            );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
