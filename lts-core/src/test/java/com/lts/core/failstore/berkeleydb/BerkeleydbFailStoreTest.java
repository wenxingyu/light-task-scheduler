package com.lts.core.failstore.berkeleydb;

import com.lts.core.cluster.Config;
import com.lts.core.cluster.NodeType;
import com.lts.core.commons.utils.CollectionUtils;
import com.lts.core.json.JSON;
import com.lts.core.constant.Constants;
import com.lts.core.domain.Job;
import com.lts.core.domain.Pair;
import com.lts.core.failstore.FailStore;
import com.lts.core.failstore.FailStoreException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Robert HG (254963746@qq.com) on 5/26/15.
 */
public class BerkeleydbFailStoreTest {

    FailStore failStore;

    private String key = "x2x3423412x";

    @Before
    public void setup() throws FailStoreException {
        Config config = new Config();
        config.setDataPath(Constants.USER_HOME);
        config.setNodeGroup("test");
        config.setNodeType(NodeType.JOB_CLIENT);
        config.setIdentity("testIdentity");
        failStore = new BerkeleydbFailStoreFactory().getFailStore(config, getFailStorePath(config));
        failStore.open();
    }

    public String getFailStorePath(Config config) {
        return config.getDataPath() + "/.lts" + "/" + config.getNodeType() + "/" + config.getNodeGroup() + "/failstore/";
    }
    @Test
    public void put() throws FailStoreException {
        Job job = new Job();
        job.setTaskId("2131232");
        for (int i = 0; i < 100; i++) {
            failStore.put(key + "" + i, job);
        }
        System.out.println("这里debug测试多线程");
        failStore.close();
    }

    @Test
    public void fetchTop() throws FailStoreException {
        List<Pair<String, Job>> pairs = failStore.fetchTop(5, Job.class);
        if (CollectionUtils.isNotEmpty(pairs)) {
            for (Pair<String, Job> pair : pairs) {
                System.out.println(JSON.toJSONString(pair));
            }
        }
    }

//    @Test
//    public void del() throws FailStoreException {
//        failStore.delete(key);
//    }

}