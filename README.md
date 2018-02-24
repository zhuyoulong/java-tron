<h1 align="center">
  <br>
  <img width=20% src="https://raw.githubusercontent.com/tronprotocol/wiki/master/images/java-tron.png">
  <br>
  java-tron
  <br>
</h1>

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
[![Gitter](https://img.shields.io/gitter/room/nwjs/nw.js.svg)](https://gitter.im/tronprotocol/java-tron)
[![Build Status](https://travis-ci.org/tronprotocol/java-tron.svg?branch=develop)](https://travis-ci.org/tronprotocol/java-tron)
[![GitHub issues](https://img.shields.io/github/issues/tronprotocol/java-tron.svg)](https://github.com/tronprotocol/java-tron/issues) 
[![GitHub pull requests](https://img.shields.io/github/issues-pr/tronprotocol/java-tron.svg)](https://github.com/tronprotocol/java-tron/pulls)
[![GitHub contributors](https://img.shields.io/github/contributors/tronprotocol/java-tron.svg)](https://github.com/tronprotocol/java-tron/graphs/contributors) 
[![license](https://img.shields.io/github/license/tronprotocol/java-tron.svg)](LICENSE)

# What's TRON?
TRON是一个基于块链的分散式智能协议和应用程序开发平台。它允许每个用户自由地发布，存储和拥有内容和数据，并以分散的自治形式，决定激励机制，通过数字资产分配，流通和交易使应用程序开发人员和内容创建人能够形成分散的内容娱乐生态系统。

TRON是Web 4.0和下一代分散式互联网的产品。
TRON is a block chain-based decentralized smart protocol and an application development platform. It allows each user to freely publish, store and own contents and data, and in the decentralized autonomous form, decides an incentive mechanism and enables application developers and content creators through digital asset distribution, circulation and transaction, thus forming a decentralized content entertainment ecosystem.

TRON is a product of Web 4.0 and the decentralized internet of next generation.

# Quick Start

> Note: This repository is a IDEA project which you can simply download and import.

**Download and build**

```shell
> git clone https://github.com/tronprotocol/java-tron.git
> cd java-tron
> gradle build
```

**Import project to IDEA**

- [File] -> [New] -> [Project from Existing Sources...]
- Select java-tron/build.gradle
- Dialog [Import Project from Gradle], confirm [Use auto-import] and [Use gradle wrapper task configuration] have been
 selected，then select Gradle JVM（JDK 1.8）and click [OK]

# Testing

**Install Kafka and create two topics (block and transaction)**

**Update the configuration**


**Starting program**

IDEA: 
- [Edit Configurations...] -> [Add New Configuration] -> [Application]
- [Edit Configurations...] -> [Main Class]: `org.tron.example.Tron`
- [Edit Configurations...] -> [Use classpath of module]: `java-tron_main`
- [Edit Configurations...] -> [Program arguments]: `--type server`
- Run

![run](https://github.com/tronprotocol/wiki/blob/master/images/commands/default-set.gif)

or simply from terminal:
- ./gradlew run -Pserver=true

**Complete process**

![help](https://github.com/tronprotocol/wiki/blob/master/images/commands/process.gif)

**Other nodes to join need to modify the connection ip**
![help](https://github.com/tronprotocol/wiki/blob/master/images/commands/node-ip.gif)

# Commands
**help**

| Description | Example |
| --- | --- |
| Help tips | `help` |

![help](https://github.com/tronprotocol/wiki/blob/master/images/commands/help.gif)

**account**

| Description | Example |
| --- | --- |
| Get address | `account` |

![help](https://github.com/tronprotocol/wiki/blob/master/images/commands/account.gif)

**getbalance**

| Description | Example |
| --- | --- |
| Get balance | `getbalance` |

![help](https://github.com/tronprotocol/wiki/blob/master/images/commands/getbalance.gif)

**send [to] [balance]**

| Description | Example |
| --- | --- |
| Send balance to address | `send 2cddf5707aefefb199cb16430fb0f6220d460dfe 2` |

![help](https://github.com/tronprotocol/wiki/blob/master/images/commands/send1.gif)

**printblockchain**

| Description | Example |
| --- | --- | 
| Print blockchain | `printblockchain`|

![help](https://github.com/tronprotocol/wiki/blob/master/images/commands/printblockchain.gif)

**exit**

| Description | Example |
| --- | --- |
| Exit | `exit` |

![help](https://github.com/tronprotocol/wiki/blob/master/images/commands/exit.gif)

# Contact

Chat with us via [Gitter](https://gitter.im/tronprotocol/java-tron).

# Contribution
Contributions are welcomed and greatly appreciated. Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details on submitting patches and the contribution workflow.

我经过程序跟踪发现了问题，第一次生成database/nodeId.properties这个文件不要删除,否则钱包地址不一致，执行getbalance命令的时候总是为0，因为钱包地址不一致的时候不会上leveldb中查询余额
