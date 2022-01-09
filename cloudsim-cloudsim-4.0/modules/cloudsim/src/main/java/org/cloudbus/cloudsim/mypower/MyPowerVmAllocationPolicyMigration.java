package org.cloudbus.cloudsim.mypower;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;

import java.util.List;

/**
 * A custom VM allocation policy, equal to Static Threshold (THR) policy with threshold value equal to 1.
 * ({@link org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationStaticThreshold}).
 * <p>
 * Citation of the relevant paper (as the code is based on the {@link org.cloudbus.cloudsim.power.PowerVmAllocationPolicyMigrationAbstract}
 * from '<b>power</b>' package):
 *
 * <ul>
 * <li><a href="http://dx.doi.org/10.1002/cpe.1867">Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012</a>
 * </ul>
 *
 * @author Mateusz Nawrot
 */
public class MyPowerVmAllocationPolicyMigration extends PowerVmAllocationPolicyMigrationAbstract {
    /**
     * Instantiates a new MyPowerVmAllocationPolicyMigration.
     *
     * @param hostList          the host list
     * @param vmSelectionPolicy the vm selection policy
     */
    public MyPowerVmAllocationPolicyMigration(final List<? extends Host> hostList,
                                              final PowerVmSelectionPolicy vmSelectionPolicy) {
        super(hostList, vmSelectionPolicy);
    }

    /**
     * Checks if a host is over utilized.
     *
     * @param host the host
     * @return true, if the host is over utilized; false otherwise
     */
    @Override
    public boolean isHostOverUtilized(final PowerHost host) {
        final double upperThreshold = 1;
        addHistoryEntry(host, upperThreshold);

        double totalRequestedMips = 0;
        for (final Vm vm : host.getVmList()) {
            totalRequestedMips += vm.getCurrentRequestedTotalMips();
        }
        final double utilization = totalRequestedMips / host.getTotalMips();

        return utilization > upperThreshold;
    }
}
