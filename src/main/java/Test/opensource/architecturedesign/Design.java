package Test.opensource.architecturedesign;

/*
前端高可用的组合:LVS+Keepalived、Nginx+Keepalived、HAproxy+Keepalived
 */
public class Design {


    /*
    nginx: keepalived + haproxy
    服务：nginx 反向代理

    redis:集群分片部署， lua 脚本实现写入至少有一个副本写入。设置至少一个节点写入成功
           Redis 选主 = 故障主节点的从节点（候选者） + 其他所有正常主节点（投票者）。 从节点不能投票，主节点不能竞选对方分片的主。


    rabbitMq:仲裁队列（不能实现水平扩容问题）取代镜像队列。  强一致性：基于 Raft 共识算法，消息必须在超过半数的副本持久化后才确认，有效防止数据丢失。   不支持消息优先级、队列TTL、非持久化消息等高级特性。
    mysql:主从复制模式：一主一从"或"一主多从
          keepalived(vip)+半同步复制（至少一个副本写入成功，不要全同步复制性能差）+GTID（优于主主模式）
          主库将事务写入Binlog后，需等待至少一个从库确认已接收并写入其中继日志（Relay Log），然后才向客户端返回成功。
          如果写入中继日志主崩溃，从库还没有应用这些日志（即还没有写入自己的数据文件）此时数据不一致

   MySQL InnoDB Cluster 代替双主
   MySQL Group Replication (MGR) 多主模式


   过半选举，永远是指“集群总节点数”的一半，而不是“当前存活节点数”的一半。
    */

    //endregion

    //region 高可用集群设计

       /*
    raft协议： RabbitMQ 仲裁队列
             RocketMQ 的 DLedger 模式 (Raft 内嵌)、 4.5 版之后Controller 模式 (独立组件)
             Kafka Apache Kafka 从 4.0 版本开始，也全面采用 Raft 协议（具体实现为 KRaft）来管理集群元数据了

             message RequestVoteRequest {
    int term;           // 任期号
    int candidateId;    // 候选者ID
    int lastLogIndex;   // 候选者最后一条日志的索引
    int lastLogTerm;    // 候选者最后一条日志的任期
}
投票者（Voter）会做如下判断：
如果候选者的 lastLogTerm > 投票者的 lastLogTerm：投票者投给候选者。
如果 lastLogTerm 相等：比较 lastLogIndex，谁的更大（日志更新），就投给谁。
如果候选者的日志比投票者旧：投票者拒绝投票。


Raft 选举 Leader 时，必须比较日志的 (Term, Index)，确保选出的 Leader 拥有最完整的数据
        */


    /*
    ZooKeeper (ZK) 用的是一种名叫 ZAB (ZooKeeper Atomic Broadcast) 的专有协议，并不是 Raft 协议。不过它们的设计思想很像“师出同门”，
    都是对 Paxos 协议的工程化改进，核心都采用了主从（Leader/Follower）模式和过半确认（Quorum）机制。
     */

    //region redis 脑裂
    /*
    redis选主：3主3从。集群设置开启 min-replicas-to-write 1 和 min-replicas-max-lag 10 这两个参数。
    从节点选举候选者，其他主节点推举候选者成为主节点，过半选举
    需要“多数派”主节点：Redis 集群的故障转移，必须由超过半数（N/2 + 1）的主节点投票同意才能进行。如果只有 2 台机器，意味着最多只能有 2 个主节点。当其中 1 台宕机或网络隔离时，剩下的 1 个主节点无法独自完成“过半”投票，集群将无法进行自动故障转移。

需要足够的从节点：防脑裂的配置 min-replicas-to-write 1，要求每个主节点至少有 1 个从节点保持实时同步。要实现这个要求，3 台机器是最小单位。例如，将 Master A 放在机器1，它的从节点 Slave A 放在机器2，这样任何一台机器故障，都不会让主从同时失联。
     */
    //endregion


    /*
    lvs :LVS（Linux Virtual Server）：LVS是基于Linux操作系统的负载均衡软件，它通过网络地址转换（NAT）
         或直接路由（DR）的方式将请求分发到后端服务器群集。LVS使用IP负载均衡技术，可以根据不同的负载均衡算法（如轮询、加权轮询、源IP哈希等）
         将请求分发给后端服务器，并支持实现高可用性和故障恢复。
   F5:硬件负载均衡，作用于网络层，请求转发到对应服务机器
   nginx:应用层负载均衡
    */

