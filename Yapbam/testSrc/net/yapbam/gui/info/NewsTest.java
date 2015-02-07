package net.yapbam.gui.info;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class NewsTest {
	@Test
	public void test() {
		Info info0 = new Info("0", "content 0");
		assertFalse(info0.isRead());
		Info info1 = new Info("1", "content 1");
		Info info2 = new Info("2", "content 2");
		
		News news = new News(Arrays.asList(new Info[]{info0, info1, info2}));
		assertEquals(3, news.size());
		
		assertEquals(info0.getId(), news.get(0).getId());
		
		info1.markRead();
		assertTrue(info1.isRead());
		assertEquals(2, news.size());
		assertEquals(info0.getId(), news.get(0).getId());
		assertEquals(info2.getId(), news.get(1).getId());
		info0.markRead();
		assertEquals(1, news.size());
		assertEquals(info2.getId(), news.get(0).getId());
		
		news.setOnlyUnread(false);
		assertEquals(info0.getId(), news.get(0).getId());
		assertEquals(info1.getId(), news.get(1).getId());
		assertEquals(info2.getId(), news.get(2).getId());
	}
}
