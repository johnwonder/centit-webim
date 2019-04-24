package com.centit.im.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.database.utils.PageDesc;
import com.centit.im.po.WebImMessage;
import com.centit.im.service.WebImMessageManager;
import com.centit.im.service.WebImSocket;
import com.centit.im.socketio.ImMessage;
import com.centit.im.socketio.ImMessageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by codefan on 17-5-20.
 */
@Controller
@RequestMapping("/webim")
@Api(value = "即时消息发送接口", tags = "即时消息发送接口")
public class WebImController extends BaseController {

    @Resource
    protected WebImSocket webImSocket;

    //发送消息，给第三方使用
    @RequestMapping(value = "/sendMessage/{receiver}/{sender}", method = RequestMethod.POST)
    public void sendMessage(
            @PathVariable String receiver,@PathVariable String sender,
            @RequestBody ImMessage message,
            HttpServletResponse response) {

        message.setReceiver(receiver);
        message.setSender(sender);
        ImMessageUtils.checkMessage(message);
        webImSocket.sendMessage(receiver,message);
        JsonResultUtils.writeSuccessJson(response);
    }

    //发送群（组、机构）消息，给第三方使用
    @RequestMapping(value = "/sendUnitMessage/{receiver}/{sender}", method = RequestMethod.POST)
    public void sendGroupMessage(
            @PathVariable String receiver,@PathVariable String sender,
            @RequestBody ImMessage message,
            HttpServletResponse response) {

        message.setReceiver(receiver);
        message.setSender(sender);
        ImMessageUtils.checkMessage(message);
        webImSocket.sendGroupMessage(receiver,message);
        JsonResultUtils.writeSuccessJson(response);
    }

    //广播消息，给第三方使用
    @RequestMapping(value = "/toall/{sender}", method = RequestMethod.POST)
    public void toallMessage(@PathVariable String sender,
                             @RequestBody ImMessage message,
                             HttpServletResponse response) {
        message.setSender(sender);
        ImMessageUtils.checkMessage(message);
        webImSocket.toallMessage(message);
        JsonResultUtils.writeSuccessJson(response);
    }

    //在线广播消息，给第三方使用
    @RequestMapping(value = "/broadcast/{sender}", method = RequestMethod.POST)
    public void broadcastMessage(@PathVariable String sender,
                                 @RequestBody ImMessage message,
                                 HttpServletResponse response) {
        message.setSender(sender);
        ImMessageUtils.checkMessage(message);
        webImSocket.broadcastMessage(message);
        JsonResultUtils.writeSuccessJson(response);
    }

}
