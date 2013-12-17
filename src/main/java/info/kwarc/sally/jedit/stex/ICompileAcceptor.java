package info.kwarc.sally.jedit.stex;

public interface ICompileAcceptor {
	void error(String file, int startLine, int startCol, int endLine, int endCol,  String message);
	void compileMessage(String msg);
}
