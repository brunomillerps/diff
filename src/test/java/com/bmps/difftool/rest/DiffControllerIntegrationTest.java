package com.bmps.difftool.rest;

import com.bmps.difftool.AbstractSpringIntegrationTest;
import com.bmps.difftool.domain.ComparatorType;
import com.bmps.difftool.domain.DiffObject;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DiffControllerIntegrationTest extends AbstractSpringIntegrationTest {

    private static final String API_V1_DIFF_LEFT = "/api/v1/diff/{id}/left";
    private static final String API_V1_DIFF_RIGHT = "/api/v1/diff/{id}/right";
    private static final String API_V1_DIFF = "/api/v1/diff/{id}";

    @Test
    public void persistLeftContentTest() throws Exception {
        //GIVEN
        String id = UUID.randomUUID().toString();
        String base64Encoded = "ewogICJtZW51IjogewogICAgImlkIjogImZpbGUiLAogICAgInZhbHVlIjogIkZpbGUiLAogICAgInBvcHVwIjogewogICAgICAibWVudWl0ZW0iOiBbCiAgICAgICAgewogICAgICAgICAgInZhbHVlIjogIk5ldyIsCiAgICAgICAgICAib25jbGljayI6ICJDcmVhdGVOZXdEb2MoKSIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICJ2YWx1ZSI6ICJPcGVuIiwKICAgICAgICAgICJvbmNsaWNrIjogIk9wZW5Eb2MoKSIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICJ2YWx1ZSI6ICJDbG9zZSIsCiAgICAgICAgICAib25jbGljayI6ICJDbG9zZURvYygpIgogICAgICAgIH0KICAgICAgXQogICAgfQogIH0KfQ==";

        //WHEN
        int statusCode = mockMvc.perform(post(API_V1_DIFF_LEFT, id)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(base64Encoded.getBytes()))
                .andDo(print()).andReturn().getResponse().getStatus();

        //THEN
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
        DiffObject diffObject = mongoTemplate.findById(id, DiffObject.class);
        Assert.assertNotNull(diffObject);
        Assert.assertNotNull(diffObject.getLeft());
        Assert.assertNull(diffObject.getRight());
        Assert.assertEquals(base64Encoded, new String(diffObject.getLeft()));
    }

    @Test
    public void persistRightContentTest() throws Exception {
        //GIVEN
        String id = UUID.randomUUID().toString();
        String base64Encoded = "ewogICJtZW51IjogewogICAgImlkIjogImZpbGUiLAogICAgInZhbHVlIjogIkZpbGUiLAogICAgInBvcHVwIjogewogICAgICAibWVudWl0ZW0iOiBbCiAgICAgICAgewogICAgICAgICAgInZhbHVlIjogIk5ldyIsCiAgICAgICAgICAib25jbGljayI6ICJDcmVhdGVOZXdEb2MoKSIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICJ2YWx1ZSI6ICJPcGVuIiwKICAgICAgICAgICJvbmNsaWNrIjogIk9wZW5Eb2MoKSIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICJ2YWx1ZSI6ICJDbG9zZSIsCiAgICAgICAgICAib25jbGljayI6ICJDbG9zZURvYygpIgogICAgICAgIH0KICAgICAgXQogICAgfQogIH0KfQ==";

        //WHEN
        int statusCode = mockMvc.perform(post(API_V1_DIFF_RIGHT, id)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(base64Encoded.getBytes()))
                .andDo(print())
                .andReturn().getResponse().getStatus();

        //THEN
        Assert.assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
        DiffObject diffObject = mongoTemplate.findById(id, DiffObject.class);
        Assert.assertNotNull(diffObject);
        Assert.assertNotNull(diffObject.getRight());
        Assert.assertNull(diffObject.getLeft());
        Assert.assertEquals(base64Encoded, new String(diffObject.getRight()));
    }

    @Test
    public void compareEqualObjectsTest() throws Exception {
        //GIVEN
        String id = UUID.randomUUID().toString();
        String base64Encoded = "ewogICJtZW51IjogewogICAgImlkIjogImZpbGUiLAogICAgInZhbHVlIjogIkZpbGUiLAogICAgInBvcHVwIjogewogICAgICAibWVudWl0ZW0iOiBbCiAgICAgICAgewogICAgICAgICAgInZhbHVlIjogIk5ldyIsCiAgICAgICAgICAib25jbGljayI6ICJDcmVhdGVOZXdEb2MoKSIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICJ2YWx1ZSI6ICJPcGVuIiwKICAgICAgICAgICJvbmNsaWNrIjogIk9wZW5Eb2MoKSIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICJ2YWx1ZSI6ICJDbG9zZSIsCiAgICAgICAgICAib25jbGljayI6ICJDbG9zZURvYygpIgogICAgICAgIH0KICAgICAgXQogICAgfQogIH0KfQ==";

        //WHEN
        // persist the left side
        mockMvc.perform(post(API_V1_DIFF_LEFT, id)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(base64Encoded.getBytes()))
                .andDo(print())
                .andExpect(status().isNoContent());

        // persist the right side
        mockMvc.perform(post(API_V1_DIFF_RIGHT, id)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(base64Encoded.getBytes()))
                .andDo(print())
                .andExpect(status().isNoContent());

        // call diff
        mockMvc.perform(post(API_V1_DIFF, id)
                .param("diffType", ComparatorType.MYER.name()))
                .andDo(print())

                //THEN
                .andExpect(status().isOk())
                .andExpect(content().string(is("{\"data\":{\"menu\":{\"id\":\"file\",\"value\":\"File\",\"popup\":{\"menuitem\":[{\"value\":\"New\",\"onclick\":\"CreateNewDoc()\"},{\"value\":\"Open\",\"onclick\":\"OpenDoc()\"},{\"value\":\"Close\",\"onclick\":\"CloseDoc()\"}]}}},\"matched\":true}")));
    }

    @Test
    public void compareSlightDifferenceObjectsTest() throws Exception {
        //GIVEN
        String id = UUID.randomUUID().toString();

        Path jsonBase64PathSource = Paths.get(getClass().getClassLoader()
                .getResource("menu-left.json").toURI());

        Path jsonBase64PathTarget = Paths.get(getClass().getClassLoader()
                .getResource("menu-right.json").toURI());

        byte[] encodedSource = Files.readAllBytes(jsonBase64PathSource);
        byte[] encodedTarget = Files.readAllBytes(jsonBase64PathTarget);
        Assert.assertNotNull(encodedSource);
        Assert.assertNotNull(encodedTarget);

        //WHEN
        // persist the left side
        mockMvc.perform(post(API_V1_DIFF_LEFT, id)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(Base64.getMimeEncoder().encode(encodedSource)))
                .andDo(print())
                .andExpect(status().isNoContent());

        // AND persist the right side
        mockMvc.perform(post(API_V1_DIFF_RIGHT, id)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .content(Base64.getMimeEncoder().encode(encodedTarget)))
                .andDo(print())
                .andExpect(status().isNoContent());

        // AND call diff
        MvcResult mvcResult =
                mockMvc.perform(post(API_V1_DIFF, id)
                        .param("diffType", ComparatorType.MYER.name()))
                        .andDo(print())
                        //THEN
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andReturn();

        InputStream isExpectedJson = getClass().getClassLoader().getResource("different-json-response.json").openStream();
        String expectedJson = IOUtils.toString(isExpectedJson, Charset.forName("UTF-8"));

        //THEN
        JSONAssert.assertEquals(expectedJson, mvcResult.getResponse().getContentAsString(), JSONCompareMode.STRICT);
    }
}