    //region 主备
    /*
    nginx 反向代理 backup
    Keepalived 主备 vip 漂移  。解决单点故障
     */
    //endregion

    //region  nginx HA

    /*
    热备方案：中间件， Keepalived、nginx、roseHA
    客户端和服务端加一个中间件，中间负责和服务通信，转发客户端和服务端通信，
    相当于把nginx的负载均衡去掉了，但是session要共享，比如放在redis中.
    可以自己实现一个这样的中间件。中间件用keepalived活nginx做高可用.

    keepalived 主备
    集群：

    */

    //region  Keepalived
    /*
    VRRP的出现是为了解决静态路由的单点故障。
    Keepalived高可用设计。VRRP全称 Virtual Router Redundancy Protocol，即 虚拟路由冗余协议。
    虚拟ip 漂移


    //keepalived 通过检测服务器的mysql 服务判断可用不可用关闭 mysql 服务
//#!/bin/bash
//# 检查 MySQL 服务是否正常运行
//if systemctl is-active --quiet mysqld; then
//    exit 0  # MySQL 正常，返回 0
//            else
//    exit 1  # MySQL 异常，返回 1
//    fi


    //region 工作流程
    3. Keepalived 的工作流程
    3.1 初始化
    每个节点启动 Keepalived 服务，读取配置文件。
    根据配置的优先级和虚拟路由器 ID，参与 VRRP 选举。

    3.2 主节点工作
    主节点定期发送 VRRP 通告消息，告知备份节点自己的状态。
    主节点绑定 VIP 并开始处理数据流量。
    主节点执行健康检查脚本，监控服务的状态。

    3.3 故障检测与切换
    如果主节点的健康检查失败，Keepalived 会降低主节点的优先级。
    备份节点检测到主节点的优先级降低或未收到通告消息，触发选举机制。
    优先级最高的备份节点成为新的主节点，并绑定 VIP。

     3.4 恢复
    当原主节点恢复后，Keepalived 会重新参与选举。
    如果原主节点的优先级高于当前主节点，VIP 会切换回原主节点。
   //endregion





    每台机器都装keepalived、haproxy ,他们只能用于linux服务器
    Keepalived 保证nginx 可用，nginx 动静分离，负载均衡
    主备：主干活，备不干活，资源浪费，可以用主备模式。session redis.
    互为主备：主备都干活。每个机器配置两个vrrp_instance, keepalived 是通过虚拟ip (vip)访问的，两个虚拟ip设置互为主备
    windows 自带的NLB 代替linux 的keepalived ，每台机器装一个keepalived，nginx 互为主备

    访问虚拟IP:keepalived 配置虚拟ip地址，虚拟ip地址在和nginx关联，就可以访问nginx.  几台Nginx 配置一样。 两台互为主备的Keepalived根据网络情况路由访问哪一台机器的nginx
             执行脚本判断nginx服务是否存在，不存在就杀掉keepalived进程。
    Keepalive 三大组件中的check 组件，监控nginx进程的脚本，如果nginx进程挂了，没有重启成功，keepalived自己停止服务，这样keepalived集群就知道应用状态。

   keepalived 配置 ： 虚拟地址-->虚拟服务器地址-->真实服务器地址
   nginx keepalived 配置参考
     */



/*
 集群高可用设计：
 每台服务器安装 Keepalived 和 haproxy nginx
 keepalive 解决haproxy的单点故障问题。保证haproxy的高可用。
 haproxy反向代理nginx实现负载均衡。haproxy一般非web服务器。
 nginx web服务器动静分离。

 客户端直接访问haproxy,haproxy反向代理到nginx,nginx反向代理到真正web服务器。

 部署参考链接
 https://blog.csdn.net/zhou641694375/article/details/127549434?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-127549434-blog-127729911.235%5Ev40%5Epc_relevant_anti_vip&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-127549434-blog-127729911.235%5Ev40%5Epc_relevant_anti_vip&utm_relevant_index=2

 HAProxy 用作LB（负载均衡）反向代理， 不能以web server 的方式工作。
 可作web server 的是Apache 或Nginx。
 HAProxy 可安装在其前端提供提供负载均衡和高可用。

web 服务器：Apache nginx tomcat iis
          同时使用apache和nginx，静态网页有nginx处理，动态交由apache处理，


          Tomcat是一个Java Servlet容器，可以运行Java Web应用程序
 */




