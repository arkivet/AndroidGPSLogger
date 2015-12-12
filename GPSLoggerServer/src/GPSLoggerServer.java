import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.json.JSONObject;

public class GPSLoggerServer {

	private static int serverPort = 8899;
	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static InputStreamReader inputStreamReader;
	private static BufferedReader bufferedReader;
	private static String data;
	private static JSONObject jsonObject;
	private static int incomingSessionID;
	private static int currentSessionID = -1;
	private static String fileName;
	private static File file;
	private static FileWriter fstream;
	private static DateFormat dateFormat;
	private static Date date;
	
	public static void main(String[] args) throws Exception {
		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
			System.out.println("Could not listen on port: " + serverPort);
		}
		dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_");
		
		System.out.println("Server started. Listening on port: " + serverPort);
		while(true){
			try {
				clientSocket = serverSocket.accept();
				System.out.println("Socket Accepted");
				inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader);
				data = bufferedReader.readLine() + "\n";
				jsonObject = new JSONObject(data);
				incomingSessionID = jsonObject.getInt("sessionID");
				sendToFile(data, incomingSessionID);
				
				inputStreamReader.close();
				clientSocket.close();
				System.out.println("Socket closed");
			} catch(IOException e) {
				System.out.println("Problem in socket reading");
			}
		}
	}
	
	public static void sendToFile(String data, int incomingSessionID) throws Exception{
		if(incomingSessionID > currentSessionID){
			date = new Date();
			fileName = dateFormat.format(date) + incomingSessionID;
			file = new File(fileName + ".txt");
			fstream = new FileWriter(file, true);
			currentSessionID = incomingSessionID;
		} else {
			fileName = dateFormat.format(date) + currentSessionID;
			fstream = new FileWriter(file, true);
		}
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(data);
		out.newLine();
		out.close();
		System.out.print(fileName);
	}
}
