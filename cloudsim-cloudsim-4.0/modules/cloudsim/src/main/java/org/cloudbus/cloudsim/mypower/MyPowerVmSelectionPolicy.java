package org.cloudbus.cloudsim.mypower;

import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.PowerVm;
import org.cloudbus.cloudsim.power.PowerVmSelectionPolicy;

import java.util.List;

/**
 * A custom VM selection policy, equal to Minimum Utilization (MU) policy
 * ({@link org.cloudbus.cloudsim.power.PowerVmSelectionPolicyMinimumUtilization}).
 * <p>
 * Citation of the relevant paper (as the code is based on the {@link org.cloudbus.cloudsim.power.PowerVmSelectionPolicy}
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
public class MyPowerVmSelectionPolicy extends PowerVmSelectionPolicy {
    @Override
    public Vm getVmToMigrate(final PowerHost host) {
        final List<PowerVm> migratableVms = getMigratableVms(host);
        if (migratableVms.isEmpty()) {
            return null;
        }

        Vm vmToMigrate = null;
        double minMetric = Double.MAX_VALUE;
        for (final Vm vm : migratableVms) {
            if (vm.isInMigration()) {
                continue;
            }
            final double metric = vm.getTotalUtilizationOfCpuMips(CloudSim.clock()) / vm.getMips();
            if (metric < minMetric) {
                minMetric = metric;
                vmToMigrate = vm;
            }
        }

        return vmToMigrate;
    }
}
