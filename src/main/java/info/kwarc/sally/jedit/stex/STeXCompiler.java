package info.kwarc.sally.jedit.stex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

public class STeXCompiler {
	Pattern errRegex = Pattern.compile("([^#]+)#textrange\\(from=(\\d+);(\\d+),to=(\\d+);(\\d+)\\)");
	LMH lmh;
	
	public STeXCompiler(LMH lmh) {
		this.lmh = lmh;
	}
	
	class ThreadCompiler implements Runnable {
		String filePath;
		ICompileAcceptor acceptor;

		public ThreadCompiler(String filePath, ICompileAcceptor acceptor) {
			this.filePath = filePath;
			this.acceptor  = acceptor;
		}

		public void run() {
			File f = new File(filePath);

			try {
				String name = FilenameUtils.removeExtension(f.getName());

				ProcessBuilder pb = new ProcessBuilder(lmh.root+"/bin/lmh", "gen", "--omdoc", name+".omdoc");

				pb.directory(f.getParentFile());

				Process p = pb.start();
				p.waitFor();
				
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = null;
				while ( (line = br.readLine()) != null) {
					System.out.println(line);
				}
				
				StringBuilder compileResult = new StringBuilder();
				br = new BufferedReader(new FileReader(f.getParentFile().getPath()+"/"+name+".ltxlog"));
				while ((line = br.readLine()) != null) {
					compileResult.append(line+"\n");
					if (!line.contains("Error")) {
						continue;
					}
					String pos = br.readLine();
					Matcher m =errRegex.matcher(pos);
					if (m.matches()) {
						acceptor.error(m.group(1).trim(), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)), line);
					}
				}
				acceptor.compileMessage(compileResult.toString());
				br.close();
			} catch (Exception e) {
				acceptor.compileMessage(e.getMessage());
				e.printStackTrace();
			}
		}
		
	}

	public void compile(String filePath, ICompileAcceptor acceptor) {
		new Thread(new ThreadCompiler(filePath, acceptor)).start();
	}

	public static void main(String[] args) {
		LMH lmh = new LMH("/home/costea/kwarc/localmh");
		new STeXCompiler(lmh).compile("/home/costea/kwarc/localmh/MathHub/smglom/smglom/source/aa-loop.en.tex", new ICompileAcceptor() {
			public void compileMessage(String msg) {
				System.out.println(msg);
			}			
			public void error(String file, int startLine, int startCol, int endLine,
					int endCol, String message) {
				System.out.println(String.format("%s:(%d:%d)-(%d:%d) - %s", file, startLine, startCol, endLine, endCol, message));
			}
		});
	}
}
