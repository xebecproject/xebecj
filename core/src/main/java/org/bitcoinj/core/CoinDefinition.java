package org.bitcoinj.core;

import java.math.BigInteger;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Hash Engineering Solutions
 * Date: 5/3/14
 * To change this template use File | Settings | File Templates.
 */
public class CoinDefinition {


    public static final String coinName = "Xebec";
    public static final String coinTicker = "Xebec";
    public static final String coinURIScheme = "xebec";
    public static final String cryptsyMarketId = "155";
    public static final String cryptsyMarketCurrency = "BTC";
    public static final String PATTERN_PRIVATE_KEY_START_UNCOMPRESSED = "[7]";
    public static final String PATTERN_PRIVATE_KEY_START_COMPRESSED = "[X]";

    public enum CoinPrecision {
        Coins,
        Millicoins,
    }
    public static final CoinPrecision coinPrecision = CoinPrecision.Coins;

    public static final String UNSPENT_API_URL = "https://xplorer.xebec.site/api.dws?q=unspent";
    public enum UnspentAPIType {
        BitEasy,
        Blockr,
        Abe,
        Cryptoid,
    };
    public static final UnspentAPIType UnspentAPI = UnspentAPIType.Cryptoid;

    public static final String BLOCKEXPLORER_BASE_URL_PROD = "http://xplorer.xebec.site/";    //blockr.io
    public static final String BLOCKEXPLORER_ADDRESS_PATH = "address/";             //blockr.io path
    public static final String BLOCKEXPLORER_TRANSACTION_PATH = "tx/";              //blockr.io path
    public static final String BLOCKEXPLORER_BLOCK_PATH = "block/";                 //blockr.io path
    public static final String BLOCKEXPLORER_BASE_URL_TEST = "http://test.xebec.site/";

    public static final String DONATION_ADDRESS = "Xui6dJWsxGKQh4mbX8UdX5ESoXySS5RAS2";  //Hash Engineering donation DASH address

    enum CoinHash {
        SHA256,
        scrypt,
        x11
    };
    public static final CoinHash coinPOWHash = CoinHash.x11;

    public static boolean checkpointFileSupport = true;

    public static final int TARGET_TIMESPAN = (int)(7200);
    public static final int TARGET_SPACING = (int)(60);
    public static final int INTERVAL = TARGET_TIMESPAN / TARGET_SPACING;  //120 blocks

    public static final int getInterval(int height, boolean testNet) {
            return INTERVAL;      //108
    }
    public static final int getIntervalCheckpoints() {
            return INTERVAL;

    }
    public static final int getTargetTimespan(int height, boolean testNet) {
            return TARGET_TIMESPAN;    //72 min
    }

    public static int spendableCoinbaseDepth = 50; //main.h: static const int COINBASE_MATURITY
    public static final long MAX_COINS = 22000000;                 //main.h:  MAX_MONEY


    public static final long DEFAULT_MIN_TX_FEE = 10000;   // MIN_TX_FEE
    public static final long DUST_LIMIT = 5460; //Transaction.h CTransaction::GetDustThreshold for 10000 MIN_TX_FEE
    public static final long INSTANTX_FEE = 100000; //0.001 DASH (updated for 12.1)
    public static final boolean feeCanBeRaised = false;

    //
    // Dash 0.12.1.x
    //
    public static final int PROTOCOL_VERSION = 70210;          //version.h PROTOCOL_VERSION
    public static final int MIN_PROTOCOL_VERSION = 70209;        //version.h MIN_PROTO_VERSION

    public static final int BLOCK_CURRENTVERSION = 2;   //CBlock::CURRENT_VERSION
    public static final int MAX_BLOCK_SIZE = 1 * 1000 * 1000;


    public static final boolean supportsBloomFiltering = true; //Requires PROTOCOL_VERSION 70000 in the client

    public static final int Port    = 27270;       //protocol.h GetDefaultPort(testnet=false)
    public static final int TestPort = 27271;     //protocol.h GetDefaultPort(testnet=true)

    //
    //  Production
    //
    public static final int AddressHeader = 76;             //base58.h CBitcoinAddress::PUBKEY_ADDRESS
    public static final int p2shHeader = 16;             //base58.h CBitcoinAddress::SCRIPT_ADDRESS
    public static final int dumpedPrivateKeyHeader = 128;   //common to all coins
    public static final long oldPacketMagic = 0x68455549;      //0xfb, 0xc0, 0xb6, 0xdb
    public static final long PacketMagic = 0x68455549;

