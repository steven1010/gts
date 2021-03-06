package com.wf.gts.core.lb;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;

import com.wf.gts.remoting.protocol.GtsManageLiveAddr;


/**
 * 一致性hash算法，同样的客户端ip会打到同样的节点
 */
public class ConsistentHashLoadBalancer  {

    private static ConcurrentHashMap<String, Selector> selectorCache = new ConcurrentHashMap<String, Selector>();
    
    public static GtsManageLiveAddr doSelect(String clientIp,List<GtsManageLiveAddr> gtsManageLiveAddrs) {
        int hashcode = gtsManageLiveAddrs.hashCode(); // 判断是否同样的服务列表
        Selector selector = selectorCache.get(clientIp);
        if (selector == null // 原来没有
            ||
            selector.getHashCode() != hashcode) { //或者服务列表已经变化
            selector = new Selector(gtsManageLiveAddrs, hashcode);
            selectorCache.put(clientIp, selector);
        }
        return selector.select(clientIp);
    }
       
}
