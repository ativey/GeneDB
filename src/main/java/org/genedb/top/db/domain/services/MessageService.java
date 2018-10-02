package org.genedb.top.db.domain.services;

import org.genedb.top.db.domain.misc.Message;

import java.util.List;

public interface MessageService {

    void addNotification(String clientName, String string, String string2);

    List<Message> checkMessages(String clientName);

}
