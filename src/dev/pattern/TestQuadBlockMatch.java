/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package dev.pattern;

import static org.junit.Assert.*;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sdb.compiler.QuadBlock;
import com.hp.hpl.jena.sparql.core.Quad;
import com.hp.hpl.jena.sparql.core.Var;

import org.junit.Test;

public class TestQuadBlockMatch
{
    Node g  = Node.createURI("http://example/g") ;
    Node x1 = Node.createURI("http://example/x1") ;
    Node x2 = Node.createURI("http://example/x2") ;
    Node x3 = Node.createURI("http://example/x3") ;
    Node x4 = Node.createURI("http://example/x4") ;
    
    Var v1 = Var.alloc("v1") ;
    Var v2 = Var.alloc("v2") ;
    Var v3 = Var.alloc("v3") ;
    Var v4 = Var.alloc("v4") ;
    
    QuadBlock data = new QuadBlock() ;
    {
        data.add(new Quad(g, x1, x2, x3)) ;
        data.add(new Quad(g, x1, x2, x4)) ;
    }

    @Test
    public void match_1()
    {
        QuadBlock pattern = new QuadBlock() ;
        pattern.add(new Quad(g, x1, x2, x3)) ;
        assertTrue(QuadBlockMatch.match(pattern, data)) ;
    }

    @Test
    public void match_2()
    {
        QuadBlock pattern = new QuadBlock() ;
        pattern.add(new Quad(g, x1, x1, x1)) ;
        assertFalse(QuadBlockMatch.match(pattern, data)) ;
    }

}

/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */