package com.dongs.drpc.registry;


import cn.hutool.json.JSONUtil;
import com.dongs.drpc.config.RegistryConfig;
import com.dongs.drpc.model.ServiceMetaInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 连接etcd
 *
 * @author dongs
 */
@Slf4j
public class EtcdRegistry implements Registry{


    private Client client;

    private KV kvClient;

    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/drpc/";


    /**
     *
     * @param registryConfig
     */
    @Override
    public void init(RegistryConfig registryConfig) {
        client = Client.builder()
                .endpoints(registryConfig.getAddress())
                .connectTimeout(Duration.ofMillis(registryConfig.getTimeout()))
                .build();
        kvClient = client.getKVClient();
    }

    /**
     * @param serviceMetaInfo 服务元信息
     */
    @Override
    public void registry(ServiceMetaInfo serviceMetaInfo) throws Exception{
        // 创建lease和KV客户端
        Lease leaseClient = client.getLeaseClient();
        // 设置30秒的租约
        long leaseId = leaseClient.grant(30).get().getID();
        // 设置要存储的键值对
        String registerKey = ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKey, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMetaInfo),StandardCharsets.UTF_8);

        // 关联租约和键值对
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
    }

    /**
     * @param serviceMetaInfo
     */
    @Override
    public void unRegistry(ServiceMetaInfo serviceMetaInfo) {
        kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceMetaInfo.getServiceNodeKey(),StandardCharsets.UTF_8));
    }

    /**
     * @param serviceKey
     * @return
     */
    @Override
    public List<ServiceMetaInfo> serviceDiscovery(String serviceKey) {
        // 前缀搜索
        String searchPrefix = ETCD_ROOT_PATH + serviceKey + "/";
        try {
            // 前缀查询
            GetOption getOption = GetOption.builder().isPrefix(true)
                    .build();
            List<KeyValue> keyValues = kvClient.get(ByteSequence.from(searchPrefix,StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();
            // 解析服务信息
            List<ServiceMetaInfo> serviceMetaInfoList = keyValues.stream()
                    .map(keyValue -> {
                        String value = keyValue.getValue().toString(StandardCharsets.UTF_8);
                        return JSONUtil.toBean(value,ServiceMetaInfo.class);
                    }).collect(Collectors.toList());
            return serviceMetaInfoList;
        }catch (Exception e){
            throw new RuntimeException("获取服务列表失败",e);
        }
    }

    /**
     *
     */
    @Override
    public void destroy() {
        System.out.println("当前结点下线");
        // 释放资源
        if (kvClient != null){
            kvClient.close();
        }
        if (client != null){
            client.close();
        }
    }

//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//
//        // 使用给定的端点创建客户端
//        // create client using endpoints
//        Client client = Client.builder().endpoints("http://localhost:2379")
//                .build();
//
//        // 获取键值对存储客户端
//        KV kvClient = client.getKVClient();
//        ByteSequence key = ByteSequence.from("test_key".getBytes());
//        ByteSequence value = ByteSequence.from("test_value".getBytes());
//
//        // put the key-value
//        kvClient.put(key, value).get();
//
//        // get the CompletableFuture
//        CompletableFuture<GetResponse> getFuture = kvClient.get(key);
//
//        // get the value from CompletableFuture
//        GetResponse response = getFuture.get();
//
//        // delete the key
//        kvClient.delete(key).get();
//    }
}
