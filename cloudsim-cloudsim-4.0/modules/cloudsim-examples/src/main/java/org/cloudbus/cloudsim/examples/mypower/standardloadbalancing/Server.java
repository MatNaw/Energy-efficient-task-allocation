package org.cloudbus.cloudsim.examples.mypower.standardloadbalancing;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Server {
    private final ServerType serverType;
    private int eas = 0;
    private int currentlyUsedOperationsPerSecond = 0;
    private List<Vm> assignedVms = new ArrayList<>();

    public void assignVm(final Vm vm) {
        currentlyUsedOperationsPerSecond += vm.getRequestedOperationsPerSecond();
        while (currentlyUsedOperationsPerSecond > serverType.getOperationsPerSecond()[eas]) {
            eas += 1;
            if (eas >= serverType.getEasCost().length) {
                throw new IllegalStateException(
                        String.format("System out of server resources - cannot handle new VM: %s", vm));
            }
        }
        assignedVms.add(vm);
    }
}
