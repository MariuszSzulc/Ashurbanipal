package pl.thorgal.ashurbanipal.spike;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;

public class UpnpConfiguration {

	private static final String MAPPING_NAME = "Ashurbanipal automated NAT forwarding";

	private UpnpService upnpService;

	public void createPortForwardingRule(String hostname, int port) {
		cleanUp();
		
		PortMapping desiredMapping = new PortMapping(port, hostname, PortMapping.Protocol.TCP, MAPPING_NAME);
		upnpService = new UpnpServiceImpl(new PortMappingListener(desiredMapping));

		upnpService.getControlPoint().search();
	}

	public void cleanUp() {
		if (upnpService != null) {
			upnpService.shutdown();
		}
	}

}
