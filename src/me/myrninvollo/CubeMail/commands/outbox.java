package me.myrninvollo.CubeMail.commands;

import java.sql.Connection;
import java.sql.ResultSet;

import me.myrninvollo.CubeMail.DBConnection;
import me.myrninvollo.CubeMail.Main;

import org.glydar.paraglydar.ParaGlydar;
import org.glydar.paraglydar.command.*;
import org.glydar.paraglydar.models.Player;
import org.glydar.paraglydar.permissions.Permission.PermissionDefault;

public class outbox implements CommandSet {   

  public Main plugin;
  public outbox(Main plugin)  {
    this.plugin = plugin;
  }

  DBConnection service = DBConnection.getInstance();
  
  @Command(name = "outbox", usage = "- Look at your outbox", permissionDefault = PermissionDefault.TRUE)
	public CommandOutcome execute(CommandSender sender, String[] args) {     
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }

    ResultSet rs;
    java.sql.Statement stmt;
    Connection con;
    try {
      con = service.getConnection();
      stmt = con.createStatement();
      String ownernick = player.getName().toLowerCase(); 

      rs = stmt.executeQuery("SELECT * FROM CM_Mail WHERE sender='" + ownernick + "'");
      
      sender.sendMessage("- ID ----- TO ----------- DATE ------");
      while(rs.next()){
        String isread = rs.getString("read");          
        if (isread.contains("NO")) {
          sender.sendMessage("  [" + rs.getInt("id") +"]"+"         "+rs.getString("target")+"          "+rs.getString("date"));            
        } else {
          sender.sendMessage("  [" +rs.getInt("id") +"]"+"         "+rs.getString("target")+"          "+rs.getString("date"));
        }
      }
      sender.sendMessage("(deleted/expired messages will not be displayed)");
      rs.close();
    } catch(Exception e) {
      ParaGlydar.getLogger().info("[CubeMail] "+"Error: "+e);        
      if (e.toString().contains("locked")) {
        sender.sendMessage("[CubeMail] The database is busy. Please wait a moment before trying again...");
      } else {
        player.sendMessage("[CubeMail] Error: "+e);
      }
    }
    
    return CommandOutcome.SUCCESS;
  }
}