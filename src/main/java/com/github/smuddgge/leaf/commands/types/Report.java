package com.github.smuddgge.leaf.commands.types;

import com.github.smuddgge.leaf.Leaf;
import com.github.smuddgge.leaf.MessageManager;
import com.github.smuddgge.leaf.commands.BaseCommandType;
import com.github.smuddgge.leaf.commands.CommandStatus;
import com.github.smuddgge.leaf.commands.CommandSuggestions;
import com.github.smuddgge.leaf.configuration.squishyyaml.ConfigurationSection;
import com.github.smuddgge.leaf.datatype.User;
import com.github.smuddgge.leaf.placeholders.PlaceholderManager;
import com.velocitypowered.api.proxy.Player;

import java.util.Objects;

/**
 * Represents the report command type.
 */
public class Report extends BaseCommandType {

    @Override
    public String getName() {
        return "report";
    }

    @Override
    public String getSyntax() {
        return "/[name] [message]";
    }

    @Override
    public CommandSuggestions getSuggestions(ConfigurationSection section, User user) {
        return null;
    }

    @Override
    public CommandStatus onConsoleRun(ConfigurationSection section, String[] arguments) {

        if (arguments.length == 0) return new CommandStatus().incorrectArguments();

        String message = section.getString("message")
                .replace("%message%", String.join(" ", arguments));
        message = PlaceholderManager.parse(message, null, new User(null, "Console"));

        String permission = section.getString("see_report", null);

        for (Player player : Leaf.getServer().getAllPlayers()) {
            User user = new User(player);

            if (permission != null && !user.hasPermission(permission)) continue;

            user.sendMessage(message);
        }

        MessageManager.log(message);

        return new CommandStatus();
    }

    @Override
    public CommandStatus onPlayerRun(ConfigurationSection section, String[] arguments, User user) {

        if (arguments.length == 0) return new CommandStatus().incorrectArguments();

        String message = section.getString("message")
                .replace("%message%", String.join(" ", arguments));
        message = PlaceholderManager.parse(message, null, user);

        String permission = section.getString("see_report", null);

        for (Player player : Leaf.getServer().getAllPlayers()) {
            User toSend = new User(player);

            if (Objects.equals(toSend.getName(), user.getName())) continue;

            if (permission != null && !toSend.hasPermission(permission)) continue;

            toSend.sendMessage(PlaceholderManager.parse(message, null, user));
        }

        user.sendMessage(message);
        MessageManager.log(message);

        return new CommandStatus();
    }

    @Override
    public void loadSubCommands() {

    }
}
