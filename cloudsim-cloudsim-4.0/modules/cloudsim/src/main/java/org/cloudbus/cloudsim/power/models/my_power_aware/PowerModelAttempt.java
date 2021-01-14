package org.cloudbus.cloudsim.power.models.my_power_aware;

import org.cloudbus.cloudsim.power.models.PowerModelSpecPower;

public class PowerModelAttempt extends PowerModelSpecPower {
    /**
     * The power consumption according to the utilization percentage.
     *
     * @see #getPowerData(int)
     * <p>
     * Based on:
     * @see org.cloudbus.cloudsim.power.models.PowerModelSpecPowerHpProLiantMl110G4Xeon3040
     */
    private final double[] power = {86, 89.4, 92.6, 96, 99.5, 102, 106, 108, 112, 114, 117};

    @Override
    protected double getPowerData(final int index) {
        return power[index];
    }
}
