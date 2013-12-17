package info.kwarc.sally.jedit.stex;

public class AutocompleteRequest {
	String file;
	String content;
	int line;
	int col;

	public int getCol() {
		return col;
	}
	public String getContent() {
		return content;
	}
	public String getFile() {
		return file;
	}
	public int getLine() {
		return line;
	}
	
	public void setCol(int column) {
		this.col = column;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	public void setLine(int line) {
		this.line = line;
	}
}
