import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PassHashGrpc;
import ie.gmit.ds.PassHashGrpc.PassHashStub;
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
	public static void init() throws InterruptedException {
		channel = ManagedChannelBuilder.forAddress("localhost", 33333).usePlaintext().build();
		asyncStub = PassHashGrpc.newStub(channel);
	}
	
	@AfterClass public static void logout() throws InterruptedException {
		Thread.currentThread().interrupt();
	}
	
	@Test
	public void testValidate() throws Exception{

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
				logger.info("ttestValidate() failed");
				fail();
			}

			@Override
			public void onCompleted() {
				logger.info("tesValidate() passed");
			}
		};
		
		
		try {
			asyncStub.validate(req, so);
		}finally {
			Thread.currentThread().join(2000);
		}
	}

	@Test
	public void testHash() throws Exception{
		HashRequest req = HashRequest.newBuilder().setUserID(5555).setPassword("test").build();
		final String pass = req.getPassword();

		StreamObserver<HashResponse> so = new StreamObserver<HashResponse>() {

			@Override
			public void onNext(HashResponse value) {
				byte[] testSalt = value.getSalt().toByteArray();
				ByteString resTesthash = ByteString.copyFrom(Passwords.hash(pass.toCharArray(), testSalt));
				HashResponse expected = HashResponse.newBuilder().setUserID(5555).setSalt(ByteString.copyFrom(testSalt))
						.setHash(resTesthash).build();
				assertTrue(value.equals(expected));
			}

			@Override
			public void onError(Throwable t) {
				logger.info("testHash() failed"); 
				fail();
			}

			@Override
			public void onCompleted() {
				logger.info("testHash() passed");
			}
		};
		
		try {
			asyncStub.hash(req, so);
		}finally {
			Thread.currentThread().join(2000);
		}
		
	}
}