    /*
    软件
    LVS、Haproxy、Nginx
    硬件
    F5

   LVS（Linux Virtual Server）即Linux虚拟服务器，

  吞吐量：tomcat<nginx <lvs < F5
  nginx 吞吐量 5--10W
     */
//endregion

    //region  Keepalived使用
    /*
    notify_master：在Keepalived状态切换到Master时执行指定的脚本

notify_backup：在Keepalived状态切换到backup时执行指定的脚本

notify_fault：在Keepalived检测到故障时执行指定的脚本

notify：在任何状态变化（Master、Backup或Fault）时执行指定的脚本

notify_stop：在Keepalived服务挂掉时执行指定的脚本，kill或者stop等停止keepalived的命令有效


 notify_master "/path/to/your/script.sh INSTANCE MASTER"
    notify_backup "/path/to/your/script.sh INSTANCE BACKUP"
    notify_fault "/path/to/your/script.sh INSTANCE FAULT"

#!/bin/bash

# Keepalived 传递的参数：
# $1 = "GROUP" 或 "INSTANCE"
# $2 = "MASTER" 或 "BACKUP" 或 "FAULT"
# $3 = 优先级（可选）

CONFIG_FILE="/path/to/your/springboot/application.yml"  # Spring Boot 配置文件路径
ACTUATOR_URL="http://localhost:8080/actuator/refresh"  # Spring Boot Actuator 刷新配置的URL
NEW_MYSQL_HOST="new_mysql_host"  # 新的 MySQL 主机地址
NEW_MYSQL_PORT="3306"            # 新的 MySQL 端口
NEW_MYSQL_USER="root"            # 新的 MySQL 用户名
NEW_MYSQL_PASSWORD="password"    # 新的 MySQL 密码

# 根据 Keepalived 状态执行操作
case "$2" in
    "MASTER")
        echo "切换为 MASTER 状态，更新 MySQL 配置..."
        # 更新 Spring Boot 配置文件中的 MySQL 连接信息
        sed -i "s/^\(spring.datasource.url: jdbc:mysql://\).*:\([0-9]*\/.*\)$/\1${NEW_MYSQL_HOST}:${NEW_MYSQL_PORT}\/\2/" $CONFIG_FILE
        sed -i "s/^\(spring.datasource.username: \).*$/\1${NEW_MYSQL_USER}/" $CONFIG_FILE
        sed -i "s/^\(spring.datasource.password: \).*$/\1${NEW_MYSQL_PASSWORD}/" $CONFIG_FILE

        # 调用 Spring Boot Actuator 刷新配置
        curl -X POST $ACTUATOR_URL
        ;;
    "BACKUP" | "FAULT")
        echo "切换为 BACKUP 或 FAULT 状态，无需更新配置。"
        ;;
    *)
        echo "未知状态: $2"
        exit 1
        ;;
esac

exit 0





     */
    //endregion

    //region  nginx
/*
upstream blance {#配置服务器的分别对应的应用ip和的端口
   #当前的server暂时不参与负载均衡
   server 192.168.3.11:8001 down;
   #预留的备份服务器
   server 192.168.3.11:8002 backup;
   #允许请求失败1次，失败后服务暂停10秒
   server 192.168.3.11:8003 max_fails=1 fail_timeout=10s;
}

因为8001端口的server1服务设置的down，不参与负载均衡； （服务不可用）
而8002端口的server2服务设置的backup，（备份）当其他节点服务正常时，不对外提供服务，当其他节点服务挂掉之后才会自动启用此备份服务；
所以只能访问到8003端口的server3应用服务（可用）


down：标记服务器为“永久下线”，不参与负载均衡。
backup：标记服务器为“备用服务器”，平时不工作，只在主服务器都宕机时才上线。
 */

//endregion

    //endregion

    //region rpc mq
    /*

    主要的区别：mq异步场景
              rpc是远程同步调用,立即获得结果
    mq 有broker 概念，负责存储消息，消息中间件
    rpc 就是一个远程函数调用，点对点通信
    消息队列是系统级、模块级的通信。RPC是对象级、函数级通信。
    */

    //endregion




