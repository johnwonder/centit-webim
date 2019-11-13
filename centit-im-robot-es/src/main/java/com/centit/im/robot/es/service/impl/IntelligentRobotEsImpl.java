package com.centit.im.robot.es.service.impl;

import com.centit.im.po.ImMessage;
import com.centit.im.po.RobotAnswer;
import com.centit.im.robot.es.po.QuestAndAnswer;
import com.centit.im.service.IntelligentRobot;
import com.centit.search.service.ESServerConfig;
import com.centit.search.service.IndexerSearcherFactory;
import com.centit.search.service.Searcher;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

/**
 * Created by codefan on 17-6-19.
 */
//@Service("intelligentRobot")
public class IntelligentRobotEsImpl implements IntelligentRobot {

    private ESServerConfig esServerConfig;
    private Searcher searcher;

    //question.robot.answer.maxsize = 4
    private int maxAnswer;

    public IntelligentRobotEsImpl(){
        maxAnswer = 4;
    }
    @Override
    public RobotAnswer sayHello(String custUserCode) {
        RobotAnswer a = new RobotAnswer("您好！很荣幸为您服务，请输入您的问题，我来为您解答。\n如果您的问题跟业务无关，您可以使用信箱服务进行问题传达。");
        a.addHttpOption("转信箱服务", "http://www.jsdpc.gov.cn/hudong/dtpage/wszx");
        a.addCommandOption("转人工服务（工作日：9:30至11:30 15：00至17:00）",
                ImMessage.CONTENT_TYPE_ASKFORSERVICE);
        return a;
    }

    @Override
    public RobotAnswer sayGoodbye(String custUserCode) {
        return new RobotAnswer("再见");
    }

    @Override
    public RobotAnswer askQuestion(String custUserCode, String question) {
        if(searcher==null) {
            searcher = IndexerSearcherFactory.obtainSearcher(esServerConfig, QuestAndAnswer.class);
        }

        Pair<Long,List<Map<String, Object>>> questiosns = searcher.search(question,1,maxAnswer);
        RobotAnswer answer = new RobotAnswer(question);
        int answerNo =0;
        for(Map<String, Object> q : questiosns.getRight()) {
            if(answerNo==0) {
                answer.setMessage("您想问的是不是：\n"
                        + StringBaseOpt.objectToString(q.get("questionTitle")
                        + "\n\n答案：\n"
                        + StringBaseOpt.objectToString(q.get("questionAnswer"))));
            }else {
                answer.addQuestionOption(StringBaseOpt.objectToString(q.get("questionTitle")),
                        StringBaseOpt.objectToString(q.get("questionTitle")));
            }
            answerNo ++;
        }
        if(answerNo==0) {
            answer.setMessage("您好，我还在成长中，您的问题可能超出了我的知识范围，要不您换个问法试试。\n" +
                    "或者请您试试人工服务\n" +
                    "如果您的问题跟业务无关，您可以使用信箱服务进行问题传达");
        }
        answer.addCommandOption(
                "转人工服务（工作日：9:30至11:30 15：00至17:00）",
                "askForService");
        answer.addHttpOption("转信箱服务",
                "http://www.jsdpc.gov.cn/hudong/dtpage/wszx");
        return answer;
    }

    public void setMaxAnswer(int maxAnswer) {
        this.maxAnswer = maxAnswer;
    }

    public void setEsServerConfig(ESServerConfig esServerConfig) {
        this.esServerConfig = esServerConfig;
    }
}
