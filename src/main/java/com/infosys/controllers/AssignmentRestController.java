package com.infosys.controllers;

import com.infosys.exceptions.ServiceException;
import com.infosys.services.AssignmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@RestController
@EnableAutoConfiguration
@RequestMapping("/api")
@Api(value = "assigmentSerivices", description = "Assignment Controller")
public class AssignmentRestController
{

    @Autowired
    AssignmentService assignmentService;

    @GetMapping(value = "/fibonacci", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get fibonacci Response", response = ResponseEntity.class)
    public ResponseEntity<Long> getFibonacciSeries(@RequestParam(value = "n", required = true) Integer inputValue) throws ServiceException
    {
        Long resultValue = assignmentService.getFibonacciSeries(inputValue);
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body(resultValue);
    }

    @GetMapping(value = "/reverseWords", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Reverse Words Response", response = ResponseEntity.class)
    public ResponseEntity<String> getReverseWords(@RequestParam(value = "sentence", required = true) String reverseWords) throws ServiceException
    {
        String resultValue = assignmentService.getReverseWords(reverseWords);
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body(resultValue);
    }


    @GetMapping(value = "/triangleType", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get trianle Type Response", response = ResponseEntity.class)
    public ResponseEntity<String> getTriangleType(@RequestParam(value = "a", required = true) Integer a, @RequestParam(value = "b", required = true) Integer b, @RequestParam(value = "c", required = true) Integer c) throws ServiceException
    {
        String resultValue = assignmentService.getTriangleType(a, b, c);
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body(resultValue);
    }


    @PostMapping(value = "/makeoneArray", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Single Array Response", response = ResponseEntity.class)
    public ResponseEntity<LinkedHashMap<String, ArrayList>> getMakeOneArray(@RequestBody LinkedHashMap<String, ArrayList> linkedHashMapObjects) throws ServiceException
    {
        LinkedHashMap<String, ArrayList> resultValue = assignmentService.getMakeoneArray(linkedHashMapObjects.values());
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl.noCache())
                .body(resultValue);
    }

}