package luck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public class Lab5 {

	private static int rank = 3;
	private static List<Node> nodes = new ArrayList<>();

	public static void main(String[] args) {
		for (int i = 0; i < rank * rank; i++) {
			Collections.addAll(nodes, getCluster());
		}
		int layerDiff = rank * 7;
		nodes.forEach(node -> {
			switch (node.getId() % 7) {
				case 5:
				case 6:
					node.addLink(nodes.get((node.getId() + layerDiff - 5) % nodes.size()));
					break;
				case 4:
					if (node.getId() / layerDiff != (node.getId() + 5) / layerDiff) {
						node.addLink(nodes.get(node.getId() + 5 - layerDiff));
					} else {
						node.addLink(nodes.get(node.getId() + 5));
					}
					break;
			}
		});
//		printLinks();

		List<List<String>> jumps = new ArrayList<>();
		Supplier<List<String>> s = ArrayList::new;
		Collections.addAll(jumps, s.get(), s.get(), s.get(), s.get());

		jumps.get(0).add("Phase 1: \n");
		nodes.forEach(node -> {
			switch (node.getId() % 7) {
				case 2:
				case 5:
					jumps.get(0).add(send(node, nodes.get(node.getId() - 2)));
					break;
				case 6:
				case 4:
					jumps.get(1).add(send(node, nodes.get(node.getId() - 3)));
					break;
				case 0:
					jumps.get(2).add(send(node, nodes.get(node.getId() + 3)));
					break;
				case 1:
					jumps.get(3).add(send(node, nodes.get(node.getId() + 2)));
					break;
			}
		});

		jumps.get(3).add("\nPhase 2:");

		if (rank > 1) {
			jumps.add(s.get());
			jumps.add(s.get());

			//init phase
			nodes.forEach(node -> {
				switch (node.getId() % 7) {
					case 3:
						jumps.get(4).add(send(node, nodes.get(node.getId() - 2)));
						break;
				}
			});
			nodes.forEach(node -> {
				switch (node.getId() % 7) {
					case 3:
						jumps.get(5).add(send(node, nodes.get(node.getId() + 2)));
						break;
				}
			});

			for (int i = 0; i < rank / 2; i++) {
				jumps.add(s.get());
				jumps.add(s.get());
				jumps.add(s.get());
				jumps.add(s.get());
				int j = i;
				nodes.forEach(node -> {
					switch (node.getId() % 7) {
						case 1:
							jumps.get(5 + j * 3).add(send(node,
									nodes.get((node.getId() + 5 + nodes.size() - layerDiff) % nodes.size())));
							break;
						case 6:
							jumps.get(6 + j * 3).add(send(node, nodes.get(node.getId() - 2)));
						case 4:
							jumps.get(7 + j * 3).add(send(node, nodes.get(node.getId() - 3)));
							break;
					}
				});
			}

			for (int i = 0; i < (rank - 1) / 2; i++) {
				int j = i;
				nodes.forEach(node -> {
					switch (node.getId() % 7) {
						case 5:
							jumps.get(6 + j * 3).add(send(node, nodes.get((node.getId() + 2) % nodes.size())));
							break;
						case 0:
							jumps.get(7 + j * 3).add(send(node, nodes.get(node.getId() + 2)));
						case 2:
							jumps.get(8 + j * 3).add(send(node, nodes.get(node.getId() + 3)));
							break;
					}
				});
			}
			if (rank > 2) {
				HashSet<String> rem = new HashSet<>();
				nodes.forEach(node -> {
					switch (node.getId() % 7) {
						case 0:
							jumps.get(jumps.size() - 2).forEach(expr -> {
								if (expr.startsWith(" (" + node.getId())) {
									rem.add(expr);
								}
							});
							jumps.get(jumps.size() - 2).add(send(node, nodes.get(node.getId() + 1)));
							break;
					}
				});
				jumps.get(jumps.size() - 2).removeAll(rem);
			}

			jumps.get(jumps.size() - 1).add("Phase 3:");

			int ptr = jumps.size();

			for (int i = 0; i < rank - 1; i++) {
				Collections.addAll(jumps, s.get(), s.get(), s.get(), s.get());
				int j = i;
				nodes.forEach(node -> {
					switch (node.getId() % 7) {
						case 1:
							jumps.get(ptr + j * 4).add(send(node, nodes.get(node.getId() + 3)));
							break;
						case 4:
							if (node.getId() / layerDiff != (node.getId() + 5) / layerDiff) {
								jumps.get(ptr + j * 4 + 1).add(send(node, nodes.get(node.getId() + 5 - layerDiff)));
							} else {
								jumps.get(ptr + j * 4 + 1).add(send(node, nodes.get(node.getId() + 5)));
							}
							break;
						case 2:
							jumps.get(ptr + j * 4 + 2).add(send(node, nodes.get(node.getId() - 2)));
							break;
					}
				});
			}
			for (int i = 0; i < rank - 2; i++) {
				int j = i;
				nodes.forEach(node -> {
					switch (node.getId() % 7) {
						case 0:
							jumps.get(ptr + j * 4 + 3).add(send(node, nodes.get(node.getId() + 1)));
							break;
					}
				});
			}
			int ptr2 = jumps.size();

			Collections.addAll(jumps, s.get(), s.get(), s.get(), s.get());
			jumps.get(ptr2).add("Phase 4:\n");

			nodes.forEach(node -> {
				switch (node.getId() % 7) {
					case 0:
						jumps.get(ptr2).add(send(node, nodes.get(node.getId() + 3)));
						jumps.get(ptr2 + 1).add(send(node, nodes.get(node.getId() + 2)));
						jumps.get(ptr2 + 2).add(send(node, nodes.get(node.getId() + 1)));
						break;
					case 3:
						jumps.get(ptr2 + 1).add(send(node, nodes.get(node.getId() + 3)));
						break;
					case 6:
						jumps.get(ptr2 + 2).add(send(node, nodes.get(node.getId() - 2)));
						break;
					case 2:
						jumps.get(ptr2 + 2).add(send(node, nodes.get(node.getId() + 3)));
				}
			});
			jumps.forEach(set -> {
				set.stream().reduce((s1, s2) -> s1 + s2).ifPresent(System.out::println);
			});
		} else {
			System.out.println("Фаза 1:");
			System.out.println("(2 -> 0) (5 -> 3)\n" +
					" (4 -> 1) (6 -> 3)\n" +
					" (0 -> 3)\n" +
					" (1 -> 3)");
			System.out.println("Фаза 2 и 3 отсутствуют в виду отсутствия ранга = 1");
			System.out.println("Фаза 4: обратная последовательность 1й фазы");
		}


	}

	private static String send(Node from, Node to) {
		return " (" + from.getId() + " -> " + to.getId() + ")";
	}

	private static void printLinks() {
		nodes.forEach(node -> {
			System.out.print(node.getId() + ":\t");
			node.getLinks().forEach(n -> {
				System.out.print(n.getId() + ",\t");
			});
			System.out.println();
		});
	}

	private static Node[] getCluster() {
		Node[] nodes = new Node[7];
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = new Node();
		}
		nodes[0].addLink(nodes[1]);
		nodes[0].addLink(nodes[2]);
		nodes[0].addLink(nodes[3]);
		nodes[1].addLink(nodes[3]);
		nodes[1].addLink(nodes[4]);
		nodes[2].addLink(nodes[5]);
		nodes[3].addLink(nodes[5]);
		nodes[3].addLink(nodes[6]);
		nodes[4].addLink(nodes[6]);
		nodes[5].addLink(nodes[6]);
		return nodes;
	}

}
