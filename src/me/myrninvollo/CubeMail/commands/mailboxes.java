package me.myrninvollo.CubeMail.commands;

import java.sql.Connection;
import java.sql.ResultSet;

import me.myrninvollo.CubeMail.DBConnection;
import me.myrninvollo.CubeMail.Main;

import org.glydar.paraglydar.ParaGlydar;
import org.glydar.paraglydar.command.*;
import org.glydar.paraglydar.models.Player;
import org.glydar.paraglydar.permissions.Permission.PermissionDefault;

public class mailboxes implements CommandSet {   

  public Main plugin;
  public mailboxes(Main plugin)  {
    this.plugin = plugin;
  }
  
  DBConnection service = DBConnection.getInstance();

  @Command(name = "mailboxes", usage = " List active mailboxes",permissionDefault = PermissionDefault.ADMIN)
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
        rs = stmt.executeQuery("SELECT DISTINCT target FROM CM_Mail");        
        sender.sendMessage("Active Inboxes: ");
        while(rs.next()){
          sender.sendMessage(" Mailbox: "+ rs.getString("target"));
        }
        rs.close();
      } catch(Exception e) {
        ParaGlydar.getLogger().info("[CubeMail] Error: "+e);        
        if (e.toString().contains("locked")) {
          sender.sendMessage("[CubeMail] The database is busy. Please wait a moment before trying again...");
        } else {
          player.sendMessage("[CubeMail] Error: "+e);
        }
      }

      return CommandOutcome.SUCCESS;
  }

}