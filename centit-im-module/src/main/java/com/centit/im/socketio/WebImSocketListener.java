package com.centit.im.socketio;

import com.centit.im.service.WebImSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by codefan on 17-5-19.
 * @author codefan
 */
@Service
@ServerEndpoint(value="/im/{userCode}"/*, configurator = MySpringConfigurator.class*/)
public class WebImSocketListener {

    private static Logger logger = LoggerFactory.getLogger(WebImSocketListener.class);

    static ApplicationContext context;

    private WebImSocket webImSocket=null;

    private WebImSocket getWebImSocket(){
        if(webImSocket == null){
            webImSocket =  WebImSocketListener.context.getBean("webImSocket", WebImSocket.class);
        }
        return webImSocket;
    }
    /*
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userCode") String userCode) {
        try {
            getWebImSocket().signInUser(userCode, session);
            //logger.debug("User Login : " + userCode + " session :" + session.getId());
        }catch (Exception e){
            logger.error("onOpen",e);
        }
    }

    /*
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        try {
            getWebImSocket().signOutUser(session);
            //logger.debug("User Logout session :" + session.getId());
        }catch (Exception e){
            logger.error("onClose",e);
        }
    }

    /*
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        try{
            getWebImSocket().recvMessage(session, message);
        }catch (Exception e){
            logger.error("onMessage" + message,e);
        }
    }

    /**
     * 发生错误时调用
     * @param error Throwable
     */
    @OnError
    public void onError(Throwable error) {
        logger.error("onError" + error.getMessage(),error);
        //error.printStackTrace();
    }
}
