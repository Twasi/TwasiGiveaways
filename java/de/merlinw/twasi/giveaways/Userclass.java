package de.merlinw.twasi.giveaways;

import net.twasi.core.database.models.User;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.plugin.api.TwasiUserPlugin;
import net.twasi.core.plugin.api.events.TwasiCommandEvent;
import net.twasi.core.plugin.api.events.TwasiInstallEvent;
import net.twasi.core.plugin.api.events.TwasiMessageEvent;

public class Userclass extends TwasiUserPlugin {

    private Giveaway giveaway = null;

    @Override
    public void onInstall(TwasiInstallEvent e) {
        e.getDefaultGroup().addKey("merlinw.giveaways.participate");
        e.getModeratorsGroup().addKey("merlinw.giveaways.operate");
    }

    @Override
    public void onUninstall(TwasiInstallEvent e) {
        e.getDefaultGroup().removeKey("merlinw.giveaways.participate");
        e.getModeratorsGroup().removeKey("merlinw.giveaways.operate");
    }

    @Override
    public void onCommand(TwasiCommandEvent e) {
        TwasiCommand command = e.getCommand();
        User Streamer = getTwasiInterface().getStreamer().getUser();
        if (command.getCommandName().equalsIgnoreCase("giveaway") || command.getCommandName().equalsIgnoreCase("ga")) {
            if (!Streamer.hasPermission(command.getSender(), "merlinw.giveaways.operate")) return;
            String[] cmdArgs = e.getCommand().getMessage().split(" ");
            if (cmdArgs.length == 1) {
                helpFile(command);
                return;
            }

            switch (cmdArgs[1].toLowerCase()) {
                case "start":
                    String enter = null, gewinn;
                    if (cmdArgs.length >= 4) enter = cmdArgs[3];
                    try {
                        gewinn = cmdArgs[2];
                    } catch (ArrayIndexOutOfBoundsException e1){
                        helpFile(command);
                        return;
                    }
                    if (this.giveaway == null || this.giveaway.hasEnded())
                        this.giveaway = new Giveaway(command, this, gewinn, enter);
                    else command.reply(getTranslation("merlinw.giveaway.operate.alreadystarted"));
                    return;
                case "stop":
                    if (giveaway != null && !this.giveaway.hasEnded())
                        this.giveaway.stop(command);
                    else command.reply(getTranslation("merlinw.giveaway.operate.notstarted"));
                    return;
                case "abort":
                    if (giveaway != null && !this.giveaway.hasEnded())
                        this.giveaway.abort(command);
                    else command.reply(getTranslation("merlinw.giveaway.operate.notstarted"));
                    return;
                case "reroll":

                    return;
                default:
                    helpFile(command);
            }
        }
    }

    @Override
    public void onMessage(TwasiMessageEvent e) {
        if(this.giveaway != null && !this.giveaway.hasEnded())
            this.giveaway.enter(e);
    }

    private void helpFile(TwasiCommand cmd) {
        cmd.reply(getTranslation("merlinw.giveaway.helptext"));
    }

}
