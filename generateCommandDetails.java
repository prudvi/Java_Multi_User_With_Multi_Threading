import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import oracle.jdbc.pool.OracleDataSource;

public class generateCommandDetails {

	

	private static String getPAssword = "SELECT RSVP_MODEL, TOKEN, Serial_number  FROM  RSVP_CUSTOM.DEVICE_IDENTITY DI, AXEDA.DEVICE DD  "
			+ "WHERE CRMNUMBER_UPPER = ? AND DD.DEVICE_ID = DI.DEVICE_ID"; 
	
	
	
	public  static DataSource DEVdataSourceDEVDefault() throws SQLException {
		System.out.println();
		System.out.println("Connected to DEV database");
		OracleDataSource dataSource = new OracleDataSource();
		dataSource.setUser("user");
		dataSource.setPassword("password");
		dataSource.setURL("jdbc:oracle:thin:@host:1521/dbName");
		dataSource.setImplicitCachingEnabled(true);
		dataSource.setFastConnectionFailoverEnabled(true);
		return dataSource;
	}
	
	
	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			
			String[] arr = { "PRUDVI_RAJU" };
			//String crmFromcmd = args[0];
			Connection conn = dataSourceSTGRSVPDefault().getConnection();
					Date date =new Date();
					List<detailsClass> crmList = new ArrayList<>();
					String time = date.getDate()+"_"+date.getHours()+"_"+date.getHours()+"_"+date.getSeconds();

					System.out.println("Started LL:::");
					for (int x=0 ; x<arr.length; x++ ) {
						//System.out.println(" crm Number::: "+arr[x]);
						PreparedStatement pstmt = conn.prepareStatement(getPAssword);
							//pstmt.setString(1, "RSVP_ALANKAR");
						pstmt.setString(1, arr[x]);
						ResultSet rs = pstmt.executeQuery();
						//writeTOFile(rs, "crmnumbers", time);
						String password = getPassword(rs);
						if (password.length() > 0) {
							detailsClass crm = new detailsClass();
							crm.setCrmNumber(arr[x]);
							crm.setPassword(password);
							crmList.add(crm);
						}
						//System.out.println(password);
					}
					String json = objectMapper.writeValueAsString(crmList);
					System.out.println(json);
					writeTOFile(time, json);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	public static  String  getPassword(ResultSet rs) throws Exception {
		String serialNumber = "";
		String token = "";
		String model = "";
		while (rs.next()) {
			serialNumber = rs.getString("Serial_number") ;
			token = rs.getString("TOKEN") ;
			model = rs.getString("RSVP_MODEL") ;
		}
		
		String input = "java -jar C:/Users/PRUDVIRAJU/Documents/commandGenerator/commandGenerate.jar "+model+" "+token+" "+serialNumber;
		//System.out.println(input);
		
		Process process = Runtime.getRuntime().exec( input); 
		process.waitFor();
		String password = "";
		String line;
		BufferedReader inputP = new BufferedReader(new InputStreamReader(process.getInputStream()));
		while((line=inputP.readLine()) != null) {
			if (line.contains("Password :")) {
				String[] splitSpace = line.split(":");
				password = splitSpace[1].trim().toString();
			}
		}
		
		return password;
	}
	
	public  static void writeTOFile( String time, String output) throws Exception {
			try {
			    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/"+"_"+time+".txt", true)));
			    out.println("\""+output+"\",");
			    out.close();
			} catch (IOException e) {
			    e.printStackTrace();
			}
				
	}
	
	static class detailsClass {
		String crmNumber;
		
		String password;

		public String getCrmNumber() {
			return crmNumber;
		}

		public void setCrmNumber(String crmNumber) {
			this.crmNumber = crmNumber;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		@Override
		public String toString() {
			return "detailsClass [crmNumber=" + crmNumber + ", password=" + password + "]";
		}
		
		
	}
}

