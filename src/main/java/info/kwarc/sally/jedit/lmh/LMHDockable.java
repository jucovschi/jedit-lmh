package info.kwarc.sally.jedit.lmh;

import info.kwarc.sally.jedit.stex.LMH;
import info.kwarc.sally.jedit.stex.LMHFile;
import info.kwarc.sally.jedit.stex.STeXPlugin;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;

public class LMHDockable extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	View view;
	String positon;
	LMH lmh;
	JTextArea filter;

	private JTree tree;

	public LMHDockable(LMH lmh) {
		this.lmh = lmh;
		init();
	}

	public LMHDockable(View view, String positon) {
		this.view = view;
		STeXPlugin plugin = (STeXPlugin) jEdit.getPlugin(
				"info.kwarc.sally.jedit.stex.STeXPlugin", true);
		this.lmh = plugin.getLmh();
		init();
	}

	class LangNode extends DefaultMutableTreeNode  {
		private static final long serialVersionUID = 1L;
		LMHFile file;
		String lang;

		LangNode(LMHFile file, String lang) {
			super(lang);
			this.file = file;
			this.lang = lang;
		}

		public void onDblClicked() {
			if (view == null)
				return;
			jEdit.openFile(view, lmh.getLangFile(file, lang));
		}
	}


	class FileNode extends DefaultMutableTreeNode {
		private static final long serialVersionUID = 1L;
		LMHFile file;

		public FileNode(LMHFile file) {
			super(file.getName());
			this.file = file;
			for (String translations : file.getTranslations()) {
				add(new LangNode(file, translations));
			}
		}

		public void onDblClicked() {
			if (view == null)
				return;
			jEdit.openFile(view, lmh.getMainFile(file));
		}
	}

	class OnDemandNodes extends DefaultMutableTreeNode {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		boolean expanded;
		String path;
		String group;
		String repository;

		public OnDemandNodes(String name, String path, String group, String repository) {
			super(name);
			this.path = path;
			this.group = group;
			this.repository = repository;
			expanded = false;
		}

		public void expand() {
			if (expanded)
				return;
			List<String> dirs = lmh.getRepDirs(group, repository, path);
			Collections.sort(dirs);
			for (String dir: dirs) {
				add(new OnDemandNodes(dir, path + "/" + dir, group, repository));
			}

			List<LMHFile> files = lmh.getRepFiles(group, repository, path);
			Collections.sort(files);
			for (LMHFile file : files) {
				add(new FileNode(file));
			}
		}

		@Override
		public boolean isLeaf() {
			return false;
		}
	}

	void createRepositoryNodes(String group, DefaultMutableTreeNode root) {
		for (String repository : lmh.getRepositories(group)) {
			root.add(new OnDemandNodes(repository, "", group, repository));
		}
	}

	void createGroupNodes(DefaultMutableTreeNode root) {
		for (String groups : lmh.getGroups()) {
			DefaultMutableTreeNode groupNode = new DefaultMutableTreeNode(groups);
			createRepositoryNodes(groups, groupNode);
			root.add(groupNode);
		}
	}

	void init() {
		setLayout(new BorderLayout());
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"Local Math Repository");
		tree = new JTree(top);
		tree.addTreeWillExpandListener(new TreeWillExpandListener() {
			public void treeWillExpand(TreeExpansionEvent event)
					throws ExpandVetoException {
				TreePath path = event.getPath();
				if (path.getLastPathComponent() instanceof OnDemandNodes) {
					OnDemandNodes node = (OnDemandNodes) path
							.getLastPathComponent();
					node.expand();
				}				
			}

			public void treeWillCollapse(TreeExpansionEvent event)
					throws ExpandVetoException {
			}
		});

		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				if (e.getClickCount() != 2 || selPath == null)
					return;
				if (selPath.getLastPathComponent() instanceof FileNode) {
					FileNode node = (FileNode) selPath.getLastPathComponent();
					node.onDblClicked();
				}
				if (selPath.getLastPathComponent() instanceof LangNode) {
					LangNode node = (LangNode) selPath.getLastPathComponent();
					node.onDblClicked();
					
				}
			}
		});

		JScrollPane treeView = new JScrollPane(tree);
		createGroupNodes(top);
		add(BorderLayout.CENTER, treeView);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		LMH lmh = new LMH("/home/costea/kwarc/localmh");
		frame.add(BorderLayout.CENTER, new LMHDockable(lmh));
		frame.pack();
		frame.setVisible(true);
	}
}