package dev.jsinco.brewery.commands;

import dev.jsinco.brewery.BreweryGarden;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface AddonSubCommand {

    boolean execute(BreweryGarden addon, CommandSender sender, String label, String[] args);

    List<String> tabComplete(BreweryGarden addon, CommandSender sender, String label, String[] args);

    String permission();

    boolean playerOnly();

    String usage(String label);
}
