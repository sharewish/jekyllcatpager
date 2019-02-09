package in.sharewish.smsplivo;

import com.plivo.api.Plivo;
import com.plivo.api.PlivoClient;
import com.plivo.api.exceptions.PlivoRestException;
import com.plivo.api.models.message.Message;
import com.plivo.api.models.message.MessageCreateResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SmsManager {
    private static final String AUTH_ID = "MAMGE2YZE1YTY4MZMZY2";
    private static final String AUTH_TOKEN = "MTE5Y2Y5NmFlOTg5NzE2MWMwOTZlNTg1NDA2NTk3";

    static {
        Plivo.init(AUTH_ID, AUTH_TOKEN);
    }


    private PlivoClient client;

    public SmsManager() {
        initPlivoClient();
    }

    private void initPlivoClient() {
        if (client == null) {
            client = new PlivoClient(AUTH_ID, AUTH_TOKEN);
        }
    }

    public void send(String phone, String message) {
        List<String> destination = new ArrayList<>();
        client.setTesting(true);
        try {
            MessageCreateResponse response = Message.creator("", destination, message).client(client).create();
            System.out.println("Message Sent and UUID is " + response.getMessageUuid());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PlivoRestException e) {
            e.printStackTrace();
        }
    }
}
