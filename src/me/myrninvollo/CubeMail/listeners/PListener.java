package me.myrninvollo.CubeMail.listeners;

import java.sql.Connection;
import java.sql.ResultSet;

import org.glydar.paraglydar.ParaGlydar;
import org.glydar.paraglydar.event.EventHandler;
import org.glydar.paraglydar.event.Listener;
import org.glydar.paraglydar.event.events.PlayerJoinEvent;
import org.glydar.paraglydar.models.Player;
import org.glydar.paraglydar.scheduler.GlydarRunnable;

import me.myrninvollo.CubeMail.DBConnection;
import me.myrninvollo.CubeMail.Main;



public class PListener implements Listener {

  public Main plugin;
  public PListener(Main plugin) {    
    this.plugin = plugin;    
    
  }
  
  DBConnection service = DBConnection.getInstance();

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
   
      final Player player = event.getPlayer();
      String targetnick = player.getName().toLowerCase();
      Connection con;
      java.sql.Statement stmt;
      ResultSet rs;
      try {        
        con = service.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery("SELECT COUNT(target) AS inboxtotal FROM CM_Mail WHERE target='"+targetnick+"' AND read='NO'");
        final int id = rs.getInt("inboxtotal");
        
        player.sendMessage("[CUbeMail] You have "  + id +" new messages");
          
          
        
        rs.close();
      } catch(Exception e) {
        player.sendMessage("[CubeMail] Error: "+e);
      }
    
  }
}