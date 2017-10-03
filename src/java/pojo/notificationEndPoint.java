package pojo;

import controller.ConnexionBean;
import ejb.MessageChatFacade;
import ejb.NotificationFacade;
import ejb.UtilisateurFacade;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.swing.JApplet;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import jpa.MessageChat;
import jpa.Notification;
import jpa.Utilisateur;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author CHABI Emmanel Landry
 */
@ServerEndpoint("/notificationEndPoint")
public class notificationEndPoint {

    @Inject
    private ConnexionBean connexionBean;
    @Inject
    NotificationFacade notificationFacade;
      @Inject
    MessageChatFacade messageChatFacade;
    static List<Utilisateur> listUserConnecte = new ArrayList<>();
    Set<Session> chatroomUsers = Collections.synchronizedSet(new HashSet<Session>());
    static Set<Session> saticChatroomUsers = Collections.synchronizedSet(new HashSet<Session>());
    private List<Notification> listNotifInstance;
    private List<MessageChat> listMessageInstance;
    private boolean alredyConnect = false;
    private static List<String> listIdSession = new ArrayList<>();
//  List<Session> chatroomUsers=new ArrayList<>();
// static List<Session>  =new ArrayList<>();

    @OnOpen
    public void open(Session userSession) {
        userSession.setMaxBinaryMessageBufferSize(1024*512);
        System.out.println("le connecté est " + connexionBean.getCurrentUser().getLogin());
        userSession.getUserProperties().put("userLogin", connexionBean.getCurrentUser().getLogin());
        userSession.getUserProperties().put("user", connexionBean.getCurrentUser());
        userSession.getUserProperties().put("name", connexionBean.getCurrentUser().getNom() + " " + connexionBean.getCurrentUser().getPrenom());
        chatroomUsers = userSession.getOpenSessions();
        saticChatroomUsers = chatroomUsers;
       

        System.out.println(" nous avons maintenant " + saticChatroomUsers.size() + " listUserConnecte -->" + listUserConnecte + " et conneionBean " + connexionBean.getListConnectes() + "le boleen est ");
  
        
//        if (!listIdSession.contains(userSession.getId())) {
//            try {
//                for (Session session : saticChatroomUsers) {
//                    if (!session.getUserProperties().get("userLogin").equals(userSession.getUserProperties().get("userLogin"))) {
//                        System.out.println("send a " + session.getUserProperties().get("userLogin"));
//                        session.getBasicRemote().sendText(construireJsonDataNewUser((String) userSession.getUserProperties().get("userLogin"), (String) session.getUserProperties().get("userLogin"), "Mr/Mm " + ((String) userSession.getUserProperties().get("name")) + " vient de se connecter"));
//
//                    }
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(notificationEndPoint.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            listIdSession.add(userSession.getId());
//        }
        
        
        listNotifInstance = notificationFacade.trouverNotifNotSee(connexionBean.getCurrentUser(),connexionBean.getCurrentAppli());
        
        listNotifInstance.stream().forEach((notif) -> {
            try {
                System.out.println("novelle notif");
                sendNotif(notif.getSender(), notif.getReceiver(), notif.getLibelle());
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ConnexionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        listNotifInstance.clear();
        listNotifInstance=notificationFacade.trouverCorrectionNotSee(connexionBean.getCurrentUser(),connexionBean.getCurrentAppli());
        listNotifInstance.stream().forEach((notif) -> {
           try {
                System.out.println("nouvelles correction");
                  sendMessageCorrection(notif.getSender(), notif.getReceiver(), notif.getLibelle());
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ConnexionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        listMessageInstance=messageChatFacade.getMessageReceive(connexionBean.getCurrentUser());
        listMessageInstance.stream().forEach((message) -> {
           try {
                System.out.println("nouveaux message");
                  sendMessageChat(message.getExpediteur(), message.getRecepteur(), message.getLibelle());
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ConnexionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @OnClose
    public void close(Session userSession) {
        chatroomUsers.remove(userSession);
    }

    @OnMessage
    public void message(String message, Session userSession) throws IOException {
        System.out.println("entrer dans l'envoi");
        String userLogin = (String) userSession.getUserProperties().get("userLogin");
        if (userLogin == null) {
            userSession.getUserProperties().put("userLogin", message);
            userSession.getBasicRemote().sendText(construireJsonData("Systeme", "vous ètes aactuellement connectés sous le nom de  " + message));
        } else {
            Iterator<Session> iterator = chatroomUsers.iterator();

            String[] RecAndMes = message.split(",");
            String[] ReceivePart = RecAndMes[0].split(":");
            String[] MessagePart = RecAndMes[1].split(":");
            System.out.println("l message est " + MessagePart[1].replace("\"", "").replace("}", "") + " et le destinataire est " + ReceivePart[1].replace("\"", ""));
            for (Session session : saticChatroomUsers) {
                if (session.getUserProperties().get("userLogin").equals(ReceivePart[1].replace("\"", ""))) {
                    System.out.println("ok bien venu peut envoyer");
                    session.getBasicRemote().sendText(construireJsonData(userLogin, MessagePart[1].replace("\"", "").replace("}", "")));
                }
            }
        }
    }

    public static String construireJsonData(String userLogin, String message) {
        JsonObject jsonObject = Json.createObjectBuilder().add("message", message).add("notif", "yes").build();
        StringWriter stringWritter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWritter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWritter.toString();
    }

    public static String construireJsonDataCorrection(String userLogin, String message) {
        JsonObject jsonObject = Json.createObjectBuilder().add("message", message).add("correctionChat", "yes").build();
        StringWriter stringWritter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWritter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWritter.toString();
    }

    public static String construireJsonDataNewUser(String userLogin, String userSending, String newUser) {
        JsonObject jsonObject = Json.createObjectBuilder().add("newUser", newUser).add("loginSocial", userSending).build();
        StringWriter stringWritter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWritter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWritter.toString();
    }
    
      public static String construireJsonDataChat(String userLogin,String message) {
     
        JsonObject jsonObject = Json.createObjectBuilder().add("Chat", message).add("IsChat", "yes").add("Sender",userLogin).build();
        StringWriter stringWritter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(stringWritter)) {
            jsonWriter.write(jsonObject);
        }
        return stringWritter.toString();
    }

    public static void sendNotif(Utilisateur sender, Utilisateur receiver, String notif) throws IOException {
        for (Session session : saticChatroomUsers) {
            if (session.getUserProperties().get("userLogin").equals(receiver.getLogin())) {
                session.getBasicRemote().sendText(construireJsonData(sender.getLogin(), notif));
                System.out.println("notif envoyée");
            }
        }
    }

    public static void sendMessageChat(Utilisateur sender, Utilisateur receiver, String message) throws IOException {
        for (Session session : saticChatroomUsers) {
            if (session.getUserProperties().get("userLogin").equals(receiver.getLogin())) {
                session.getBasicRemote().sendText(construireJsonDataChat(sender.getLogin(), message));
                System.out.println("Chat envoyé");
            }
        }
    }
    
    public static void sendMessageCorrection(Utilisateur sender, Utilisateur receiver, String message) throws IOException {
        for (Session session : saticChatroomUsers) {
            if (session.getUserProperties().get("userLogin").equals(receiver.getLogin())) {
                session.getBasicRemote().sendText(construireJsonDataCorrection(sender.getLogin(),sender.getFonction().getCode()+" :"+ message));
                System.out.println("correction envoyée");
            }
        }
    }

    public List<Utilisateur> findLesConnectes(List<Session> listSession) {
        List<Utilisateur> listUser = new ArrayList<>();
        for (Session session : listSession) {
            if (!((Utilisateur) session.getUserProperties().get("user") == connexionBean.getCurrentUser())) {
                listUser.add((Utilisateur) session.getUserProperties().get("user"));
            }
        }

        return listUser;
    }

    
        @OnMessage
       public void processVideo(byte[] imageData, Session session) {
              System.out.println("INsite process Video");
              try {
                     // Wrap a byte array into a buffer
                  
                     ByteBuffer buf = ByteBuffer.wrap(imageData);
//                   imageBuffers.add(buf);
                    
                     for(Session session2 : saticChatroomUsers){
                           session2.getBasicRemote().sendBinary(buf);
                     }
                    
                    
              } catch (Throwable ioe) {
                     System.out.println("Error sending message " + ioe.getMessage());
              }
       }
    
}
