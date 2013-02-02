package com.ne0nx3r0.rareitemhunter;

import com.ne0nx3r0.rareitemhunter.bosses.BossManager;
import com.ne0nx3r0.rareitemhunter.bosses.RandomlyGenerateBossTask;
import com.ne0nx3r0.rareitemhunter.commands.RareItemHunterCommandExecutor;
import com.ne0nx3r0.rareitemhunter.listeners.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.bukkit.plugin.java.JavaPlugin;

public class RareItemHunter extends JavaPlugin
{
    public BossManager bossManager;
    public RecipeManager recipeManager;
    
    @Override
    public void onEnable()
    {
        getDataFolder().mkdirs();
        
        File configFile = new File(getDataFolder(),"config.yml");
        
        if(!configFile.exists())
        {
            copy(getResource("config.yml"), configFile);
        }
        
        this.bossManager = new BossManager(this);
        
        getServer().getPluginManager().registerEvents(new RareItemHunterEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new RareItemHunterPlayerListener(this), this);
        
        getCommand("ri").setExecutor(new RareItemHunterCommandExecutor(this));
        
// Random boss generation
        int iTimer = 20 * this.getConfig().getInt("timeBetweenChancesToGenerateBossEgg",60 * 60 * 20);
        int iMaxChance = this.getConfig().getInt("maxChanceToGenerateBossEgg",20);
        int iExpiration = 20 * this.getConfig().getInt("bossEggExpiration",15 * 60 * 20);
        
        this.getServer().getScheduler().scheduleSyncRepeatingTask(
                this,
                new RandomlyGenerateBossTask(this,iMaxChance,iTimer,iExpiration), 
                iTimer, 
                iTimer);
        
        this.recipeManager = new RecipeManager(this);
    }
    
// Public helper methods
    
    public void copy(InputStream in, File file)
    {
        try
        {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0)
            {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
