package org.cloudbus.cloudsim.examples.mypower.standardloadbalancing;

import lombok.Data;

@Data
public class Vm {
    private final int requestedOperationsPerSecond;
    private Integer serverIndex = null;
}
