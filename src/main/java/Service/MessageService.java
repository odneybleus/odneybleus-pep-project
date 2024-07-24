package Service;
import DAO.MessageDAO;
import Model.Message;

import java.util.*;
public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message) {
        List<Message> messages = messageDAO.getAllMessages();
        for (Message m : messages){
            if (m.getMessage_id() == message.getMessage_id()){
                return null;
            }
        }
        return messageDAO.insertMessage(message);
    }
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageByID(int id){
        return messageDAO.getMessageByID(id);
    }

    public Message updateMessage(int message_id, Message message){
        Message temp = messageDAO.getMessageByID(message_id);
        if (temp == null){
            return null;
        }
        temp.setMessage_text(message.getMessage_text());
        if (temp.getMessage_text().length() > 254 || temp.getMessage_text().isEmpty())
        {
            return null;
        }
        return temp;
    }

    public List<Message> getMessagesByAccountID(int message){
        List<Message> messages = messageDAO.getAllMessagesFromAccountID(message);
        return messages;
    }
}