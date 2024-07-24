package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessageHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class); 
            Account addedAccount = accountService.addAccount(account);
            if(addedAccount.getUsername().isBlank() == false && addedAccount.getPassword().length() >= 4){
                ctx.json(mapper.writeValueAsString(addedAccount));
            }
            else{
                ctx.status(400);
            }
        }
        catch(NullPointerException e){
            ctx.status(400);
        }
        
    }

    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account a = accountService.getAccount(account);
        if (a == null){
            ctx.status(401);
        }
        else{
            ctx.json(mapper.writeValueAsString(a));
        }
    }

    public void postMessageHandler(Context ctx) throws JsonProcessingException {
        try{
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);
            Message postMessage = messageService.addMessage(message);
            if(postMessage == null){
                ctx.status(400);
            }
            if(postMessage.getMessage_text().isBlank() == false && postMessage.getMessage_text().length() < 255){
                ctx.json(mapper.writeValueAsString(postMessage));
                ctx.status(200);
            }
            else{
                ctx.status(400);
            }}
        catch (NullPointerException e){
            ctx.status(400);
        }
    }

    private void getAllMessageHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    public void getMessageByIDHandler(Context ctx){
        int mess_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByID(mess_id);
        try {
            if (message.getMessage_text().isBlank() == false){
                ctx.json(message);
            }
        }
        catch(NullPointerException e){
            ctx.status(200);
        }
    }

    public void deleteMessageHandler(Context ctx) {
        String idString = ctx.pathParam("message_id");
        int id = Integer.parseInt(idString);
        Message message = messageService.getMessageByID(id);
        try {
            if (message.getMessage_text().isBlank() == false){
                ctx.json(message);
            }
        }
        catch(NullPointerException e){
            ctx.status(200);
        }
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int mess_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(mess_id, message);
        if(updatedMessage == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }

    }

    private void getAllMessagesByAccountHandler(Context ctx){
        int mess_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountID(mess_id);
        ctx.json(messages);
    }
}