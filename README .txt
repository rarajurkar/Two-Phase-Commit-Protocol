All files were compiled using the javac command on the UTD DC01, DC02, DC03, DC04, DC05, DC06, DC07, DC08, DC09, D10 machines. 
They should work on all the other DC machines.


Instructions to run the project:

1. Create separate folders for the Clients, Server1, Server2 and Server3 on the machines. Since the directories and files are shared between the machines, 
separate folders are required for simulating the different machines. One client must be chosen as the initiator, it is DC04 in this case.


2. Place the files Client.java in the Clients folder, and the Server.java file in the Server folders and compile them.


3. Place a file named "ipServerList.txt" in the Client folder. This file should contain the ip addresses of the servers in each line. 
For example:
10.176.66.51
10.176.66.52
10.176.66.53

4. Place a file named "iplist.txt" in each of the Server folders, these should contain the ip addresses of the other two servers per line.

For example, in the Server1 folder, we may have this file, which contains the ip addresses of the second and third servers:
10.176.66.53
10.176.66.54

5. 
Place a file named "ipClientList.txt" in the initiator Client folder, which contains the client ID and ip addresses of all the clients in the following format:
ID Address

For example:
1 10.176.66.54
2 10.176.66.55
3 10.176.66.56
4 10.176.66.57
5 10.176.66.58
6 10.176.66.59
7 10.176.66.60

6. 
Place a file named "leader.txt" in all the client folders, which contains true if the client is starting the functionality(initiator client). 
False for all other clients.


7. Run the servers in different machines using the command:
java Server


8. Run the clients using the following command:
java Client
Note: The initiator client must be run last.

9. The Servers run continuously, they can be terminated by pressing CTRL + C. 

The output is printed in "File.txt". It is adentical for all Servers. 

The log is maintained at the client side in "log.txt". This contains the information of randomly selected Server and time elapsed between request and receiving acknowledgement in milliseconds for each request.
and the total request messages sent and acknowledgement messages received.

The log is maintained at the Server side in "log.txt". 

Observations:


