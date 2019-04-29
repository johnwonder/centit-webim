package com.centit.im.robot.es.service;

import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.im.robot.es.po.QuestAndAnswer;

/**
 * Created by zhang_gd on 2017/9/26.
 */
public interface QuestAndAnswerManager extends BaseEntityManager<QuestAndAnswer,String> {
    void deleteQuestionCatalogSign(String questionId);
    void updateQuestionCatalog(QuestAndAnswer questAndAnswer);
}
