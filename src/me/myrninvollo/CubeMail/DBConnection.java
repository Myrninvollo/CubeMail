package me.myrninvollo.CubeMail;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.glydar.paraglydar.ParaGlydar;

import me.myrninvollo.CubeMail.Main;


public class DBConnection {
    private static DBConnection instance = new DBConnection();
    public Connection con = null;
    public  int Timeout = 30;
    public Statement stmt;

    public Main plugin;

    private DBConnection() {
    }

    public static synchronized DBConnection getInstance() {
        return instance;
    }
    
    /**
     * We set the plugin that is to be used for these connections.
     * @param plugin
     */
    public void setPlugin(Main plugin) {
        this.plugin = plugin;
    }

    public void setConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");
        String strDirectoy ="database";
        new File(strDirectoy).mkdir();
        con = DriverManager.getConnection("jdbc:sqlite:"+ strDirectoy +"/Mail.db");
    }

    public Connection getConnection() {
        return con;
    }

    public void closeConnection() {
        try { con.close(); } catch (Exception ignore) {}
    }

    public void createTable() {
        Statement stmt;
        try {
            stmt = con.createStatement();
            String queryC = "CREATE TABLE IF NOT EXISTS CM_Mail (id INTEGER PRIMARY KEY, sender varchar(16) collate nocase, target varchar(16) collate nocase, date timestamp, message varchar(30), read varchar(10), expiration timestamp)";
            stmt.executeUpdate(queryC);
        } catch(Exception e) {
            ParaGlydar.getLogger().info("[CubeMail] "+"Error: "+e);
        }
    }  

    public void setStatement() throws Exception {
        if (con == null) {
            setConnection();
        }
        Statement stmt = con.createStatement();
        stmt.setQueryTimeout(Timeout);  // set timeout to 30 sec.
    }

    public  Statement getStatement() {
        return stmt;
    }

    public void executeStmt(String instruction) throws SQLException {
        stmt.executeUpdate(instruction);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Clone is not allowed.");
    }
}

