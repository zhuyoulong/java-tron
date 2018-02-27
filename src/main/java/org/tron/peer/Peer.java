/*
 * java-tron is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * java-tron is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tron.peer;

import com.google.protobuf.InvalidProtocolBufferException;
import org.tron.command.ConsensusCommand;
import org.tron.consensus.client.BlockchainClientListener;
import org.tron.consensus.client.Client;
import org.tron.core.Blockchain;
import org.tron.core.PendingStateImpl;
import org.tron.core.TransactionUtils;
import org.tron.core.UTXOSet;
import org.tron.crypto.ECKey;
import org.tron.overlay.Net;
import org.tron.protos.core.TronBlock;
import org.tron.protos.core.TronTransaction;
import org.tron.utils.ByteArray;
import org.tron.wallet.Wallet;

public class Peer {

    private Blockchain blockchain;
    private UTXOSet utxoSet;
    private Wallet wallet;

    private String type;

    /**
     * 私钥
     */
    private ECKey myKey;

    private Client client;

    public Peer(
            String type,
            Blockchain blockchain,
            UTXOSet utxoSet,
            Wallet wallet,
            Client client,
            ECKey key) {

        this();
        this.setType(type);
        this.setBlockchain(blockchain);
        this.setUTXOSet(utxoSet);
        this.setWallet(wallet);
        this.setClient(client);
        this.myKey = key;
        init();
    }

    public Peer() {

    }

    public void addReceiveTransaction(String message) {
        try {
            TronTransaction.Transaction transaction = TronTransaction
                    .Transaction.parseFrom(ByteArray.fromHexString(message));
            System.out.println(TransactionUtils.toPrintString
                    (transaction));
            PendingStateImpl pendingState = new PendingStateImpl();
            pendingState.addPendingTransaction(blockchain, transaction);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    public void addReceiveBlock(String message) {
        TronBlock.Block block = null;
        try {
            block = TronBlock.Block.parseFrom(ByteArray.fromHexString(message
            ));
            blockchain.receiveBlock(block, utxoSet, this);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        blockchain.addListener(new BlockchainClientListener(client, this));
        initLoadBlock();
        if (client != null) {
            new ConsensusCommand(client).listen(this, this.type);
        }
    }

    private void initLoadBlock() {
        if (this.type.equals(PeerType.PEER_NORMAL) && client != null) {
            System.out.println("BlockChain loading  ...");
            client.loadBlock(this);
        }
    }

    public ECKey getMyKey() {
        return myKey;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    public UTXOSet getUTXOSet() {
        return utxoSet;
    }

    public void setUTXOSet(UTXOSet utxoSet) {
        this.utxoSet = utxoSet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {

        if (!PeerType.isValid(type)) {
            throw new IllegalArgumentException("Invalid type given");
        }

        this.type = type;
    }

    public Net getNet() {
        return null;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
