import org.eclipse.jetty.server.Server;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationStarter {

        public static void main(String[] args) throws Exception {
            Server jettyServer;
            jettyServer = new Server(9090);
            jettyServer.setHandler(new AppContextConfig().getHandlersConfig());
            try {
                jettyServer.start();
                jettyServer.join();
            } catch (Exception e) {
                Logger.getLogger(ApplicationStarter.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                jettyServer.destroy();
            }
        }
}
