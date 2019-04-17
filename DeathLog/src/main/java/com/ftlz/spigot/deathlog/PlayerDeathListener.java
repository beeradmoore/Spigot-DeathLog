package com.ftlz.spigot.deathlog;

import java.io.FileOutputStream;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.json.simple.JSONObject;
 
public class PlayerDeathListener implements Listener
{
    App _app;
    public PlayerDeathListener(App app)
    {
        _app = app;        
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void onEntityDeath(PlayerDeathEvent event)
    {
        Player playerEntity = event.getEntity();
        Location deathLocation = event.getEntity().getLocation();
        
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", playerEntity.getName());
        jsonObject.put("world", deathLocation.getWorld().getName());
        jsonObject.put("block_x", deathLocation.getBlockX());
        jsonObject.put("block_y", deathLocation.getBlockY());
        jsonObject.put("block_z", deathLocation.getBlockZ());
        jsonObject.put("message", event.getDeathMessage());
        jsonObject.put("time", (int)(System.currentTimeMillis() / 1000L));

        AppendToDeathlog(jsonObject.toString());
    }

    public void AppendToDeathlog(String data)
    {
        FileOutputStream fileOutputStream = null;
        try
        {
            fileOutputStream = new FileOutputStream("DeathLog.jsonl", true);

            java.nio.channels.FileLock lock = fileOutputStream.getChannel().lock();
            try
            {
                fileOutputStream.write((data + "\n").getBytes("utf-8"));
            }
            catch (Exception err)
            {
                _app.getLogger().log(Level.WARNING, err.getMessage());
            }
            finally
            {
                lock.release();
                fileOutputStream.close();
                fileOutputStream = null;
            }
        }
        catch (Exception err)
        {
            _app.getLogger().log(Level.WARNING, err.getMessage());
        }
        finally
        {
            if (fileOutputStream != null)
            {
                try
                {
                    fileOutputStream.close();
                }
                catch (Exception err)
                {
                    _app.getLogger().log(Level.WARNING, err.getMessage());
                }
            }
        }
    } 
}