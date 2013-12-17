package info.kwarc.sally.jedit.stex;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class NNexusSuggestions extends Frame{

	private static final long serialVersionUID = 1L;

	NNexusLinks[] links;
	int current;
	IBuffer  buff;

	JLabel category;
	JLabel concept;
	JLabel link;
	JLabel domain;
	
	Button next;
	Button prev;
	Button accept;
	
	void reset() {
		current = 0;
	}

	Panel createInfoPanel() {
		Panel infoPanel = new Panel();
		infoPanel.setLayout(new GridLayout(4, 2));
		

		infoPanel.add(new JLabel("Domain")); 
		domain = new JLabel();
		infoPanel.add(domain);
		
		infoPanel.add(new JLabel("Category ")); 
		category = new JLabel(); infoPanel.add(category);
		
		infoPanel.add(new JLabel("Concept ")); 
		concept = new JLabel();
		infoPanel.add(concept);

		infoPanel.add(new JLabel("Link ")); 
		link = new JLabel();
		infoPanel.add(link);
		return infoPanel;
	}
	
	Panel createActions() {
		Panel actionsPanel = new Panel();
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.PAGE_AXIS));

		next = new Button("Next");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				current++;
				updateLink();
			}
		});
		
		prev = new Button("Previous");
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				current--;
				updateLink();
			}
		});
		
		accept = new Button("Accept");
		accept.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				NNexusLinks lnk = links[current];
				buff.replace(lnk.getOffset_begin(), lnk.getOffset_end(), String.format("\\trefi{%s}", lnk.concept));
			}
		});
		
		actionsPanel.add(next);
		actionsPanel.add(prev);
		actionsPanel.add(accept);
		return actionsPanel;
	}

	public NNexusSuggestions() {
		super("NNexus suggestions");
		
		Panel cancelPanel = new Panel();
		Button cancel = new Button("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		cancelPanel.add(cancel);
		add(BorderLayout.SOUTH, cancelPanel);


		setSize(new Dimension(500, 500));
		add(BorderLayout.NORTH, new JLabel("NNexus suggestion"));

		Panel suggestionPanel = new Panel();
		suggestionPanel.setLayout(new BoxLayout(suggestionPanel, BoxLayout.LINE_AXIS));
		suggestionPanel.add(createInfoPanel());
		suggestionPanel.add(createActions());
		
		add(BorderLayout.CENTER, suggestionPanel);

		pack();
		reset();
		links = null;
	}

	public void setBuff(IBuffer buff) {
		this.buff = buff;
	}
	
	public void updateLink() {
		if (links.length == 0) {
			next.setEnabled(false);
			prev.setEnabled(false);
			accept.setEnabled(false);
			category.setText("No links available");
			return;
		}
		if (current == links.length - 1) {
			next.setEnabled(false);
		} else {
			next.setEnabled(true);
		}
		if (current == 0) {
			prev.setEnabled(false);
		} else {
			prev.setEnabled(true);
		}
		accept.setEnabled(true);
		NNexusLinks lnk = links[current];
		category.setText(lnk.getCategory());
		concept.setText(lnk.getConcept());
		link.setText(lnk.getLink());
		domain.setText(lnk.getDomain());
		
		if (buff != null)
			buff.select(lnk.getOffset_begin(), lnk.getOffset_end());
		pack();
	}

	public void setVisible(boolean visible) {
		super.setVisible(visible);
		updateLink();
	}

	public NNexusLinks[] getLinks() {
		return links;
	}

	public void setLinks(NNexusLinks[] links) {
		reset();
		this.links = links;
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
		NNexusLinks [] links = new NNexusService("http://127.0.0.1:3000").getAnnotations("Injective functions are fun! Also quadratic functions!");

		NNexusSuggestions sugg = new NNexusSuggestions();
		sugg.setBuff(new IBuffer() {

			public void select(int offsetBegin, int length) {
				System.out.println("Selecting from "+offsetBegin+" "+length);
			}
			
			public void replace(int offsetBegin, int length, String newText) {
				
			}
		});
		
		sugg.setLinks(links);
		sugg.setVisible(true);;
	}
}
