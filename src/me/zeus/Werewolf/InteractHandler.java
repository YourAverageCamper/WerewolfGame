

package me.zeus.Werewolf;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InteractHandler implements Listener
{
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onClick(PlayerInteractEvent e)
    {
        if (!e.getPlayer().isSneaking())
            return;
        
        final Player p = e.getPlayer();
        Block clicked = e.getClickedBlock();
        
        if (clicked != null && !clicked.getType().equals(Material.IRON_BLOCK))
            return;
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        
        // check block locations FACING NORTH
        if (clicked.getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getType().equals(Material.RED_ROSE)
                && clicked.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getType().equals(Material.RED_ROSE))
        {
            if (clicked.getLocation().subtract(0, 1, 0) == null)
                return;
            
            Block below = clicked.getLocation().subtract(0, 1, 0).getBlock();
            
            if ((below.getTypeId() == 155) && (below.getRelative(BlockFace.EAST).getTypeId() == 155) && (below.getRelative(BlockFace.WEST).getTypeId() == 155))
            {
                if (below.getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getType().equals(Material.OBSIDIAN)
                        && below.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getType().equals(Material.OBSIDIAN))
                {
                    if (below.getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getType().equals(Material.GRASS)
                            && below.getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getType().equals(Material.GRASS))
                    {
                        if (below.getRelative(BlockFace.SOUTH).getType().equals(Material.OBSIDIAN))
                        {
                            if (below.getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getType().equals(Material.OBSIDIAN)
                                    && below.getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getType().equals(Material.OBSIDIAN))
                            {
                                if ((below.getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getTypeId() == 155)
                                        && (below.getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getTypeId() == 155))
                                {
                                    if ((below.getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getType().equals(Material.GRASS))
                                            && (below.getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getType().equals(Material.GRASS)))
                                    {
                                        if ((below.getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.EAST).getRelative(BlockFace.UP).getType()
                                                .equals(Material.RED_ROSE))
                                                && (below.getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.WEST).getRelative(BlockFace.UP)
                                                        .getType().equals(Material.RED_ROSE)))
                                        {
                                            // STRUCTURE IS ALL FINE
                                            
                                            // if they are already a wolf, stop
                                            if (Werewolf.getInstance().getWolves().containsKey(p.getName()))
                                            {
                                                p.sendMessage("§8[§4§oWerewolf§r§8] §cYou are already a werewolf!");
                                                return;
                                            }
                                            
                                            // if they don't have enough stuff, tell them
                                            if (!(p.getInventory().containsAtLeast(new ItemStack(Material.EMERALD), 1) && p.getInventory().containsAtLeast(new ItemStack(Material.BONE), 10) && p
                                                    .getInventory().containsAtLeast(new ItemStack(Material.PORK), 5)))
                                            {
                                                p.sendMessage("§8[§4§oWerewolf§r§8] §cYou require more energy to transform...");
                                                return;
                                            }
                                            
                                            final int time = Werewolf.getInstance().getConfig().getInt("transform-time");
                                            
                                            p.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.BONE, 10), new ItemStack(Material.EMERALD, 1), new ItemStack(Material.PORK, 5) });
                                            p.updateInventory();
                                            p.playSound(p.getLocation(), Sound.LAVA_POP, 1.0F, 1.0F);
                                            p.sendMessage("§8[§4§oWerewolf§r§8] §7You begin to feel dizzy.");
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 3));
                                            
                                            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Werewolf.getInstance(), new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    p.playSound(p.getLocation(), Sound.WOLF_GROWL, 0.2F, 5.0F);
                                                    p.sendMessage("§8[§4§oWerewolf§r§8] §aYour transformation has completed...");
                                                    ZWolf wolf = new ZWolf(p.getName());
                                                    Werewolf.getInstance().getWolves().put(p.getName(), wolf);
                                                    p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, Werewolf.getInstance().getConfig().getInt("potion-levels.speed")));
                                                }
                                            }, time);
                                            
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
