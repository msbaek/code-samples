package wewlc.ch15;

import org.jetbrains.annotations.NotNull;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class MailingListServer {
    public static final String SUBJECT_MARKER = "[list]";
    public static final String LOOP_HEADER = "X-Loop";
    private static HostInformation host;
    private static String listAddress;
    private static int interval;
    private static Roster roster;
    private static MailSender mailSender;
    private static Session session;
    private static MailForwarder mailForwarder;

    public MailingListServer() {
        mailSender = new MailSender();
        mailForwarder = new MailForwarder();
    }

    public static void main(String[] args) throws MessagingException {
        if (args.length != 8) {
            System.err.println("Usage: java MailingList <popHost> " +
                    "<smtpHost> <pop3user> <pop3password> " +
                    "<smtpuser> <smtppassword> <listname> " +
                    "<relayinterval>");
            return;
        }

        host = new HostInformation(args[0], args[1], args[2], args[3], args[4], args[5]);

        listAddress = args[6];

        interval = new Integer(args[7]).intValue();

        roster = null;
        try {
            roster = new FileRoster("roster.txt");
        } catch (Exception e) {
            System.err.println("unable to open roster.txt");
            return;
        }

        Properties properties = System.getProperties();

        Folder folder = null;
        Store store = null;

        try {
            while (true) {
                session = Session.getDefaultInstance(properties, null);
                store = session.getStore("pop3");
                store.connect(host.pop3Host, -1, host.pop3User, host.pop3Password);
                Folder defaultFolder = store.getDefaultFolder();
                if (defaultFolder == null) {
                    System.err.println("Unable to open default folder");
                    return;
                }
                folder = defaultFolder.getFolder("INBOX");
                if (folder == null) {
                    System.err.println("Unable to get: "
                            + defaultFolder);
                    return;
                }
                folder.open(Folder.READ_WRITE);
                if (folder.getMessageCount() != 0) {
                    Message[] messages = folder.getMessages();
                    FetchProfile fp = new FetchProfile();
                    fp.add(FetchProfile.Item.ENVELOPE);
                    fp.add(FetchProfile.Item.FLAGS);
                    fp.add("X-Mailer");
                    folder.fetch(messages, fp);
                    for (Message message : messages) {
                        mailForwarder.processMessage(message);
                    }
                }
                System.err.print(".");
                try {
                    Thread.sleep(interval * 1000);
                } catch (InterruptedException e) {
                }
            }
        } catch (Exception e) {
            System.err.println("message handling error");
            e.printStackTrace(System.err);
        } finally {
            folder.close(true);
            store.close();
        }
    }

    private static class MailSender {
        public void sendMail(MimeMessage forward) throws MessagingException {
            Properties props = new Properties();
            props.put("mail.smtp.host", host.smtpHost);

            Session smtpSession = Session.getDefaultInstance(props, null);
            Transport transport = smtpSession.getTransport("smtp");
            transport.connect(host.smtpHost, host.smtpUser, host.smtpPassword);
            transport.sendMessage(forward, roster.getAddresses());
        }
    }

    private static class MailForwarder {
        public boolean processMessage(Message message) throws MessagingException, IOException {
            if (message.getFlags().contains(Flags.Flag.DELETED))
                return true;
            System.out.println("message received: " + message.getSubject());
            if (!roster.containsOneOf(message.getFrom()))
                return true;
            MimeMessage forward = createForwardMessage(message);
            mailSender.sendMail(forward);
            message.setFlag(Flags.Flag.DELETED, true);
            return false;
        }

        @NotNull
        private MimeMessage createForwardMessage(Message message) throws MessagingException, IOException {
            InternetAddress result = null;
            Address[] fromAddress = message.getFrom();
            if (fromAddress != null && fromAddress.length > 0)
                result = new InternetAddress(fromAddress[0].toString());
            InternetAddress from = result;
            MimeMessage forward = new MimeMessage(session);
            forward.setFrom(from);
            forward.setReplyTo(new Address[]{ new InternetAddress(listAddress)});
            forward.addRecipients(Message.RecipientType.TO, listAddress);
            forward.addRecipients(Message.RecipientType.BCC, roster.getAddresses());
            String subject = message.getSubject();
            if (!message.getSubject().contains(SUBJECT_MARKER))
                subject = SUBJECT_MARKER + " " + message.getSubject();
            forward.setSubject(subject);
            forward.setSentDate(message.getSentDate());
            forward.addHeader(LOOP_HEADER, listAddress);
            Object content = message.getContent();
            if (content instanceof Multipart)
                forward.setContent((Multipart) content);
            else
                forward.setText((String) content);
            return forward;
        }
    }
}
