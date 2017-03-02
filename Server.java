import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Created by prasshanth and rucha on 4/19/16.
 */
public class Server {
    static final int MAXRETRY = 1000;
    static boolean serversDone[] = {false, false};
    static boolean clientsDone[] = {false, false, false, false, false, false, false};
    static int messagesReceived[] = {0, 0, 0, 0, 0, 0};
    static int messagesSent[] = {0, 0, 0, 0, 0, 0};

    public static boolean allclients(){
        for(int i = 0; i < 7; i++){
            if(clientsDone[i] == false){
                return false;
            }
        }
        return true;
    }

    public static boolean allServers(){
        if(clientsDone[0] == true && clientsDone[1] == true){
            return true;
        }
        else{
            return false;
        }
    }
    public static void log() throws IOException {
        File file = new File("log.txt");
        if(file.exists()){
            file.delete();
        }
        Writer writeToLog = new FileWriter("log.txt", true);
        writeToLog.write("Clients requests received : " + messagesReceived[0] + "\n");
        writeToLog.write("Server requests received: " + messagesReceived[1] + "\n");
        writeToLog.write("Agreed messages received: " + messagesReceived[2] + "\n");
        writeToLog.write("Commit requests received: " + messagesReceived[3] + "\n");
        writeToLog.write("Commit messages received: " + messagesReceived[4] + "\n");
        writeToLog.write("Acknowledgement messages received: " + messagesReceived[5] + "\n\n\n");
        writeToLog.write("Server requests sent: " + messagesSent[0] + "\n");
        writeToLog.write("Agreed messages sent: " + messagesSent[1] + "\n");
        writeToLog.write("Commit requests sent: " + messagesSent[2] + "\n");
        writeToLog.write("Commit messages sent: " + messagesSent[3] + "\n");
        writeToLog.write("Acknowledgement messages sent: " + messagesSent[4] + "\n");
        writeToLog.write("Acknowledgement messages to client sent: " + messagesSent[5]);
        writeToLog.flush();
    }
    public static void send(String IP, int port, String output) throws InterruptedException {
        Socket tempSocket = null;
        OutputStream tempOutput = null;

        boolean connected = false;

        try {
            int retryCount = 1;
            while(!connected && retryCount <= MAXRETRY) {
                try {
                    tempSocket = new Socket(IP, port);
                    connected = true;
                } catch (Exception e) {
                    Thread.sleep(2000);
                    System.out.println("Attempting retry to host " + IP);
                    retryCount++;
                }
            }
            if(!connected){
                System.out.println("Error connecting to host " + IP);
            }

            tempOutput = tempSocket.getOutputStream();
            PrintWriter out = new PrintWriter(tempOutput, true);
            out.println(output);
            out.close();
            tempOutput.close();
            tempSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String args[]) throws IOException {
        String iplist = "iplist.txt";
        String leaderIP = "10.176.66.51";
        Socket sSocket, c1Socket, c2Socket, tempSocket;
        int serverPort = 0;
        String isLeader = "false";
        Map<String, String> messageStore = new HashMap<String, String>();
        String host1 = null;
        String host2 = null;
        String currentHost = null;
        String client1 = null;
        String client2 = null;
        String client3 = null;
        String client4 = null;
        String client5 = null;
        String client6 = null;
        String client7 = null;
        int cport1 = 0;
        int cport2 = 0;
        int cport3 = 0;
        int cport4 = 0;
        int cport5 = 0;
        int cport6 = 0;
        int cport7 = 0;
        int port1 = 0;
        int port2 = 0;
        boolean reply[] = {false, false, false, false, false, false, false};
        boolean ack[] = {false, false, false, false, false, false ,false};



        try{
            FileReader fileReader = new FileReader(iplist);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            isLeader = bufferedReader.readLine();
            serverPort = Integer.parseInt(bufferedReader.readLine());
            String[] temp = bufferedReader.readLine().split(" ");
            currentHost = temp[0];
            temp = bufferedReader.readLine().split(" ");
            host1 = temp[0];
            port1 = Integer.parseInt(temp[1]);
            temp = bufferedReader.readLine().split(" ");
            host2 = temp[0];
            port2 = Integer.parseInt(temp[1]);
            temp = bufferedReader.readLine().split(" ");
            client1 = temp[0];
            cport1 = Integer.parseInt(temp[1]);
            temp = bufferedReader.readLine().split(" ");
            client2 = temp[0];
            cport2 = Integer.parseInt(temp[1]);
            temp = bufferedReader.readLine().split(" ");
            client3 = temp[0];
            cport3 = Integer.parseInt(temp[1]);
            temp = bufferedReader.readLine().split(" ");
            client4 = temp[0];
            cport4 = Integer.parseInt(temp[1]);
            temp = bufferedReader.readLine().split(" ");
            client5 = temp[0];
            cport5 = Integer.parseInt(temp[1]);
            temp = bufferedReader.readLine().split(" ");
            client6 = temp[0];
            cport6 = Integer.parseInt(temp[1]);
            temp = bufferedReader.readLine().split(" ");
            client7 = temp[0];
            cport7 = Integer.parseInt(temp[1]);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        File file = new File("File.txt");
        if(file.exists()){
            file.delete();
        }
        Writer writeTofile = new FileWriter("File.txt", true);


        ServerSocket serverSocket = new ServerSocket(serverPort);
        try{
            System.out.println("Server Started and listening to the port "+serverPort);
            while(true){

                sSocket = serverSocket.accept();
                InputStream is = sSocket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                /*c1Socket = new Socket(host1, port1);
                c2Socket = new Socket(host2, port2);

                OutputStream c1Output = c1Socket.getOutputStream();
                OutputStream c2Output = c2Socket.getOutputStream();

                PrintWriter c1Out = new PrintWriter(c1Output, true);
                PrintWriter c2Out = new PrintWriter(c2Output, true);*/

                String input = br.readLine();
                System.out.println(input);
                if(input == null || input.split(" ")[0].equals("quit")){
                    /*br.close();
                    isr.close();
                    is.close();
                    sSocket.close();*/
                    continue;
                }
                String[] inputParts = input.split(" ");
                if(inputParts[0].equals("c")){
                    messagesReceived[0]++;
                    messageStore.put(inputParts[1], inputParts[1] + " " + inputParts[2] + " " +
                    inputParts[3]);
                    send(host1, port1, "s" + " " + "request" + " " + inputParts[1] + " " +
                    inputParts[2] + " " + inputParts[3] + " " + currentHost);
                    send(host2, port2, "s" + " " + "request" + " " + inputParts[1] + " " +
                            inputParts[2] + " " + inputParts[3] + " " + currentHost);
                    messagesSent[0]++;
                    messagesSent[0]++;
                }
                else {
                    if(inputParts[1].equals("request")){
                        messagesReceived[1]++;
                        messageStore.put(inputParts[2], inputParts[2] + " " + inputParts[3] + " " +
                        inputParts[4]);
                        if(inputParts[5].equals(host1)){
                            send(host1, port1, "s" + " " + "agreed" + " " + inputParts[2] + " " +
                            inputParts[3] + " " + inputParts[4] + " " + currentHost);
                        }
                        else{
                            send(host2, port2, "s" + " " + "agreed" + " " + inputParts[2] + " " +
                                    inputParts[3] + " " + inputParts[4] + " " + currentHost);
                        }
                        messagesSent[1]++;
                    }
                    else if(inputParts[1].equals("agreed")){
                        messagesReceived[2]++;
                        if(reply[Integer.parseInt(inputParts[2])-1] == false){
                           reply[Integer.parseInt(inputParts[2])-1]= true;
                        }
                        else{
                            reply[Integer.parseInt(inputParts[2])-1] = false;
                            System.out.println(isLeader.equals("true"));
                            if(isLeader.equals("true")){
                                /*write to file*/
                                if(Integer.parseInt(inputParts[3]) == 39){
                                    clientsDone[Integer.parseInt(inputParts[2])-1] = true;
                                }
                                writeTofile.write(inputParts[2] + " " + inputParts[3] + " " + inputParts[4] + "\n");
                                writeTofile.flush();
                                send(host1, port1, "s" + " " + "commit" + " " + inputParts[2] + " " + inputParts[3] +
                                        " " + inputParts[4] + " " + currentHost);
                                send(host2, port2, "s" + " " + "commit" + " " + inputParts[2] + " " + inputParts[3] +
                                        " " + inputParts[4] + " " + currentHost);
                                messagesSent[3]++;
                                messagesSent[3]++;
                            }
                            else{
                                if(host1.equals(leaderIP)){
                                    send(host1, port1, "s" + " " + "commitRequest" + " " + inputParts[2] + " " + inputParts[3]
                                    + " " + inputParts[4] + " " + currentHost);
                                }
                                else
                                {
                                    send(host2, port2, "s" + " " + "commitRequest" + " " + inputParts[2] + " " + inputParts[3]
                                            + " " + inputParts[4] + " " + currentHost);
                                }
                                messagesSent[2]++;
                            }
                        }
                    }
                    else if(inputParts[1].equals("commitRequest")){
                        messagesReceived[3]++;
                        /*write to file*/
                        if(Integer.parseInt(inputParts[3]) == 39){
                            clientsDone[Integer.parseInt(inputParts[2])-1] = true;
                        }
                        writeTofile.write(inputParts[2] + " " + inputParts[3] + " " + inputParts[4] + "\n");
                        writeTofile.flush();
                        send(host1, port1, "s" + " " + "commit" + " " + inputParts[2] + " " + inputParts[3] +
                                " " + inputParts[4] + " " + currentHost);
                        send(host2, port2, "s" + " " + "commit" + " " + inputParts[2] + " " + inputParts[3] +
                                " " + inputParts[4] + " " + currentHost);
                        messagesSent[3]++;
                        messagesSent[3]++;
                    }
                    else if(inputParts[1].equals("commit")){
                        messagesReceived[4]++;
                        /*write to file*/
                        if(Integer.parseInt(inputParts[3]) == 39){
                            clientsDone[Integer.parseInt(inputParts[2])-1] = true;
                        }
                        writeTofile.write(inputParts[2] + " " + inputParts[3] + " " + inputParts[4] + "\n");
                        writeTofile.flush();
                        String serverDone = "false";
                        if(allclients()){
                            serverDone = "true";

                        }
                        if(host1.equals(leaderIP)){
                            send(host1, port1, "s" + " " + "ack" + " " + inputParts[2] + " " + inputParts[3] +
                                    " " + inputParts[4] + " " + currentHost + " " + serverDone);
                        }
                        else{
                            send(host2, port2, "s" + " " + "ack" + " " + inputParts[2] + " " + inputParts[3] +
                                    " " + inputParts[4] + " " + currentHost + " " + serverDone);
                        }
                        messagesSent[4]++;
                        if(allclients()){
                            log();
                            System.out.println("Run finished for server, exiting.");
                            System.exit(0);
                        }

                    }
                    else if(inputParts[1].equals("ack")){
                        messagesReceived[5]++;
                        if(inputParts[6].equals("true")){
                            if(host1.equals(inputParts[5])){
                                serversDone[0] = true;
                            }
                            else{
                                serversDone[1] = true;
                            }

                        }
                        if(ack[Integer.parseInt(inputParts[2])-1] == true){
                            ack[Integer.parseInt(inputParts[2])-1] = false;
                            String tempHost = null;
                            int tempPort = 0;
                            if(inputParts[2].equals("1")){
                                tempHost = client1;
                                tempPort = cport1;
                            }
                            else if(inputParts[2].equals("2")){
                                tempHost = client2;
                                tempPort = cport2;
                            }
                            else if(inputParts[2].equals("3")){
                                tempHost = client3;
                                tempPort = cport3;
                            }
                            else if(inputParts[2].equals("4")){
                                tempHost = client4;
                                tempPort = cport4;
                            }
                            else if(inputParts[2].equals("5")){
                                tempHost = client5;
                                tempPort = cport5;
                            }
                            else if(inputParts[2].equals("6")){
                                tempHost = client6;
                                tempPort = cport6;
                            }
                            else if(inputParts[2].equals("7")){
                                tempHost = client7;
                                tempPort = cport7;
                            }
                            send(tempHost, tempPort, "ack");
                            messagesSent[5]++;
                            if(allclients() && allServers()){
                                log();
                                System.out.println("Run finished for all servers, exiting");
                                System.exit(0);

                            }

                            /*tempSocket = new Socket(tempHost, tempPort);
                            PrintWriter tempOut = new PrintWriter(tempSocket.getOutputStream(), true);
                            tempOut.println("ack");
                            tempOut.close();
                            tempSocket.close();*/
                        }
                        else {
                            ack[Integer.parseInt(inputParts[2])-1] = true;
                        }

                    }


                }
                /*c1Out.println("quit" + " " + currentHost);
                c2Out.println("quit" + " " + currentHost);
                c1Out.close();
                c2Out.close();
                c1Output.close();
                c2Output.close();
                c1Socket.close();
                c2Socket.close();
                /*br.close();
                isr.close();
                is.close();
                sSocket.close();*/
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            serverSocket.close();
        }
    }
}