    //region mysql ha
//    无法做到严格的主从同步，都会有延迟，如果强制从主库读就违反了，读写分离原则。如果将写入数据同步到redis 缓存，加大复杂性
    /*
     主主互为主从+keepalived :
     主主+keepalived  masterA和masterB互为主从，keepalived vip保证A写，B同步A，B和slave之间同步，slave会有延迟
     两台机器都装keepalived 、mysq,  java通过vip访问mysql 。两台mysql 互为主从

      MHA :会尝试保存故障主库的 binlog，但无法保证 100% 数据一致性。建议结合半同步复制使用。
     主从：master--- keepalive--vip-- mysqlA 和mysqlB
          slave---keepalive--vip-- mysqlC mysqlD mysqlE

  异步复制      主库将事务写入二进制日志（Binlog）后，立即向客户端返回成功，无需等待从库确认
  半同步复制    主库将事务写入Binlog后，需等待至少一个从库确认已接收并写入其中继日志（Relay Log），然后才向客户端返回成功。
  完全同步复制  主库需等待所有从库都提交了事务，才向客户端返回成功

     */

    /*
    mysql  mgr 高可用
     mysql 分片：用分库来实现

    高可用：中间件
     ProxySQL
     MySQL Router ：高可用建议通过Percona XtraDB Cluster或MariaDB Galera或MySQL官方的group replication实现，
                   如果实在没有选择，还可以通过MHA实现。
     */

     /*
      mysql主从同步：建议半同步复制+GTID
      MySQL主从复制默认异步复制进行同步。
      MySQL主从复制的原理：同步复制、异步复制（默认）、半同步复制、并行复制
      全同步复制（组复制 5.7支持）：配置、当master节点写数据的时候，会等待所有的slave节点完成数据的复制，然后才继续往下进行；组复制的每一个节点都可能是slave
      异步复制（默认）：主库 提交不关心从库是否提交、
      半同步复制：主库在执行完事务后，会等待至少一个从库接收并写入 Relay Log 后，才返回结果给客户端、
                需要安装插件并启用。只保证中继日志复制到从库，不保证日志被写入mysql .需要结合GTID或MHA保证数据一致性
      并行复制：



      GTID 复制：（GTID-Based Replication）： MySQL 5.6 引入的特性，为每个事务分配一个全局唯一的标识符
                      。从库通过 GTID 来追踪主库的事务，而不是通过二进制日志的文件名和位置。
                  流程：
                  1. 主库生成 GTID 并记录事务
                事务提交：
                当主库上执行一个事务时，MySQL 会为该事务生成一个全局唯一的 GTID。
                GTID 的格式为：source_id:transaction_id，其中：
                source_id 是主库的唯一标识（通常是 server_uuid）。
                transaction_id 是事务的唯一标识，从 1 开始递增。
                记录到二进制日志：
                主库将事务的 GTID 和事务内容记录到二进制日志（Binary Log）中。
                二进制日志中会包含 GTID 事件（GTID_LOG_EVENT），用于标识事务的 GTID。

                返回结果给客户端：
                主库在事务提交后，立即返回结果给客户端（如果是异步复制）。
                如果是半同步复制，主库会等待至少一个从库确认接收事务后，再返回结果。

                2. 从库获取 GTID 并应用事务
                从库连接主库：
                从库通过配置的复制用户连接到主库，并请求获取二进制日志。
                读取 GTID 事件：
                从库从主库的二进制日志中读取 GTID 事件，获取事务的 GTID 和事务内容。
                从库会记录已经接收到的 GTID 集合（Retrieved_Gtid_Set）。
                检查 GTID 是否已执行：
                从库会检查当前事务的 GTID 是否已经存在于自己的 gtid_executed 集合中。
                如果 GTID 已经存在，说明该事务已经执行过，从库会跳过该事务，避免重复执行。
                应用事务：
                如果 GTID 不存在于 gtid_executed 集合中，从库会将事务内容写入自己的 Relay Log（中继日志）。
                从库的 SQL 线程会读取 Relay Log 中的事务，并在本地执行。
                更新 GTID 集合：
                从事务成功执行后，从库会将该事务的 GTID 添加到自己的 gtid_executed 集合中。
                从库会定期将 gtid_executed 集合持久化到 mysql.gtid_executed 表中。

                4. GTID 复制的故障恢复
                如果从库在复制过程中遇到错误（如主键冲突或数据不一致），可以通过以下步骤恢复：

                停止从库复制：
                STOP SLAVE;

                跳过错误事务：

                手动设置 GTID，跳过错误事务：
                SET GTID_NEXT='source_id:transaction_id';
                BEGIN; COMMIT;
                SET GTID_NEXT='AUTOMATIC';
                将错误事务的 GTID 添加到 gtid_executed 集合中。

                重新启动从库复制：
                START SLAVE;



      GTID 主从：可以自动选主

      spring boot mysql 主从 ：sharding-jdbc-spring-boot-starter
     sharding-jdbc（shardingsphere）、mycat 读写分离，配置
     //sharding-jdbc 强制下一据查询主读
    HintManager.getInstance().setMasterRouteOnly();
    List<Order> b2 = orderMapper.findByUserId(6);


      mysql 主   备：
            主   从：建议一主多从，半同步复制
            多主多从：




      双主互为主从： mysql + keepalived  性能不如MMM，但MMM高并发有问题

      MHA：在主宕机，可以在从中选择（半同步复制）同步主日志的从作为主。
      mysql8 HA解决方案：
      (MMM 不维护) replication-manager:两主多从，只有一个主写，热备vip，每台服务器都要代理
      orchestrator:https://github.com/openark/orchestrator
      */





/*
方法 2：Orchestrator（官方推荐）
Orchestrator 是一个开源的 MySQL 高可用工具，支持 主从复制（Replication） 和 Group Replication，可以自动提升新的 Master。

工作流程
监控主库（Master）。
发现主库故障后，选择合适的 Slave 作为新 Master，并自动切换主从关系。
支持 自动 DNS 更新 或 VIP 变更，让应用程序透明地连接到新 Master。
优点

轻量级，MySQL 官方推荐。
Web 界面可视化管理数据库拓扑结构。
适用于大规模 MySQL 集群。
缺点
需要额外部署 Orchestrator 服务器。







应用程序连接配置更新
无论使用哪种方案，应用程序连接都需要更新，可以通过以下方式实现：

VIP（Virtual IP）：使用 Keepalived 绑定 VIP 到主库，Failover 时自动切换 VIP。
DNS 动态解析：更新 DNS 记录，指向新的 Master。
ProxySQL / HAProxy：应用程序连接 ProxySQL，由 ProxySQL 负责路由到正确的 Master。
环境变量：使用配置管理工具（如 Consul、Zookeeper）动态更新数据库连接。
 */


