package net.yapbam.JTree.krylov;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.*;
import java.awt.dnd.DropTargetContext;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class TreeDndSubTtree extends JFrame implements DropTargetListener {
	static Vector vLeft = new Vector();
	static Vector v1 = new Vector();
	static Vector vRight = new Vector();
	static Map<String, Vector> mapLeft = new HashMap<String, Vector>();
	static Map<String, Vector> mapRight = new HashMap<String, Vector>();
	static Map<String, Vector> submap = new HashMap<String, Vector>();
	static DefaultTreeModel tmLeft;
	static DefaultTreeModel tmRight;
	static String selected = "";
	static String selectedRight = "";
	DropTarget dt;
	JTree jtLeft = new JTree(tmLeft);
	JTree jtRight = new JTree(tmRight);
	JTree targetTree;
	JTree startTree;
	TreeNode targetNode;
	TreeNode startNode;
	static String dragged = "";
	TreeNode parentDraggednode;

	public TreeDndSubTtree() {
		super("Drop Test");
		setSize(500, 500);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		jtLeft.setDragEnabled(true);
		jtRight.setDragEnabled(true);
		expandAll(jtLeft);
		expandAll(jtRight);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				jtLeft, jtRight);
		splitPane.setBounds(0, 0, 500, 400);
		int loc = splitPane.getDividerLocation();
		loc = (int) ((splitPane.getBounds().getWidth() - splitPane
				.getDividerSize()) / 2);
		splitPane.setDividerLocation(loc);
		double propLoc = .5D;
		splitPane.setDividerLocation(propLoc);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		dt = new DropTarget(jtLeft, this);
		dt = new DropTarget(jtRight, this);
		setVisible(true);
	}

	public void dragEnter(DropTargetDragEvent dtde) {
		System.out.println("Drag Enter");
	}

	public void dragExit(DropTargetEvent dte) {
		System.out.println("Drag Exit");
	}

	private TreeNode getNodeForEvent(DropTargetDragEvent dtde) {
		Point p = dtde.getLocation();
		DropTargetContext dtc = dtde.getDropTargetContext();
		JTree tree = (JTree) dtc.getComponent();
		targetTree = tree;
		TreePath path = tree.getClosestPathForLocation(p.x, p.y);
		return (TreeNode) path.getLastPathComponent();
	}

	public void dragOver(DropTargetDragEvent dtde) {
		System.out.println("Drag Over");

		TreeNode node = getNodeForEvent(dtde);
		System.out.println("targetTree " + targetTree.getModel().getRoot());
		System.out.println("node " + node);
		targetNode = node;
		String sel = "";
		if (jtLeft.getLastSelectedPathComponent() != null) {
			sel = jtLeft.getLastSelectedPathComponent().toString();
			selected = sel;
		}
		if (jtRight.getLastSelectedPathComponent() != null) {
			sel = jtRight.getLastSelectedPathComponent().toString();
			selected = sel;
		}
		if (jtLeft.getLastSelectedPathComponent() != null) {
			startTree = jtLeft;
			sel = jtLeft.getLastSelectedPathComponent().toString();
			dragged = sel;
			TreeNode drgnode = (TreeNode) jtLeft.getLastSelectedPathComponent();
			parentDraggednode = drgnode.getParent();
			startNode = drgnode;
			String parentDragged = parentDraggednode.toString();
			System.out.println(parentDragged);
		}
		if (jtRight.getLastSelectedPathComponent() != null) {
			startTree = jtRight;
			sel = jtRight.getLastSelectedPathComponent().toString();
			dragged = sel;
			TreeNode drgnode = (TreeNode) jtRight
					.getLastSelectedPathComponent();
			parentDraggednode = drgnode.getParent();
			startNode = drgnode;
			String parentDragged = parentDraggednode.toString();
			System.out.println(parentDragged);
		}
		System.out.println("sel " + sel);
	}

	public void dropActionChanged(DropTargetDragEvent dtde) {
		System.out.println("Drop Action Changed");
	}

	public void drop(DropTargetDropEvent dtde) {
		try {
			if (targetTree.getModel().getRoot().toString().equals(
					startTree.getModel().getRoot().toString())) {
				dropSubTreeToItself(targetTree);
			} else {
				if (startTree.getModel().getRoot().toString().equals("a")) {
					dropSubTreeToAnotherTree(jtLeft, jtRight);
				}
				if (startTree.getModel().getRoot().toString().equals("rt3")) {
					dropSubTreeToAnotherTree(jtRight, jtLeft);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			dtde.rejectDrop();
		}
	}

	public void dropSubTreeToItself(JTree jtLeft) {
		String root = "";
		Map<String, Vector> map1 = new HashMap<String, Vector>();
		DefaultTreeModel tm1 = null;
		if (jtLeft.getModel().getRoot().toString().equals("a")) {
			map1 = mapLeft;
			tm1 = tmLeft;
			root = "a";
		}
		if (jtLeft.getModel().getRoot().toString().equals("rt3")) {
			map1 = mapRight;
			tm1 = tmRight;
			root = "rt3";
		}
		String parentDragged = parentDraggednode.toString();
		Vector vpath = new Vector();
		TreeNode[] path = tm1.getPathToRoot(targetNode);
		for (int i = 0; i < path.length; i++) {
			vpath.add(path[i].toString());
		}
		Set keys = new HashSet();
		keys = submap.keySet();
		keys.retainAll(vpath);
		submap(map1, dragged, map1.get(dragged));
		if (!vpath.contains(startNode.toString()) && (keys.size() <= 1)) {

			if (map1.get(targetNode.toString()) == null) {
				Vector v2 = new Vector();
				v2.add(dragged);
				map1.get(parentDragged).remove(dragged);
				submap.put(targetNode.toString(), v2);
				map1.putAll(submap);
				tm1 = createTreeModel(tm1, map1, root);
				jtLeft.setModel(tm1);
				expandAll(jtLeft);
			} else {
				Vector v2 = new Vector();
				v2 = map1.get(targetNode.toString());
				v2.add(dragged);
				map1.get(parentDragged).remove(dragged);
				submap.put(targetNode.toString(), v2);
				map1.putAll(submap);
				tm1 = createTreeModel(tm1, map1, root);
				jtLeft.setModel(tm1);
				expandAll(jtLeft);
			}
		}
		jtLeft.clearSelection();
	}

	public void dropSubTreeToAnotherTree(JTree jtDonor, JTree jtRecipient) {
		Map<String, Vector> map1 = new HashMap<String, Vector>();
		Map<String, Vector> map2 = new HashMap<String, Vector>();
		DefaultTreeModel tm = (DefaultTreeModel) jtRecipient.getModel();
		String rootName = jtRecipient.getModel().getRoot().toString();
		if (jtDonor == jtLeft) {
			map1 = mapLeft;
			map2 = mapRight;
		}
		if (jtDonor == jtRight) {
			map2 = mapLeft;
			map1 = mapRight;
		}
		String parentDragged = parentDraggednode.toString();

		submap(map1, dragged, map1.get(dragged));
		if (map2.get(targetNode.toString()) == null) {
			Vector v2 = new Vector();
			v2.add(dragged);
			Vector vpath = new Vector();
			TreeNode[] path = tm.getPathToRoot(targetNode);
			for (int i = 0; i < path.length; i++) {
				vpath.add(path[i].toString());
			}
			Set keys = new HashSet();
			// keys=submap.keySet();
			keys.addAll(submap.keySet());
			keys.retainAll(vpath);
			if (!vpath.contains(startNode.toString()) && (keys.size() <= 1)) {
				submap.put(targetNode.toString(), v2);
				map2.putAll(submap);
				tm = createTreeModel(tm, map2, rootName);

				jtRecipient.setModel(tm);
				expandAll(jtRecipient);
			}
			jtDonor.clearSelection();
		} else {
			Vector v2 = new Vector();
			v2 = map2.get(targetNode.toString());
			v2.add(dragged);
			Vector vpath = new Vector();
			TreeNode[] path = tm.getPathToRoot(targetNode);
			for (int i = 0; i < path.length; i++) {
				System.out.println("path " + path[i]);
				vpath.add(path[i].toString());
				System.out.println("startNode " + startNode);
			}
			Set keys = new HashSet();
			keys.addAll(submap.keySet());
			keys.retainAll(vpath);
			if (!vpath.contains(startNode.toString()) && (keys.size() <= 1)) {
				submap.put(targetNode.toString(), v2);
				map2.putAll(submap);
				tm = createTreeModel(tm, map2, rootName);

				jtRecipient.setModel(tm);
				expandAll(jtRecipient);
			}
			jtDonor.clearSelection();
		}
	}

	static DefaultTreeModel createTreeModel(DefaultTreeModel tm, Map<String, Vector> map, String rootnode) {
		Vector vv = new Vector();
		DefaultMutableTreeNode root3 = new DefaultMutableTreeNode(rootnode);
		// DefaultTreeModel
		tm = new DefaultTreeModel(root3);
		vv = map.get(rootnode);
		addNewNodes(tm, map, root3, vv);
		return tm;
	}

	public void expandAll(JTree tree) {
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
	}

	static void addNewNodes(DefaultTreeModel tm, Map<String, Vector> map,
			DefaultMutableTreeNode currNode, Vector vec) {
		DefaultMutableTreeNode node0 = null;
		DefaultMutableTreeNode node1 = null;
		System.out.println("currNode " + currNode);
		for (int i = 0; i < vec.size(); i++) {
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(vec
					.get(i));
			tm.insertNodeInto(childNode, currNode, currNode.getChildCount());
			if (map.get(childNode.toString()) != null) {
				v1 = map.get(childNode.toString());
				addNewNodes(tm, map, childNode, v1);
			}
		}
	}

	static void submap(Map<String, Vector> map, String key, Vector vLeft) {
		if (map.containsKey(key)) {
			submap.put(key, vLeft);
			for (int i = 0; i < vLeft.size(); i++) {
				String key1 = vLeft.get(i).toString();
				// submap.put(key1);
				System.out.println("!!! " + key1);
				submap(map, key1, map.get(key1));
			}
		}
	}

	public static void main(String args[]) {
		vLeft = new Vector();
		vLeft.add("b");
		vLeft.add("c");
		mapLeft.put("a", vLeft);
		vLeft = new Vector();
		vLeft.add("b1");
		vLeft.add("b2");
		mapLeft.put("b", vLeft);
		vLeft = new Vector();
		vLeft.add("c1");
		vLeft.add("c2");
		vLeft.add("c3");
		mapLeft.put("c", vLeft);
		vLeft = new Vector();
		vLeft.add("c11");
		vLeft.add("c12");
		vLeft.add("c13");
		vLeft.add("c14");
		mapLeft.put("c1", vLeft);
		//
		vLeft = new Vector();
		vLeft.add("c21");
		vLeft.add("c22");
		vLeft.add("c23");
		mapLeft.put("c2", vLeft);
		//
		vLeft = new Vector();
		vLeft.add("c221");
		vLeft.add("c222");
		vLeft.add("c223");
		mapLeft.put("c22", vLeft);
		// JTree tree;
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("a");
		tmLeft = new DefaultTreeModel(root);
		vLeft = mapLeft.get("a");
		addNewNodes(tmLeft, mapLeft, root, vLeft);
		DefaultMutableTreeNode node0 = new DefaultMutableTreeNode(vLeft.get(0));
		v1 = mapLeft.get(node0.toString());
		System.out.println("v1 " + v1);
		addNewNodes(tmLeft, mapLeft, node0, v1);
		// jTree1.setModel(tmLeft);
		vRight = new Vector();
		vRight.add("rb");
		vRight.add("rc");
		mapRight.put("rt3", vRight);
		vRight = new Vector();
		vRight.add("rb1");
		vRight.add("rb2");
		mapRight.put("rb", vRight);
		vRight = new Vector();
		vRight.add("rc1");
		vRight.add("rc2");
		vRight.add("rc3");
		mapRight.put("rc", vRight);

		DefaultMutableTreeNode root3 = new DefaultMutableTreeNode("rt3");
		tmRight = new DefaultTreeModel(root3);
		vRight = mapRight.get("rt3");
		addNewNodes(tmRight, mapRight, root3, vRight);
		new TreeDndSubTtree();
	}
}
