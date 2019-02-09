package in.sharewish.smsplivo;

public class SmsSender {
    public static void main(String[] args) {
        SmsManager smsManager = new SmsManager();
        smsManager.send("+91989971",
                "ShareWish wishes you a happy valentine this year, Express and Win your love with ❤\uD83E\uDD14 sharewish.in");
    }
}
