package info.kwarc.sally.jedit.stex;

import java.util.ArrayList;
import java.util.List;

public class LMHFile implements Comparable<LMHFile>{
	enum LMHType {Module, Configuration, Target};

	LMHType fileType;
	String name;
	String group;
	String path;
	String repository;
	
	List<String> translations;
	
	public LMHFile(String group, String repository, String path, String name) {
		this.name = name;
		this.group = group;
		this.repository = repository;
		this.path = path;
		
		translations = new ArrayList<String>();
	}
	
	public void addTranslation(String code) {
		translations.add(code);
	}
	
	public List<String> getTranslations() {
		return translations;
	}

	public String getName() {
		return name;
	}
	
	public String getGroup() {
		return group;
	}
	
	public String getRepository() {
		return repository;
	}
	
	public String getPath() {
		return path;
	}
	
	
	public int compareTo(LMHFile o) {
		return name.compareTo(o.getName());
	}
}
