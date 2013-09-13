package me.myrninvollo.CubeMail.commands;

import java.sql.Connection;
import java.sql.ResultSet;

import me.myrninvollo.CubeMail.DBConnection;
import me.myrninvollo.CubeMail.Main;

import org.glydar.paraglydar.ParaGlydar;
import org.glydar.paraglydar.command.*;
import org.glydar.paraglydar.models.Player;
import org.glydar.paraglydar.permissions.Permission.PermissionDefault;

public class inbox implements CommandSet {   

  public Main plugin;
  public inbox(Main plugin)  {
    this.plugin = plugin;
  }

  DBConnection service = DBConnection.getInstance();
  
  @Command(name = "inbox", usage = "- Look at your inbox", permissionDefault = PermissionDefault.TRUE)
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
      String targetnick = player.getName().toLowerCase(); 

      rs = stmt.executeQuery("SELECT * FROM CM_Mail WHERE target='" + targetnick + "'");        
      sender.sendMessage("- ID ----- FROM ----------- DATE ------");
      while(rs.next()){
        String isread = rs.getString("read");          
        if (isread.contains("NO")) {
          sender.sendMessage("  [" +rs.getInt("id") +"]"+"         "+rs.getString("sender")+"          "+rs.getString("date"));            
        } else {
          sender.sendMessage("  [" +rs.getInt("id") +"]"+"         "+rs.getString("sender")+"          "+rs.getString("date"));
        }
      }
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