package com.c4soft.springaddons.tutorials;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;

import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenId;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.AddonsWebmvcTestConf;
import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ImportAutoConfiguration({ AddonsWebmvcTestConf.class })
class ResourceServerMultitenantDynamicApplicationTests {
	@Autowired
	MockMvcSupport api;

	@Test
	@OpenId(
			authorities = "AUTHOR",
			claims = @OpenIdClaims(usernameClaim = StandardClaimNames.PREFERRED_USERNAME, preferredUsername = "Tonton Pirate", email = "ch4mp@c4-soft.com"))
	void givenUserIsAuthenticated_whenGreet_thenOk() throws Exception {
		api.get("/greet").andExpect(status().isOk())
				.andExpect(jsonPath("$.body").value("Hi Tonton Pirate! You are granted with: [AUTHOR] and your email is ch4mp@c4-soft.com."));
	}

	@Test
	void givenRequestIsAnonymous_whenGreet_thenUnauthorized() throws Exception {
		api.get("/greet").andExpect(status().isUnauthorized());
	}

	@Test
	@OpenId(
			authorities = { "NICE", "AUTHOR" },
			claims = @OpenIdClaims(usernameClaim = StandardClaimNames.PREFERRED_USERNAME, preferredUsername = "Tonton Pirate", email = "ch4mp@c4-soft.com"))
	void givenUserIsGrantedWithNice_whenGetNice_thenOk() throws Exception {
		api.get("/nice").andExpect(status().isOk()).andExpect(jsonPath("$.body").value("Dear Tonton Pirate! You are granted with: [NICE, AUTHOR]."));
	}

	@Test
	@OpenId(authorities = { "AUTHOR" }, claims = @OpenIdClaims(preferredUsername = "Tonton Pirate"))
	void givenUserIsNotGrantedWithNice_whenGetNice_thenForbidden() throws Exception {
		api.get("/nice").andExpect(status().isForbidden());
	}

	@Test
	void givenRequestIsAnonymous_whenGetNice_thenUnauthorized() throws Exception {
		api.get("/nice").andExpect(status().isUnauthorized());
	}
}