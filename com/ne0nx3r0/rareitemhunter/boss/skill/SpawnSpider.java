package com.ne0nx3r0.rareitemhunter.boss.skill;

import com.ne0nx3r0.rareitemhunter.boss.Boss;
import com.ne0nx3r0.rareitemhunter.boss.BossSkill;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SpawnSpider extends BossSkill
{
    public SpawnSpider()
    {
        super("Spawn Spider");
    }
    
    @Override
    public boolean activateSkill(Boss boss,EntityDamageByEntityEvent e, Entity eAttacker, int level)
    {       
        Location l = e.getEntity().getLocation();
        
        World w = l.getWorld();
        
        for(int i=0;i<level;i++)
        {
            w.spawnEntity(l, EntityType.SPIDER);
        }
        
        return true;
    }
}