package Clay.Sam.nations.Utils;

import Clay.Sam.nations.Nations;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class SignWriter implements Listener {

    //Top and bottom lines are always ""
    private final String[] signText = new String[4];
    private final Plugin plugin;

    private Player player;

    public SignWriter() {
        plugin = Nations.getPlugin();
        // Initialize signText with empty strings
        Arrays.fill(signText, "");
    }

    public String[] getSignWriterText(@NotNull Player player, String title) {

        String signTitle = title != null ? title : "Default Title";
        this.player = player; // Store the player reference for later use

        openSign(player, signTitle);

        return signText;
    }

    private void openSign(Player player, String title) {
    // Create a temporary sign block at the player's location
    Location signLocation = player.getLocation().add(0, 1, 0); // Place it above the player
    Block block = signLocation.getBlock();
    
    // Save the original block state
    Material originalMaterial = block.getType();
    
    // Set the block to a sign temporarily
    block.setType(Material.OAK_SIGN);
    
    // Get the sign block state
    org.bukkit.block.Sign sign = (org.bukkit.block.Sign) block.getState();
    
    // Get the front side of the sign
    SignSide frontSide = sign.getSide(Side.FRONT);
    
    // Set the initial lines (including the title) using the new API
    frontSide.line(0, Component.text(title != null ? title : ""));
    frontSide.line(1, Component.text(""));
    frontSide.line(2, Component.text(""));
    frontSide.line(3, Component.text(""));
    
    sign.update();
    
    // Open the sign for editing
    player.openSign(sign);
    
    // Restore the original block after a short delay
    Bukkit.getScheduler().runTaskLater(plugin, () -> {
        block.setType(originalMaterial);
    }, 2L); // 2 ticks delay
}

    @EventHandler
    public void onSignChange(SignChangeEvent event) {

        if(event.getPlayer() != player) return;

        // Check if line 0 or line 3 has been changed
        if (!Objects.equals(event.line(0).toString(), signText[0]) ||
                !Objects.equals(event.line(3).toString(), signText[3])) {
            event.setCancelled(true);
            return;
        }


        /**
         * Only grab the middle lines
         */
        signText[1] = String.valueOf(event.line(1));
        signText[2] = String.valueOf(event.line(2));

    }

}