package net.yapbam.gui.info;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class MessageTest {
	@Test
	public void test() {
		Message info0 = new Message("0", "news", "content 0");
		assertFalse(info0.isRead());
		Message info1 = new Message("1", "warning", "content 1");
		Message info2 = new Message("2", "news", "content 2");
		
		Messages news = new Messages(Arrays.asList(new Message[]{info0, info1, info2}));
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
