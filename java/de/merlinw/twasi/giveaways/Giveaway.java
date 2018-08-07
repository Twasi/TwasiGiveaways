package de.merlinw.twasi.giveaways;

import net.twasi.core.database.models.TwitchAccount;
import net.twasi.core.models.Message.TwasiCommand;
import net.twasi.core.plugin.api.events.TwasiMessageEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class Giveaway {

    private HashMap<TwitchAccount, Integer> users = new HashMap<>();
    private String enter = "#Twasi", gewinn;
    private int ppu = 1; // ppu = max participation number per user
    private boolean hasended = false;
    private Userclass userclass;

    Giveaway(TwasiCommand command, Userclass userclass, String gewinn, String enter) {
        if(enter != null) this.enter = enter;
        this.gewinn = gewinn;
        this.userclass = userclass;
        command.reply(userclass.getTranslation("merlinw.giveaway.start", this.gewinn, this.enter));
    }

    void stop(TwasiCommand command) {
        this.hasended = true;
        if(users.size() == 0) command.reply(userclass.getTranslation("merlinw.giveaway.noparticipant"));
        else {
            ArrayList<TwitchAccount> accountsIn = new ArrayList<>();
            for(TwitchAccount account : users.keySet()){
                for(int i=0; i<users.get(account); i++) accountsIn.add(account);
            }
            Collections.shuffle(accountsIn);
            command.reply(userclass.getTranslation("merlinw.giveaway.stop", accountsIn.get(0).getDisplayName()));
        }

    }

    boolean hasEnded() {
        return hasended;
    }

    void abort(TwasiCommand command) {
        this.hasended = true;
        command.reply(userclass.getTranslation("merlinw.giveaway.abort"));
    }

    void enter(TwasiMessageEvent event) {
        if(!event.getMessage().getMessage().split(" ")[0].equalsIgnoreCase(this.enter)) return;
        TwitchAccount user = event.getMessage().getSender();
        if (!users.containsKey(user)) users.put(user, 1);
    }
}
