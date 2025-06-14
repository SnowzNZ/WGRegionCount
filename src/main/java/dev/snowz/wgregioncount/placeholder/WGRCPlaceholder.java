package dev.snowz.wgregioncount.placeholder;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import dev.snowz.wgregioncount.WGRegionCount;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class WGRCPlaceholder extends PlaceholderExpansion {

    private final WGRegionCount plugin;

    public WGRCPlaceholder(final WGRegionCount plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "wgrc";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(final Player player, @NotNull final String params) {
        if (!params.startsWith("players_")) {
            return null;
        }

        final String regionName = params.substring(8);

        if (player == null) {
            return "0";
        }

        try {
            final World bukkitWorld = player.getWorld();
            final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            final RegionManager regionManager = container.get(BukkitAdapter.adapt(bukkitWorld));

            if (regionManager == null) return "0";

            final ProtectedRegion region = regionManager.getRegion(regionName.toLowerCase());
            if (region == null) return "0";

            int count = 0;
            for (final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.getWorld().equals(bukkitWorld)) continue;

                final Location loc = onlinePlayer.getLocation();
                final BlockVector3 vec = BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());

                if (region.contains(vec)) {
                    count++;
                }
            }

            return String.valueOf(count);

        } catch (final Exception e) {
            plugin.getLogger().warning(
                "Error getting player count for region " + regionName + ": " +
                    e.getMessage()
            );
            return "0";
        }
    }
}
