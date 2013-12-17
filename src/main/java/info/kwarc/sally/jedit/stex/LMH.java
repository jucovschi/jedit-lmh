package info.kwarc.sally.jedit.stex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.filefilter.PrefixFileFilter;

public class LMH {
	String root;
	String mathHubRoot;
	final String [] countryCodes = {"en", "de", "es"};
	Pattern intlFile;

	public enum PathType {INVALID, ROOT, GROUP, REPOSITORY, REP_ROOT, SOURCE};

	public LMH(String root) {
		this.root = new File(root).getAbsolutePath();
		this.mathHubRoot = new File(root+"/MathHub").getAbsolutePath();

		StringBuffer buffer = new StringBuffer();
		for (int i=0; i<countryCodes.length; ++i) {
			buffer.append("|"+countryCodes[i]);
		}
		intlFile = Pattern.compile("(.*)\\.("+buffer.toString()+")\\.tex");
	}

	public String getMainFile(LMHFile file) {
		return String.format("%s/%s/%s/source/%s/%s.tex", mathHubRoot, file.getGroup(), file.getRepository(), file.getPath(), file.getName());
	}
	
	public String getLangFile(LMHFile file, String lang) {
		return String.format("%s/%s/%s/source/%s/%s.%s.tex", mathHubRoot, file.getGroup(), file.getRepository(), file.getPath(), file.getName(), lang);
	}
	
	public boolean isDirectory(String dir) {
		File f = new File(resolveLMHPath(dir));
		return f.isDirectory();
	}

	public List<String> getGroups() {
		ArrayList<String> result = new ArrayList<String>();
		for (File f : getFiles(mathHubRoot)) {
			if (f.isDirectory()) {
				result.add(f.getName());
			}
		}
		return result;
	}

	public List<String> getRepositories(String group) {
		ArrayList<String> result = new ArrayList<String>();
		for (File f : getFiles(mathHubRoot+"/"+group)) {
			if (f.isDirectory()) {
				result.add(f.getName());
			}
		}
		return result;
	}

	public List<String> getRepDirs(String group, String repository, String path) {
		ArrayList<String> result = new ArrayList<String>();
		for (File f : getFiles(String.format("%s/%s/%s/source/%s", mathHubRoot, group, repository, path))) {
			if (f.isDirectory()) {
				result.add(f.getName());
			}
		}
		return result;
	}

	public List<LMHFile> getRepFiles(String group, String repository, String path) {
		ArrayList<LMHFile> result = new ArrayList<LMHFile>();
		HashMap<String, LMHFile> map= new HashMap<String, LMHFile>();

		for (File f : getFiles(String.format("%s/%s/%s/source/%s", mathHubRoot, group, repository, path))) {
			if (f.isFile() && f.getName().endsWith(".tex")) {
				Matcher m = intlFile.matcher(f.getName());
				String modName = null;
				String lang = null;
				if (m.find()) {
					modName = m.group(1);
					lang = m.group(2);
				} else {
					modName = f.getName().substring(0, f.getName().length()-4);
				}

				LMHFile file;
				if (!map.containsKey(modName)) {
					file = new LMHFile(group, repository, path, modName);
					result.add(file);
					map.put(modName, file);
				} else
					file = map.get(modName);
				if (lang != null) {
					file.addTranslation(m.group(2));
				}
			}
		}
		return result;
	}

	public File[] getFiles(String path) {
		File f = new File(path);
		String lastComponent = "";
		if (!f.isDirectory()) {
			lastComponent = f.getName();
			f = f.getParentFile();
		}
		File [] result = f.listFiles((FileFilter) new PrefixFileFilter(lastComponent));
		if (result == null)
			return new File[0];
		else
			return result;
	}

	public class PathProperties {
		PathType type;
		String [] components;

		public PathType getType() {
			return type;
		}

		public PathProperties(PathType type) {
			this.type = type;
		}

		public void setComponents(String[] components) {
			this.components = components;
		}

		public String[] getComponents() {
			return components;
		}
	}

	public PathProperties getPathProps(String dir) {
		PathType [] types = {PathType.ROOT, PathType.GROUP, PathType.REPOSITORY, PathType.REP_ROOT, PathType.SOURCE};

		String path = dir;

		if (!path.startsWith(mathHubRoot)) {
			return new PathProperties(PathType.INVALID);
		}
		if (path.equals(mathHubRoot)) {
			return new PathProperties(PathType.ROOT);			
		}

		String rest = path.substring(mathHubRoot.length()+1);
		String [] parts = rest.split(File.separator);
		int length = parts.length;
		if (dir.endsWith("/"))
			length++;

		PathProperties result = new PathProperties(types[Math.min(length, types.length - 1)]);
		result.setComponents(parts);
		return result;
	}

	public String resolveLMHPath(String path) {
		if (path.length() == 0)
			return mathHubRoot;
		return root+"/MathHub/"+path;
	}

	public String getFile(String path) {
		StringBuilder result = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(resolveLMHPath(path)));
			String line;
			while ((line = br.readLine()) != null) {
				result.append(line+"\n");
			}
			br.close();
		} catch (IOException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result.toString();
	}	

	public static void main(String[] args) {
		LMH lmh = new LMH("/home/costea/kwarc/localmh");
		lmh.getRepFiles("smglom", "smglom", "");
	}
}
