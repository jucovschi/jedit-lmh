package info.kwarc.sally.jedit.stex;

import java.awt.TextField;

import org.gjt.sp.jedit.AbstractOptionPane;
import org.gjt.sp.jedit.jEdit;

public class LMHOptions extends AbstractOptionPane {

	final String prefix="info.kwarc.sally.jedit.lmh.";
	TextField lmhPath;
	
	public LMHOptions() {
		super("LMH Options");
		lmhPath = new TextField(jEdit.getProperty(prefix+"lmh_root"));
		addComponent("LMH Root", lmhPath);
	}

	@Override
	protected void _save() {
		super._save();
		jEdit.setProperty(prefix+"lmh_root", lmhPath.getText());
	}
}
