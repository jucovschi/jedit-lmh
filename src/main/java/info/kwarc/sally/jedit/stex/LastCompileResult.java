package info.kwarc.sally.jedit.stex;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class LastCompileResult extends Frame{

	private static final long serialVersionUID = 1L;

	String lastMsg;
	JEditorPane editorPane ;
		
	public void update() {
		lastMsg = lastMsg.replace("\n", "<br/>");
		lastMsg = lastMsg.replace("Error:", "<span style=\"color:red\"> Error:</span>");
		editorPane.setText(lastMsg);
	}
	
	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
		update();
	}
	
	public LastCompileResult() {
		super("Last sTeX Compilation Results");
		
		Panel cancelPanel = new Panel();
		Button cancel = new Button("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		lastMsg = "No messages";
		
		cancelPanel.add(cancel);
		add(BorderLayout.SOUTH, cancelPanel);

		setSize(new Dimension(500, 500));
		add(BorderLayout.NORTH, new JLabel("NNexus suggestion"));
		
		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		
		JScrollPane scrollPane = new JScrollPane(editorPane);      
		
		update();

		add(BorderLayout.CENTER, scrollPane);

		pack();
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
	}

	public boolean handleEvent(Event evt)
	{
		switch(evt.id)
		{
		case Event.WINDOW_DESTROY:
		{
			setVisible(false);
			return true;
		}
		default:
		}		
		return false;
	}

	public static void main(String[] args) {
		LastCompileResult sugg = new LastCompileResult();
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(Paths.get("/home/costea/kwarc/gits/fcad/source/design14/requirement.list.ltxlog"));
			sugg.setLastMsg(Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sugg.setVisible(true);
	}
}
