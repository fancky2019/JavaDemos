package Test.opensource.architecturedesign;

/*
前端高可用的组合:LVS+Keepalived、Nginx+Keepalived、HAproxy+Keepalived
 */
public class Design {
    //region  HA

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

    Keepalive 三大组件中的check 组件，监控nginx进程的脚本，如果nginx进程挂了，没有重启成功，keepalived自己停止服务，这样keepalived集群就知道应用状态。

     */



/*
 每台服务器安装 keepalived 和 haproxy nginx
 keepalive 解决haproxy的单点故障问题。保证haproxy的高可用。
 haproxy反向代理nginx实现负载均衡。haproxy一般非web服务器。
 nginx web服务器动静分离。

 客户端直接访问haproxy,haproxy反向代理到nginx,nginx反向代理到真正web服务器。
 */
    /*
     rabbitmq 镜像模式
     */

    /*
    软件
    LVS、Haproxy、Nginx
    硬件
    F5

   LVS（Linux Virtual Server）即Linux虚拟服务器，

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
}
