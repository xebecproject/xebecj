package org.bitcoinj.evolution;


import org.bitcoinj.core.*;
import org.bitcoinj.crypto.BLSPublicKey;
import org.bitcoinj.script.Script;
import org.json.JSONObject;
import org.spongycastle.util.encoders.Base64;

import java.io.IOException;
import java.io.OutputStream;

public class ProviderRegisterTx extends SpecialTxPayload {
    public static final int CURRENT_VERSION = 1;
    public static final int MESSAGE_SIZE = 274;
    public static final int MESSAGE_SIZE_WITHOUT_SIGNATURE = 209;


    int type; //short
    int mode;  //short
    TransactionOutPoint collateralOutpoint;
    MasternodeAddress address;
    KeyId keyIDOwner;
    BLSPublicKey pubkeyOperator;
    KeyId keyIDVoting;
    int operatorReward;
    Script scriptPayout;
    Sha256Hash inputsHash;  //replay protection
    MasternodeSignature signature;

    public ProviderRegisterTx(NetworkParameters params, Transaction tx) {
        super(params, tx);
    }

    public ProviderRegisterTx(NetworkParameters params, int version, int type, int mode, TransactionOutPoint collateralOutpoint,
            MasternodeAddress address, KeyId keyIDOwner, BLSPublicKey pubkeyOperator,
            KeyId keyIDVoting, int operatorReward,
            Script scriptPayout, Sha256Hash inputsHash) {
        super(params, version);
        this.type = type;
        this.mode = mode;
        this.collateralOutpoint = collateralOutpoint;
        this.address = address.duplicate();
        this.keyIDVoting = keyIDVoting;
        this.keyIDOwner = keyIDOwner;
        this.pubkeyOperator = pubkeyOperator;
        this.operatorReward = operatorReward;
        this.scriptPayout = scriptPayout;
        this.inputsHash = inputsHash;
        length = MESSAGE_SIZE_WITHOUT_SIGNATURE;
    }

    public ProviderRegisterTx(NetworkParameters params, int version, int type, int mode, TransactionOutPoint collateralOutpoint,
                              MasternodeAddress address, KeyId keyIDOwner, BLSPublicKey pubkeyOperator,
                              KeyId keyIDVoting, int operatorReward,
                              Script scriptPayout, Sha256Hash inputsHash, MasternodeSignature signature) {
        this(params, version, type, mode, collateralOutpoint, address, keyIDOwner, pubkeyOperator, keyIDVoting, operatorReward, scriptPayout, inputsHash);
        this.signature = signature;
        length = MESSAGE_SIZE;
    }

    public ProviderRegisterTx(NetworkParameters params, int version, int type, int mode, TransactionOutPoint collateralOutpoint,
                              MasternodeAddress address, KeyId keyIDOwner, BLSPublicKey pubkeyOperator,
                              KeyId keyIDVoting, int operatorReward,
                              Script scriptPayout, Sha256Hash inputsHash, ECKey signingKey) {
        this(params, version, type, mode, collateralOutpoint, address, keyIDOwner, pubkeyOperator, keyIDVoting, operatorReward, scriptPayout, inputsHash);
        sign(signingKey);
    }

    @Override
    protected void parse() throws ProtocolException {
        super.parse();
        type = readUint16();
        mode = readUint16();
        collateralOutpoint = new TransactionOutPoint(params, payload, cursor);
        cursor += collateralOutpoint.getMessageSize();
        address = new MasternodeAddress(params, payload, cursor, NetworkParameters.ProtocolVersion.CURRENT.getBitcoinProtocolVersion());
        cursor += address.getMessageSize();

        keyIDOwner = new KeyId(params, payload, cursor);
        cursor += keyIDOwner.getMessageSize();
        pubkeyOperator = new BLSPublicKey(params, payload, cursor);
        cursor += pubkeyOperator.getMessageSize();
        keyIDVoting = new KeyId(params, payload, cursor);
        cursor += keyIDVoting.getMessageSize();

        operatorReward = readUint16();
        scriptPayout = new Script(readByteArray());
        inputsHash = readHash();
        signature = new MasternodeSignature(params, payload, cursor);
        cursor += signature.getMessageSize();

        length = cursor - offset;
    }

