import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.Properties;
import java.lang.String;
import java.rmi.RemoteException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.cli.*;

import com.cisco.ccm.taps.service.testByte;
import com.cisco.ccm.taps.service.TAPSIntf;


public class Main {

    public static void main(String[] args) {

        //CUCM IP Address or Host
        String cucmHost = "none";
        int cucmRMIPort = 9050;
        String autodn = "none";
        String batdn = "none";
        String useText = "java -jar tapdance.jar -r <ip> -b <dn> -a <dn>";
        String header = "\nCisco TAPS CLI interface to associate BAT DN with an Auto Registered DN\n\nArguments:\n\n";
        String footer = "\nPlease report issues to: nmarus@gmail.com\n\n";

        //create the options
        Options options = new Options();
        try {
            options.addOption(Option.builder("r").required(true).longOpt("rmi-host").hasArg().desc("Cisco Communication Manager RMI Hostname or IP").build());
            options.addOption(Option.builder("b").required(true).longOpt("bat-dn").hasArg().desc("Directory number of BAT phone").build());
            options.addOption(Option.builder("a").required(true).longOpt("auto-dn").hasArg().desc("Directory number of Auto Registered phone").build());
            options.addOption(Option.builder("h").required(false).longOpt("help").desc("Help").build());
        }
        catch(Exception e) {}

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options,args );

            if(line.hasOption("help")) {
                formatter.printHelp(useText, header, options, footer);
                System.exit(0);
            }

            // validate that all required options are set
            if(line.hasOption("rmi-host") && line.hasOption("bat-dn") && line.hasOption("auto-dn")) {
                cucmHost = line.getOptionValue("rmi-host");
                batdn = line.getOptionValue("bat-dn");
                autodn = line.getOptionValue("auto-dn");
            } else {
                formatter.printHelp(useText, header, options, footer);
                System.exit(1);
            }

        }
        catch(ParseException exp) {
            formatter.printHelp(useText, header, options, footer);
            System.exit(1);
        }

        //CUCM RMI URL
        String rmiURL = "rmi://"+cucmHost+":"+cucmRMIPort+"/TAPS";

        //Setup Cisco TAPS
        Properties objProperties = new Properties();
        testByte objTest = new testByte();

        //Test Socket
        if(testsocket(cucmHost, cucmRMIPort)) {
            try {
                //Setup Cisco TAPS Object
                TAPSIntf objTAPS = (TAPSIntf) java.rmi.Naming.lookup(rmiURL);
                //Execute TAPS call to CUCM and get return code
                int TAPStatus = objTAPS.getBATConfig(autodn,batdn);
                if(TAPStatus == 0) {
                    System.out.println("Success.");
                    System.exit(0);
                } else {
                    System.out.println("Error: ("+TAPStatus+")");
                    System.exit(TAPStatus);
                }
            } catch (NotBoundException e) {
                System.out.println("Error: Not bound.");
                System.exit(1);
            } catch (MalformedURLException e) {
                System.out.println("Error: Malformed URL.");
                System.exit(1);
            } catch (RemoteException e) {
                System.out.println("Error: Remote Exception.");
                System.exit(1);
            }
        } else {
            System.out.println("Error: Connection not available to RMI at "+rmiURL);
            System.exit(1);
        }

        System.exit(0);
    }

    public static boolean testsocket(String ipadd, int ipport ) {

        boolean portAvailable;
        int delay = 1000; // 1 s
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ipadd, ipport), delay);
            portAvailable = socket.isConnected();
            socket.close();
        }
        catch (Exception e) {
            portAvailable = false;
        }
        return portAvailable;

    }
}
