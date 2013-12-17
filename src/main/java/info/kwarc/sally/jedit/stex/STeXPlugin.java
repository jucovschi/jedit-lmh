package info.kwarc.sally.jedit.stex;

import org.gjt.sp.jedit.EditPlugin;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.textarea.Selection;
import org.gjt.sp.jedit.textarea.TextArea;

import errorlist.DefaultErrorSource;
import errorlist.DefaultErrorSource.DefaultError;
import errorlist.ErrorSource;

public class STeXPlugin extends EditPlugin {
	String collection = "users";
	
	STeXCompiler compiler;
	NNexusService nnexusService;
	ErrorSource errors;
	NNexusSuggestions suggestions; 
	LastCompileResult lastCompileResult;
	LMH lmh;
	
	public LMH getLmh() {
		return lmh;
	}
	
	public STeXPlugin() {
		lmh = new LMH("/home/costea/kwarc/localmh");
		compiler = new STeXCompiler(lmh);
		
		errors = new DefaultErrorSource("STeX", jEdit.getFirstView());
		nnexusService  = new NNexusService("http://127.0.0.1:3000");
		suggestions = new NNexusSuggestions();
		lastCompileResult = new LastCompileResult();
		
		ErrorSource.registerErrorSource(errors);
	}
	
	public void showLastCompileResult() {
		lastCompileResult.setVisible(true);
	}
	
	public void addDockable(View view) {
		
	}
	
	public void compile(View view) {
		String file = view.getBuffer().getPath();
		compiler.compile(file, new ICompileAcceptor() {
			
			public void error(String file, int startLine, int startCol, int endLine,
					int endCol, String message) {
				errors.addError(new DefaultError(errors, ErrorSource.ERROR, file, startLine, startCol, endCol, message));
			}
			
			public void compileMessage(String msg) {
				lastCompileResult.setLastMsg(msg);
			}
		});
	}
	
	public void nnexus_show_suggestions(View view) {
		final TextArea buff = view.getTextArea();
		
		suggestions.setBuff(new IBuffer() {
			public void select(int offsetBegin, int length) {
				buff.setSelection(new Selection.Range(offsetBegin, length));
			}
			
			public void replace(int offsetBegin, int length, String newText) {
				select(offsetBegin, length);
				buff.replaceSelection(newText);
			}
		});
		suggestions.setLinks(nnexusService.getAnnotations(buff.getText()));
		suggestions.setVisible(true);
	}
	
	@Override
	public void start() {
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
	}
}