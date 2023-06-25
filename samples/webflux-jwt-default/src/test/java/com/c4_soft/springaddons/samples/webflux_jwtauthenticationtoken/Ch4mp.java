package com.c4_soft.springaddons.samples.webflux_jwtauthenticationtoken;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.c4_soft.springaddons.security.oauth2.test.annotations.ClasspathClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockJwtAuth;

@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@WithMockJwtAuth(
		authorities = { "ROLE_AUTHORIZED_PERSONNEL", "AUTHOR" },
		claims = @OpenIdClaims(usernameClaim = "$['https://c4-soft.com/user']['name']", jsonFile = @ClasspathClaims("ch4mp.json")))
public @interface Ch4mp {
}
