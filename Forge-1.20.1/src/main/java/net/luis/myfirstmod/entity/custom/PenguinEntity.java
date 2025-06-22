package net.luis.myfirstmod.entity.custom;

import net.luis.myfirstmod.entity.ModEntities;
import net.luis.myfirstmod.entity.ai.PenguinSwimAroundGoal;
import net.luis.myfirstmod.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PenguinEntity extends Animal {

    public PenguinEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState swimAnimationState = new AnimationState();

    private int idleAnimationTimeout = 0;
    private boolean wasInWater = false;

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();
        }

        // Handle water speed changes
        handleWaterSpeedChanges();
    }

    private void handleWaterSpeedChanges() {
        boolean currentlyInWater = this.isInWater();

        if (currentlyInWater != wasInWater) {
            if (currentlyInWater) {
                // Entering water - increase speed
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2D); // 2x base speed
            } else {
                // Leaving water - reset to normal speed
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1D); // normal speed
            }
            wasInWater = currentlyInWater;
        }

        // Encourage movement in water
        if (currentlyInWater && this.getRandom().nextInt(100) < 2) { // 2% chance per tick
            // Give the penguin a slight push to encourage swimming
            this.setDeltaMovement(this.getDeltaMovement().add(
                    (this.getRandom().nextDouble() - 0.5) * 0.1,
                    0,
                    (this.getRandom().nextDouble() - 0.5) * 0.1
            ));
        }
    }

    private void setupAnimationStates() {
        boolean isMoving = this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;

        if (isMoving) {
            this.idleAnimationState.stop();
            this.idleAnimationTimeout = 0;

            if (this.isInWater()) {
                this.walkAnimationState.stop();
                this.swimAnimationState.startIfStopped(this.tickCount);
            } else {
                this.swimAnimationState.stop();
                this.walkAnimationState.startIfStopped(this.tickCount);
            }
        } else {
            this.walkAnimationState.stop();
            this.swimAnimationState.stop();

            if (this.idleAnimationTimeout <= 0) {
                this.idleAnimationTimeout = this.random.nextInt(40) + 80;
                this.idleAnimationState.start(this.tickCount);
            } else {
                --this.idleAnimationTimeout;
            }
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if (this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6f, 1f);
        } else {
            f = 0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.15D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, Ingredient.of(Items.COD), false));
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.1D));

        this.goalSelector.addGoal(4, new PenguinSwimAroundGoal(this, 2f));
        this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.2D, 40)); // Swim more frequently
        this.goalSelector.addGoal(6, new TryFindWaterGoal(this)); // Seek water
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D)); // Normal land walking

        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 4D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return ModEntities.PENGUIN.get().create(pLevel);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.COD);
    }

    @Override
    public float getSpeed() {
        if (this.isInWater()) {
            return super.getSpeed() * 5.0f;
        }
        return super.getSpeed();
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.9f;
    }

    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isInWater() && !this.level().isClientSide()) {
            if (this.getDeltaMovement().horizontalDistanceSqr() < 0.01 && this.getRandom().nextInt(60) == 0) {
                double motionX = (this.getRandom().nextDouble() - 0.5) * 0.2;
                double motionZ = (this.getRandom().nextDouble() - 0.5) * 0.2;
                this.setDeltaMovement(this.getDeltaMovement().add(motionX, 0, motionZ));
            }
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.PENGUIN_NOOT_NOOT.get();
    }
}