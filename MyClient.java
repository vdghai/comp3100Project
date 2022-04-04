import java.io.*;
import java.net.*;

public class MyClient {
	public static void main(String[] args) throws Exception {

		Socket s = new Socket("localhost", 50000);
		DataOutputStream dout = new DataOutputStream(s.getOutputStream());
		BufferedReader dis = new BufferedReader(new InputStreamReader(s.getInputStream()));

		String S_reply;
		String client = "HELO\n";
		String gets;
		int count = 1;
		int[] lrrArray;
		int max = 0;
		int idx = 0;
		String lrrName = "";
		int totalServers = 0;

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

		gets = S_reply;
		System.out.println("Gets = " + gets);
		int id1 = gets.indexOf("DATA") + 5;
		int id2 = gets.indexOf(' ', gets.indexOf(' ') + 1);
		System.out.println("1 : " + id1);
		System.out.println("2 : " + id2);
		String extract = gets.substring(id1, id2);
		System.out.println("Extract = " + extract);
		int getsInt = Integer.parseInt(extract);
		System.out.println("Int = " + getsInt);

		lrrArray = new int[getsInt];

		client = "OK\n";
		dout.write(client.getBytes());
		dout.flush();

		String[] nRec = new String[getsInt];
		int i = 0;
		while (i < getsInt) {
			S_reply = dis.readLine();
			nRec[i] = S_reply;
			System.out.println("nRec: " + nRec[i]);
			i++;
		}

		int[] coreNum = new int[getsInt];
		String[] cores;

		for (int k = 0; k < lrrArray.length; k++) {
			cores = nRec[k].split(" ");
			coreNum[k] = Integer.parseInt(cores[4]);
		}

		for (int j = 0; j < coreNum.length; j++) {
			if (coreNum[j] > coreNum[idx]) {
				max = j;
			}
		}

		String[] temp = nRec[max].split(" ");
		lrrName = temp[0];

		for (int r = 0; r < nRec.length; r++) {
			if (nRec[r].contains(lrrName)) {
				totalServers++;
			}
		}

		int currServer = 0;

		System.out.println(lrrName);
		System.out.println(totalServers);

		client = "OK\n";
		dout.write(client.getBytes());
		dout.flush();

		S_reply = dis.readLine();
		System.out.println(S_reply);

		client = "SCHD 0 " + lrrName + " 0\n";
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
				client = "SCHD " + count + " " + lrrName + " " + currServer + "\n";
				dout.write(client.getBytes());
				dout.flush();
				S_reply = dis.readLine();
				System.out.println(S_reply);
				count++;
				currServer++;
				if (currServer == totalServers) {
					currServer = 0;
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