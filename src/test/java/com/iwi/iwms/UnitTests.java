package com.iwi.iwms;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwi.iwms.api.common.errors.ExceptionHandlers;
import com.iwi.iwms.api.common.resolver.LoginUserInfoArgumentResolver;
import com.iwi.iwms.api.comp.controller.CompController;
import com.iwi.iwms.api.comp.mapper.CompMapper;
import com.iwi.iwms.api.comp.service.CompService;
import com.iwi.iwms.config.redis.RedisProvider;
import com.iwi.iwms.utils.PropertiesUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UnitTests {
	
	@InjectMocks
	private CompController target;
	
    @Mock
    private CompService compService;
    
    @Mock
    private RedisProvider redisProvider;
    
    @Mock
    private ObjectMapper objectMapper;
    
    @MockBean
    private CompMapper compMapper;

    private MockMvc mockMvc;
	
    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(target)
        		.setCustomArgumentResolvers(new LoginUserInfoArgumentResolver(redisProvider, objectMapper))
        		.setControllerAdvice(new ExceptionHandlers())
        		.defaultRequest(null)
                .build();
    }
    
	@Test
	@DisplayName("simple test")
	public void propertiesUtilTest() throws Exception {
		//given
		
		//when
		
		//then
		
		
		
	}
	
	
}
