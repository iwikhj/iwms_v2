package com.iwi.iwms.api.login.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwi.iwms.api.common.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "Social login test", description = "IWMS 소셜 로그인 관리")
@RequiredArgsConstructor
@Controller
@RequestMapping("/naver")
public class NaverController {
	
	@Operation(hidden = true)
    @GetMapping(value = "/login")
	public ModelAndView swagger(@Parameter(hidden = true) Locale locale) throws Exception {
        
		SecureRandom random = new SecureRandom();
		String state = new BigInteger(130, random).toString(32);

		StringBuffer url = new StringBuffer();
		url.append("https://nid.naver.com/oauth2.0/authorize");
		url.append("?client_id=FaYiuzskAMsfGAIyIXod");
		url.append("&response_type=code");
		url.append("&redirect_url=http://localhost/naver/token");
		url.append("&state=" + state);
		
		ModelAndView view = new ModelAndView("redirect:" + url);
		
		return view;
	}

	@Operation(hidden = true)
	@GetMapping(value = "/token")
	@ResponseBody
	public ResponseEntity<ApiResponse<Map<String, Object>>> naverToken(HttpServletRequest request
			, @RequestParam(value = "code", required = false) String code
			, @RequestParam(value = "state", required = false) String state) throws JsonMappingException, JsonProcessingException {
		
		log.info("code: {}, state: {}", code, state);
		
	    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	    params.add("grant_type","authorization_code");
	    params.add("client_id", "FaYiuzskAMsfGAIyIXod");
	    params.add("client_secret", "e35kt1bH3z");
	    params.add("code", code);
	    params.add("state", state);
		
	    HttpEntity<MultiValueMap<String, String>> naverTokenRequest = makeTokenRequest(params);

	    RestTemplate rt = new RestTemplate();
	    ResponseEntity<String> tokenResponse = rt.exchange(
	    		"https://nid.naver.com/oauth2.0/token",
	            HttpMethod.POST,
	            naverTokenRequest,
	            String.class
	    );
	    
	    log.info("tokenResponse: {}", tokenResponse.getBody());
    
	    ObjectMapper objectMapper = new ObjectMapper();
	    Map<String, Object> naverToken = objectMapper.readValue(tokenResponse.getBody(), HashMap.class);
	    log.info("naverToken: {}", naverToken);
		
		return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
				.data(naverToken)
				.build());
	}
	
	@Operation(hidden = true)
	@GetMapping(value = "/profile")
	@ResponseBody
	public ResponseEntity<ApiResponse<Map<String, Object>>> naverProfile(HttpServletRequest request
			, @RequestParam(value = "token", required = false) String token) throws JsonMappingException, JsonProcessingException {
	    
	    HttpEntity<MultiValueMap<String, String>> naverProfileRequest = makeProfileRequest(token);

	    RestTemplate rt = new RestTemplate();
	    ResponseEntity<String> profileResponse = rt.exchange(
	    		"https://openapi.naver.com/v1/nid/me",
			    HttpMethod.POST,
			    naverProfileRequest,
			    String.class
	    );

	    log.info("profileResponse: {}", profileResponse.getBody());
	    
	    ObjectMapper objectMapper = new ObjectMapper();
	    Map<String, Object> naverProfile = objectMapper.readValue(profileResponse.getBody(), HashMap.class);
		
		return ResponseEntity.ok(ApiResponse.<Map<String, Object>>builder()
				.data(naverProfile)
				.build());
	}

	private HttpEntity<MultiValueMap<String, String>> makeTokenRequest(MultiValueMap<String, String> params) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	    HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);
	    return naverTokenRequest;
	}
	
	private HttpEntity<MultiValueMap<String, String>> makeProfileRequest(String naverToken) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", "Bearer "+ naverToken);
	    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
	    HttpEntity<MultiValueMap<String, String>> naverProfileRequest = new HttpEntity<>(headers);
	    return naverProfileRequest;
	}
	
    public void ldap() {
        String id = "khj";
        String pw = "alskdi123";
        String domain = "iwi.co.kr";
        String server = "LDAP://iwi.co.kr:389";

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, server);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, id + "@" + domain);
        env.put(Context.SECURITY_CREDENTIALS, pw);

        LdapContext ctx;

 

        try {

            ctx = new InitialLdapContext(env, null);

 
            System.out.println("Connection Success!");

 

            String _filterName = "현종*";

            String returnedAtts[] = { "cn", "name", "sAMAccountName" };

           

            String searchBase = "dc=iwi";

            String searchFilter = "(&(objectClass=user)(givenname=" + _filterName + "))";

 

            // Create the search controls

            SearchControls searchCtls = new SearchControls();

            // 가져올 항목을 필터링 하려면 여기

            searchCtls.setReturningAttributes(returnedAtts);

            // Specify the search scope

            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

 

            NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);

            while (answer.hasMoreElements()) {

                SearchResult sr = (SearchResult) answer.next();
                log.info(">>>{}", sr);
                Attributes attrs = sr.getAttributes();

                Map amap = null;

                if (attrs != null) {

                    amap = new HashMap();

                    NamingEnumeration ne = attrs.getAll();

                    while (ne.hasMore()) {

                        Attribute attr = (Attribute) ne.next();

                        amap.put(attr.getID(), attr.get());

                    }

                    ne.close();

                }

               

                amap.forEach((k, v) -> {

                    System.out.format("key : %s, value : %s%n", k, v);

                });

            }

        } catch (NamingException e) {

            // TODO Auto-generated catch block

            System.out.println("Connection Fail!");

            e.printStackTrace();

        }

    }
}
