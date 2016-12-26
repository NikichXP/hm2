package luck;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Node {
	private Set<Node> links;
	private int id;
	private static int genId = 0;

	public Node() {
		id = genId++;
		links = new HashSet<>();
	}

	public void addLink(Node node) {
		if (this.links.contains(node)) {
			return;
		}
		links.add(node);
		node.addLink(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Node)) return false;

		Node node = (Node) o;

		if (id != node.id) return false;
		return links != null ? links.equals(node.links) : node.links == null;

	}

	@Override
	public int hashCode() {
		return id;
	}
}
