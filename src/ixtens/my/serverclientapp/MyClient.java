/**
 * Author Asya
 * Date Oct 21, 2016
 */
package ixtens.my.serverclientapp;

/**
 * Client application
 *
 */
public class MyClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Client client = new Client("localhost", 2323);
        for (int i = 0; i < 10; i++) {
            new Thread(new Caller(client)).start();
        }
    }

    private static class Caller implements Runnable {
        private Client client;
        
        public Caller(Client client) {
            this.client = client;
        }

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            while (true) {
                try {
                    client.remoteCall("service1", "sleep", new Object[] { new String("lala") });
                    System.out.println(
                            "Current Date is:" + client.remoteCall("service1", "getCurrentDate", new Object[] {}));
                } finally {
                    if (!client.isConnected()) {
                        client.close();
                        return;
                    }
                }
            }
        }
    }
}
