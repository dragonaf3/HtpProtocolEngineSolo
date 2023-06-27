import htp.HTPEndpoint;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1 && args.length != 2) {
            System.out.println("Verwendung: htp.HTPEndpoint <Port> [<Hostname>]");
            return;
        }

        HTPEndpoint htpEndpoint = new HTPEndpoint();

        int port = Integer.parseInt(args[0]);
        htpEndpoint.startServer(port);

        if (args.length == 2) {
            String hostname = args[1];
            htpEndpoint.connectToServer(hostname, port);
        }
    }
}

