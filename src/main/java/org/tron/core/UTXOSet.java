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

package org.tron.core;

import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tron.crypto.ECKey;
import org.tron.protos.core.TronTXOutput;
import org.tron.protos.core.TronTXOutputs;
import org.tron.protos.core.TronTXOutputs.TXOutputs;
import org.tron.storage.leveldb.LevelDbDataSourceImpl;
import org.tron.utils.ByteArray;

@Slf4j
public class UTXOSet {
    private Blockchain blockchain;
    private LevelDbDataSourceImpl txDB;

    @Inject
    public UTXOSet(@Named("transaction") LevelDbDataSourceImpl txDb, Blockchain blockchain) {
        this.txDB = txDb;
        this.blockchain = blockchain;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }


    public void reindex() {
        log.info("reindex");

        txDB.resetDB();

        HashMap<String, TXOutputs> utxo = blockchain.findUTXO();

        Set<Map.Entry<String, TXOutputs>> entrySet = utxo.entrySet();

        for (Map.Entry<String, TXOutputs> entry : entrySet) {
            String key = entry.getKey();
            TXOutputs value = entry.getValue();

            for (TronTXOutput.TXOutput ignored : value.getOutputsList()) {
                txDB.putData(ByteArray.fromHexString(key), value.toByteArray());
            }
        }
    }

    public SpendableOutputs findSpendableOutputs(byte[] pubKeyHash, long amount) {
        SpendableOutputs spendableOutputs = new SpendableOutputs();
        HashMap<String, long[]> unspentOutputs = new HashMap<>();
        long accumulated = 0L;

        Set<byte[]> keySet = txDB.allKeys();

        for (byte[] key : keySet) {
            byte[] txOutputsData = txDB.getData(key);
            try {
                TXOutputs txOutputs = TronTXOutputs.TXOutputs.parseFrom(txOutputsData);
                int len = txOutputs.getOutputsCount();
                for (int i = 0; i < len; i++) {
                    TronTXOutput.TXOutput txOutput = txOutputs.getOutputs(i);
                    if (ByteArray.toHexString(ECKey.computeAddress(pubKeyHash))
                            .equals(ByteArray.toHexString(txOutput
                                    .getPubKeyHash()
                                    .toByteArray())) && accumulated < amount) {
                        accumulated += txOutput.getValue();

                        long[] v = unspentOutputs.get(ByteArray.toHexString(key));

                        if (v == null) {
                            v = new long[0];
                        }

                        long[] tmp = Arrays.copyOf(v, v.length + 1);
                        tmp[tmp.length - 1] = i;

                        unspentOutputs.put(ByteArray.toHexString(key), tmp);
                    }
                }
            } catch (InvalidProtocolBufferException e) {
                log.error("解析协议消息以某种方式无效异常", e);
            }
        }

        spendableOutputs.setAmount(accumulated);
        spendableOutputs.setUnspentOutputs(unspentOutputs);

        return spendableOutputs;
    }

    public List<TronTXOutput.TXOutput> findUTXO(byte[] pubKeyHash) {
        List<TronTXOutput.TXOutput> utxos = Lists.newArrayList();

        Set<byte[]> keySet = txDB.allKeys();
        for (byte[] key : keySet) {
            byte[] txData = txDB.getData(key);
            try {
                TXOutputs txOutputs = TXOutputs.parseFrom(txData);
                for (TronTXOutput.TXOutput txOutput : txOutputs.getOutputsList()) {
                    //97652a55acea9229fcf80fe4a79a414c4e467853 钱包地址
//          String pubKeyHashStr = ByteArray.toHexString(ECKey.computeAddress(pubKeyHash));
//          logger.info("pubKeyHashStr={}",pubKeyHashStr);
                    //存到leveldb中的
//          String txDataPubKeyHashStr = ByteArray.toHexString(txOutput.toByteArray());
//          logger.info("txDataPubKeyHashStr={}",txDataPubKeyHashStr);
                    if (ByteArray.toHexString(ECKey.computeAddress(pubKeyHash))
                            .equals(ByteArray.toHexString(txOutput
                                    .getPubKeyHash()
                                    .toByteArray()))) {
                        utxos.add(txOutput);
                    }
                }
            } catch (InvalidProtocolBufferException e) {
                log.error("解析协议消息以某种方式无效异常", e);
            }
        }
        return utxos;
    }
}
