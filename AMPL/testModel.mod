set SERVERS;
set SERVERS_EAS;
set LINKS;
set DEMAND;

param server_output {l in LINKS, s in SERVERS};
param server_input {l in LINKS, s in SERVERS};
param demand {d in DEMAND};
param server_efficiency_in_eas {s in SERVERS, se in SERVERS_EAS};
param server_energetic_cost {s in SERVERS, se in SERVERS_EAS};



var server_in_eas {s in SERVERS, se in SERVERS_EAS} binary;
var eas_1 binary;
var eas_2 binary;
var eas_3 binary;
param cost_in_eas_1 := 10;
param cost_in_eas_2 := 20;
param cost_in_eas_3 := 120;


var demand_in_link {d in DEMAND, l in LINKS};



param link_capacity_1 := 10;
param link_capacity_2 := 20;
param link_capacity_3 := 150;

var link_capacity =
	(link_capacity_1 * eas_1) + (link_capacity_2 * eas_2) + (link_capacity_3 * eas_3);


subject to demand_fulfilled {d in DEMAND}: demand[d] <= link_capacity;
subject to unique_server_state {s in SERVERS}: sum {se in SERVERS_EAS} server_in_eas[s,se] >= 1;




var server_costs = sum {s in SERVERS, se in SERVERS_EAS} (server_energetic_cost[s,se] * server_in_eas[s,se]);
var link_costs = (cost_in_eas_1 * eas_1) + (cost_in_eas_2 * eas_2) + (cost_in_eas_3 * eas_3);
var total_cost = server_costs + link_costs;

minimize TotalCost: total_cost;










