package fr.k0bus.creativemanager.event;

import fr.k0bus.creativemanager.log.BlockLog;
import fr.k0bus.creativemanager.settings.Protections;
import fr.k0bus.k0buslib.utils.Messages;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import fr.k0bus.creativemanager.CreativeManager;

public class PlayerBreak implements Listener{

	CreativeManager plugin;

	public PlayerBreak(CreativeManager instance)
	{
		plugin = instance;
	}

	@EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent e)
    {
		Player p = e.getPlayer();
		if(p.getGameMode() == GameMode.CREATIVE)
		{
			if(plugin.getSettings().getProtection(Protections.BUILD) && !p.hasPermission("creativemanager.bypass.build"))
			{
				Messages.sendMessage(plugin.getMessageManager(), p, "permission.build");
				e.setCancelled(true);
			}
		}
		else
		{
			if(!p.hasPermission("creativemanager.bypass.log") && plugin.getSettings().getProtection(Protections.LOOT))
			{
				BlockLog blockLog = plugin.getDataManager().getBlockFrom(e.getBlock().getLocation());
				if(blockLog != null)
				{
					if(blockLog.isCreative())
					{
						e.setDropItems(false);
						plugin.getDataManager().removeBlock(blockLog.getLocation());
					}
				}
			}
		}

    }
}
