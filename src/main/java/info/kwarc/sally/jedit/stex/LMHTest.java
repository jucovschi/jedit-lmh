package info.kwarc.sally.jedit.stex;

import static org.junit.Assert.*;

import org.junit.Test;

public class LMHTest {

	LMH lmh;
	
	public LMHTest() {
		lmh = new LMH("/a/b");
	}
	
	@Test
	public void lmt_test_root() {
		assertSame(LMH.PathType.ROOT, lmh.getPathProps("/a/b").type);
		assertSame(LMH.PathType.ROOT, lmh.getPathProps("/a/b/").type);
		assertSame(LMH.PathType.ROOT, lmh.getPathProps("/a//b/").type);
	}
	
	@Test
	public void lmt_test_group() {
		assertSame(LMH.PathType.GROUP, lmh.getPathProps("/a/b/c").type);
		assertSame(LMH.PathType.GROUP, lmh.getPathProps("/a/b/c/").type);
	}

	@Test
	public void lmt_test_repo() {
		assertSame(LMH.PathType.REPOSITORY, lmh.getPathProps("/a/b/c/d").type);
		assertSame(LMH.PathType.REPOSITORY, lmh.getPathProps("/a/b/c/d/").type);
		assertSame(LMH.PathType.REPOSITORY, lmh.getPathProps("/a/b/c/d//").type);
	}

	@Test
	public void lmt_test_src() {
		assertSame(LMH.PathType.SOURCE, lmh.getPathProps("/a/b/c/d/e").type);
		assertSame(LMH.PathType.SOURCE, lmh.getPathProps("/a/b/c/d/e/").type);
		assertSame(LMH.PathType.SOURCE, lmh.getPathProps("/a/b/c/d/e/f/g").type);
	}

	@Test
	public void lmt_test_invalid() {
		assertSame(LMH.PathType.INVALID, lmh.getPathProps("/c").type);
		assertSame(LMH.PathType.INVALID, lmh.getPathProps("/").type);
	}

}
