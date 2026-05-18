package com.karyam.operations.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.karyam.operations.constant.IResponseConstants;
import com.karyam.operations.dto.request.LoginRequest;
import com.karyam.operations.dto.request.RegisterRequest;
import com.karyam.operations.dto.response.LoginResponse;
import com.karyam.operations.filter.SecurityFilter;
import com.karyam.operations.service.IAuthService;
import com.karyam.operations.util.RestUtil;
import com.karyam.operations.util.TestDataUtil;

@WebMvcTest(value = AuthRestController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityFilter.class))
@AutoConfigureMockMvc(addFilters = false)
class AuthRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private IAuthService authService;

	@Test
	void testRegister() throws JsonProcessingException, Exception {
		RegisterRequest registerRequest = TestDataUtil.getRegisterRequest();

		when(authService.register(registerRequest)).thenReturn(IResponseConstants.REGISTRATION_SUCCESS);
		
		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(RestUtil.toJsonString(registerRequest)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.message").value(IResponseConstants.REGISTRATION_SUCCESS));
	}

	@Test
	void testLogin() throws JsonProcessingException, Exception {
		LoginRequest loginRequest = TestDataUtil.getLoginRequest();
		LoginResponse response = TestDataUtil.getLoginResponse();
		
		when(authService.login(loginRequest)).thenReturn(response);
		
		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(RestUtil.toJsonString(loginRequest)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content.email").value(response.getEmail()))
		.andExpect(jsonPath("$.content.token").value(response.getToken()))
		.andExpect(jsonPath("$.content.userId").value(response.getUserId()))
		.andExpect(jsonPath("$.content.role").value(response.getRole().toString()));
	}
}
