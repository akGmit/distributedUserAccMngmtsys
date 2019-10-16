package ie.gmit.ds;

import java.io.IOException;
import java.util.logging.Logger;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class PassServer {
	private static final Logger logger = Logger.getLogger(PassServer.class.getName());

	private final int port = 33333;
	private final Server server;

	public PassServer() {
		server = ServerBuilder.forPort(port).addService(new PassHashImpl()).build();
	}

	public void start() {
		try {
			server.start();
			logger.info("Server started running...");
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

	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	public static void main(String[] args) throws Exception {
		PassServer server = new PassServer();
		server.start();
		server.blockUntilShutdown();
	}
}
