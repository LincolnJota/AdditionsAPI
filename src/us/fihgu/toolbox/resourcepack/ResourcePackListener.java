package us.fihgu.toolbox.resourcepack;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import org.bukkit.plugin.java.JavaPlugin;
import com.chrismin13.moreminecraft.MoreMinecraft;
import com.chrismin13.moreminecraft.utils.LangFileUtils;

public class ResourcePackListener implements Listener {
	public void register(JavaPlugin plugin) {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		final Player player = event.getPlayer();
		Bukkit.getServer().getScheduler().runTask(MoreMinecraft.getInstance(), () -> {
			if (ResourcePackManager.hasResource()) {
				String link;
				if (event.getAddress().getHostAddress().equals("127.0.0.1")) {
					link = "http://" + ResourcePackServer.localhost + ":" + ResourcePackServer.port
							+ ResourcePackServer.path;
				} else {
					link = "http://" + ResourcePackServer.host + ":" + ResourcePackServer.port
							+ ResourcePackServer.path;
				}
				player.setResourcePack(link);
			}
		});
	}

	@EventHandler
	public void onResourcestatusChange(PlayerResourcePackStatusEvent event) {
		if (ResourcePackManager.getForceResourcePack()) {
			Status status = event.getStatus();
			switch (status) {
			case DECLINED:
			case FAILED_DOWNLOAD:
				final Player player = event.getPlayer();
				Bukkit.getServer().getScheduler().runTask(MoreMinecraft.getInstance(),
						() -> player.kickPlayer(LangFileUtils.get("resource_pack_kick")));
				break;
			case ACCEPTED:
			case SUCCESSFULLY_LOADED:
			default:
				break;
			}
		}
	}
}
