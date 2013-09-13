package me.myrninvollo.CubeMail.commands;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import me.myrninvollo.CubeMail.DBConnection;
import me.myrninvollo.CubeMail.Main;

import org.glydar.paraglydar.models.Player;
import org.glydar.paraglydar.command.Command;
import org.glydar.paraglydar.command.CommandOutcome;
import org.glydar.paraglydar.command.CommandSender;
import org.glydar.paraglydar.command.CommandSet;
import org.glydar.paraglydar.permissions.Permission.PermissionDefault;

public class sendmail implements CommandSet {   

  public Main plugin;
  public sendmail(Main plugin)  {
    this.plugin = plugin;
  }
  
  DBConnection service = DBConnection.getInstance();

  @Command(name = "sendmail", usage = "<player> [Message] - Send a message to a player", permissionDefault = PermissionDefault.TRUE)
	public CommandOutcome execute(CommandSender sender, String playerName, String[] args) {    
    Player player = null;
    if (sender instanceof Player) {
      player = (Player) sender;
    }
  
      
      Connection con;
      java.sql.Statement stmt;
      try {        
        con = service.getConnection();
        stmt = con.createStatement();

        StringBuilder sb = new StringBuilder();
        for (String arg : args)
          sb.append(arg + " ");            
            String[] temp = sb.toString().split(" ");
            String[] temp2 = Arrays.copyOfRange(temp, 1, temp.length);
            sb.delete(0, sb.length());
            for (String details : temp2)
            {
              sb.append(details);
              sb.append(" ");
            }
            String details = sb.toString();  

            String Rightnow = plugin.getCurrentDTG("date");            
            

            ResultSet rs2 = stmt.executeQuery("SELECT COUNT(target) AS inboxtotal FROM CM_Mail WHERE target='"+playerName+"'");
            int MaxMailboxSize = 10;
            if (rs2.getInt("inboxtotal") >= MaxMailboxSize) {
              sender.sendMessage("[CubeMail] Player's Inbox is full");
              rs2.close();
              return CommandOutcome.SUCCESS;
            }
            PreparedStatement statement = con.prepareStatement("insert into CM_MAIL values (?,?,?,?,?,?,?);");
                                    
              statement.setString(2, sender.getName());              

              statement.setString(3, playerName);
              
              statement.setString(4, Rightnow);              
              statement.setString(5, details);
              
              statement.setString(6, "NO");
              statement.setString(7, "NONE");
              statement.executeUpdate();
              statement.close();
              
              sender.sendMessage("[CubeMail] Message Sent to: " +  playerName);
              if (plugin.getPlayer(playerName) != null ) {
                  plugin.getPlayer(playerName).sendMessageToPlayer("[CubeMail] You've Got Mail!");
                }
              return CommandOutcome.SUCCESS;
           
      } catch(Exception e) {
    	  plugin.getLogger().info("[CubeMail] "+"Error: "+e);        
          if (e.toString().contains("locked")) {
            sender.sendMessage("[CubeMail] The database is busy. Please wait a moment before trying again...");
          } else {
            player.sendMessage("[CubeMail] Error: "+e);
          }
      }
     
      return CommandOutcome.SUCCESS;   
  }

}