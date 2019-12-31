package org.bitcoinj.quorums;

import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.UnsafeByteArrayOutputStream;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.BLSPublicKey;

import java.io.IOException;
import java.util.ArrayList;

public class LLMQUtils {
    static public Sha256Hash buildCommitmentHash(LLMQParameters.LLMQType llmqType, Sha256Hash blockHash, ArrayList<Boolean> validMembers, BLSPublicKey pubKey, Sha256Hash vvecHash)
    {
        try {
            UnsafeByteArrayOutputStream bos = new UnsafeByteArrayOutputStream();
            bos.write(llmqType.getValue());
            bos.write(blockHash.getReversedBytes());
            Utils.booleanArrayListToStream(validMembers, bos);
            pubKey.bitcoinSerialize(bos);
            bos.write(vvecHash.getReversedBytes());
            return Sha256Hash.wrap(Sha256Hash.hashTwice(bos.toByteArray()));
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }


    static public Sha256Hash buildSignHash(int llmqType, Sha256Hash quorumHash, Sha256Hash id, Sha256Hash msgHash)
    {
        try {
            UnsafeByteArrayOutputStream bos = new UnsafeByteArrayOutputStream();
            bos.write(llmqType);
            bos.write(quorumHash.getReversedBytes());
            bos.write(id.getBytes());
            bos.write(msgHash.getReversedBytes());
            return Sha256Hash.wrap(Sha256Hash.hashTwice(bos.toByteArray()));
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

    static public Sha256Hash buildSignHash(LLMQParameters.LLMQType llmqType, Sha256Hash quorumHash, Sha256Hash id, Sha256Hash msgHash)
    {
        return buildSignHash(llmqType.getValue(), quorumHash, id, msgHash);
    }

    public static Sha256Hash buildSignHash(RecoveredSignature recoveredSignature) {
        return buildSignHash(recoveredSignature.llmqType, recoveredSignature.quorumHash, recoveredSignature.id,
                recoveredSignature.msgHash);
    }

    static public Sha256Hash buildLLMQBlockHash(LLMQParameters.LLMQType llmqType, Sha256Hash blockHash)
    {
        try {
            UnsafeByteArrayOutputStream bos = new UnsafeByteArrayOutputStream();
            bos.write(llmqType.getValue());
            bos.write(blockHash.getReversedBytes());
            return Sha256Hash.wrapReversed(Sha256Hash.hashTwice(bos.toByteArray()));
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }
}
