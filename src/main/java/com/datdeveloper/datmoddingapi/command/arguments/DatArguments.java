package com.datdeveloper.datmoddingapi.command.arguments;

import com.datdeveloper.datmoddingapi.Datmoddingapi;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DatArguments {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registry.COMMAND_ARGUMENT_TYPE_REGISTRY, Datmoddingapi.MODID);

    /* ========================================= */
    /* Argument Registration
    /* ========================================= */

    public static final RegistryObject<LimitedStringArgument.Info> LIMITED_STRING_COMMAND_ARGUMENT_TYPE = COMMAND_ARGUMENT_TYPES.register("limitedstring", () ->
            ArgumentTypeInfos.registerByClass(LimitedStringArgument.class, new LimitedStringArgument.Info()));
}
