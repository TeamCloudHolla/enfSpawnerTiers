package com.enforcedmc.spawnertiers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Util
{
  public static ItemStack createHead(String owner, String name, String... lore)
  {
    ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
    SkullMeta meta = (SkullMeta)item.getItemMeta();
    meta.setOwner(owner);
    meta.setDisplayName(name);
    List<String> l = new ArrayList();
    String[] arrayOfString;
    int j = (arrayOfString = lore).length;
    for (int i = 0; i < j; i++)
    {
      String s = arrayOfString[i];
      l.add(s);
    }
    meta.setLore(l);
    item.setItemMeta(meta);
    return item;
  }
  
  public static ItemStack createItem(Material mat, int amt, int durability, String name, List<String> lore)
  {
    ItemStack item = new ItemStack(mat, amt);
    ItemMeta meta = item.getItemMeta();
    meta.setDisplayName(name);
    meta.setLore(lore);
    if (durability != 0) {
      item.setDurability((short)durability);
    }
    item.setItemMeta(meta);
    return item;
  }
  
  public static ItemStack createItem(Material mat, int amt, String name, List<String> lore)
  {
    return createItem(mat, amt, 0, name, lore);
  }
  
  public static ItemStack createItem(Material mat, int amt, int durability, String name, String... lore)
  {
    List<String> l = new ArrayList();
    String[] arrayOfString;
    int j = (arrayOfString = lore).length;
    for (int i = 0; i < j; i++)
    {
      String s = arrayOfString[i];
      l.add(s);
    }
    return createItem(mat, amt, durability, name, l);
  }
  
  public static ItemStack createItem(Material mat, int amt, String name, String... lore)
  {
    return createItem(mat, amt, 0, name, lore);
  }
  
  public static ItemStack createItem(Material mat, String name, String... lore)
  {
    return createItem(mat, 1, 0, name, lore);
  }
  
  public static void remove(final Inventory inv, final Material mat, final int amt) {
      int amount = 0;
      ItemStack[] contents;
      for (int length = (contents = inv.getContents()).length, j = 0; j < length; ++j) {
          final ItemStack i = contents[j];
          if (i != null && i.getType() == mat) {
              amount += i.getAmount();
          }
      }
      inv.remove(mat);
      if (amount > amt) {
          inv.addItem(new ItemStack[] { new ItemStack(mat, amount - amt) });
      }
  }
  
  public static String caps(String string)
  {
    String[] list = string.split("_");
    String s = "";
    String[] arrayOfString1;
    int j = (arrayOfString1 = list).length;
    for (int i = 0; i < j; i++)
    {
      String st = arrayOfString1[i];
      s = s + st.substring(0, 1).toUpperCase() + st.substring(1).toLowerCase();
    }
    return s;
  }
  
  public static boolean isInt(String s)
  {
    try
    {
      Integer.parseInt(s);
      return true;
    }
    catch (NumberFormatException e) {}
    return false;
  }
  
  public static String color(String s)
  {
    return ChatColor.translateAlternateColorCodes('&', s);
  }
  
  public static List<String> color(List<String> list)
  {
    List<String> colored = new ArrayList();
    for (String s : list) {
      colored.add(color(s));
    }
    return colored;
  }
  
  public static Material getMaterial(String s)
  {
    if (Material.getMaterial(s) != null) {
      return Material.getMaterial(s);
    }
    if ((isInt(s)) && (Material.getMaterial(Integer.parseInt(s)) != null)) {
      return Material.getMaterial(Integer.parseInt(s));
    }
    if (Material.matchMaterial(s) != null) {
      return Material.matchMaterial(s);
    }
    if (Material.valueOf(s.toUpperCase()) != null) {
      return Material.valueOf(s.toUpperCase());
    }
    return null;
  }
  
  public static List<Integer> getBorder(int size)
  {
    switch (size)
    {
    case 54: 
      return Arrays.asList(new Integer[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(49), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53) });
    case 45: 
      return Arrays.asList(new Integer[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44) });
    case 36: 
      return Arrays.asList(new Integer[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30), Integer.valueOf(31), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35) });
    case 27: 
      return Arrays.asList(new Integer[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26) });
    }
    return null;
  }
  
  public static int getTotalExperience(Player player)
  {
    int exp = Math.round(getExpAtLevel(player.getLevel()) * player.getExp());
    int currentLevel = player.getLevel();
    while (currentLevel > 0)
    {
      currentLevel--;
      exp += getExpAtLevel(currentLevel);
    }
    if (exp < 0) {
      exp = Integer.MAX_VALUE;
    }
    return exp;
  }
  
  public static int getExpAtLevel(int level)
  {
    if (level > 29) {
      return 62 + (level - 30) * 7;
    }
    if (level > 15) {
      return 17 + (level - 15) * 3;
    }
    return 17;
  }
  
  public static boolean isArmour(Material m)
  {
    return Enchantment.PROTECTION_ENVIRONMENTAL.canEnchantItem(new ItemStack(m));
  }
  
  public static boolean isDiamond(Material m)
  {
    return m.toString().contains("DIAMOND");
  }
  
  public static boolean isGold(Material m)
  {
    return m.toString().contains("GOLD");
  }
  
  public static boolean isIron(Material m)
  {
    return m.toString().contains("IRON");
  }
  
  public static boolean isLeather(Material m)
  {
    return m.toString().contains("LEATHER");
  }
  
  public static boolean isChain(Material m)
  {
    return m.toString().contains("CHAIN");
  }
  
  public static boolean isSword(Material m)
  {
    return m.toString().contains("SWORD");
  }
  
  public static boolean isAxe(Material m)
  {
    return m.toString().endsWith("_AXE");
  }
  
  public static boolean isPickaxe(Material m)
  {
    return m.toString().contains("PICKAXE");
  }
  
  public static boolean isWeapon(Material m)
  {
    return Enchantment.DAMAGE_ALL.canEnchantItem(new ItemStack(m));
  }
  
  public static boolean isTool(Material m)
  {
    return Enchantment.DIG_SPEED.canEnchantItem(new ItemStack(m));
  }
  
  public static String getName(EntityType e)
  {
    if (e.equals(EntityType.PIG_ZOMBIE)) {
      return "Zombie Pigman";
    }
    if (!e.toString().contains("_")) {
      return e.toString().substring(0, 1).toUpperCase() + e.toString().substring(1).toLowerCase();
    }
    String[] split = e.toString().split("_");
    String name = "";
    String[] arrayOfString1;
    int j = (arrayOfString1 = split).length;
    for (int i = 0; i < j; i++)
    {
      String s = arrayOfString1[i];
      name = name + s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase() + " ";
    }
    return name.trim();
  }
  
  public static EntityType getEntity(String e)
  {
    if (e.equalsIgnoreCase("Zombie Pigman")) {
      return EntityType.PIG_ZOMBIE;
    }
    e = e.replaceAll(" ", "_");
    if (!e.contains("_")) {
      return EntityType.valueOf(e.toUpperCase());
    }
    String[] split = e.toString().split(" ");
    String name = "";
    String[] arrayOfString1;
    int j = (arrayOfString1 = split).length;
    for (int i = 0; i < j; i++)
    {
      String s = arrayOfString1[i];
      name = name + s.toUpperCase() + "_";
    }
    return EntityType.valueOf(name.substring(0, name.length() - 1));
  }
  
  public static enum Pane
  {
    WHITE(0),  ORANGE(1),  MAGENTA(2),  LIGHT_BLUE(3),  YELLOW(4),  LIME(5),  PINK(6),  GRAY(7),  LIGHT_GRAY(8),  CYAN(9),  PURPLE(10),  BLUE(11),  BROWN(12),  GREEN(13),  RED(14),  BLACK(15);
    
    private int value;
    
    private Pane(int value)
    {
      this.value = value;
    }
    
    public int value()
    {
      return this.value;
    }
  }
  
  public static class EnchantGlow
    extends EnchantmentWrapper
  {
    private static Enchantment glow = null;
    private String name;
    
    public static void addGlow(ItemStack itemstack)
    {
      itemstack.addEnchantment(getGlow(), 1);
    }
    
    public static Enchantment getGlow()
    {
      if (glow != null) {
        return glow;
      }
      Field field = null;
      try
      {
        field = Enchantment.class.getDeclaredField("acceptingNew");
      }
      catch (NoSuchFieldException|SecurityException e)
      {
        e.printStackTrace();
        return glow;
      }
      field.setAccessible(true);
      try
      {
        field.set(null, Boolean.valueOf(true));
      }
      catch (IllegalArgumentException|IllegalAccessException e)
      {
        e.printStackTrace();
      }
      try
      {
        glow = new EnchantGlow(Enchantment.values().length + 100);
      }
      catch (Exception e)
      {
        glow = Enchantment.getByName("Glow");
      }
      if (Enchantment.getByName("Glow") == null) {
        Enchantment.registerEnchantment(glow);
      }
      return glow;
    }
    
    public String getName()
    {
      return this.name;
    }
    
    public Enchantment getEnchantment()
    {
      return Enchantment.getByName("Glow");
    }
    
    public int getMaxLevel()
    {
      return 1;
    }
    
    public int getStartLevel()
    {
      return 1;
    }
    
    public EnchantmentTarget getItemTarget()
    {
      return EnchantmentTarget.ALL;
    }
    
    public boolean canEnchantItem(ItemStack item)
    {
      return true;
    }
    
    public boolean conflictsWith(Enchantment other)
    {
      return false;
    }
    
    public EnchantGlow(final int i) {
        super(i);
        this.name = "Glow";
    }
  }
}
