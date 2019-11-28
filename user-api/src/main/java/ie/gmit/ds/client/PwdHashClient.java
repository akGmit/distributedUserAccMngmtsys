package ie.gmit.ds.client;

import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import ie.gmit.ds.HashRequest;
import ie.gmit.ds.HashResponse;
import ie.gmit.ds.PassHashGrpc;
import ie.gmit.ds.PassHashGrpc.PassHashBlockingStub;
import ie.gmit.ds.PassHashGrpc.PassHashStub;
import ie.gmit.ds.User;
import ie.gmit.ds.ValidateRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class PwdHashClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(PwdHashClient.class);
	private ManagedChannel channel;
	private PassHashStub asyncStub;
	private PassHashBlockingStub syncStub;

	public PwdHashClient(String host, int port) {
		LOGGER.info("PwdHashClient using " + host + " and " + port + " port. Initializing...");
		channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		asyncStub = PassHashGrpc.newStub(channel);
		syncStub = PassHashGrpc.newBlockingStub(channel);
	}

	public boolean validate(@NotNull String password, @NotNull byte[] hash, @NotNull byte[] salt) {

		ValidateRequest req = ValidateRequest.newBuilder().setPassword(password).setHash(ByteString.copyFrom(hash))
				.setSalt(ByteString.copyFrom(salt)).build();

		BoolValue valid = BoolValue.newBuilder().setValue(false).build();

		try {
			valid = syncStub.validate(req);
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}
		return valid.getValue();
	}

	public User hash(@NotNull int userID, @NotNull String password, @NotNull User u) {

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
				LOGGER.error(t.getMessage());
			}

			@Override
			public void onCompleted() {
				LOGGER.info("Hashing done!");
			}
		};

		try {
			asyncStub.hash(req, so);
		} catch (Exception e) {
			LOGGER.error(e.toString());
		}

		return user;
	}
}