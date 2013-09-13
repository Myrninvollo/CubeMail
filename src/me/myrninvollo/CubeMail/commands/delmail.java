package me.myrninvollo.CubeMail.commands;

import java.sql.Connection;
import java.sql.ResultSet;

import me.myrninvollo.CubeMail.DBConnection;
import me.myrninvollo.CubeMail.Main;

import org.glydar.paraglydar.command.*;
import org.glydar.paraglydar.models.Player;
import org.glydar.paraglydar.permissions.Permission.PermissionDefault;

public class delmail implements CommandSet {   

  public Main plugin;
  public delmail(Main plugin)  {
    this.plugin = plugin;
  }
  
  DBConnection service = DBConnection.getInstance();
  
  @Command(name = "delmail", usage = "<ID>", permissionDefault = PermissionDefault.TRUE)
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
      String Playername = player.getName().toLowerCase();

      rs = stmt.executeQuery("SELECT * FROM CM_Mail WHERE id='"+args[0]+"' AND target='"+Playername+"'");
      
     

      if (!rs.getString("target").equalsIgnoreCase(Playername)) {
        sender.sendMessage("[CubeMail] This is not your message to delete or it does not exist. ");
      } else {
        stmt.executeUpdate("DELETE FROM CM_Mail WHERE id='"+args[0]+"' AND target='"+Playername+"'");
        sender.sendMessage("[CubeMail] Message Deleted.");
      } 
      rs.close();
      stmt.close();
        
    } catch(Exception e) {         
      if (e.toString().contains("ResultSet closed")) {
        sender.sendMessage("[CubeMail] This is not your message to read or it does not exist.");
      } else if (e.toString().contains("java.lang.ArrayIndexOutOfBoundsException")) {
        sender.sendMessage("/readmail <id>");
      } else {
        player.sendMessage("[CubeMail] Error: "+e);
      }
    }
    return CommandOutcome.SUCCESS;

      

}
}