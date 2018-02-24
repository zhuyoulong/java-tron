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

package org.tron.command;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.ArrayList;
import java.util.List;

import org.tron.application.CliApplication;
import org.tron.peer.Peer;
import org.tron.protos.core.TronTXOutput;

public class GetBalanceCommand extends Command {
  public GetBalanceCommand() {
  }

  @Override
  public void execute(CliApplication app, String[] parameters) {
    Peer peer = app.getPeer();

    byte[] pubKeyHash = peer.getWallet().getEcKey().getPubKey();
    List<TronTXOutput.TXOutput> utxos = peer.getUTXOSet().findUTXO(pubKeyHash);

    long balance = 0;

    for (TronTXOutput.TXOutput txOutput : utxos) {
      balance += txOutput.getValue();
    }

    System.out.println(balance);
  }

  @Override
  public void usage() {
    System.out.println("");

    System.out.println(ansi().eraseScreen().render(
        "@|magenta,bold USAGE|@\n\t@|bold getbalance|@"
    ));

    System.out.println("");

    System.out.println(ansi().eraseScreen().render(
        "@|magenta,bold DESCRIPTION|@\n\t@|bold The command 'getbalance' get your balance.|@"
    ));

    System.out.println("");
  }

  @Override
  public boolean check(String[] parameters) {
    return true;
  }
}
