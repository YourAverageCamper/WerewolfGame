

package me.zeus.Werewolf;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Werewolf extends JavaPlugin
{
    private Map<String, ZWolf> wolfs;
    private static Werewolf instance;
    private boolean night;
    
    @Override
    public void onEnable()
    {
        night = false;
        instance = this;
        try
        {
            initializeData();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        
        File config = new File(getDataFolder() + "config.yml");
        if (!config.exists())
        {
            getConfig().addDefault("transform-time", 10 * 20);
            getConfig().addDefault("potion-levels.speed", 1);
            getConfig().addDefault("potion-levels.nightvision", 1);
            getConfig().addDefault("potion-levels.jump", 1);
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        
        getServer().getPluginManager().registerEvents(new InteractHandler(), this);
        getServer().getPluginManager().registerEvents(new DamageHandler(), this);
        getServer().getPluginManager().registerEvents(new InventoryHandler(), this);
        startTasks();
    }
    
    @Override
    public void onDisable()
    {
        for (ZWolf wolf : wolfs.values())
        {
            File f = new File(getDataFolder() + "/wolves/" + wolf.getName());
            try
            {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(wolf);
                oos.close();
                System.out.println("Saved data for " + wolf.getName());
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        instance = null;
    }
    
    private void initializeData() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        wolfs = new HashMap<String, ZWolf>();
        File f = new File(getDataFolder() + "");
        if (!f.exists())
        {
            f.mkdir();
        }
        File f2 = new File(getDataFolder() + "/wolves/");
        if (!f2.exists())
        {
            f2.mkdir();
        }
        File[] wolves = f2.listFiles();
        for (int i = 0; i < wolves.length; i++)
        {
            File aa = wolves[i];
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(aa));
            ZWolf wolf = (ZWolf) ois.readObject();
            wolfs.put(wolf.getName(), wolf);
            ois.close();
            System.out.println("Loaded wolf data for: " + wolf.getName());
        }
    }
    
    private void startTasks()
    {
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            @SuppressWarnings("deprecation")
            @Override
            public void run()
            {
                if (Bukkit.getWorld("world").getTime() > 12400)
                {
                    night = true;
                }
                else
                {
                    night = false;
                }
                
                for (ZWolf w : wolfs.values())
                {
                    Player p = getServer().getPlayer(w.getName());
                    if (night)
                    {
                        if (p != null)
                        {
                            p.playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 1);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, getConfig().getInt("potion-levels.speed")));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, getConfig().getInt("potion-levels.jump")));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, getConfig().getInt("potion-levels.nightvision")));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
                            
                            if (p.getInventory().getArmorContents().length > 0)
                            {
                                ItemStack[] armor = p.getInventory().getArmorContents();
                                for (int i = 0; i < armor.length; i++)
                                {
                                    ItemStack is = armor[i];
                                    p.getInventory().addItem(is);
                                    p.updateInventory();
                                }
                                p.getInventory().setHelmet(null);
                                p.getInventory().setChestplate(null);
                                p.getInventory().setLeggings(null);
                                p.getInventory().setBoots(null);
                            }
                        }
                    }
                    
                    else
                    {
                        if (p != null)
                        {
                            for (PotionEffect effect : p.getActivePotionEffects())
                            {
                                p.removePotionEffect(effect.getType());
                                System.out.println(p.getActivePotionEffects().toString());
                            }
                        }
                    }
                }
            }
        }, 0, 20L * 5);
    }
    
    public static Werewolf getInstance()
    {
        return instance;
    }
    
    public Map<String, ZWolf> getWolves()
    {
        return wolfs;
    }
    
    public boolean isNight()
    {
        return night;
    }
    
}
