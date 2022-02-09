package org.cloudbus.cloudsim.examples.mypower.standardloadbalancing;

import java.util.ArrayList;
import java.util.List;

public class StandardLoadBalancerRunner {
    public static void main(final String[] args) {
        final int NUMBER_OF_SERVERS = 4;
        final ServerType SERVER_TYPE = ServerType.IbmX5675();
        final int NUMBER_OF_VMS = 20;
        final int VM_OPERATIONS_PER_SECOND = 41000;

        final List<Vm> vms = createTaskQueue(VM_OPERATIONS_PER_SECOND, NUMBER_OF_VMS);
        final List<Server> servers = createServers(SERVER_TYPE, NUMBER_OF_SERVERS);

        System.out.printf("Number of servers: %s%n", NUMBER_OF_SERVERS);
        System.out.printf("Server type: %s%n", SERVER_TYPE.getTypeName());
        System.out.printf("Number of VMs: %s%n", NUMBER_OF_VMS);
        System.out.printf("VMs requested operations per second: %s%n", VM_OPERATIONS_PER_SECOND);

        new StandardLoadBalancerAlgorithm(servers, vms);
    }

    private static List<Server> createServers(final ServerType serverType, final int numberOfServers) {
        final List<Server> result = new ArrayList<>();
        for (int i = 0; i < numberOfServers; i++) {
            result.add(new Server(serverType));
        }
        return result;
    }

    private static List<Vm> createTaskQueue(final int requestedOperations, final int numberOfTasks) {
        final List<Vm> result = new ArrayList<>();
        for (int i = 0; i < numberOfTasks; i++) {
            result.add(new Vm(requestedOperations));
        }
        return result;
    }
}
