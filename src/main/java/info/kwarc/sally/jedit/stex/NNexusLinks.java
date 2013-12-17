package info.kwarc.sally.jedit.stex;

public class NNexusLinks {
	String link;
	int offset_begin;
	int offset_end;

	String scheme;
	String firstword;
	String domain;
	String category;
	String concept;
	String [] multilinks;

	int objectid;
	int conceptid;
	
	public String getLink() {
		return link;
	}
	
	public String[] getMultilinks() {
		return multilinks;
	}
	
	public int getOffset_begin() {
		return offset_begin;
	}
	public int getOffset_end() {
		return offset_end;
	}
	public String getScheme() {
		return scheme;
	}
	public String getFirstword() {
		return firstword;
	}
	public String getDomain() {
		return domain;
	}
	public String getCategory() {
		return category;
	}
	public String getConcept() {
		return concept;
	}
	public int getObjectid() {
		return objectid;
	}
	public int getConceptid() {
		return conceptid;
	}
}
