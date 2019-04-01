package com.mcmoddev.communitymod.gegy.squashable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Squashable implements ICapabilitySerializable<NBTTagCompound> {
    private EnumFacing.Axis squashedAxis;

    public void squash(EnumFacing.Axis axis) {
        this.squashedAxis = axis;
    }

    @Nullable
    public EnumFacing.Axis getSquashedAxis() {
        return this.squashedAxis;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == SquashableMod.squashableCap();
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == SquashableMod.squashableCap()) {
            return SquashableMod.squashableCap().cast(this);
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        if (this.squashedAxis != null) {
            compound.setString("squashed_axis", this.squashedAxis.getName());
        }
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        this.squashedAxis = EnumFacing.Axis.byName(compound.getString("squashed_axis"));
    }

    public static class Storage implements Capability.IStorage<Squashable> {
        public static final Storage INSTANCE = new Storage();

        private Storage() {
        }

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<Squashable> capability, Squashable instance, EnumFacing side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<Squashable> capability, Squashable instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound compound = (NBTTagCompound) nbt;
            instance.deserializeNBT(compound);
        }
    }
}
