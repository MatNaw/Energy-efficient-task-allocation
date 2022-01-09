package org.cloudbus.cloudsim.examples.mypower;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cloudbus.cloudsim.power.models.PowerModel;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G3PentiumD930;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G5Xeon3075;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerIbmX3250XeonX3470;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerIbmX3250XeonX3480;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerIbmX3550XeonX5670;
import org.cloudbus.cloudsim.power.models.PowerModelSpecPowerIbmX3550XeonX5675;

/**
 * Specification of the hosts, defining the:
 * <ul>
 *     <li>{@link PowerModel}</li>
 *     <li>MIPS (Million Instructions Per Second) value</li>
 *     <li>number of PEs (Processing Elements)</li>
 *     <li>amount of RAM</li>
 *     <li>number of maximum operations per second performed by the host</li>
 * </ul>
 * The class contains the predefined static methods representing the instances of host specifications, which were used
 * in the simulation of the energy-efficient task allocation.
 * <br/><br/>
 * In order to compare the CloudSim simulations with solution achieved in AMPL, ths MIPS value of the VMs
 * must be rescaled. See {@link VmSpecification} for more details.
 *
 * @author Mateusz Nawrot
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HostSpecification {
    private final PowerModel powerModel;
    private final int mips;
    private final int pes;
    private final int ram;
    private final int maxOperationsPerSecond;

    public static HostSpecification HpProLiantMl110G3PentiumD930() {
        return new HostSpecification(
                new PowerModelSpecPowerHpProLiantMl110G3PentiumD930(), 3000, 2, 4096, 52303);
    }

    public static HostSpecification HpProLiantMl110G4Xeon3040() {
        return new HostSpecification(
                new PowerModelSpecPowerHpProLiantMl110G4Xeon3040(), 1860, 2, 4096, 54479);
    }

    public static HostSpecification HpProLiantMl110G5Xeon3075() {
        return new HostSpecification(
                new PowerModelSpecPowerHpProLiantMl110G5Xeon3075(), 2660, 2, 4096, 98472);
    }

    public static HostSpecification IbmX3250XeonX3470() {
        return new HostSpecification(
                new PowerModelSpecPowerIbmX3250XeonX3470(), 2933, 4, 8192, 313804);
    }

    public static HostSpecification IbmX3250XeonX3480() {
        return new HostSpecification(
                new PowerModelSpecPowerIbmX3250XeonX3480(), 3067, 4, 8192, 320730);
    }

    public static HostSpecification IbmX3550XeonX5670() {
        return new HostSpecification(
                new PowerModelSpecPowerIbmX3550XeonX5670(), 2933, 6, 12288, 912178);
    }

    public static HostSpecification IbmX3550XeonX5675() {
        return new HostSpecification(
                new PowerModelSpecPowerIbmX3550XeonX5675(), 3067, 6, 16384, 890651);
    }
}
