import java.io.*;
import java.net.*;

public class trial {
	public static void main(String[] args) throws Exception{
		Socket s=new Socket("localhost",50000);
		DataOutputStream dout=new DataOutputStream(s.getOutputStream());
		BufferedReader dis=new BufferedReader(new
		InputStreamReader(s.getInputStream()));
		
		int count = 0;
		
		String strServer;
		String client = "HELO\n";
		
		dout.write(client.getBytes());
		dout.flush();
		
		strServer = dis.readLine();
		System.out.println("Message = " + strServer);	
		
		String username = System.getProperty("user.name");
		client = "AUTH "+username+"\n";
		
		dout.write(client.getBytes());
		dout.flush();

		strServer = dis.readLine();
		System.out.println("Message = " + strServer);
		
		while (!(strServer.contains("NONE"))) {
		
			client = "REDY\n";
			dout.write(client.getBytes());
			dout.flush();
		
			strServer = dis.readLine(); //JOB
			//System.out.println("Message = " + strServer);
			
			if (strServer.contains("JOBN")) {
		
				String[] core = strServer.split(" ");
		
				client = "GETS Avail " + core[4] + " " + core[5] + " " + core[6] + "\n";
				dout.write(client.getBytes());
				dout.flush();
		
				strServer = dis.readLine(); //DATA
				System.out.println("Message = " + strServer);
				client = "OK\n";
				dout.write(client.getBytes());
				dout.flush();
				
				String[] data = strServer.split(" ");
			//	System.out.println(servers);
			
			
			if( Integer.parseInt(data[1])!= 0){
				int servers = Integer.parseInt(data[1]);

				String[] nRec = new String[servers]; //SERVERS

				for (int i = 0; i < servers; i++) {
					strServer = dis.readLine();
					nRec[i] = strServer;
					//System.out.println("nRec: " + nRec[i]);
				}
				client = "OK\n";
				dout.write(client.getBytes());
				dout.flush();
		
				strServer = dis.readLine();
		
				String[] first = nRec[0].split(" "); //1st
		
				client = "SCHD " + count + " " + first[0] + " " + first[1] + "\n";
				
				dout.write(client.getBytes());
				dout.flush();
		
				strServer = dis.readLine();
				//System.out.println("Message = " + strServer); 
				count++;
			}
			else{
				client = "GETS Capable " + core[4] + " " + core[5] + " " + core[6] + "\n";
                dout.write(client.getBytes());
                dout.flush();

				strServer = dis.readLine(); //DATA
				//System.out.println("Message = " + strServer);

				client = "OK\n";
			    dout.write(client.getBytes());
				dout.flush();
				
				strServer = dis.readLine();

				String[] data2 = strServer.split(" ");
				int servers2 = Integer.parseInt(data2[1]);
				//System.out.println(servers2);

                String[] nRec = new String[servers2]; //SERVERS
		
				for (int i = 0; i <servers2; i++) {
					strServer = dis.readLine();
					nRec[i] = strServer;
					System.out.println("nRec: " + nRec[i]);
				}
		
				client = "OK\n";
		
				dout.write(client.getBytes());
				dout.flush();
		
				strServer = dis.readLine();
		
				String[] first = nRec[0].split(" "); //1st
		
				client = "SCHD " + count + " " + first[0] + " " + first[1] + "\n";
				
				dout.write(client.getBytes());
				dout.flush();
		
				strServer = dis.readLine();
				System.out.println("Message = " + strServer); 
				count++;
			}
		}
		}
		String quit = "QUIT\n";
		
		dout.write(quit.getBytes());
		dout.flush();
		
		strServer = dis.readLine();
		System.out.println("Message = " + strServer);
		
		dout.close();
		s.close();
	
	}
}
