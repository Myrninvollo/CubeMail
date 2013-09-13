package me.myrninvollo.CubeMail.commands;

import me.myrninvollo.CubeMail.Main;





import org.glydar.paraglydar.command.*;
import org.glydar.paraglydar.permissions.Permission.PermissionDefault;

public class mail implements CommandSet {   

  public Main plugin;
  public mail(Main plugin)  {
    this.plugin = plugin;
  }

  @Command(name = "mail", usage = "- Get all the  commands", permissionDefault = PermissionDefault.TRUE)
	public CommandOutcome execute(CommandSender sender, String[] args) {  
    
	  	sender.sendMessage("---------[CubeMail HELP]----------");
	    sender.sendMessage(" /inbox - Check your inbox");
	    sender.sendMessage(" /outbox - Check your outbox");
	    sender.sendMessage(" /sendmail <player> <msg> - Send a message");
	    sender.sendMessage(" /readmail <id> - Read a message");
	    sender.sendMessage(" /delmail <id> - Delete a message");
	       
	    sender.sendMessage("-----------ADMIN ONLY------------");
	    sender.sendMessage(" /mailboxes - List active mailboxes");
	    sender.sendMessage(" /clearmailbox <playername> - Clear an active mailbox");      
	    
	    return CommandOutcome.SUCCESS;
  }

}