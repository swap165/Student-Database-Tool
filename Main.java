import HTTP.HTTPHandler;
import Logging.LOG_SECTION;
import Logging.LoggerHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;

//TODO implement SQL Database

public class Main {
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);


        //Setting up HTTP Server
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
            HttpContext context = server.createContext("/");
            context.setHandler(new HTTPHandler());

            server.setExecutor(threadPoolExecutor);
            server.start();



        } catch (IOException e) {
            e.printStackTrace(); //Writes to Console
            LoggerHandler.writelog(LOG_SECTION.HTTP_LOG, "custom error", Level.WARNING); //Writes to Log
        }



        //Database Connection Setup - SQLlite
        Connection conn = null;
        String projectPath = System.getProperty("user.dir") + "\\src\\database\\";
        try{
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + projectPath + "database.db";
            System.out.println(url);
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
            String sql = "CREATE TABLE IF NOT EXISTS USERS (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	name text NOT NULL,\n"
                    + "	courses text NOT NULL\n"
                    + ");";

            Statement stmt = conn.createStatement();
                // create a new table
            stmt.execute(sql);
            String insertUser = "INSERT INTO USERS (name, courses) VALUES ('Swapnil', 'EECS2011, EECS2021, MATH2030, MATH1025, ECON1010');";
            stmt.execute(insertUser);

           // String eachCourse = "CREATE TABLE IF NOT EXISTS " + ""

        } catch (SQLException e) {
                System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
        }

        //Database Connection Setup - MySQL
/*
        try{


            Class.forName("org.gjt.mm.mysql.Driver");

            Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/swapmikeproj?autoReconnect=true&useSSL=false", "root", "password");

            //linux server - port forwarding and nginx
            //request linux server ip, the linux server -> localhost, linux server's ip, which can be masked as a domain swap&mike.com -> 198.283.283.12 -> localhost


            Statement stmt = c.createStatement();

            int result = stmt.executeUpdate("CREATE TABLE test (name VARCHAR(20))");
            int result2 = stmt.executeUpdate("INSERT into test (name) VALUES ('Mike')");


            System.out.println(result);



        }catch(Exception e){
            e.printStackTrace();
        }

 */
    }
}


//need mysqlserver, username root, password password
//heidisql setup as TCP/IP
//the connector provided here
//mysql installer, server only