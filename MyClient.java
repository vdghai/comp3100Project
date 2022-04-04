import java.io.*;
import java.net.*;

public class MyClient {
	public static void main(String[] args) throws Exception {

		Socket s = new Socket("localhost", 50000);
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());
		BufferedReader dis = new BufferedReader(new InputStreamReader(s.getInputStream()));

		String S_reply;
		String client = "HELO\n";
		String data;
		int count = 1;
		int[] Array_Server;
		int max = 0;
		String lrrServer = "";
		int allServers = 0;

		dout.write(client.getBytes());
		dout.flush();

		S_reply = dis.readLine();
		System.out.println("message= " + S_reply);

		String username = System.getProperty("user.name");
		client = "AUTH " + username + "\n";
		dout.write(client.getBytes());
		dout.flush();

		S_reply = dis.readLine();
		System.out.println("message= " + S_reply);

		client = "REDY\n";
		dout.write(client.getBytes());
		dout.flush();

		S_reply = dis.readLine();
		System.out.println("message= " + S_reply);

		client = "GETS All\n";
		dout.write(client.getBytes());
		dout.flush();

		S_reply = dis.readLine();
		System.out.println("message= " + S_reply);

		data = S_reply;
		System.out.println("Servers = " + data);
		int id1 = data.indexOf("DATA") + 5;
		int id2 = data.indexOf(' ', data.indexOf(' ') + 1);
		String records = data.substring(id1, id2);
		System.out.println("nRec = " + records);
		int dataInt = Integer.parseInt(records);
		System.out.println("Int = " + dataInt);

		Array_Server = new int[dataInt];

		client = "OK\n";
		dout.write(client.getBytes());
		dout.flush();

		String[] nRec = new String[dataInt];
		int i = 0;
		while (i < dataInt) {
			S_reply = dis.readLine();
			nRec[i] = S_reply;
			System.out.println("nRec: " + nRec[i]);
			i++;
		}

		int[] core = new int[dataInt];
		String[] cores;

		for (int k = 0; k < Array_Server.length; k++) {
			cores = nRec[k].split(" ");
			core[k] = Integer.parseInt(cores[4]);
		}

		for (int j = 0; j < core.length; j++) {
			if (core[j] > core[0]) {
				max = j;
			}
		}

		String[] hold = nRec[max].split(" ");
		lrrServer = hold[0];

		for (int r = 0; r < nRec.length; r++) {
			if (nRec[r].contains(lrrServer)) {
				allServers++;
			}
		}

		int ServerID = 0;

		System.out.println(lrrServer);
		System.out.println(allServers);

		client = "OK\n";
		dout.write(client.getBytes());
		dout.flush();

		S_reply = dis.readLine();
		System.out.println(S_reply);

		client = "SCHD 0 " + lrrServer + " 0\n";
		dout.write(client.getBytes());
		dout.flush();
		S_reply = dis.readLine();
		System.out.println(S_reply);

		while (!(S_reply.contains("NONE"))) {
			client = "REDY\n";
			dout.write(client.getBytes());
			dout.flush();

			S_reply = dis.readLine();
			System.out.println("message= " + S_reply);
			if (S_reply.contains("JOBN")) {
				client = "SCHD " + count + " " + lrrServer + " " + ServerID + "\n";
				dout.write(client.getBytes());
				dout.flush();
				S_reply = dis.readLine();
				System.out.println(S_reply);
				count++;
				ServerID++;
				if (ServerID == allServers) {
					ServerID = 0;
				}
			}
		}
		client = "QUIT\n";
		dout.write(client.getBytes());
		dout.flush();

    S_reply = dis.readLine();
		System.out.println("message= " + S_reply);
		
    dout.close();

		s.close();
	}
}