package me.lxct.bestviewdistance.functions;

import me.lxct.bestviewdistance.functions.data.Variable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static me.lxct.bestviewdistance.functions.Get.getSettingsViewDistance;
import static me.lxct.bestviewdistance.functions.Limit.limitViewDistance;
import static me.lxct.bestviewdistance.functions.data.Variable.*;

public class Calculations {
    // THE MAIN FUNCTION ! CALCULATE BEST PLAYER VIEW DISTANCE WITH REDUCTION INDICE
    public static void calculatePlayersBestViewDistance(double ReductionIndice) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            int supportedViewDistance;
            int sendVD;
            if (playerViewDistance.get(player.getName()) != null) {
                supportedViewDistance = playerViewDistance.get(player.getName()); // View distance supported by player
            } else {
                supportedViewDistance = onLoginView;
            }
            if(usePing) {
                int ping = player.spigot().getPing(); // Ping of player
                if (ping < Variable.aping && ping >= safePing) {
                    ++supportedViewDistance; // increase
                } // Low ping = More View Distance
                else if (ping >= Variable.rping) {
                    --supportedViewDistance; // Decrease
                } // Big ping = Less View Distance
            } else {
                supportedViewDistance = max;
            }
            
            playerViewDistance.put(player.getName(), limitViewDistance(supportedViewDistance)); // Store in var

            if(supportedViewDistance > getSettingsViewDistance(player)) {
                sendVD = getSettingsViewDistance(player);
            } else {
                sendVD = supportedViewDistance;
            }

            int viewDistance = Math.round((int) (sendVD * (1 - ReductionIndice))); // Apply percentage
            // About the line under this comment. We set player view distance only if view distance doesn't get over limits
            // And respect player settings
            playerLiveViewDistance.put(player.getName(), limitViewDistance(viewDistance)); // Store result of calculations
        }
    }
}
