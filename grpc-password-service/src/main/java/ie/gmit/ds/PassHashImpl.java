package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;

import ie.gmit.ds.PassHashGrpc.PassHashImplBase;

public class PassHashImpl extends PassHashImplBase {

	@Override
	public void hash(ie.gmit.ds.HashRequest request,
			io.grpc.stub.StreamObserver<ie.gmit.ds.HashResponse> responseObserver) {

		byte[] salt = Passwords.getNextSalt();
		byte[] hashed = Passwords.hash(request.getPassword().toCharArray(), salt);
		HashResponse r = HashResponse.newBuilder().setUserID(request.getUserID()).setHash(ByteString.copyFrom(hashed))
				.setSalt(ByteString.copyFrom(salt)).build();

		responseObserver.onNext(r);
		responseObserver.onCompleted();
	}

	@Override
	public void validate(ie.gmit.ds.ValidateRequest request,
			io.grpc.stub.StreamObserver<com.google.protobuf.BoolValue> responseObserver) {

		byte[] salt = request.getSalt().toByteArray();
		byte[] h = request.getHash().toByteArray();
		String pass = request.getPassword();

		boolean isValid = Passwords.isExpectedPassword(pass.toCharArray(), salt, h);

		responseObserver.onNext(BoolValue.of(isValid));
		responseObserver.onCompleted();
	}
}
