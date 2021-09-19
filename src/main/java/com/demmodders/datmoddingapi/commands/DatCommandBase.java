package com.demmodders.datmoddingapi.commands;

import com.demmodders.datmoddingapi.util.DemConstants;
import com.demmodders.datmoddingapi.util.Permissions;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public abstract class DatCommandBase extends CommandBase {
    /**
     * Sends the given message to the given recipient
     * @param recipient The ICommandSender (console, player, etc) receiving the message
     * @param message The message being sent to the ICommandSender
     */
    protected void sendMessage(ICommandSender recipient, ITextComponent message) {
        recipient.sendMessage(message);
    }

    /**
     * Sends an informational message to the given recipient
     * @param recipient The ICommandSender (console, player, etc) receiving the message
     * @param message The message being sent to the ICommandSender
     */
    protected void sendInfo(ICommandSender recipient, String message) {
        sendMessage(recipient, new TextComponentString(DemConstants.TextColour.INFO + message));
    }

    /**
     * Sends an error message to the given recipient
     * @param recipient The ICommandSender (console, player, etc) receiving the message
     * @param message The message being sent to the ICommandSender
     */
    protected void sendError(ICommandSender recipient, String message) {
        sendMessage(recipient, new TextComponentString(DemConstants.TextColour.ERROR + message));
    }

    /**
     * Gets the permission node for this command
     * @return The permission node
     */
    protected abstract String getPermissionNode();

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return Permissions.checkPermission(sender, getPermissionNode(), getRequiredPermissionLevel());
    }
}
