package de.merlinw.twasi.giveaways;

import net.twasi.core.plugin.TwasiPlugin;
import net.twasi.core.plugin.api.TwasiUserPlugin;

public class Main extends TwasiPlugin {

    @Override
    public void onActivate() {

    }

    @Override
    public void onDeactivate() {

    }

    public Class<? extends TwasiUserPlugin> getUserPluginClass() {
        return Userclass.class;
    }

}
