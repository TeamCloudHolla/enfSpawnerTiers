package com.enforcedmc.spawnertiers;

import org.bukkit.entity.*;
import org.bukkit.*;

public class Spawner
{
    private EntityType type;
    private int level;
    private Location loc;
    
    public Spawner(final EntityType type, final int level, final Location loc) {
        this.type = type;
        this.level = level;
        this.loc = loc;
    }
    
    public EntityType getType() {
        return this.type;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public void setLevel(final int lvl) {
        this.level = lvl;
    }
    
    public Location getLoc() {
        return this.loc;
    }
    
    public String getName() {
        return Util.getName(this.type);
    }
}
