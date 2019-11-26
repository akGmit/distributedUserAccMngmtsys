package ie.gmit.ds.client;

import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PassHashGrpc;
import ie.gmit.ds.ValidateRequest;
import ie.gmit.ds.PassHashGrpc.PassHashBlockingStub;
import ie.gmit.ds.PassHashGrpc.PassHashStub;
import ie.gmit.ds.User;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class PwdHashClient {
	private final Logger logger = Logger.getLogger(PwdHashClient.class.getName());
	private ManagedChannel channel;
	private PassHashStub asyncStub;
	private PassHashBlockingStub syncStub;

	public PwdHashClient(String host, int port) {
		logger.info("PwdHashClient creating...");
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		asyncStub = PassHashGrpc.newStub(channel);
		syncStub = PassHashGrpc.newBlockingStub(channel);
		logger.info("Created!");
	}

	public boolean validate(@NotNull String password, @NotNull byte[] hash, @NotNull byte[] salt) {

		logger.info("Validating...");

		ValidateRequest req = ValidateRequest.newBuilder().setPassword(password).setHash(ByteString.copyFrom(hash))
				.setSalt(ByteString.copyFrom(salt)).build();

		BoolValue valid = BoolValue.newBuilder().setValue(false).build();

		try {
			valid = syncStub.validate(req);
		} catch (Exception e) {
			logger.warning("Validation: something went wrong");
		}

		return valid.getValue();
	}

	public User hash(@NotNull int userID, @NotNull String password, @NotNull User u) {
		logger.info("Getting hash and salt...");

		User user = u;

		HashRequest req = HashRequest.newBuilder().setUserID(userID).setPassword(password).build();

		StreamObserver<HashResponse> so = new StreamObserver<HashResponse>() {

			@Override
			public void onNext(HashResponse value) {
				user.setHashedPassword(value.getHash().toByteArray());
				user.setSalt(value.getSalt().toByteArray());
			}

			@Override
			public void onError(Throwable t) {
				logger.info("Hashing: something went wrong.");
			}

			@Override
			public void onCompleted() {
				logger.info("Hashing done!");
			}
		};

		try {
			asyncStub.hash(req, so);
		} catch (Exception e) {
			logger.warning("Hashing: something went wrong.");
		}

		return user;
	}
}
