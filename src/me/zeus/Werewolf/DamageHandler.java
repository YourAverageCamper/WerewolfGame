

package me.zeus.Werewolf;


import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageHandler implements Listener
{
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e)
    {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player)
        {
            final Player damaged = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            
            if (Werewolf.getInstance().getWolves().containsKey(damager.getName()))
            {
                if (Werewolf.getInstance().isNight())
                {
                    if (damager.getItemInHand() != null && !damager.getItemInHand().getType().equals(Material.AIR))
                    {
                        e.setCancelled(true);
                        damager.sendMessage("§8[§4§oWerewolf§r§8] §cYou can not attack with an item in your hand!");
                        return;
                    }
                    
                    damaged.damage(e.getDamage() * 2);
                    
                    int chance = new Random().nextInt(100);
                    if (chance == 100)
                    {
                        if (Werewolf.getInstance().getWolves().containsKey(damaged.getName()))
                        {
                            return;
                        }
                        // init process
                        final int time = Werewolf.getInstance().getConfig().getInt("transform-time");
                        damaged.playSound(damaged.getLocation(), Sound.LAVA_POP, 1.0F, 1.0F);
                        damaged.sendMessage("§8[§4§oWerewolf§r§8] §7You begin to feel dizzy.");
                        damaged.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 3));
                        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Werewolf.getInstance(), new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                damaged.playSound(damaged.getLocation(), Sound.WOLF_GROWL, 0.2F, 5.0F);
                                damaged.sendMessage("§8[§4§oWerewolf§r§8] §aYour transformation has completed...");
                                ZWolf wolf = new ZWolf(damaged.getName());
                                Werewolf.getInstance().getWolves().put(damaged.getName(), wolf);
                                damaged.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, Werewolf.getInstance().getConfig().getInt("potion-levels.speed")));
                            }
                        }, time);
                        
                    }
                }
            }
            
            if (damager.getItemInHand() != null && damager.getItemInHand().getType().equals(Material.IRON_SWORD))
            {
                damaged.damage(e.getDamage());
                damaged.damage(e.getDamage());
            }
        }
    }
}