    //endregion

    //region   mysql ha 服务器架构
    /*
    3.2 基于 GTID 和半同步复制的高可用架构
        架构组成：
        1 台主库（Master）。
        2 台从库（Slave）。
        1 台管理节点（用于故障检测和切换）。
        工作流程：
        主库处理写操作，并通过 GTID 和半同步复制确保至少一个从库接收数据。
        管理节点监控主库和从库的状态。
        如果主库故障，管理节点选择一个从库提升为新的主库，并自动调整复制拓扑。
        优点：
        数据一致性更强。
        支持自动故障切换。
        缺点：
        需要更多服务器资源（至少 3 台）。
        配置和管理复杂度较高。


    3.4 基于 MHA（Master High Availability）的高可用架构
    架构组成：
            1 台主库（Master）。
            2 台从库（Slave）。
            1 台管理节点（MHA Manager）。
    工作流程：
    MHA Manager 监控主库和从库的状态。
    如果主库故障，MHA Manager 自动选择一个从库提升为新的主库，并调整其他从库的复制配置。
    优点：
    支持自动故障切换。
    配置相对简单。
    缺点：
    需要额外的管理节点。
    对网络和服务器性能有一定要求。
*/
    //endregion

    //region rbac
    //user
    //menu（树形结构）：区分menu_type 菜单和按钮权限
    //role
    //用户角色
    //角色菜单：包含按钮


    /*优化设计
    用户组（树形结构） ：用户加入用户组，指定用户组的角色。就不用新增用户时候赋值每个角色
    角色组 （树形结构）：角色组可避免新增功能时候为每个角色分配菜单权限
    */

    //不设计权限表：直接设计菜单权限，按钮权限和菜单区分，
    //endregion

    //region 发布
    /*蓝绿发布：两套环境并行， 可以快速回滚。   数据库采用一套新版本兼容老版本，其中一个版本只读（看情况），回滚时候
        任何添加到新版本的新数据也必须在回滚时传递给旧数据库。
        */

