import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class MainClass {
    public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new CurrencyBot());

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    static class DefaultTrustManager implements X509TrustManager {


        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}


        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}


        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
