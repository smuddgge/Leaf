package com.github.smuddgge.leaf.commands.types;

import com.github.smuddgge.leaf.MessageManager;
import com.github.smuddgge.leaf.commands.BaseCommandType;
import com.github.smuddgge.leaf.commands.CommandStatus;
import com.github.smuddgge.leaf.commands.CommandSuggestions;
import com.github.smuddgge.leaf.commands.subtypes.friends.List;
import com.github.smuddgge.leaf.commands.subtypes.friends.Request;
import com.github.smuddgge.leaf.commands.subtypes.friends.Requests;
import com.github.smuddgge.leaf.configuration.squishyyaml.ConfigurationSection;
import com.github.smuddgge.leaf.datatype.User;
import com.github.smuddgge.leaf.inventorys.inventorys.FriendListInventory;

public class Friends extends BaseCommandType {

    @Override
    public String getName() {
        return "friends";
    }

    @Override
    public String getSyntax() {
        return "/[name] <optional argument>";
    }

    @Override
    public CommandSuggestions getSuggestions(ConfigurationSection section, User user) {
        return null;
    }

    @Override
    public CommandStatus onConsoleRun(ConfigurationSection section, String[] arguments) {
        return new CommandStatus();
    }

    @Override
    public CommandStatus onPlayerRun(ConfigurationSection section, String[] arguments, User user) {

        // Open friend list inventory.
        try {
            FriendListInventory friendListInventory = new FriendListInventory(section.getSection("list"), user);
            friendListInventory.open();
        } catch (Exception exception) {
            user.sendMessage(section.getSection("list").getString("error", "{error_colour}Error occurred when opening inventory."));

            MessageManager.warn("Exception occurred when opening a friend list inventory! [/friends]");
            exception.printStackTrace();
        }

        return new CommandStatus();
    }

    @Override
    public void loadSubCommands() {
        this.addSubCommandType(new List());
        this.addSubCommandType(new Request());
        this.addSubCommandType(new Requests());
    }
}
