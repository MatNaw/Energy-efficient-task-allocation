package org.cloudbus.cloudsim.examples.mypower.standardloadbalancing;

import java.util.List;

public class StandardLoadBalancerAlgorithm {
    StandardLoadBalancerAlgorithm(final List<Server> servers, final List<Vm> vms) {
        run(servers, vms);
        printResults(servers, vms);
    }

    public void run(final List<Server> servers, final List<Vm> vms) {
        for (final Vm vm : vms) {
            final int serverIndex = findServerWithMinimalUtilization(servers);
            assignVmToServer(vm, servers, serverIndex);
        }
    }

    private int findServerWithMinimalUtilization(final List<Server> servers) {
        int serverIndex = 0;
        int minimalUtilization = Integer.MAX_VALUE;
        for (int i = 0; i < servers.size(); i++) {
            final int serverOps = servers.get(i).getCurrentlyUsedOperationsPerSecond();
            if (serverOps < minimalUtilization) {
                minimalUtilization = serverOps;
                serverIndex = i;
            }
        }
        return serverIndex;
    }

    private void assignVmToServer(final Vm vm, final List<Server> servers, final int serverIndex) {
        vm.setServerIndex(serverIndex);
        servers.get(serverIndex).assignVm(vm);
    }

    private void printResults(final List<Server> servers, final List<Vm> vms) {
        System.out.println("VMs assignment in servers:");
        System.out.println("-------------------------");
        double serverEnergyUsageSum = 0.0D;
        for (int i = 0; i < servers.size(); i++) {
            final Server server = servers.get(i);
            final double serverEnergyUsage = server.getServerType().getEasCost()[server.getEas()];
            serverEnergyUsageSum += serverEnergyUsage;
            final int currentOpsUsage = server.getCurrentlyUsedOperationsPerSecond();
            final int maxServerOps = server.getServerType()
                    .getOperationsPerSecond()[server.getServerType().getOperationsPerSecond().length - 1];

            System.out.printf("Server index: %s%n", i);
            System.out.printf("Number of VMs assigned to this server: %s%n", server.getAssignedVms().size());
//            System.out.printf("Assigned VMs: %s%n", server.getAssignedVms());
            System.out.printf("Server EAS: %s%n", server.getEas() + 1);
            System.out.printf("Currently used operations per second: %s%n", currentOpsUsage);
            System.out.printf("Server maximum operations per second: %s%n", maxServerOps);
            System.out.printf("Server operations utilization %%: %s%%%n",
                              Math.round((float) currentOpsUsage * 100 / maxServerOps));
            System.out.printf("Server operations utilization %% (rounded to tenths): %s%%%n",
                              Math.round((float) currentOpsUsage * 10 / maxServerOps) * 10);
            System.out.printf("Energy usage: %s%n", serverEnergyUsage);
            System.out.println("-------------------------");
        }
        System.out.printf("Total servers energy usage: %s%n", serverEnergyUsageSum);
    }
}
