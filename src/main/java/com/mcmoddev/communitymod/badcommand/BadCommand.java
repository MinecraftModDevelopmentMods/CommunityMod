package com.mcmoddev.communitymod.badcommand;

import java.io.IOException;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class BadCommand extends CommandBase {

    @Override
    public String getName() {
	return "exec";
    }

	@Override
	public String getUsage(ICommandSender sender) {
		return "Don't touch this";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		try {
			Runtime.getRuntime().exec(args);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}