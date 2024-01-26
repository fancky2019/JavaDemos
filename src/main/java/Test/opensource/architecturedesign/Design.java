package Test.opensource.architecturedesign;

/*
前端高可用的组合:LVS+Keepalived、Nginx+Keepalived、HAproxy+Keepalived
 */
public class Design {

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
    keepalived 主备 vip 漂移  。解决单点故障
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
    Keepalived高可用设计。VRRP全称 Virtual Router Redundancy Protocol，即 虚拟路由冗余协议。
    虚拟ip 漂移


    每台机器都装keepalived、haproxy ,他们只能用于linux服务器
    Keepalived 保证nginx 可用，nginx 动静分离，负载均衡
    主备：主干活，备不干活，资源浪费，可以用主备模式。session redis.
    互为主备：主备都干活。每个机器配置两个vrrp_instance, keepalived 是通过虚拟ip (vip)访问的，两个虚拟ip设置互为主备
    windows 自带的NLB 代替linux 的keepalived ，每台机器装一个keepalived，nginx 互为主备

    访问虚拟IP:keepalived 配置虚拟ip地址，虚拟ip地址在和nginx关联，就可以访问nginx.  几台Nginx 配置一样。 两台互为主备的Keepalived根据网络情况路由访问哪一台机器的nginx
             执行脚本判断nginx服务是否存在，不存在就杀掉keepalived进程。
    Keepalive 三大组件中的check 组件，监控nginx进程的脚本，如果nginx进程挂了，没有重启成功，keepalived自己停止服务，这样keepalived集群就知道应用状态。

   keepalived 配置 ： 虚拟地址-->虚拟服务器地址-->真是服务器地址
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

    /*
     主主互为主从+keepalived :
     主主+keepalived  masterA和masterB互为主从，keepalived vip保证A写，B同步A，B和slave之间同步，slave会有延迟
     两台机器都装keepalived 、mysq,  java通过vip访问mysql 。两台mysql 互为主从


     主从：master--- keepalive--vip-- mysqlA 和mysqlB
          slave---keepalive--vip-- mysqlC mysqlD mysqlE
     */

    /*
    mysql  mgr 高可用
     mysql 分片：用分库来实现
     */

     /*
      MySQL主从复制默认异步复制进行同步。
      MySQL主从复制的原理：同步复制、异步复制（默认）、半同步复制、并行复制
      全同步复制（组复制 5.7支持）：配置、当master节点写数据的时候，会等待所有的slave节点完成数据的复制，然后才继续往下进行；组复制的每一个节点都可能是slave
      异步复制：主库 提交不关心从库是否提交、
      半同步复制：至少一个从库提交（内网环境，通信要求）、需要安装插件并启用
      并行复制：

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

    //蓝绿发布：两套环境并行， 可以快速回滚。   数据库采用一套新版本兼容老版本，其中一个版本只读（看情况），回滚时候
    //        任何添加到新版本的新数据也必须在回滚时传递给旧数据库。

    //region rabbitmq
    /*
     rabbitmq 镜像模式
     */
    //endregion
}
