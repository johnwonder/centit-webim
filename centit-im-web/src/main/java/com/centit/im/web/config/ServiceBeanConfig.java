package com.centit.im.web.config;

import com.centit.fileserver.utils.FileStore;
import com.centit.fileserver.utils.OsFileStore;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.components.impl.NotificationCenterImpl;
import com.centit.framework.components.impl.TextOperationLogWriterImpl;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.framework.model.adapter.MessageSender;
import com.centit.framework.model.adapter.NotificationCenter;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.im.robot.es.service.impl.IntelligentRobotEsImpl;
import com.centit.im.service.IntelligentRobotFactory;
import com.centit.im.service.impl.IntelligentRobotFactoryRpcImpl;
import com.centit.im.service.impl.IntelligentRobotFactorySingleImpl;
import com.centit.im.web.plugins.JsfgwSmsMessageSender;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@ComponentScan(basePackages = "com.centit",
        excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
@EnableConfigurationProperties(WebImProperties.class)
public class ServiceBeanConfig {

    @Autowired
    private WebImProperties webImProperties;


    @Autowired
    IntegrationEnvironment integrationEnvironment;

    @Bean
    public IntelligentRobotFactory intelligentRobotFactory() {
        if("es".equals(webImProperties.getRobot().getType())){
            IntelligentRobotFactorySingleImpl intelligentRobotFactory
                    = new IntelligentRobotFactorySingleImpl();
            IntelligentRobotEsImpl intelligentRobot = new IntelligentRobotEsImpl();
            intelligentRobot.setMaxAnswer(webImProperties.getRobot().getMaxAnswer());
            intelligentRobotFactory.setIntelligentRobot(intelligentRobot );
            return intelligentRobotFactory;
        }else{
            IntelligentRobotFactoryRpcImpl intelligentRobotFactory
                    = new IntelligentRobotFactoryRpcImpl();
            intelligentRobotFactory.setIntegrationEnvironment(
                    integrationEnvironment
            );
            return intelligentRobotFactory;
        }
    }

    @Bean
    public FileStore fileStore(){

        String baseHome = webImProperties.getFileStore().getBaseDir();
        if(StringUtils.isBlank(baseHome)) {
            baseHome = SysParametersUtils.getUploadHome();
        }

        return new OsFileStore(baseHome);
    }

    @Bean
    public NotificationCenter notificationCenter() {
        NotificationCenterImpl notificationCenter = new NotificationCenterImpl();
        notificationCenter.initDummyMsgSenders();
        //notificationCenter.registerMessageSender("innerMsg",innerMessageManager);
        return notificationCenter;
    }

    @Bean
    @Lazy(value = false)
    public OperationLogWriter operationLogWriter() {
        TextOperationLogWriterImpl operationLog =  new TextOperationLogWriterImpl();
        operationLog.init();
        return operationLog;
    }

    @Bean
    public MessageSender smsMessageManager(){
        JsfgwSmsMessageSender smsMessageManager =new JsfgwSmsMessageSender();
        smsMessageManager.setSmsSendUrl(webImProperties.getSms().getSendUrl());
        return smsMessageManager;
    }

    @Bean
    public InstantiationServiceBeanPostProcessor instantiationServiceBeanPostProcessor() {
        return new InstantiationServiceBeanPostProcessor();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new HttpSessionCsrfTokenRepository();
    }

}
