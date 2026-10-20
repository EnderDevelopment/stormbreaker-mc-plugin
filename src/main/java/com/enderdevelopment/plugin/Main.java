package com.enderdevelopment.plugin;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(this, this);
        registerStormbreakerRecipe();
    }

    private void registerStormbreakerRecipe() {
        ItemStack stormbreaker = new ItemStack(Material.TRIDENT);
        ItemMeta meta = stormbreaker.getItemMeta();
        meta.setDisplayName("§6Stormbreaker");
        meta.setLore(Arrays.asList("§7A legendary trident that summons lightning"));
        stormbreaker.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "stormbreaker");
        ShapedRecipe recipe = new ShapedRecipe(key, stormbreaker);
        recipe.shape(" D ", " D ", " R ");
        recipe.setIngredient('D', Material.DIAMOND_BLOCK);
        recipe.setIngredient('R', Material.WITHER_SKELETON_SKULL);
        getServer().addRecipe(recipe);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (player.isSneaking() && player.getInventory().getItemInMainHand().getType() == Material.TRIDENT) {
                ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                if (meta != null && "§6Stormbreaker".equals(meta.getDisplayName())) {
                    if (cooldowns.containsKey(player.getUniqueId()) && System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) < 60000) {
                        player.sendMessage("§cStormbreaker is on cooldown!");
                        return;
                    }

                    player.getWorld().strikeLightning(player.getTargetBlock(null, 50).getLocation());
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 1));
                    cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }
}