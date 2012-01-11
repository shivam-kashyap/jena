/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** A servlet that dumps its request
 */

// Could be neater - much, much neater!
package org.openjena.fuseki.servlets;

import java.io.IOException ;
import java.io.PrintWriter ;
import java.util.List ;

import javax.servlet.http.HttpServlet ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;

import org.openjena.fuseki.Fuseki ;
import org.openjena.fuseki.server.DatasetRef ;
import org.openjena.fuseki.server.SPARQLServer ;

import com.hp.hpl.jena.Jena ;
import com.hp.hpl.jena.query.ARQ ;
import com.hp.hpl.jena.tdb.TDB ;

/** Control functions for a Fuskei server */

public class ServerServlet extends HttpServlet
{
    public ServerServlet()
    {

    }

    @Override
    public void init()
    {
        return ;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        try {
            
            //serverLog.info("Fuseki Server Config servlet") ;
            
            PrintWriter out = resp.getWriter() ;
            resp.setContentType("text/plain");
            SPARQLServer server = Fuseki.getServer() ;
            
            out.println("Software:") ;
            
            String fusekiVersion = Fuseki.VERSION ;
            if ( fusekiVersion.equals("${project.version}") )
                fusekiVersion = "(development)" ;
            
            out.printf("  %s %s\n", Fuseki.NAME, fusekiVersion) ;
            out.printf("  %s %s\n", TDB.NAME, TDB.VERSION) ;
            out.printf("  %s %s\n", ARQ.NAME, ARQ.VERSION) ;
            out.printf("  %s %s\n", Jena.NAME, Jena.VERSION) ;
            
            //out.printf("Port: %s\n", server.getServer().getConnectors()[0].getPort()) ;
            out.println() ;
            
            for ( DatasetRef dsRef : server.getDatasets() )
            {
                datasetRefDetails(out, dsRef) ;
                out.println() ;
            }
            
            
        } catch (IOException ex) {} 
            
    }
    
    private static void datasetRefDetails(PrintWriter out, DatasetRef dsRef)
    {
        if ( dsRef.name != null )
            out.println("Name = "+dsRef.name) ;
        else
            out.println("Name = <unset>") ;
        
        endpointDetail(out, "Query",  dsRef, dsRef.queryEP) ;
        endpointDetail(out, "Update", dsRef, dsRef.updateEP) ;
        endpointDetail(out, "Upload", dsRef, dsRef.uploadEP) ;
        endpointDetail(out, "Graphs(Read)", dsRef, dsRef.readGraphStoreEP) ;
        endpointDetail(out, "Graphs(RW)", dsRef, dsRef.readWriteGraphStoreEP) ;

        // dataset
    }
    
//    public String name                          = null ;
//    public List<String> queryEP                 = new ArrayList<String>() ;
//    public List<String> updateEP                = new ArrayList<String>() ;
//    public List<String> uploadEP                = new ArrayList<String>() ;
//    public List<String> readGraphStoreEP        = new ArrayList<String>() ;
//    public List<String> readWriteGraphStoreEP   = new ArrayList<String>() ;
//    public DatasetGraph dataset                 = null ;
    
//    @Override
//    public void doPost(HttpServletRequest req, HttpServletResponse resp)
//    {
//    }

    private static void endpointDetail(PrintWriter out, String label, DatasetRef dsRef , List<String> endpoints)
    {
        boolean first = true ;
        out.printf("   %-15s :: ", label) ;
        
        for ( String s : endpoints )
        {
            if ( ! first )
                out.print(" , ") ;
            first = false ;
            s= "/"+dsRef.name+"/"+s ;
            out.print(s) ;
        }
        out.println() ;
    }

    @Override
    public String getServletInfo()
    {
        return "Fuseki Control Servlet" ;
    }
}