    //Genesis Block Information from main.cpp: LoadBlockIndex
    static public long genesisBlockDifficultyTarget = (0x1e0ffff0L);         //main.cpp: LoadBlockIndex
    static public long genesisBlockTime = 1577048270L;                       //main.cpp: LoadBlockIndex
    static public long genesisBlockNonce = (1435894);                         //main.cpp: LoadBlockIndex
    static public String genesisHash = "0000043b448cb9d63d11bb4d56f7672831f447764cf3c1f9b61f9e560696178d"; //main.cpp: hashGenesisBlock
    static public String genesisMerkleRoot = "e0028eb9648db56b1ac77cf090b99048a8007e2bb64b68f092c03c7f56a662c7";
    static public int genesisBlockValue = 50;                                                              //main.cpp: LoadBlockIndex
    //taken from the raw data of the block explorer
   // static public String genesisTxInBytes = "04ffff001d01044c5957697265642030392f4a616e2f3230313420546865204772616e64204578706572696d656e7420476f6573204c6976653a204f76657273746f636b2e636f6d204973204e6f7720416363657074696e6720426974636f696e73";   //"limecoin se convertira en una de las monedas mas segura del mercado, checa nuestros avances"
   // static public String genesisTxOutBytes = "040184710fa689ad5023690c80f3a49c8f13f8d45b8c857fbcbc8bc4a8e4d3eb4b10f4d4604fa08dce601aaf0f470216fe1b51850b4acf21b179c45070ac7b03a9";

    //net.cpp strDNSSeed
    static public String[] dnsSeeds = new String[] {
            "dnsseed.xebec.site",
            "dnsseed2.xebec.site",
    };


    public static int minBroadcastConnections = 0;   //0 for default; Using 3 like BreadWallet.

    //
    // TestNet - DASH
    //
    public static final boolean supportsTestNet = true;
    public static final int testnetAddressHeader = 140;             //base58.h CBitcoinAddress::PUBKEY_ADDRESS_TEST
    public static final int testnetp2shHeader = 19;             //base58.h CBitcoinAddress::SCRIPT_ADDRESS_TEST
    public static final long testnetPacketMagic = 0xce2ecaff;      //
    public static final String testnetGenesisHash = "000009cf3f4d499d62e2ec9e67316bf69f871fa48b03fdec2480fda211c11f04";
    static public long testnetGenesisBlockDifficultyTarget = (0x1e0ffff0L);         //main.cpp: LoadBlockIndex
    static public long testnetGenesisBlockTime = 1577045108L;                       //main.cpp: LoadBlockIndex
    static public long testnetGenesisBlockNonce = (1030818L);                         //main.cpp: LoadBlockIndex





    //main.cpp GetBlockValue(height, fee)
    public static final Coin GetBlockReward(int height)
    {
        int COIN = 1;
        Coin nSubsidy = Coin.valueOf(100, 0);
        if (height == 1)
            nSubsidy = Coin.valueOf(420000, 0);
        return nSubsidy;
    }

    public static int subsidyDecreaseBlockCount = 210240;     //main.cpp GetBlockValue(height, fee)

    public static BigInteger proofOfWorkLimit = Utils.decodeCompactBits(0x1e0fffffL);  //main.cpp bnProofOfWorkLimit (~uint256(0) >> 20); // digitalcoin: starting difficulty is 1 / 2^12

    static public String[] testnetDnsSeeds = new String[] {
            "test.xebec.site",
    };
    //from main.h: CAlert::CheckSignature
    public static final String SATOSHI_KEY = "0467004605d9b40e006c157e82ac994e530e15b1acfc9252460d3c0e149792cbb5f24399def0345cfa6fa3aa4c5bd5c0ef586a39abc92b918d2c5ec39cd6b96943";
    public static final String TESTNET_SATOSHI_KEY = "04517d8a699cb43d3938d7b24faaff7cda448ca4ea267723ba614784de661949bf632d6304316b244646dea079735b9a6fc4af804efb4752075b9fe2245e14e412";

    /** The string returned by getId() for the main, production network where people trade things. */
    public static final String ID_MAINNET = "org.darkcoin.production";
    /** The string returned by getId() for the testnet. */
    public static final String ID_TESTNET = "org.darkcoin.test";
    /** Unit test network. */
    public static final String ID_UNITTESTNET = "com.google.darkcoin.unittest";

    //checkpoints.cpp Checkpoints::mapCheckpoints
    public static void initCheckpoints(Map<Integer, Sha256Hash> checkpoints)
    {

        checkpoints.put(  1, Sha256Hash.wrap("0000042e8a528c958c14eb192729b4e1ad72c574131f74025413514b1f2bcf45"));
        checkpoints.put(  10, Sha256Hash.wrap("000004de8465ca3858d90f116330aa13839cdd1b744c08da635a88a365d507c1"));
        checkpoints.put(  700, Sha256Hash.wrap("0000033e08a2fda184ae2e17342eba2448673001f53cda965d161e49696a1e11"));

    }

    //Unit Test Information
    public static final String UNITTEST_ADDRESS = "XgxQxd6B8iYgEEryemnJrpvoWZ3149MCkK";
    public static final String UNITTEST_ADDRESS_PRIVATE_KEY = "XDtvHyDHk4S3WJvwjxSANCpZiLLkKzoDnjrcRhca2iLQRtGEz1JZ";

}
