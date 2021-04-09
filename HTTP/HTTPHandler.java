package HTTP;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.logging.Logger;

//TODO handlePostRequest
//TODO Organize This Better so the Response is cleaner and has styling if needed


public class HTTPHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String requestParamValue=null;              
        
        if ("GET".equals(httpExchange.getRequestMethod())) { 
        	
        	//Follows HTTP, a request to a page is inherently GET
        	
        	requestParamValue = handleGetRequest(httpExchange);
            System.out.println(httpExchange.getRequestURI().toString());
        }
        
        else if ("POST".equals(httpExchange.getRequestMethod())) {
                
        	if (!hasAdminRights(httpExchange)){
        		return;
            }
        	
        	else { 
            requestParamValue = handlePostRequest(httpExchange);
            System.out.println(httpExchange.getRequestURI().toString());
        	}
        	
        }

        handleResponse(httpExchange, requestParamValue);
        System.out.println(requestParamValue);
        }

        
        private boolean hasAdminRights(HttpExchange httpExchange) {
            //will call parseRequestBody, which returns either admin = true or admin = false
            //return true if admin = true otherwise false
        	
            String cookie = httpExchange.getRequestHeaders().get("Cookie").toString().substring(6);
            
            if (cookie.equals("true")) {
            	return true;
            }
            return false;
        }

        /*
         
        private void parseRequestBody(HttpExchange httpExchange){
        	
            String cookie = httpExchange.getRequestHeaders().get("Cookie").toString().substring(8);
        }
        
		*/
        
        private String handleGetRequest(HttpExchange httpExchange) {
        	
            if (!httpExchange.getRequestURI().toString().contains("?=")) {
                return null;
            }
            
            String getQuery[] = httpExchange.getRequestURI().toString().split("?=")[1].split("/");
            
            Connection conn = null;
            String projectPath = System.getProperty("user.dir") + "\\src\\database\\";
            String output = "";
            try{
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:" + projectPath + "database.db";
                System.out.println(url);
                conn = DriverManager.getConnection(url);

                System.out.println("Connection to SQLite has been established.");
                
                Statement stmt = conn.createStatement();
                String getInfo = "SELECT courses FROM " + getQuery[0] + " WHERE name = '" + getQuery[1] + "'";
                output = getInfo;
            } 
            catch (SQLException e) {
                    System.out.println(e.getMessage());
            } 
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            } 
            finally {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
            }
            return output;
        }


        private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
            
        	OutputStream outputStream = httpExchange.getResponseBody();
            String response = String.format("<h1>Your Request was: %s</h1>", requestParamValue); //basic response handle better

            httpExchange.sendResponseHeaders(201, response.length());
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        }
        
        
        private String handlePostRequest(HttpExchange httpExchange) {
        	
        	if (!httpExchange.getRequestURI().toString().contains("?=")) {
        		return null;
        	}
        	
        	return httpExchange.getRequestURI().toString().split("\\?")[1].split("=")[1];
        	
        }
    }



