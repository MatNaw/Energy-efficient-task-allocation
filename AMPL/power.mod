# Author: Mateusz Nawrot

######### Declarations #########
set SERVERS;
set SERVER_EAS;
set VMS;
set VMS_DEMAND;
set SWITCHES;
set LINKS;
set LINK_EAS;

# Directed links
param switch_input {e in LINKS, r in SWITCHES} binary;
param switch_output {e in LINKS, r in SWITCHES} binary;
param server_input {e in LINKS, n in SERVERS} binary;
param server_output {e in LINKS, n in SERVERS} binary;

param network_traffic_demand {d in VMS_DEMAND};
# s^d, t^d ?
param server_efficiency_in_eas {n in SERVERS, q in SERVER_EAS};
param vm_required_efficiency {n in SERVERS, m in VMS};

param server_energetic_cost {n in SERVERS, q in SERVER_EAS};
param link_capacity {e in LINKS, k in LINK_EAS};
param link_energetic_cost {e in LINKS, k in LINK_EAS};
param const_router_cost {r in SWITCHES};

######### Variables #########
var vm_in_server {m in VMS, n in SERVERS} binary;
var server_in_eas {n in SERVERS, q in SERVER_EAS} binary;
var demand_in_link {d in VMS_DEMAND, e in LINKS} binary;
var link_eas_used {e in LINKS, k in LINK_EAS} binary;
var switch_used {r in SWITCHES} binary;

######### Subjects #########
subject to max_capacity {e in LINKS}: sum {k in LINK_EAS} link_eas_used[e,k] <= 1;
subject to unique_server_state {n in SERVERS}: sum {q in SERVER_EAS} server_in_eas[n,q] <= 1;
# TODO Point 3 next

######### Objective function #########
# Server costs
var server_costs = sum {n in SERVERS, q in SERVER_EAS} (server_energetic_cost[n,q] * server_in_eas[n,q]);
# Link costs
var link_costs = sum {e in LINKS, k in LINK_EAS} (link_energetic_cost[e,k] * link_eas_used[e,k]);
# Router costs
var router_costs = sum {r in SWITCHES} (const_router_cost[r] * switch_used[r]);

var rating = server_costs + link_costs + router_costs;

minimize Rating: rating;