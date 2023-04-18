package com.iwi.iwms;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.iwi.iwms.api.common.logging.LoggingAspect;
import com.iwi.iwms.utils.AES256Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class IwmsApplicationTests {

	@Test
	@DisplayName("간단 테스트")
	void test() throws Exception {
		String test = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJmN0pKd2g3aEZDTEt2OVhEaE5NOEFNTlZpRkRZTGZxVmdqUGpXMUN1bEhZIn0."
				+ "eyJleHAiOjE2ODE4MjM3NjEsImlhdCI6MTY4MTc4Nzc2MSwianRpIjoiNTFmYTIzZTktZjhlNC00ZDVhLWI4YzYtZDY1MDdjODU0Yzg2IiwiaXNzIjoiaHR0cDovL3JlZ2lzdHJ5Lml3aS5jby5rcjo4MTgwL3JlYWxtcy9tYXN0ZXIiLCJzdWIiOiI3YWZjYzlhOC02MzY0LTRhNGQtYjdjZi1hZDM2YWI2MmRhNTMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJpd21zIiwic2Vzc2lvbl9zdGF0ZSI6IjQwMjgzMmYyLWY2NzEtNGZkMy1hMzU0LTA3NmM4ZDRjMDc3YiIsImFjciI6IjEiLCJzY29wZSI6Iml3bXMiLCJzaWQiOiI0MDI4MzJmMi1mNjcxLTRmZDMtYTM1NC0wNzZjOGQ0YzA3N2IiLCJhdXRob3JpdGllcyI6WyJST0xFX0lXTVNfQURNSU4iXX0."
				+ "DL5AnEzh0xeHeo_XDo3J3wpC1RS8QLJ9Y1ivm_9XOgnyiXQmab2W_PSLDtvNzB6HYpxTEfYQwawX0EVDCp6LA0lpmSHJxJIofW-G9VMpQYZsyKF5t2osH9D77tk2ml_72nCzZOlJfO04n8ee53CYImMgiSQWochLHfgFfnX13RNyaXRnJP4dvZOneHgqWAVOqu1L6OaanjMfitbgXnMmUKlWhHhNEz3zxt-tg971-npJv8l1fvR2oWRqPsAGBGFYMlovYPbAf4KKPUJrF-dgLe-s6GACD0tsg17aIufZdI9KKz8o8XnKfAAZlo6E-FHrsFpeXzwSUfKSgmvpoqDEfg"; 
		
		String sub = "7afcc9a8-6364-4a4d-b7cf-ad36ab62da53".replaceAll("-", "");
		String encSub = AES256Util.encrypt(sub);
		System.out.println(encSub);
		System.out.println(encSub.length());
		String [] arr = test.substring(test.lastIndexOf(".") + 1, test.length()).split("");
		
		
		String a = IntStream.range(0, arr.length)
			.filter(i -> (i % 10) == 0)
	        .mapToObj(i -> arr[i])
	        .limit(32)
	        .collect(Collectors.joining());
		
		System.out.println(arr.length);
		System.out.println(a);
		System.out.println(a.length());    
		String enc = AES256Util.encrypt(a);
		System.out.println(enc);
		System.out.println(enc.length());
		
	}
	
	@Test
	@DisplayName("간단 테스트2")
	void test2() throws Exception {
		String [] antMatchers = {"/login", "/reissue", "/popup/login/**", "/files/**"};
		antMatchers = List.of(antMatchers).stream().map(v -> "/iwms/v1" + v).peek(System.out::println).toArray(String[]::new);
		System.out.println("========================================");
		List.of(antMatchers).stream().forEach(System.out::println);
	}
}