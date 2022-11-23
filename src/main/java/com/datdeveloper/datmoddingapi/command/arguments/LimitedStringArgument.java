package com.datdeveloper.datmoddingapi.command.arguments;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class LimitedStringArgument implements ArgumentType<String> {
    private static final SimpleCommandExceptionType ERROR_TOO_LONG = new SimpleCommandExceptionType(Component.literal("Argument exceeds maximum length"));

    StringArgumentType delegate;
    int maxLength;
    protected LimitedStringArgument(final StringArgumentType.StringType type, final int maxLength) {
        try {
            final Constructor<StringArgumentType> constructor = StringArgumentType.class.getDeclaredConstructor(StringArgumentType.StringType.class);
            constructor.setAccessible(true);
            this.delegate = constructor.newInstance(type);
            this.maxLength = maxLength;
        } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String parse(final StringReader reader) throws CommandSyntaxException {
        final String string = delegate.parse(reader);
        if (string.length() > maxLength) throw ERROR_TOO_LONG.create();
        return string;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        return delegate.listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return delegate.getExamples();
    }

    public static class Info implements ArgumentTypeInfo<LimitedStringArgument, Info.Template> {
        @Override
        public void serializeToNetwork(final Template pTemplate, final FriendlyByteBuf pBuffer) {
            pBuffer.writeEnum(pTemplate.type);
            pBuffer.writeInt(pTemplate.maxLength);
        }

        @Override
        public Template deserializeFromNetwork(final FriendlyByteBuf pBuffer) {
            final StringArgumentType.StringType type = pBuffer.readEnum(StringArgumentType.StringType.class);
            final int maxLength = pBuffer.readInt();
            return new Template(type, maxLength);
        }

        @Override
        public void serializeToJson(final Template pTemplate, final JsonObject pJson) {
            final String s = switch (pTemplate.type) {
                case SINGLE_WORD -> "word";
                case QUOTABLE_PHRASE -> "phrase";
                case GREEDY_PHRASE -> "greedy";
                default -> throw new IncompatibleClassChangeError();
            };
            pJson.addProperty("type", s);
            pJson.addProperty("maxLength", pTemplate.maxLength);
        }

        @Override
        public Template unpack(final LimitedStringArgument pArgument) {
            return new Template(pArgument.delegate.getType(), pArgument.maxLength);
        }

        public class Template implements ArgumentTypeInfo.Template<LimitedStringArgument> {
            final StringArgumentType.StringType type;
            final int maxLength;

            public Template(final StringArgumentType.StringType type, final int maxLength) {
                this.type = type;
                this.maxLength = maxLength;
            }

            @Override
            public LimitedStringArgument instantiate(final CommandBuildContext pContext) {
                return new LimitedStringArgument(type, maxLength);
            }

            @Override
            public ArgumentTypeInfo<LimitedStringArgument, ?> type() {
                return Info.this;
            }
        }
    }
}
