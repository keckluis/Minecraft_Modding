package net.luis.myfirstmod.entity.ai;

import net.luis.myfirstmod.entity.custom.PenguinEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class PenguinSwimAroundGoal extends Goal {
    private final PenguinEntity penguin;
    private final double speedModifier;
    private int timeToRecalcPath;
    private final int maxTimeToRecalcPath;

    @Nullable
    private Vec3 wantedPos;

    public PenguinSwimAroundGoal(PenguinEntity penguin, double speedModifier) {
        this.penguin = penguin;
        this.speedModifier = speedModifier;
        this.maxTimeToRecalcPath = 120; // Recalculate path every 6 seconds
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.penguin.isInWater()) {
            return false;
        }

        if (this.penguin.getRandom().nextInt(120) != 0) {
            return false;
        }

        return this.findPos();
    }

    @Override
    public boolean canContinueToUse() {
        return this.penguin.isInWater() &&
                this.wantedPos != null &&
                !this.penguin.getNavigation().isDone() &&
                this.timeToRecalcPath > 0;
    }

    @Override
    public void start() {
        if (this.wantedPos != null) {
            this.penguin.getNavigation().moveTo(this.wantedPos.x, this.wantedPos.y, this.wantedPos.z, this.speedModifier);
        }
        this.timeToRecalcPath = this.maxTimeToRecalcPath;
    }

    @Override
    public void stop() {
        this.wantedPos = null;
        this.timeToRecalcPath = 0;
    }

    @Override
    public void tick() {
        --this.timeToRecalcPath;

        if (this.timeToRecalcPath <= 0) {
            if (this.findPos()) {
                this.penguin.getNavigation().moveTo(this.wantedPos.x, this.wantedPos.y, this.wantedPos.z, this.speedModifier);
                this.timeToRecalcPath = this.maxTimeToRecalcPath;
            } else {
                this.timeToRecalcPath = this.maxTimeToRecalcPath / 2;
            }
        }
    }

    private boolean findPos() {
        Vec3 vec3 = DefaultRandomPos.getPos(this.penguin, 10, 7);

        if (vec3 == null) {
            return false;
        }

        BlockPos targetPos = new BlockPos((int) vec3.x, (int) vec3.y, (int) vec3.z);
        if (this.penguin.level().getBlockState(targetPos).is(Blocks.WATER) ||
                this.penguin.level().getBlockState(targetPos.above()).is(Blocks.WATER)) {
            this.wantedPos = vec3;
            return true;
        }

        return false;
    }
}