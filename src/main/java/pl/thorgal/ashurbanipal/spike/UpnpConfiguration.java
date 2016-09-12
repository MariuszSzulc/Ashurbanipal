package pl.thorgal.ashurbanipal.spike;

import java.util.List;

import com.offbynull.portmapper.PortMapperFactory;
import com.offbynull.portmapper.gateway.Bus;
import com.offbynull.portmapper.gateway.Gateway;
import com.offbynull.portmapper.gateways.network.NetworkGateway;
import com.offbynull.portmapper.gateways.network.internalmessages.KillNetworkRequest;
import com.offbynull.portmapper.gateways.process.ProcessGateway;
import com.offbynull.portmapper.gateways.process.internalmessages.KillProcessRequest;
import com.offbynull.portmapper.mapper.MappedPort;
import com.offbynull.portmapper.mapper.PortMapper;
import com.offbynull.portmapper.mapper.PortType;

public class UpnpConfiguration {

	private Bus networkBus;
	private Bus processBus;

	private PortMapper mapper;
	private MappedPort mappedPort;

	public String createPortForwardingRule(int port) throws Exception {
		cleanUp();
		
		// Start gateways
		Gateway network = NetworkGateway.create();
		Gateway process = ProcessGateway.create();
		networkBus = network.getBus();
		processBus = process.getBus();

		// Discover port forwarding devices and take the first one found
		List<PortMapper> mappers = PortMapperFactory.discover(networkBus, processBus);
		if (mappers.isEmpty()) {
			return "No upnp devices discovered.";
		}
		
		mapper = mappers.get(0);
		mappedPort = mapper.mapPort(PortType.TCP, 12345, 55555, 60);
		
		return "Port mapping added: " + mappedPort;

		// Refresh mapping half-way through the lifetime of the mapping (for
		// example,
		// if the mapping is available for 40 seconds, refresh it every 20
		// seconds)
		// while(!shutdown) {
		// mappedPort = mapper.refreshPort(mappedPort, mappedPort.getLifetime()
		// / 2L);
		// System.out.println("Port mapping refreshed: " + mappedPort);
		// Thread.sleep(mappedPort.getLifetime() * 1000L);
		// }
	}

	public void cleanUp() throws Exception {
		if (mapper != null) {
			mapper.unmapPort(mappedPort);
		}

		// Stop gateways
		if (networkBus != null) {
			networkBus.send(new KillNetworkRequest());
		}
		if (processBus != null) {
			processBus.send(new KillProcessRequest());
		}
	}

}
