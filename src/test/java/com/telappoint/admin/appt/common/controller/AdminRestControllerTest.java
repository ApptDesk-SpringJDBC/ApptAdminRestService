package com.telappoint.admin.appt.common.controller;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SuppressWarnings("ALL")
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
//@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class)
@ContextConfiguration(locations = {
        "classpath*:applicationContextTest.xml",
        "classpath*:DispatcherServlet-servletTest.xml" })
public class AdminRestControllerTest {

    public static final String CLIENT_CODE = "LOUIMGOV";
    private MockMvc mockMvc;
    private MockHttpSession httpSession;

/*
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new MyController())
                .alwaysExpect(forwardedUrl(null))
                .build();
    }*/

    @Configuration
    @EnableWebMvc
//    @Controller
    @ComponentScan(basePackages = {"org.springframework.jdbc","org.springframework.jdbc","org.springframework.jdbc.core",
            "com.telappoint.admin","com.telappoint.admin.appt", "com.telappoint.admin.appt.common","com.telappoint.admin.appt.common.controller"
    ,"com.telappoint.admin.appt.common.component","com.telappoint.admin.appt.common.dao","com.telappoint.admin.appt.common.dao.impl"})
    static class ContextConfiguration {

    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    protected WebApplicationContext wac;

    static {
//        JavaAgent.initialize();
    }

    public AdminRestControllerTest() {
    }

    @Before
    public void setup() {
//        this.mockMvc = webAppContextSetup(this.wac).build();
        mockMvc = MockMvcBuilders.standaloneSetup(new AdminRestController())
                .alwaysExpect(forwardedUrl(null))
                .build();
        this.httpSession = new MockHttpSession();
    }

    @Test
    public void getAdminHomePage() throws Exception {
//        homePageRequest

//        Map<String, String> homePageRequest = new HashMap<String, String>();
//        homePageRequest.put("clientCode", "CAANWALB");
//        homePageRequest.put("loginUserId", "1");
//        HomePageRequest homePageRequest = new HomePageRequest();
 //       homePageRequest.setClientCode("LOUIMGOV");
 //       homePageRequest.setLoginUserId("1");
//        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//        ObjectOutputStream out = new ObjectOutputStream(byteOut);
//        out.writeObject(homePageRequest);
        Gson gson = new Gson();
        String json = "";//gson.toJson(homePageRequest);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
//                .post("/getAdminHomePage")
                .post("/service/getAdminHomePage")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json.getBytes())
                .session(httpSession);

        testAssertion(request);
    }

    public void testAssertion(MockHttpServletRequestBuilder request) throws Exception {
        MvcResult result = mockMvc.perform(request).andReturn();
        Assert.assertSame(null, result.getResponse().getErrorMessage());
        Assert.assertEquals(200, result.getResponse().getStatus());
        assertFalse(null == result.getModelAndView().getModel());
    }

}