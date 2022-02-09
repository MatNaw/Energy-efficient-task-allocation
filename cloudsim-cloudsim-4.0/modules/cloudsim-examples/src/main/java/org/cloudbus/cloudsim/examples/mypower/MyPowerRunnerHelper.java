package org.cloudbus.cloudsim.examples.mypower;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.UtilizationModelNull;
import org.cloudbus.cloudsim.examples.power.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * The helper class for the energy-efficient task runner.
 * <p>
 * It provides two static variables representing the number of hosts and VMS (which will be created during
 * the simulation startup) and a method for creating cloudlets.
 *
 * @author Mateusz Nawrot
 */
public class MyPowerRunnerHelper {
    public final static int NUMBER_OF_HOSTS = Constants.TEST_VM_SPECIFICATION.getAmplNetworkType() == AmplNetworkType.SMALL
            ? 4   // small network
            : 20; // big network
    public final static int NUMBER_OF_VMS = Constants.TEST_HOST_SPECIFICATION.getPes() *
            (Constants.TEST_VM_SPECIFICATION.getAmplNetworkType() == AmplNetworkType.SMALL
                    ? 20 //31  // small network
                    : 10); //20 // big network

    /**
     * Creates the cloudlet list.
     *
     * @param brokerId        the broker id
     * @param cloudletsNumber the number of cloudlets to be created
     * @return List of created cloudlets
     */
    public static List<Cloudlet> createCloudletList(final int brokerId, final int cloudletsNumber) {
        final List<Cloudlet> list = new ArrayList<>();

        final long fileSize = 300;
        final long outputSize = 300;
        final UtilizationModel utilizationModelNull = new UtilizationModelNull();

        for (int i = 0; i < cloudletsNumber; i++) {
            final Cloudlet cloudlet = new Cloudlet(
                    i,
                    Constants.CLOUDLET_LENGTH,
                    Constants.CLOUDLET_PES,
                    fileSize,
                    outputSize,
                    new UtilizationModelFull(),
                    utilizationModelNull,
                    utilizationModelNull);
            cloudlet.setUserId(brokerId);
            cloudlet.setVmId(i);
            list.add(cloudlet);
        }

        return list;
    }
}
