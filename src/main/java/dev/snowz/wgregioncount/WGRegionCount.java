package dev.snowz.wgregioncount;

import dev.snowz.wgregioncount.placeholder.WGRCPlaceholder;
import org.bukkit.plugin.java.JavaPlugin;

public final class WGRegionCount extends JavaPlugin {

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("PlaceholderAPI not found! Plugin disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            getLogger().warning("WorldGuard not found! Plugin disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new WGRCPlaceholder(this).register();
    }
}