    protected void bitcoinSerializeWithoutSignature(OutputStream stream) throws IOException{
        super.bitcoinSerializeToStream(stream);
        Utils.uint16ToByteStreamLE(type, stream);
        Utils.uint16ToByteStreamLE(mode, stream);
        collateralOutpoint.bitcoinSerialize(stream);
        address.bitcoinSerialize(stream);

        keyIDOwner.bitcoinSerialize(stream);
        pubkeyOperator.bitcoinSerialize(stream);
        keyIDVoting.bitcoinSerialize(stream);

        Utils.uint16ToByteStreamLE(operatorReward, stream);
        Utils.bytesToByteStream(scriptPayout.getProgram(), stream);
        stream.write(inputsHash.getReversedBytes());
    }
    @Override
    protected void bitcoinSerializeToStream(OutputStream stream) throws IOException {
        bitcoinSerializeWithoutSignature(stream);
        if(signature != null)
            signature.bitcoinSerialize(stream);
        else MasternodeSignature.createEmpty().bitcoinSerialize(stream);
    }

    public int getCurrentVersion() {
        return CURRENT_VERSION;
    }

    public String toString() {
        String payee = "unknown";
        try {
            payee = scriptPayout.getToAddress(params).toString();
        } catch (AddressFormatException x) {
            //swallow
        } catch (ScriptException x) {
            //swallow
        }
        return String.format("ProRegTx(version=%d, collateralOutpoint=%s, address=%s, operatorReward=%f, ownerAddress=%s, pubKeyOperator=%s, votingAddress=%s, scriptPayout=%s)",
                version, collateralOutpoint.toStringShort(), address, (double)operatorReward / 100,
                new Address(params, params.getAddressHeader(), keyIDOwner.getBytes()), pubkeyOperator,
                new Address(params, params.getAddressHeader(), keyIDVoting.getBytes()), payee);

    }

    @Override
    public Transaction.Type getType() {
        return Transaction.Type.TRANSACTION_PROVIDER_REGISTER;
    }

    @Override
    public String getName() {
        return "providerRegisterTx";
    }

    @Override
    public JSONObject toJson() {
        JSONObject result = new JSONObject();

        result.append("version", version);
        result.append("collateralHash", collateralOutpoint.getHash());
        result.append("collateralIndex", (int)collateralOutpoint.getIndex());
        result.append("service", address.toString());
        result.append("ownerAddress", new Address(params, params.getAddressHeader(), keyIDOwner.getBytes()));
        result.append("votingAddress", new Address(params, params.getAddressHeader(), keyIDVoting.getBytes()));

        try {
            Address destination = scriptPayout.getToAddress(params);
            result.append("payoutAddress", destination);
        } catch (AddressFormatException x) {
            //swallow
        }
        result.append("pubKeyOperator", pubkeyOperator);
        result.append("operatorReward", (double)operatorReward / 100);

        result.append("inputsHash", inputsHash);

        return result;
    }

    String makeSignString()
    {
        StringBuilder s = new StringBuilder();

        // We only include the important stuff in the string form...

        String strPayout;
        try {
            strPayout = scriptPayout.getToAddress(params).toString();
        } catch(ScriptException x) {
            strPayout = Utils.HEX.encode(scriptPayout.getProgram());
        }

        s.append(strPayout + "|");
        s.append(String.format("%d", operatorReward) + "|");
        s.append(new Address(params, params.getAddressHeader(), keyIDOwner.getBytes()).toString() + "|");
        s.append(new Address(params, params.getAddressHeader(), keyIDVoting.getBytes()).toString() + "|");

        // ... and also the full hash of the payload as a protection against malleability and replays
        s.append(Utils.HEX.encode(getHash().getBytes()));

        return s.toString();
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

    void sign(ECKey signingKey) {
        signature = MessageSigner.signMessage(makeSignString(), signingKey);
        length = MESSAGE_SIZE;
        unCache();
    }
}
