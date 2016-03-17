/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.common;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harun Al Rasyid
 */
public class ToolTest {
    
    public ToolTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isBlank method, of class Tool.
     */
    //@org.junit.Test
    public void testIsBlank() {
        System.out.println("isBlank");
        String string = "";
        boolean expResult = false;
        boolean result = Tool.isBlank(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isExists method, of class Tool.
     */
    //@org.junit.Test
    public void testIsExists() {
        System.out.println("isExists");
        String string = "";
        boolean expResult = false;
        boolean result = Tool.isExists(string);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of neo method, of class Tool.
     */
    //@org.junit.Test
    public void testNeo() {
        System.out.println("neo");
        Object expResult = null;
        Object result = Tool.neo(null);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMember method, of class Tool.
     */
    //@org.junit.Test
    public void testGetMember() {
        System.out.println("getMember");
        String memberName = "";
        MemberFinder expResult = null;
        MemberFinder result = Tool.getMember(memberName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parseArguments method, of class Tool.
     */
    //@org.junit.Test
    public void testParseArguments() {
        System.out.println("parseArguments");
        String text = "";
        String[] expResult = null;
        String[] result = Tool.parseArguments(text);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of idToNo method, of class Tool.
     */
    @org.junit.Test
    public void testIdToNo() {
        System.out.println("idToNo");
        String id = "HS001";
        long expResult = 5211562177336442880L;
        long result = Tool.idToNo(id);
        System.out.println(result);
        assertEquals(expResult, result);
    }

    /**
     * Test of noToId method, of class Tool.
     */
    @org.junit.Test
    public void testNoToId() {
        System.out.println("noToId");
        long no = 5211562177336442880L;
        String expResult = "HS001";
        String result = Tool.noToId(no);
        System.out.println(result);
        assertEquals(expResult, result);
    }
    
}
