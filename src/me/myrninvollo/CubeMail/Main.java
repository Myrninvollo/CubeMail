package me.myrninvollo.CubeMail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.glydar.paraglydar.ParaGlydar;
import org.glydar.paraglydar.command.*;
import org.glydar.paraglydar.models.Player;
import org.glydar.paraglydar.plugin.Plugin;

import me.myrninvollo.CubeMail.commands.clearmailbox;
import me.myrninvollo.CubeMail.commands.delmail;
import me.myrninvollo.CubeMail.commands.inbox;
import me.myrninvollo.CubeMail.commands.mail;
import me.myrninvollo.CubeMail.commands.mailboxes;
import me.myrninvollo.CubeMail.commands.outbox;
import me.myrninvollo.CubeMail.commands.readmail;
import me.myrninvollo.CubeMail.commands.sendmail;
import me.myrninvollo.CubeMail.listeners.PListener;




public class Main extends Plugin {
  

  

  DBConnection service = DBConnection.getInstance();

  public void onEnable(){    
     getLogger().info(" " + getName() + " " + getVersion() + " enabled.");
     
   
    // declare new listener
    
    getServer().getEventManager().register(this, new PListener(this));
    
    getServer().getCommandManager().register(this, new mail(this));
    getServer().getCommandManager().register(this, new sendmail(this));
    
    getServer().getCommandManager().register(this, new readmail(this));
    getServer().getCommandManager().register(this, new delmail(this));
    
    getServer().getCommandManager().register(this, new clearmailbox(this));
    getServer().getCommandManager().register(this, new inbox(this));
    getServer().getCommandManager().register(this, new outbox(this));
    getServer().getCommandManager().register(this, new mailboxes(this));
    // Create connection & table
    try {
      service.setPlugin(this);
      service.setConnection();
      service.createTable();
    } catch(Exception e) {
    	//ParaGlydar.getLogger().info("[SimpleMail] "+"Error: "+e); 
    	getLogger().info("[CubeMail]  Error:"+e);
    	//ParaGlydar.getLogger().
    }
    // Check for and delete any expired tickets, display progress.
    getLogger().info("[CubeMail] "+expireMail()+" Expired Messages Cleared");
  }

  public void onDisable(){    
    // Check for and delete any expired tickets, display progress.
	  getLogger().info("[CubeMail] "+expireMail()+" Expired Messages Cleared");
    // Close DB connection
    service.closeConnection();
    getLogger().info(" " + getName() + " " + getVersion() + " disabled.");
  }  

  
	  
	  
	  public Player getPlayer(String name) {
		  @SuppressWarnings("rawtypes")
	      Iterator iterator = ParaGlydar.getServer().getConnectedPlayers().iterator();

	      Player player;
	      do {
	          if (!iterator.hasNext()) {
	              return null;
	          }
	          player = (Player) iterator.next();
	      } while (!player.getName().equalsIgnoreCase(name));
	      return player;
	  }
	  
	  // Get player from sender
	    public Player getPlayer(CommandSender s) {
	        @SuppressWarnings("rawtypes")
			Iterator iterator = ParaGlydar.getServer().getConnectedPlayers().iterator();
	        Player player;
	            if (!iterator.hasNext()) {
	                return null;
	            }
	            player = (Player) iterator.next();
	        return player;
	    }

  public String getCurrentDTG (String string) {
    Calendar currentDate = Calendar.getInstance();
    SimpleDateFormat dtgFormat = new SimpleDateFormat ("dd/MMM/yy HH:mm");    
    return dtgFormat.format (currentDate.getTime());
  }

  public String getExpiration(String date) {  
    String mailExpiration = "14";
    for (char c : mailExpiration.toCharArray()) {
      if (!Character.isDigit(c)) {
        mailExpiration = "14";
      }
    }
    int expire = Integer.parseInt(mailExpiration);
    Calendar cal = Calendar.getInstance();
    cal.getTime();
    cal.add(Calendar.DAY_OF_WEEK, expire);
    java.util.Date expirationDate = cal.getTime();
    SimpleDateFormat dtgFormat = new SimpleDateFormat ("dd/MMM/yy HH:mm");    
    return dtgFormat.format (expirationDate);  
  }

  public int expireMail() {
    ResultSet rs;
    java.sql.Statement stmt;
    Connection con;
    int expirations = 0;
    try {
      con = service.getConnection();
      stmt = con.createStatement();
      Statement stmt2 = con.createStatement();
      rs = stmt.executeQuery("SELECT * FROM SM_Mail");
      while(rs.next()){
        String date = rs.getString("date");
        String expiration = rs.getString("expiration");
        String id = rs.getString("id");
        // IF AN EXPIRATION HAS BEEN APPLIED 
        if (!expiration.equalsIgnoreCase("NONE")) {
          // CONVERT DATE-STRINGS FROM DB TO DATES 
          Date dateNEW = new SimpleDateFormat("dd/MMM/yy HH:mm", Locale.ENGLISH).parse(date);
          Date expirationNEW = new SimpleDateFormat("dd/MMM/yy HH:mm", Locale.ENGLISH).parse(expiration);
          // COMPARE STRINGS
          int HasExpired = dateNEW.compareTo(expirationNEW);
          if (HasExpired >= 0) {
            stmt2.executeUpdate("DELETE FROM SM_Mail WHERE id='"+id+"'");
            expirations++;          
          } 
        }
      }
      return expirations;
    } catch(Exception e) {
       ParaGlydar.getLogger().info("[CubeMail] "+"Error: "+e);
    }  
    return expirations;
  }

  

@Override
public String getName() {
	
	return "CubeMail";
}

@Override
public String getVersion() {
	
	return "V1.o";
}
}