import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;

public class Client {
	private static Socket socket; 
        private static String ipAddress;
        private static int port;
        private static int seqNum=0;
        private static int clientId;
	public static void main(String args[]){
                int count=0;
		String ipServerList = "ipServerList.txt";
		String ipClientList = "ipClientList.txt";
		String leader = "leader.txt";
        int requestsSent = 0;
        int ackReceived = 0;
		boolean isLeader;
		String line;
                    try{
                        BufferedReader br = new BufferedReader(new FileReader(leader));
                        if(br.readLine().equals("true")){
                                isLeader = true;
                        }
                        else
                        {
                                isLeader = false;
                        }
                        if(isLeader){
                                BufferedReader bufferedR = new BufferedReader(new FileReader(ipClientList));
                                while ((line = bufferedR.readLine()) != null) {
                                        String[] splitLine = line.split(" ");
                            Socket tempClientSocket = new Socket(splitLine[0], 44444);
                            PrintWriter temp = new PrintWriter(tempClientSocket.getOutputStream(), true);
                            temp.println("start");
                                }
                        }
                        else{
                                ServerSocket clientServer = new ServerSocket(44444);
                                Socket cSocket = clientServer.accept();
                                BufferedReader clientBR = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
                                String startMessage = clientBR.readLine();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    try {
                        ServerSocket server = new ServerSocket(26001);
                        File file = new File("log.txt");
                        if(file.exists()){file.delete();};
                        Writer writeToLog = new FileWriter("log.txt", true);

                    while(count<40){
                            try{
                                FileReader fileReader = new FileReader(ipServerList);
                                BufferedReader bufferedReader = new BufferedReader(fileReader);
                                String tempLine; 
                                Map<String,Integer> ipServers = new HashMap<String,Integer>();
                                clientId = Integer.parseInt(bufferedReader.readLine());
                                while((tempLine=bufferedReader.readLine()) != null)
                                {
                                        String split[] =  tempLine.split(" ");
                                        ipServers.put(split[0], Integer.parseInt(split[1]));
                                }
                                //String array[]= ipServers.keySet();
                                int i=0;
                                Set<String> ips = ipServers.keySet();
                                String array[] = new String[3];
                                for(String s : ips){
                                        array[i++]= s;
                                }
                                int randomServerNumber = new Random().nextInt(array.length);
                                ipAddress = (array[randomServerNumber]);
                                port = ipServers.get(ipAddress);
                                socket = new Socket(ipAddress, port);
                                OutputStream os = socket.getOutputStream();
                                PrintWriter out = new PrintWriter(os, true);
                                InetAddress ip = InetAddress.getLocalHost();
                                String hostname = ip.getHostName();
                                out.println("c "+clientId +" "+seqNum +" "+ hostname);
                                long sendTime = System.currentTimeMillis();
                                requestsSent++;
                                /*Date date = new Date();
                                Timestamp timestamp = new Timestamp(date.getTime());
                                long sendTime = timestamp.getTime();*/
                                Socket cs = server.accept();
                                InputStream is = cs.getInputStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader buffer = new BufferedReader(isr);
                                buffer.readLine();
                                ackReceived++;
                                /*long receiveTime = timestamp.getTime();*/
                                long elapsedTime = System.currentTimeMillis() - sendTime;
                                writeToLog.write("Elapsed time for request number " + seqNum + " from server number " +
                                        (randomServerNumber + 1) + " is " + elapsedTime + "\n");
                                writeToLog.flush();
                                System.out.println("Acknowledgement received for sequence number + " + seqNum);
                                seqNum++;
                                count++;
                    }catch(Exception e){
                        e.printStackTrace();
                    } }
                    writeToLog.write("Request messages sent: " + requestsSent + "\n");
                    writeToLog.write("Acknowledgement messages received: " + ackReceived);
                    writeToLog.flush();}catch(Exception f){
                           f.printStackTrace();
                        }
              }
    } 


