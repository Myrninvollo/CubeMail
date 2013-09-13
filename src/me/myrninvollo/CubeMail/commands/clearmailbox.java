package me.myrninvollo.CubeMail.commands;

import java.sql.Connection;

import org.glydar.paraglydar.ParaGlydar;
import org.glydar.paraglydar.command.Command;
import org.glydar.paraglydar.command.CommandOutcome;
import org.glydar.paraglydar.command.CommandSender;
import org.glydar.paraglydar.command.CommandSet;
import org.glydar.paraglydar.models.Player;
import org.glydar.paraglydar.permissions.Permission.PermissionDefault;

import me.myrninvollo.CubeMail.DBConnection;
import me.myrninvollo.CubeMail.Main;

public class clearmailbox implements CommandSet {   

  public Main plugin;
  public clearmailbox(Main plugin)  {
    this.plugin = plugin;
  }

  DBConnection service = DBConnection.getInstance();
  
  @Command(name = "clearmailbox", usage = "<player> - Clear Players Mail Box", permissionDefault = PermissionDefault.ADMIN, maxArgs = 1)
	public CommandOutcome execute(CommandSender sender, String playerName) {
	  Player player = null;
	    if (sender instanceof Player) {
	      player = (Player) sender;
	    }
	   
    java.sql.Statement stmt;
    Connection con;
    try { 
    	 String Playername = player.getName();
      con = service.getConnection();
      stmt = con.createStatement();
      stmt.executeUpdate("DELETE FROM SM_Mail WHERE target='"+ Playername +"'");
      sender.sendMessage("[CubeMail] Mailbox Cleared.");
      return CommandOutcome.SUCCESS;
    } catch(Exception e) {
      ParaGlydar.getLogger().info("[SimpleMail] "+"Error: "+e);        
      if (e.toString().contains("locked")) {
        sender.sendMessage("[CubeMail] The database is busy. Please wait a moment before trying again...");
      } else {
        player.sendMessage("[CubeMail] Error: "+e);
      }
    }

    return CommandOutcome.SUCCESS;

  }

}