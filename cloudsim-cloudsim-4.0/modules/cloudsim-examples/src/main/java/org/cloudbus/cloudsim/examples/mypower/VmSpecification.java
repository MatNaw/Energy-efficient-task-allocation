package org.cloudbus.cloudsim.examples.mypower;

import lombok.Getter;

/**
 * Specification of the VMs, defining the MIPS (Million Instructions Per Second) value and custom {@link AmplNetworkType},
 * which will be used during the simulation.
 * <br/><br/>
 * In order to compare the CloudSim simulations with solution achieved in AMPL, ths MIPS value of the VMs
 * must be rescaled according to the three factors:
 * <ul>
 *     <li>host's MIPS value</li>
 *     <li>number of available host's PEs</li>
 *     <li>maximum level of '<b>ssj_ops</b>' (number of maximum operations per second performed by the host)</li>
 * </ul>
 * The maximum level of '<b>ssj_ops</b>' per host is presented by <i>Standard Performance Evaluation Corporation</i> in
 * the following documentation:
 * <a href="https://www.spec.org/power_ssj2008/results/res2011q1/">First Quarter 2011 SPECpower_ssj2008 Results</a>
 *
 * @author Mateusz Nawrot
 */
@Getter
public class VmSpecification {
    // static values of OPS (Operations Per Second) used during the AMPL solution researches for small and big network
    final static int AMPL_VM_OPS_SMALL_NETWORK = 5000;
    final static int AMPL_VM_OPS_BIG_NETWORK = 50000;

    private final int mips;
    private final AmplNetworkType amplNetworkType;

    public VmSpecification(final HostSpecification hostSpecification, final AmplNetworkType amplNetworkType) {
        this.mips = calculateMips(hostSpecification, amplNetworkType);
        this.amplNetworkType = amplNetworkType;
    }

    private int calculateMips(final HostSpecification hostSpecification, final AmplNetworkType amplNetworkType) {
        if (amplNetworkType == AmplNetworkType.SMALL) {
            return Math.round(
                    hostSpecification.getMips() * (AMPL_VM_OPS_SMALL_NETWORK / (float) hostSpecification.getMaxOperationsPerSecond()));
        }
        return Math.round(
                hostSpecification.getMips() * (AMPL_VM_OPS_BIG_NETWORK / (float) hostSpecification.getMaxOperationsPerSecond()));
    }
}
