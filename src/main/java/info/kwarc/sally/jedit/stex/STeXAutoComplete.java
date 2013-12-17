package info.kwarc.sally.jedit.stex;

import info.kwarc.sally.jedit.stex.LMH.PathProperties;
import info.kwarc.sally.jedit.stex.LMH.PathType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class STeXAutoComplete {
	MMT mmt;
	LMH lmh;

	public STeXAutoComplete(MMT mmt, LMH lmh) {
		this.mmt = mmt;
		this.lmh = lmh;
	}

	Pattern mathHub = Pattern.compile("\\\\MathHub\\s*\\{([\\w/-]*)$");

	String computeSuggest(File f, String fullPath) {
		String q = f.toString().substring(fullPath.length()) + "/";
		if (q.startsWith("/")) {
			q = q.substring(1);
		}
		return q;
	}

	List<String> autocompletePath(String fullPath) {
		List<String> result = new ArrayList<String>();
		PathProperties pathProps = lmh.getPathProps(fullPath);

		if (pathProps.getType() == PathType.INVALID)
			return result;

		File [] autoFiles = lmh.getFiles(fullPath);

		if (pathProps.getType() == PathType.ROOT || pathProps.getType() == PathType.GROUP || pathProps.getType() == PathType.REPOSITORY) {
			for (File f : autoFiles) {
				if (f.isDirectory()) {
					result.add(computeSuggest(f, fullPath));
				}
			}
		}

		if (pathProps.getType() == PathType.REP_ROOT) {
			for (File f : autoFiles) {
				if (f.getName().equals("source")) {
					result.add(computeSuggest(f, fullPath));
				}
			}
		}

		if (pathProps.getType() == PathType.SOURCE) {
			for (File f : lmh.getFiles(fullPath)) {
				if (f.isDirectory() || f.getName().endsWith(".tex"))
					result.add(f.toString().substring(fullPath.length()));
			}
		}

		return result;		
	}
	
	List<String> mathHubAutocomplete(String line, int col) {
		List<String> result = new ArrayList<String>();
		line = line.substring(0, col);
		System.out.println(line);
		Matcher m = mathHub.matcher(line);
		if (!m.find())
			return result;

		String fullPath = lmh.resolveLMHPath(m.group(1));
		return autocompletePath(fullPath);
	}

	public List<String> autocomplete(String content, int row, int col) {
		String[] lines = content.split("\n");
		String line = lines[row];
		List<String> result = new ArrayList<String> ();
		result.addAll(mathHubAutocomplete(line, col));
		return result;
	}

	public List<String> autocompleteFile(String file, int row, int col) {
		String content = lmh.getFile(file);
		return autocomplete(content, row, col);
	}

	public static void printList(List<String> auto) {
		for (String s : auto) {
			System.out.println(s);
		}
	}

	public static void main(String[] args) {
		MMT mmt = new MMT("http://localhost:8181");
		LMH lmh = new LMH("/home/costea/kwarc/localmh");
		STeXAutoComplete auto = new STeXAutoComplete(mmt, lmh);
		printList(auto.autocompleteFile("KwarcMH/FormalCAD/source/design14/requirement.list.tex", 3, 29));
		printList(auto.autocompleteFile("KwarcMH/FormalCAD/source/design14/requirement.list.tex", 3, 31));
		printList(auto.autocompleteFile("KwarcMH/FormalCAD/source/design14/requirement.list.tex", 3, 40));
		printList(auto.autocompleteFile("KwarcMH/FormalCAD/source/design14/requirement.list.tex", 3, 44));
		printList(auto.autocompleteFile("KwarcMH/FormalCAD/source/design14/requirement.list.tex", 3, 45));
		printList(auto.autocompleteFile("KwarcMH/FormalCAD/source/design14/requirement.list.tex", 3, 70));	
	}
}
