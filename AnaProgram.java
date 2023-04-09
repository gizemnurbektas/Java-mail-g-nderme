import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// 21120205003-GİZEM NUR BEKTAŞ

public class AnaProgram {

    static ArrayList Uyeler;

    public static void main(String[] args) throws IOException, MessagingException {

        AnaProgram ap = new AnaProgram();

        Dosyaislemleri di = new Dosyaislemleri();
        Uyeler = di.Dosyaoku();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("MENU");
            System.out.println("1- Elit üye ekleme");
            System.out.println("2- Genel Üye ekleme");
            System.out.println("3- Mail Gönderme");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    ap.ElitUyeEkle();
                    di.DosyaYaz(Uyeler);
                    break;
                case 2:
                    ap.GenelUyeEkle();
                    di.DosyaYaz(Uyeler);
                    break;
                case 3:
                    ap.EmailKontrol();
                    break;
                default:
                    System.out.println("Geçersiz seçim");
                    break;
            }
            break;
        }
    }

    public void EmailKontrol() throws IOException, MessagingException {
        Dosyaislemleri di = new Dosyaislemleri();
        ArrayList<String> TumUyeler = di.Dosyaoku();
        String eposta_liste = "";
        String Mail_icerik = "";
        while (true) {
            System.out.println("MENU");
            System.out.println("1- Elit Üyelere Mail");
            System.out.println("2- Genel Üyelere Mail");
            System.out.println("3- Tüm Üyelere Mail");
            Scanner scanner = new Scanner(System.in);
            int secim = scanner.nextInt();

            switch (secim) {
                case 1:
                    for (int i = 0; i < TumUyeler.size(); i += 4) {
                        if (TumUyeler.get(i).equalsIgnoreCase("ELİT ÜYELER")) {
                            eposta_liste = (eposta_liste +  TumUyeler.get(i + 3)+ ";").trim();
                            eposta_liste=eposta_liste.substring(0, eposta_liste.length() - 1);
                        }
                    }
                    System.out.println("Elit Üyelere Gönderilecek Mesaj İçeriğini Giriniz..");
                    Mail_icerik = scanner.nextLine();
                    EpostaGonder(eposta_liste, "Bilgi", Mail_icerik);
                    break;
                case 2:
                    for (int i = 0; i < TumUyeler.size(); i += 4) {
                        if (TumUyeler.get(i).equalsIgnoreCase("GENEL ÜYELER")) {
                            eposta_liste = (eposta_liste +  TumUyeler.get(i + 3)+ ";").trim();
                        }
                    }
                    System.out.println("Genel Üyelere Gönderilecek Mesaj İçeriğini Giriniz..");
                    Mail_icerik = scanner.nextLine();
                    EpostaGonder(eposta_liste, "Bilgi", Mail_icerik);

                    break;
                case 3:
                    for (int i = 0; i < TumUyeler.size(); i += 4) {
                        eposta_liste = (eposta_liste +  TumUyeler.get(i + 3)+ ";").trim();
                    }

                    System.out.println("Tüm Üyelere Gönderilecek Mesaj İçeriğini Giriniz..");
                    Mail_icerik = scanner.nextLine();
                    EpostaGonder(eposta_liste, "Bilgi", Mail_icerik);

                    break;
                default:
                    System.out.println("Geçersiz seçim");
                    break;
            }
            break;

        }
    }

    public void ElitUyeEkle() {
        ArrayList<String> ElitUye = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        System.out.print("İsim: ");
        String isim = scanner.nextLine();
        ElitUye.add("ELİT ÜYELER");
        ElitUye.add(isim);
        System.out.print("Soyisim: ");
        String soyisim = scanner.nextLine();
        ElitUye.add(soyisim);
        System.out.print("Email: ");
        String email = scanner.nextLine();
        ElitUye.add(email);
        Uyeler.addAll(ElitUye);
    }

    public void GenelUyeEkle() {
        ArrayList<String> GenelUye = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        System.out.print("İsim: ");
        String isim = scanner.nextLine();
        GenelUye.add("GENEL ÜYELER");
        GenelUye.add(isim);
        System.out.print("Soyisim: ");
        String soyisim = scanner.nextLine();
        GenelUye.add(soyisim);
        System.out.print("Email: ");
        String email = scanner.nextLine();
        GenelUye.add(email);
        Uyeler.addAll(GenelUye);
    }

    public void EpostaGonder(String alici, String konu, String metin) throws AddressException, MessagingException {
        // MailConfig sınıfını kullanarak bir Session nesnesi oluşturuyoruz.
        MailAyarlari mailConfig = new MailAyarlari("username", "password");
        Session session = mailConfig.getSession();

        // MimeMessage sınıfı kullanarak bir e-posta mesajı oluşturuyoruz.
        MimeMessage mesaj = new MimeMessage(session);
        mesaj.setFrom(new InternetAddress("adress"));
        mesaj.addRecipient(Message.RecipientType.TO, new InternetAddress(alici));
        mesaj.setSubject(konu);
        mesaj.setText(metin);

        // Transport sınıfının send() metodunu kullanarak mesajı gönderiyoruz.
        Transport.send(mesaj);

    }

}


