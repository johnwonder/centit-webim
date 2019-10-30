package com.centit.im.service.impl;


import com.centit.framework.ip.po.OsInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.im.service.IntelligentRobot;
import com.centit.im.service.IntelligentRobotFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codefan on 17-6-19.
 */
//@Service("intelligentRobotFactory")
public class IntelligentRobotFactoryRpcImpl implements IntelligentRobotFactory {
    private static ConcurrentHashMap<String, IntelligentRobot> intelligentRobotMap
            = new ConcurrentHashMap<>();//根据用户找session

    public void setIntegrationEnvironment(IntegrationEnvironment integrationEnvironment) {
        this.integrationEnvironment = integrationEnvironment;
    }

    //@Autowired
    protected IntegrationEnvironment integrationEnvironment;

    @Override
    public IntelligentRobot getIntelligentRobot(String osId){
        if(osId == null)
            return null;
        IntelligentRobot robot = intelligentRobotMap.get(osId);
        if(robot == null){
            IntelligentRobotRpcImpl robotImpl = new IntelligentRobotRpcImpl();
            OsInfo osInfo = integrationEnvironment.getOsInfo(osId);
            robotImpl.initAppSession(osInfo.getOsUrl()+"/service/askrobot");
            intelligentRobotMap.put(osId,robotImpl);
            robot = robotImpl;
        }
        return robot;
    }
}
