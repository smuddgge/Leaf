package com.github.smuddgge.leaf.commands.types;

import com.github.smuddgge.leaf.Leaf;
import com.github.smuddgge.leaf.MessageManager;
import com.github.smuddgge.leaf.commands.BaseCommandType;
import com.github.smuddgge.leaf.commands.CommandStatus;
import com.github.smuddgge.leaf.commands.CommandSuggestions;
import com.github.smuddgge.leaf.configuration.squishyyaml.ConfigurationSection;
import com.github.smuddgge.leaf.datatype.User;
import com.github.smuddgge.leaf.placeholders.PlaceholderManager;

/**
 * Represents the find command type.
 */
public class Find extends BaseCommandType {

    @Override
    public String getName() {
        return "find";
    }

    @Override
    public String getSyntax() {
        return "/[name] [player]";
    }

    @Override
    public CommandSuggestions getSuggestions(ConfigurationSection section, User user) {
        if (user.isNotVanishable()) return new CommandSuggestions().appendPlayers();
        if (!section.getBoolean("vanishable_players", false)) return new CommandSuggestions().appendPlayers();
        return new CommandSuggestions().appendPlayersRaw();
    }

    @Override
    public CommandStatus onConsoleRun(ConfigurationSection section, String[] arguments) {
        if (arguments.length == 0) return new CommandStatus().incorrectArguments();

        if (Leaf.getServer().getPlayer(arguments[0]).isEmpty()) {
            String notFound = section.getString("not_found");
            MessageManager.log(notFound);
            return new CommandStatus();
        }

        User user = new User(Leaf.getServer().getPlayer(arguments[0]).get());

        String found = section.getString("found");

        MessageManager.log(PlaceholderManager.parse(found, null, user));

        return new CommandStatus();
    }

    @Override
    public CommandStatus onPlayerRun(ConfigurationSection section, String[] arguments, User user) {
        if (arguments.length == 0) return new CommandStatus().incorrectArguments();

        if (Leaf.getServer().getPlayer(arguments[0]).isEmpty()) {
            String notFound = section.getString("not_found");
            user.sendMessage(notFound);
            return new CommandStatus();
        }

        User foundUser = new User(Leaf.getServer().getPlayer(arguments[0]).get());

        // If vanishable players can find vanishable players, and the user is vanishable
        if (section.getBoolean("vanishable_players", false) && !user.isNotVanishable()) {
            String found = section.getString("found");

            user.sendMessage(PlaceholderManager.parse(found, null, foundUser));

            return new CommandStatus();
        }

        if (foundUser.isVanished()) {
            String notFound = section.getString("not_found");
            user.sendMessage(notFound);
            return new CommandStatus();
        }

        String found = section.getString("found");

        user.sendMessage(PlaceholderManager.parse(found, null, foundUser));

        return new CommandStatus();
    }

    @Override
    public void loadSubCommands() {

    }
}