import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class Dosyaislemleri {

    public ArrayList<String> Dosyaoku() throws IOException {
        ArrayList<String> Uyeler = new ArrayList<String>();
        String fileName = "Kullanıcılar.txt";
        FileReader fr = new FileReader(fileName, Charset.forName("UTF-8"));

        try {
            BufferedReader bufferedReader = new BufferedReader(fr);

            String line = bufferedReader.readLine();
            String rol = "";

            while (line != null) {
                if (!"".equals(line)) {
                    if (line.startsWith("#")) {
                        if (line.startsWith("#") && line.endsWith("#")) {
                            rol = line.substring(1, line.length() - 1).trim();

                        } else {
                            rol = line.substring(1);
                        }

                    } else {
                        String[] data = line.split("\t");
                        String isim = data[0];
                        String soyisim = data[1];
                        String email = data[2];
                        Uyeler.add(rol);
                        Uyeler.add(isim);
                        Uyeler.add(soyisim);
                        Uyeler.add(email);
                    }
                }
                line = bufferedReader.readLine();

            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uyeler;
    }

    public void DosyaYaz(ArrayList<String> Yazilacak_bilgiler) throws IOException {
        try {
            String fileName = "Kullanıcılar.txt";
            FileWriter writer = new FileWriter(fileName, false);
            writer.write("#ELİT ÜYELER#" + "\n");
            for (int i = 0; i < Yazilacak_bilgiler.size(); i += 4) {

                if (Yazilacak_bilgiler.get(i).equalsIgnoreCase("ELİT ÜYELER")) {
                    writer.write(Yazilacak_bilgiler.get(i + 1) + "\t");
                    writer.write(Yazilacak_bilgiler.get(i + 2) + "\t");
                    writer.write(Yazilacak_bilgiler.get(i + 3) + "\n");
                }
            }
            writer.write("#GENEL ÜYELER" + "\n");
            for (int i = 0; i < Yazilacak_bilgiler.size(); i += 4) {
                if (Yazilacak_bilgiler.get(i).equalsIgnoreCase("GENEL ÜYELER")) {
                    writer.write(Yazilacak_bilgiler.get(i + 1) + "\t");
                    writer.write(Yazilacak_bilgiler.get(i + 2) + "\t");
                    writer.write(Yazilacak_bilgiler.get(i + 3) + "\n");
                }
            }

            writer.close();
            System.out.println("Bilgiler başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        Dosyaislemleri di = new Dosyaislemleri();

        for (int i = 0; i < di.Dosyaoku().size(); i++) {
            System.out.println(di.Dosyaoku().get(i));
        }
        di.DosyaYaz(di.Dosyaoku());

    }

}


import java.util.Properties;
import javax.mail.Session;


public class MailAyarlari extends MailOzellikleri {

    private String username;
    private String password;
    private String protocol;

    public MailAyarlari(String username, String password) {
        super("smtp.gmail.com", "587", "true", "true");
        this.protocol = "smtp";
        this.username = username;
        this.password = password;
    }

    public Session getSession() {
        Properties props = this.getProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props,new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });
    }
}


import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

public class MailGonder {
    private String host;
    private String username;
    private String password;
    private Properties props;

    public MailGonder(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;

    }

    public void sendMail(String recipient, String subject, String body) throws MessagingException {
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}


import java.util.Properties;

public class MailOzellikleri {

    private final String host;
    private final String port;
    private final String auth;
    private final String starttls;
    
    public MailOzellikleri(String host, String port, String auth, String starttls) {
        this.host = host;
        this.port = port;
        this.auth = auth;
        this.starttls = starttls;
    }
    
    public Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        return props;
    }
    
}