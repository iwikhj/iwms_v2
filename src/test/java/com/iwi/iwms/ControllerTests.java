package com.iwi.iwms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.iwi.iwms.api.common.resolver.LoginUserInfoArgumentResolver;
import com.iwi.iwms.api.comp.controller.CompController;
import com.iwi.iwms.api.comp.domain.CompInfo;
import com.iwi.iwms.api.comp.service.CompService;

@WebMvcTest(value = CompController.class)
public class ControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CompService compService;
	
	@MockBean
	private LoginUserInfoArgumentResolver loginUserInfoArgumentResolver;
	
	@Test
	void compTest() throws Exception {
		
		mockMvc.perform(get("/iwms/v1/companies")).andExpect(status().isOk());
		
		Map<String, Object> map = new HashMap<>();
		map.put("loginUserSeq", 1);
		
		List<CompInfo> compList = compService.listComp(map);
		
		assertThat(compList.size()).isEqualTo(100);
	}
}
