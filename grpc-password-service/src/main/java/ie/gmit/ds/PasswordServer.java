package ie.gmit.ds;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class PasswordServer {
	private Server server;
	private static final int PORT = 50551;
	
	private void start() throws IOException {
		server = ServerBuilder.forPort(PORT)
                .addService(new PasswordServiceImpl())
                .build()
                .start();

    }

    private void stop() {
        if (server != null) {
        	server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
        	server.awaitTermination();
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
        final PasswordServer inventoryServer = new PasswordServer();
        inventoryServer.start();
        inventoryServer.blockUntilShutdown();
    }
}
