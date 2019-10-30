package ie.gmit.ds;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class PassServer {
	private static final Logger logger = Logger.getLogger(PassServer.class.getName());
	private final Server server;

	public PassServer(int port) {
		server = ServerBuilder.forPort(port).addService(new PassHashImpl()).build();
	}

	public void start() {
		try {
			server.start();
			logger.info("\nServer started running. Listening on " + server.getPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	@SuppressWarnings("unused")
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		int serverPort = 33333;
		
		try {
	        String port_param = args[0];
	        if(port_param != null) {
				serverPort = Integer.parseInt(port_param);
			}
	    }
	    catch (ArrayIndexOutOfBoundsException e){
	        System.out.println("Using default port.\n");
	    }
	   
		PassServer server = new PassServer(serverPort);
		server.start();
		//server.blockUntilShutdown();
		
		String stop;
		System.out.println("\nEnter stop to shutdown server: ");
		stop = in.nextLine();
		while(!stop.equalsIgnoreCase("stop")) {
			System.out.println("Enter stop to shutdown server: ");
			stop = in.nextLine();
		}
		
		in.close();
		server.stop();
		
	}
}
