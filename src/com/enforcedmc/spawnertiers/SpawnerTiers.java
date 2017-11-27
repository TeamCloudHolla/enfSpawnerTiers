package com.enforcedmc.spawnertiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnerTiers extends JavaPlugin implements Listener
{
    List<Spawner> spawners;
    String s;
    
    public SpawnerTiers() {
        this.spawners = new ArrayList<Spawner>();
        this.s = "§a§l* ";
    }
    
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();
        if (Util.EnchantGlow.getGlow() == null) {
            this.getServer().getConsoleSender().sendMessage("§7§l|------------------------------------------------------------|");
            this.getServer().getConsoleSender().sendMessage("");
            this.getServer().getConsoleSender().sendMessage(String.valueOf(this.s) + "§cEnchantment Glow is invalid. Error has occured");
            this.getServer().getConsoleSender().sendMessage("");
            this.getServer().getConsoleSender().sendMessage("§7§l|------------------------------------------------------------|");
        }
        if (this.getConfig().isConfigurationSection("Spawners")) {
            for (final String s : this.getConfig().getConfigurationSection("Spawners").getValues(false).keySet()) {
                if (this.getLocation(s) != null) {
                    this.spawners.add(new Spawner(Util.getEntity(this.getConfig().getString("Spawners." + s + ".Type")), this.getConfig().getInt("Spawners." + s + ".Level"), this.getLocation(s)));
                }
            }
        }
    }
    
    public void onDisable() {
        if (this.getConfig().isConfigurationSection("Spawners")) {
            for (final String s : this.getConfig().getConfigurationSection("Spawners").getValues(false).keySet()) {
                this.getConfig().set("Spawners." + s, (Object)null);
            }
        }
        for (final Spawner sp : this.spawners) {
            this.getConfig().set("Spawners." + this.getLoc(sp.getLoc()) + ".Type", (Object)sp.getName());
            this.getConfig().set("Spawners." + this.getLoc(sp.getLoc()) + ".Level", (Object)sp.getLevel());
        }
        this.saveConfig();
    }
    
    Location getLocation(final String s) {
        try {
            final String[] args = s.split(",");
            return new Location(this.getServer().getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
        }
        catch (NullPointerException | NumberFormatException | ArrayIndexOutOfBoundsException ex2) {
            final RuntimeException ex = null;
            final RuntimeException e = ex;
            return null;
        }
    }
    
    String getLoc(final Location l) {
        try {
            return String.valueOf(l.getWorld().getName()) + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
        }
        catch (Exception e) {
            return "";
        }
    }
    
    String strip(final String s) {
        return ChatColor.stripColor(s);
    }
    
    Spawner getSpawner(final CreatureSpawner s) {
        for (final Spawner sp : this.spawners) {
            if (sp.getLoc().equals((Object)s.getLocation()) || (sp.getLoc().getBlockX() == s.getLocation().getBlockX() && sp.getLoc().getBlockY() == s.getLocation().getBlockY() && sp.getLoc().getBlockZ() == s.getLocation().getBlockZ() && s.getSpawnedType().equals((Object)sp.getType()))) {
                return sp;
            }
        }
        return null;
    }
    
    Spawner getSpawner(final String s) {
        for (final Spawner sp : this.spawners) {
            if (sp.getLoc().equals((Object)this.getLocation(s))) {
                return sp;
            }
        }
        return null;
    }
    
    @EventHandler
    public void spawn(final SpawnerSpawnEvent e) {
        final CreatureSpawner sp = e.getSpawner();
        if (this.getSpawner(sp) != null) {
            this.getServer().getScheduler().cancelAllTasks();
            new BukkitRunnable() {
                public void run() {
                    sp.setDelay(120 - SpawnerTiers.this.getSpawner(sp).getLevel() * 20 + new Random().nextInt(120));
                }
            }.runTaskLater((Plugin)this, 0L);
        }
        else {
            this.spawners.add(new Spawner(sp.getSpawnedType(), 0, sp.getLocation()));
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockPlace(final BlockPlaceEvent e) {
        final ItemStack b = e.getItemInHand();
        if (b == null || !b.getType().equals((Object)Material.MOB_SPAWNER) || e.isCancelled()) {
            return;
        }
        EntityType type = null;
        int lvl = 1;
        if (b.hasItemMeta() && b.getItemMeta().hasLore() && b.getItemMeta().getLore().size() == 4) {
            type = Util.getEntity(this.strip(b.getItemMeta().getLore().get(1)));
            lvl = Integer.parseInt(this.strip(b.getItemMeta().getLore().get(3)));
        }
        final CreatureSpawner sp = (CreatureSpawner)e.getBlock().getState();
        if (type != null) {
            sp.setSpawnedType(type);
            this.spawners.add(new Spawner(type, lvl, e.getBlock().getLocation()));
        }
        else if (b.getDurability() != 0) {
            sp.setSpawnedType(EntityType.fromId((int)b.getDurability()));
            this.spawners.add(new Spawner(sp.getSpawnedType(), 0, e.getBlock().getLocation()));
        }
        else if (b.getItemMeta().hasDisplayName() && b.getItemMeta().getDisplayName().startsWith("§e")) {
            final String name = this.strip(b.getItemMeta().getDisplayName()).replaceAll(" Spawner", "");
            if (Util.getEntity(name) != null) {
                sp.setSpawnedType(Util.getEntity(name));
                this.spawners.add(new Spawner(Util.getEntity(name), 0, e.getBlock().getLocation()));
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void blockBreak(final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        final Block b = e.getBlock();
        if (!b.getType().equals((Object)Material.MOB_SPAWNER) || !(b.getState() instanceof CreatureSpawner)) {
            return;
        }
        final CreatureSpawner sp = (CreatureSpawner)b.getState();
        final Spawner s = this.getSpawner(sp);
        this.spawners.remove(s);
        if (p.getItemInHand() == null || !p.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
            return;
        }
        if (s != null) {
            b.getWorld().dropItemNaturally(b.getLocation(), Util.createItem(Material.MOB_SPAWNER, 1, s.getType().getTypeId(), "§eMob Spawner", "§cType:", "§7§o" + s.getName(), "§cLevel:", "§7§o" + s.getLevel()));
        }
        else {
            b.getWorld().dropItemNaturally(b.getLocation(), Util.createItem(Material.MOB_SPAWNER, 1, sp.getSpawnedType().getTypeId(), "§eMob Spawner", "§cType:", "§7§o" + Util.getName(sp.getSpawnedType()), "§cLevel:", "§7§o0"));
        }
    }
    
    @EventHandler
    public void interact(final PlayerInteractEvent e) {
        if (e.getClickedBlock() == null || !e.getClickedBlock().getType().equals((Object)Material.MOB_SPAWNER) || !e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        final Player p = e.getPlayer();
        if (p.getItemInHand().getType().isBlock() && !p.getItemInHand().getType().equals((Object)Material.AIR)) {
            return;
        }
        final Block b = e.getClickedBlock();
        if (!(b.getState() instanceof CreatureSpawner)) {
            return;
        }
        final CreatureSpawner sp = (CreatureSpawner)b.getState();
        Spawner s = this.getSpawner(sp);
        if (s == null) {
            this.spawners.add(s = new Spawner(sp.getSpawnedType(), 0, b.getLocation()));
        }
        final Inventory inv = this.getServer().createInventory((InventoryHolder)null, 27, "§7Spawner Tiers");
        for (int i = 0; i < 27; ++i) {
            inv.setItem(i, Util.createItem(Material.STAINED_GLASS_PANE, 1, 15, " ", new String[0]));
        }
        inv.setItem(4, Util.createItem(Material.MOB_SPAWNER, "§b§lSpawner Upgrade Menu", "§eType: §a" + s.getName(), "§eLevel: §a" + s.getLevel(), "§eLocation: §a" + this.getLoc(s.getLoc())));
        inv.setItem(22, Util.createItem(Material.MOB_SPAWNER, "§b§lSpawner Upgrade Menu", "§eType: §a" + s.getName(), "§eLevel: §a" + s.getLevel(), "§eLocation: §a" + this.getLoc(s.getLoc())));
        inv.setItem(11, Util.createItem(Material.STAINED_GLASS_PANE, 1, 5, "§aUpgrade to Tier §7Level 1", "", "§8» §aSpawn Rate §75.0 - 10.0s", "§8» §aPrice §730 XP (Levels)", "§8» §aSpawner Type §7" + Util.caps(s.getType().toString()), "", "§8» §aCurrent Level §7Tier " + s.getLevel()));
        inv.setItem(12, Util.createItem(Material.STAINED_GLASS_PANE, 1, 3, "§bUpgrade to Tier §7Level 2", "", "§8» §bSpawn Rate §74.0 - 9.0s", "§8» §bPrice §750 XP (Levels)", "§8» §bSpawner Type §7" + Util.caps(s.getType().toString()), "", "§8» §bCurrent Level §7Tier " + s.getLevel()));
        inv.setItem(13, Util.createItem(Material.STAINED_GLASS_PANE, 1, 14, "§cUpgrade to Tier §7Level 3", "", "§8» §cSpawn Rate §73.0 - 8.0s", "§8» §cPrice §775 XP (Levels)", "§8» §cSpawner Type §7" + Util.caps(s.getType().toString()), "", "§8» §cCurrent Level §7Tier " + s.getLevel()));
        inv.setItem(14, Util.createItem(Material.STAINED_GLASS_PANE, 1, 1, "§6Upgrade to Tier §7Level 4", "", "§8» §6Spawn Rate §72.0 - 7.0s", "§8» §6Price §7100 XP (Levels)", "§8» §6Spawner Type §7" + Util.caps(s.getType().toString()), "", "§8» §6Current Level §7Tier " + s.getLevel()));
        inv.setItem(15, Util.createItem(Material.STAINED_GLASS_PANE, 1, 4, "§eUpgrade to Tier §7Level 5", "", "§8» §eSpawn Rate §71.0 - 6.0s", "§8» §ePrice §7150 XP (Levels)", "§8» §eSpawner Type §7" + Util.caps(s.getType().toString()), "", "§8» §eCurrent Level §7Tier " + s.getLevel()));
        switch (s.getLevel()) {
            case 5: {
                Util.EnchantGlow.addGlow(inv.getItem(15));
            }
            case 4: {
                Util.EnchantGlow.addGlow(inv.getItem(14));
            }
            case 3: {
                Util.EnchantGlow.addGlow(inv.getItem(13));
            }
            case 2: {
                Util.EnchantGlow.addGlow(inv.getItem(12));
            }
            case 1: {
                Util.EnchantGlow.addGlow(inv.getItem(11));
                break;
            }
        }
        p.openInventory(inv);
    }
    
    @EventHandler
    public void invClick(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals((Object)Material.AIR)) {
            return;
        }
        if (e.getInventory().getTitle().equals("§7Spawner Tiers")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getType().equals((Object)Material.MOB_SPAWNER)) {
                return;
            }
            if (e.getRawSlot() == 11) {
                if (e.getCurrentItem().containsEnchantment(Util.EnchantGlow.getGlow())) {
                    p.sendMessage(String.valueOf(this.s) + "§7That spawner is already rank §b§l§n1§7!");
                }
                else if (p.getLevel() >= 30) {
                    p.setLevel(p.getLevel() - 30);
                    p.sendMessage(String.valueOf(this.s) + "§7Spawner ranked up to §b§l§n1§7!");
                    this.getSpawner(e.getInventory().getItem(4).getItemMeta().getLore().get(2).replace("§eLocation: §a", "")).setLevel(1);
                }
                else {
                    p.sendMessage(String.valueOf(this.s) + "§7Not enough exp to rankup");
                }
            }
            else if (e.getRawSlot() == 12) {
                if (e.getCurrentItem().containsEnchantment(Util.EnchantGlow.getGlow())) {
                    p.sendMessage(String.valueOf(this.s) + "§7That spawner is already rank §b§l§n2§7!");
                }
                else if (!e.getInventory().getItem(11).containsEnchantment(Util.EnchantGlow.getGlow())) {
                    p.sendMessage(String.valueOf(this.s) + "§7Spawner is not rank §b§l§n1§7 yet");
                }
                else if (p.getLevel() >= 50) {
                    p.setLevel(p.getLevel() - 50);
                    p.sendMessage(String.valueOf(this.s) + "§7Spawner ranked up to §b§l§n2§7!");
                    this.getSpawner(e.getInventory().getItem(4).getItemMeta().getLore().get(2).replace("§eLocation: §a", "")).setLevel(2);
                }
                else {
                    p.sendMessage(String.valueOf(this.s) + "§7Not enough exp to rankup");
                }
            }
            else if (e.getRawSlot() == 13) {
                if (e.getCurrentItem().containsEnchantment(Util.EnchantGlow.getGlow())) {
                    p.sendMessage(String.valueOf(this.s) + "§7That spawner is already rank §b§l§n3§7!");
                }
                else if (!e.getInventory().getItem(12).containsEnchantment(Util.EnchantGlow.getGlow())) {
                    p.sendMessage(String.valueOf(this.s) + "§7Spawner is not rank §b§l§n2§7 yet");
                }
                else if (p.getLevel() >= 75) {
                    p.setLevel(p.getLevel() - 75);
                    p.sendMessage(String.valueOf(this.s) + "§7Spawner ranked up to §b§l§n3§7!");
                    this.getSpawner(e.getInventory().getItem(4).getItemMeta().getLore().get(2).replace("§eLocation: §a", "")).setLevel(3);
                }
                else {
                    p.sendMessage(String.valueOf(this.s) + "§7Not enough exp to rankup");
                }
            }
            else if (e.getRawSlot() == 14) {
                if (e.getCurrentItem().containsEnchantment(Util.EnchantGlow.getGlow())) {
                    p.sendMessage(String.valueOf(this.s) + "§7That spawner is already rank §b§l§n4§7!");
                }
                else if (!e.getInventory().getItem(13).containsEnchantment(Util.EnchantGlow.getGlow())) {
                    p.sendMessage(String.valueOf(this.s) + "§7Spawner is not rank §b§l§n3§7 yet");
                }
                else if (p.getLevel() >= 100) {
                    p.setLevel(p.getLevel() - 100);
                    p.sendMessage(String.valueOf(this.s) + "§7Spawner ranked up to §b§l§n4§7!");
                    this.getSpawner(e.getInventory().getItem(4).getItemMeta().getLore().get(2).replace("§eLocation: §a", "")).setLevel(4);
                }
                else {
                    p.sendMessage(String.valueOf(this.s) + "§7Not enough exp to rankup");
                }
            }
            else if (e.getRawSlot() == 15) {
                if (e.getCurrentItem().containsEnchantment(Util.EnchantGlow.getGlow())) {
                    p.sendMessage(String.valueOf(this.s) + "§7That spawner is already rank §b§l§n5§7!");
                }
                else if (!e.getInventory().getItem(14).containsEnchantment(Util.EnchantGlow.getGlow())) {
                    p.sendMessage(String.valueOf(this.s) + "§7Spawner is not rank §b§l§n4§7 yet");
                }
                else if (p.getLevel() >= 150) {
                    p.setLevel(p.getLevel() - 150);
                    p.sendMessage(String.valueOf(this.s) + "§7Spawner ranked up to §b§l§n5§7!");
                    this.getSpawner(e.getInventory().getItem(4).getItemMeta().getLore().get(2).replace("§eLocation: §a", "")).setLevel(5);
                }
                else {
                    p.sendMessage(String.valueOf(this.s) + "§7Not enough exp to rankup");
                }
            }
            p.closeInventory();
        }
    }
}
