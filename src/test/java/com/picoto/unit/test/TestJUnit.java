package com.picoto.unit.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.picoto.test.domain.TestDomain;

@ExtendWith(MockitoExtension.class)
public class TestJUnit {

	@Mock
	TestDomain testMock;

	@Test
	public void testQuery() {
		assertNotNull(testMock);
		when(testMock.getId()).thenReturn(1);
		assertEquals(testMock.getId(), 1);
	}
}
