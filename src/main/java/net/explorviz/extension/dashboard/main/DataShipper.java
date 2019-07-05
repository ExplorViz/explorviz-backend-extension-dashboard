package net.explorviz.extension.dashboard.main;

import java.util.ArrayList;
import java.util.List;
import net.explorviz.shared.landscape.model.application.Clazz;
import net.explorviz.shared.landscape.model.application.Component;
import net.explorviz.shared.landscape.model.landscape.Landscape;
import net.explorviz.shared.landscape.model.landscape.Node;
import net.explorviz.shared.landscape.model.landscape.NodeGroup;
import net.explorviz.shared.landscape.model.landscape.System;
import widget.activeclassinstances.ActiveClassInstancesModel;
import widget.activeclassinstances.ActiveClassInstancesService;
import widget.programminglanguage.ProgrammingLanguagesModel;
import widget.programminglanguage.ProgrammingLanguagesService;
import widget.ramcpu.RamCpuModel;
import widget.ramcpu.RamCpuService;
import widget.totaloverview.TotalOverviewModel;
import widget.totaloverview.TotalOverviewService;
import widget.totalrequests.TotalRequestsService;

public class DataShipper {

	private static DataShipper instance;

	private DataShipper() {
	}

	public static DataShipper getInstance() {
		if (DataShipper.instance == null) {
			DataShipper.instance = new DataShipper();
		}
		return DataShipper.instance;
	}

	private List<ActiveClassInstancesModel> activeClassInstances = new ArrayList<ActiveClassInstancesModel>();
	private List<ProgrammingLanguagesModel> programmingLanguageList = new ArrayList<ProgrammingLanguagesModel>();
	private List<RamCpuModel> ramCpuModelList = new ArrayList<RamCpuModel>();

	public void update(Landscape l) {

		TotalRequestsService.getInstance().update(l.getId(), l.getTimestamp().getTotalRequests(),
				l.getTimestamp().getTimestamp());

		activeClassInstances = new ArrayList<ActiveClassInstancesModel>();
		programmingLanguageList = new ArrayList<ProgrammingLanguagesModel>();
		ramCpuModelList = new ArrayList<RamCpuModel>();
		
		TotalOverviewModel totalOverviewModel = new TotalOverviewModel(0,0,0,0);
		totalOverviewModel.setTimestamp(l.getTimestamp().getTimestamp());

		List<System> systems = l.getSystems();
		//java.lang.System.out.println("Systems: " + systems.size());
		totalOverviewModel.setNumberOfSystems(systems.size());

		for (int i = 0; i < systems.size(); i++) {

			List<NodeGroup> nodeGroups = systems.get(i).getNodeGroups();
			//java.lang.System.out.println("NodeGroups: " + nodeGroups.size());

			for (int j = 0; j < nodeGroups.size(); j++) {

				List<Node> nodes = nodeGroups.get(j).getNodes();
				//java.lang.System.out.println("Nodes: " + nodes.size());
				
				totalOverviewModel.setNumberOfNodes(nodes.size() + totalOverviewModel.getNumberOfNodes());

				// Nodes
				for (int k = 0; k < nodes.size(); k++) {

					long timestamp = l.getTimestamp().getTimestamp();
					String nodeName = nodes.get(k).getDisplayName();
					double cpuUtilization = nodes.get(k).getCpuUtilization();
					long freeRam = nodes.get(k).getFreeRAM();
					long usedRam = nodes.get(k).getUsedRAM();

					ramCpuModelList.add(new RamCpuModel(timestamp, nodeName, cpuUtilization, freeRam, usedRam));

					List<net.explorviz.shared.landscape.model.application.Application> applications = nodes.get(k)
							.getApplications();
					//java.lang.System.out.println("Applications: " + applications.size());

					totalOverviewModel.setNumberOfApplications(applications.size() + totalOverviewModel.getNumberOfApplications());
					
					// Applications
					for (int n = 0; n < applications.size(); n++) {

						List<Component> components = applications.get(n).getComponents();
						// java.lang.System.out.println("Components: " + components.size());
						ProgrammingLanguagesModel programmingLanguagesModel = new ProgrammingLanguagesModel(
								l.getTimestamp().getTimestamp(), applications.get(n).getName(),
								applications.get(n).getProgrammingLanguage());

						programmingLanguageList.add(programmingLanguagesModel);

						checkComponents(components, l);

					}
				}
			}

		}

		ActiveClassInstancesService.getInstance().update(activeClassInstances);
		ProgrammingLanguagesService.getInstance().update(programmingLanguageList);
		RamCpuService.getInstance().update(ramCpuModelList);
		TotalOverviewService.getInstance().update(totalOverviewModel);
	}

	private void checkComponents(List<Component> c, Landscape l) {
		// java.lang.System.out.println("Components: " + c.size());
		for (int i = 0; i < c.size(); i++) {

			if (c.get(i).getClazzes() != null) {
				List<Clazz> clazzes = c.get(i).getClazzes();
				// java.lang.System.out.println("Clazzes: " + clazzes.size());

				for (int j = 0; j < clazzes.size(); j++) {

					Clazz clazz = clazzes.get(j);

					activeClassInstances
							.add(new ActiveClassInstancesModel(l.getId(), clazz.getName(), clazz.getInstanceCount()));

					// java.lang.System.out.println("classe wurde geaddet !");
				}
			}

			if (c.get(i).getChildren() != null) {
				checkComponents(c.get(i).getChildren(), l);
			}

		}
	}

}
