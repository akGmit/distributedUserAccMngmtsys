import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PassHashGrpc;
import ie.gmit.ds.PassHashGrpc.PassHashStub;
import ie.gmit.ds.PassServer;
import ie.gmit.ds.Passwords;
import ie.gmit.ds.ValidateRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class test_PassHashImpl {
	private static final Logger logger = Logger.getLogger(test_PassHashImpl.class.getName());
	private static ManagedChannel channel;
	private static PassHashStub asyncStub;

	@BeforeClass
	public static void init() {
		channel = ManagedChannelBuilder.forAddress("localhost", 33333).usePlaintext().build();
		asyncStub = PassHashGrpc.newStub(channel);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					PassServer.main(null);
				} catch (Exception e) {}
			}
		}).start();
	}

	@Test
	public void testValidate() {

		byte[] testSalt = Passwords.getNextSalt();
		byte[] testHash = Passwords.hash("test".toCharArray(), testSalt);
		
		ValidateRequest req = ValidateRequest.newBuilder().setPassword("test").setHash(ByteString.copyFrom(testHash))
				.setSalt(ByteString.copyFrom(testSalt)).build();
 
		StreamObserver<BoolValue> so = new StreamObserver<BoolValue>() {

			@Override
			public void onNext(BoolValue value) {
				assertTrue(value.getValue());
			}

			@Override
			public void onError(Throwable t) {
			}

			@Override
			public void onCompleted() {
				logger.info("tesValidate() complete");
			}
		};

		asyncStub.validate(req, so);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testHash() {
		HashRequest req = HashRequest.newBuilder().setUserID(5555).setPassword("test").build();
		final String pass = req.getPassword();
		
		StreamObserver<HashResponse> so = new StreamObserver<HashResponse>() {

			@Override
			public void onNext(HashResponse value) {
				byte[] testSalt = value.getSalt().toByteArray();
				byte[] testHash = value.getHash().toByteArray();
				ByteString resTesthash = ByteString.copyFrom(Passwords.hash(pass .toCharArray(), testSalt));
				HashResponse expected = HashResponse.newBuilder()
						.setUserID(5555)
							.setSalt(ByteString.copyFrom(testSalt))
								.setHash(resTesthash).build();
				assertTrue(value.equals(expected));
 			}

			@Override
			public void onError(Throwable t) {
			}

			@Override
			public void onCompleted() {
				logger.info("testHash() complete");
			}
		};
		asyncStub.hash(req, so);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
