package me.myrninvollo.CubeMail.commands;

import java.sql.Connection;
import java.sql.ResultSet;

import me.myrninvollo.CubeMail.DBConnection;
import me.myrninvollo.CubeMail.Main;

import org.glydar.paraglydar.command.*;
import org.glydar.paraglydar.permissions.Permission.PermissionDefault;
import org.glydar.paraglydar.models.Player;

public class readmail implements CommandSet {   

  public Main plugin;
  public readmail(Main plugin)  {
    this.plugin = plugin;
  }
  
  DBConnection service = DBConnection.getInstance();

  @Command(name = "readmail", usage = "<ID>", permissionDefault = PermissionDefault.TRUE)
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
        String date = rs.getString("date");
        String id = rs.getString("id");
        if (rs.getString("expiration").equalsIgnoreCase("NONE")) {
          String expiration = plugin.getExpiration(date);        

          sender.sendMessage("Message Open: "+id);        
          while(rs.next()){
            sender.sendMessage(" From: " + rs.getString("sender"));
            sender.sendMessage(" Date: " + date );      
            sender.sendMessage(" Expires: " + expiration);
            sender.sendMessage(" Message: " + rs.getString("message"));

          }
          rs.close();
          stmt.executeUpdate("UPDATE CM_Mail SET read='YES', expiration='"+expiration+"' WHERE id='"+args[0]+"' AND target='"+Playername+"'");
        } else {
          String expiration = rs.getString("expiration");        

          sender.sendMessage("Message Open: "+id);        
          while(rs.next()){
            sender.sendMessage(" From: " + rs.getString("sender"));
            sender.sendMessage(" Date: " + date );      
            sender.sendMessage(" Expires: " + expiration);
            sender.sendMessage(" Message: " + rs.getString("message"));

          }
          rs.close();
          stmt.close();
        }
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