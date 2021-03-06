package com.infosys;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infosys.exceptions.ServiceException;
import com.infosys.services.AssignmentService;
import com.infosys.util.TriangleTypes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for AssignmentTest App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssignmentTest
{
    @Mock
    AssignmentService assignmentMockService;

    @Autowired
    AssignmentService assignmentService;

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    @Test
    public void testMakeoneArray() throws Exception
    {
        LinkedHashMap<String, ArrayList> arrayList = mapper.readValue(new File("src/test/resources/makeoneArrayInput.json"),
                new TypeReference<LinkedHashMap<String, ArrayList>>()
                {
                });
        String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayList);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/makeoneArray").content(jsonInString)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        LinkedHashMap<String, ArrayList> arrayOutput = mapper.readValue(new File("src/test/resources/makeoneArrayOutput.json"),
                new TypeReference<LinkedHashMap<String, ArrayList>>()
                {
                });
        String jsonOutputString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayOutput);

        JSONAssert.assertEquals(jsonOutputString, response.getContentAsString(), JSONCompareMode.NON_EXTENSIBLE);
        Mockito.verify(assignmentMockService, Mockito.times(0)).getMakeOneArray(Mockito.anyList());
    }

    @Test
    public void testFibonacci() throws Exception
    {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/fibonacci?n=10")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        JSONAssert.assertEquals("55", response.getContentAsString(), JSONCompareMode.NON_EXTENSIBLE);
        Mockito.verify(assignmentMockService, Mockito.times(0)).getFibonacciSeries(Mockito.anyInt());
    }

    @Test
    public void testReverseWords() throws Exception
    {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/reverseWords?sentence=how are you")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals("woh era uoy", response.getContentAsString());
        Mockito.verify(assignmentMockService, Mockito.times(0)).getReverseWords(Mockito.anyString());
    }

    @Test
    public void testTriangleTypes() throws Exception
    {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/triangleType?a=1&b=3&c=3")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals("ISOSCELES", response.getContentAsString());
        Mockito.verify(assignmentMockService, Mockito.times(0)).getTriangleType(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
    }


    @Test
    public void testTriangleTypesBadRequest() throws Exception
    {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/triangleType?a=1&b=3")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andExpect(status().isBadRequest())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        Mockito.verify(assignmentMockService, Mockito.times(0)).getTriangleType(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testFibonacciService() throws Exception
    {
        Mockito.when(assignmentMockService.getFibonacciSeries(10)).thenReturn(new BigInteger("55"));
        BigInteger result = assignmentMockService.getFibonacciSeries(10);
        Assert.assertEquals(result.longValue(), 55L);
        Mockito.verify(assignmentMockService, Mockito.times(1)).getFibonacciSeries(Mockito.anyInt());
    }

    @Test
    public void testFibonacciBadService() throws Exception
    {
        try
        {
            assignmentService.getFibonacciSeries(null);
            Assert.fail("Expected ServiceException");
        }
        catch (ServiceException e)
        {
            Assert.assertNotNull(e.getMessage());
            Assert.assertEquals(e.getMessage(), "Invalid input null");
        }
    }


    @Test
    public void testReverseWordsService() throws Exception
    {
        Mockito.when(assignmentMockService.getReverseWords(Mockito.anyString())).thenReturn("oho");
        String result = assignmentMockService.getReverseWords("oho");
        Assert.assertEquals(result, "oho");
        Mockito.verify(assignmentMockService, Mockito.times(1)).getReverseWords(Mockito.anyString());
    }


    @Test
    public void testTriangleTypeService() throws Exception
    {
        Mockito.when(assignmentMockService.getTriangleType(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(TriangleTypes.EQUILATERAL.name());
        String result = assignmentMockService.getTriangleType(1, 1, 1);
        Assert.assertEquals(result, TriangleTypes.EQUILATERAL.name());
        Mockito.verify(assignmentMockService, Mockito.times(1)).getTriangleType(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());
    }


    @Test
    public void testMakeoneArrayService() throws Exception
    {

        LinkedHashMap<String, ArrayList> arrayList = mapper.readValue(new File("src/test/resources/makeoneArrayOutput.json"),
                new TypeReference<LinkedHashMap<String, ArrayList>>()
                {
                });
        Mockito.when(assignmentMockService.getMakeOneArray(Mockito.anyCollection())).thenReturn(arrayList);
        Map<String, ArrayList> linkedHashMapObjects = new LinkedHashMap<String, ArrayList>();
        Map<String, ArrayList> result = assignmentMockService.getMakeOneArray(linkedHashMapObjects.values());
        Assert.assertEquals(result, arrayList);
        Mockito.verify(assignmentMockService, Mockito.times(1)).getMakeOneArray(Mockito.anyCollection());
    }

    @Test
    public void testTriangleTypeActualService() throws Exception
    {
        String result = assignmentService.getTriangleType(1, 1, 1);
        Assert.assertEquals(result, TriangleTypes.EQUILATERAL.name());

        String result1 = assignmentService.getTriangleType(1, 34, 1);
        Assert.assertEquals(result1, TriangleTypes.INVALID.name());

        String result2 = assignmentService.getTriangleType(10, 12, 34);
        Assert.assertEquals(result2, TriangleTypes.INVALID.name());
    }

    @Test
    public void testTriangleTypeInvalidService() throws Exception
    {

        String result2 = assignmentService.getTriangleType(8, 0, 1);
        Assert.assertEquals(result2, TriangleTypes.INVALID.name());

        String result3 = assignmentService.getTriangleType(8, 30, 1);
        Assert.assertEquals(result3, TriangleTypes.INVALID.name());

    }

    @Test
    public void testFibonacciActualService() throws Exception
    {
        BigInteger result = assignmentService.getFibonacciSeries(10);
        Assert.assertEquals(result, new BigInteger("55"));

        BigInteger result1 = assignmentService.getFibonacciSeries(1);
        Assert.assertEquals(result1, new BigInteger("1"));

        BigInteger result2 = assignmentService.getFibonacciSeries(2);
        Assert.assertEquals(result2, new BigInteger("1"));
    }

    @Test
    public void testReverseWordsActualService() throws Exception
    {
        String result = assignmentService.getReverseWords("hi");
        Assert.assertEquals(result, "ih");
    }

    @Test
    public void testReverseWordBadRequest() throws Exception
    {
        String value = null;
        try
        {
            assignmentService.getReverseWords(value);
            Assert.fail("Expected ServiceException");
        }
        catch (ServiceException e)
        {
            Assert.assertNotNull(e.getMessage());
            Assert.assertEquals("Input cannot be null", e.getMessage());
        }

        value = "who  wq@%%##qw";
        try
        {
            String result = assignmentService.getReverseWords(value);
            Assert.assertEquals(result, "ohw  wq##%%@qw");
        }
        catch (ServiceException e)
        {
            Assert.assertNotNull(e.getMessage());
            Assert.assertEquals("Input cannot be null", e.getMessage());
        }
    }


    @Test
    public void testReverseWordsBadReq() throws Exception
    {
        try
        {
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/reverseWords?sentence1=how are you")
                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }
        catch (Exception e)
        {
            Assert.fail(e.toString());
        }
    }


    @Test
    public void testFibonacciBadReq() throws Exception
    {
        try
        {
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/fibonacci?n=q")
                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON);
            MvcResult result = mockMvc.perform(requestBuilder).andReturn();
            MockHttpServletResponse response = result.getResponse();
            Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        }
        catch (Exception e)
        {
            Assert.fail(e.toString());
        }
    }
}