

package me.zeus.Werewolf;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

public class InventoryHandler implements Listener
{
    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        Player clicker = (Player) e.getWhoClicked();
        if (e.getSlotType().equals(SlotType.ARMOR) && e.getInventory().getItem(e.getSlot()) == null)
        {
            if (Werewolf.getInstance().getWolves().containsKey(clicker.getName()))
            {
                if (Werewolf.getInstance().isNight())
                {
                    clicker.sendMessage("§8[§4§oWerewolf§r§8] §cYou can not put on armor at night!");
                    e.setCancelled(true);
                }
            }
        }
    }
}
