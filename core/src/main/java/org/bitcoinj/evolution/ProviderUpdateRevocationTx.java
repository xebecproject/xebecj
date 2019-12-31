package org.bitcoinj.evolution;


import org.bitcoinj.core.*;
import org.bitcoinj.crypto.BLSPublicKey;
import org.bitcoinj.crypto.BLSSecretKey;
import org.bitcoinj.crypto.BLSSignature;
import org.bitcoinj.script.Script;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public class ProviderUpdateRevocationTx extends SpecialTxPayload {
    public static final int CURRENT_VERSION = 1;
    public static final int MESSAGE_SIZE = 164;
    public static final int MESSAGE_SIZE_WITHOUT_SIGNATURE = MESSAGE_SIZE - 96;

    enum Reason {
        REASON_NOT_SPECIFIED(0),
        REASON_TERMINATION_OF_SERVICE(1),
        REASON_COMPROMISED_KEYS(2),
        REASON_CHANGE_OF_KEYS(3),
        REASON_LAST(REASON_CHANGE_OF_KEYS.value);

        int value;
        Reason(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    Sha256Hash proTxHash;
    int reason;
    Sha256Hash inputsHash;  //replay protection
    BLSSignature signature;

    public ProviderUpdateRevocationTx(NetworkParameters params, Transaction tx) {
        super(params, tx);
    }

    public ProviderUpdateRevocationTx(NetworkParameters params, int version, Sha256Hash proTxHash,
                                      int reason, Sha256Hash inputsHash) {
        super(params, version);
        this.proTxHash = proTxHash;
        this.reason = reason;
        this.inputsHash = inputsHash;
        length = MESSAGE_SIZE_WITHOUT_SIGNATURE;
    }

    public ProviderUpdateRevocationTx(NetworkParameters params, int version, Sha256Hash proTxHash,
                                      int reason, Sha256Hash inputsHash, BLSSignature signature) {
        this(params, version, proTxHash, reason, inputsHash);
        this.signature = signature;
        length = MESSAGE_SIZE;
    }

    public ProviderUpdateRevocationTx(NetworkParameters params, int version, Sha256Hash proTxHash,
                                      int reason, Sha256Hash inputsHash, BLSSecretKey signingKey) {
        this(params, version, proTxHash, reason, inputsHash);
        sign(signingKey);
    }

    @Override
    protected void parse() throws ProtocolException {
        super.parse();  //read version
        proTxHash = readHash();
        reason = readUint16();
        inputsHash = readHash();
        signature = new BLSSignature(params, payload, cursor);
        cursor += signature.getMessageSize();

        length = cursor - offset;
    }

    protected void bitcoinSerializeWithoutSignature(OutputStream stream) throws IOException{
        super.bitcoinSerializeToStream(stream);
        stream.write(proTxHash.getReversedBytes());
        Utils.uint16ToByteStreamLE(reason, stream);
        stream.write(inputsHash.getReversedBytes());
    }
    @Override
    protected void bitcoinSerializeToStream(OutputStream stream) throws IOException {
        bitcoinSerializeWithoutSignature(stream);
        signature.bitcoinSerialize(stream);
    }

    public int getCurrentVersion() {
        return CURRENT_VERSION;
    }

    public String toString() {
        return String.format("ProUpRevTx(version=%d, proTxHash=%s, reason=%s)",
                version, proTxHash, reason);

    }

    @Override
    public Transaction.Type getType() {
        return Transaction.Type.TRANSACTION_PROVIDER_UPDATE_REVOKE;
    }

    @Override
    public String getName() {
        return "providerRegisterTx";
    }

    @Override
    public JSONObject toJson() {
        JSONObject result = new JSONObject();

        result.append("version", version);
        result.append("proTxHash", proTxHash);
        result.append("reason", reason);
        result.append("inputsHash", inputsHash);

        return result;
    }

    @Override
    public Sha256Hash getHash() {
        try {
            UnsafeByteArrayOutputStream bos = new UnsafeByteArrayOutputStream();
            bitcoinSerializeWithoutSignature(bos);
            return Sha256Hash.wrapReversed(Sha256Hash.hashTwice(bos.toByteArray()));
        } catch (IOException x) {
            throw new RuntimeException(x.getMessage());
        }
    }

    public Sha256Hash getSignatureHash() {
        try {
            UnsafeByteArrayOutputStream bos = new UnsafeByteArrayOutputStream();
            bitcoinSerializeWithoutSignature(bos);
            return Sha256Hash.wrap(Sha256Hash.hashTwice(bos.toByteArray()));
        } catch (IOException x) {
            throw new RuntimeException(x.getMessage());
        }
    }

    void sign(BLSSecretKey signingKey) {
        signature = signingKey.Sign(getSignatureHash());
        length = MESSAGE_SIZE;
        unCache();
    }
}