   //endregion

    //region rabbitmq
    /*
     rabbitmq 镜像模式  、仲裁队列（Quorum Queue）


  强一致性：基于 Raft 算法，消息必须在超过半数的副本持久化后才确认，有效防止数据丢失。   不支持消息优先级、队列TTL、非持久化消息等高级特性。


     仲裁队列是 RabbitMQ 3.8 后引入的新队列类型，旨在解决镜像队列的一些痛点。
核心优势：
强一致性：基于 Raft 算法，消息必须在超过半数的副本持久化后才确认，有效防止数据丢失。
非阻塞恢复：节点重新上线后，数据同步过程不会阻塞队列操作。

重要限制：
功能缺失：不支持消息优先级、队列TTL、非持久化消息等高级特性。
内存与磁盘使用：所有消息常驻内存，且在发布-订阅模式下磁盘写入放大效应更明显。务必设置队列长度限制（如 x-max-length）并监控内存。
多数节点要求：若可用副本数不足半数，队列将不可用且可能永久丢失数据。
     */
    //endregion

    //region 订单超时取消
    /*
    高并发、高精度要求	时间轮算法(netty HashedWheelTimer ) + 数据库批处理

    中小规模、低并发	Redis 过期事件 + 定时任务兜底
    高并发、高精度要求	时间轮算法 + 数据库批处理
    需要动态调整延迟时间	定时任务 + 外部配置（如 Apollo）
    已有 MQ 基础设施	MQ 延迟消息 + 定时任务补偿机制


    xxl-job  订单超时设计
    SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.createTime < :expireTime

    查出超时未支付的订单，修改其状态为取消

     */
    //endregion

    //登录
//    1、 HTTPS  传输是前提

       /*
       1、 HTTPS + 前端哈希（带固定盐或用户名）
       2、 HTTPS + RSA 加密。高安全  使用 bcrypt 类库
       采用 HTTPS 传输 前端SHA256哈希 到后端， 后端BCrypt加密的前端哈希值存储到数据库
        前端计算：传输哈希 = SHA256(用户的密码 + 后端下发的临时盐)。
        后端验证：
        后端拿到 传输哈希。
        后端从数据库取出该用户的存储盐。
        后端使用存储盐对数据库中的密码哈希值进行同样的运算，比对结果。
盐：用户表每个用户存储一个不同的盐（随机字符串），盐长度：16位+字符串


1、为了防止彩虹表攻击，前端可以在哈希前将密码与一个动态的盐值组合。
流程：
前端请求登录页时，后端返回一个随机的 salt（例如 UUID）。
前端将用户输入的密码与这个 salt 拼接。
对拼接后的字符串进行哈希。
将 username 和 hashResult 发送给后端。
后端使用数据库中该用户对应的盐，对存储的密码进行同样的哈希运算，比对结果。

2、非对称加密（RSA）
前端使用后端提供的公钥加密密码，后端使用私钥解密。即使 HTTPS 被破解，攻击者拿到加密数据，没有私钥也无法解开。
后端拿到密文后解密得到密码原文，然后可以用 BCrypt 等算法入库。这保证了传输过程中密码的绝对安全。


3、不考虑 哈希加密（最常用：SHA256）
这是最常见的做法。前端将用户输入的密码进行哈希处理，然后将哈希值传输给后端。后端再对哈希值进行比对（或再次哈希后比对）。

优点：实现简单，不可逆。

缺点：如果仅使用明文哈希（如 MD5），容易受到彩虹表攻击。通常需要配合加盐使用。

彩虹表是一种预先计算好的哈希值查找表。它本质上是一个巨大的键值对数据库：
键（Key）： 常见密码（如 123456, password, qwerty）经过哈希算法（如MD5）计算后的哈希值。
值（Value）： 生成该哈希值的原始明文密码。

盐（Salt） 是一个随机生成的字符串。它的核心作用是将用户设定的密码伪装起来，使得同样的密码产生完全不同的哈希值。
公式：Hash(密码 + 盐) 或 Hash(盐 + 密码)
盐的特性：每个用户都应该有一个唯一的盐，并且盐的长度要足够长（至少16位以上）。



返回前端一个短期有效的 accessToken 和一个长期有效的 refreshToken
        */
    //endregion
}
