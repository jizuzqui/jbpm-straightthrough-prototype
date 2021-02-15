/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import com.sample.rest.ProcessEndpoint;
import com.sample.service.StraightThroughService;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;

//@RunWith(Arquillian.class)
public class ProcessEndpointTest {

    private HttpPost httpPost = new HttpPost( "http://localhost:8080/jbpm-prototype/rest/processes/sample-process");

    static File[] files = Maven.resolver().loadPomFromFile("pom.xml")
        .importRuntimeDependencies().resolve().withTransitivity().asFile();
        
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "jbpm-prototype.war")
            .addClasses(ProcessEndpoint.class, StraightThroughService.class, HttpUriRequest.class, HttpRequest.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml").addAsLibraries(files);
    }

	@Test()
    public void testProcessEndpoint() throws Exception {
        try {
            CloseableHttpResponse execute = HttpClientBuilder.create().build().execute(httpPost);
            assertEquals(200, execute.getStatusLine().getStatusCode());
        } catch (Exception e) {
            fail("Exception when trying HTTP Request: "+ e.getMessage());
        } 
    } 

	@Test()
    public void checkPerformanceProcessEndpoint() throws Exception {
        try {           
            Runnable r = new Runnable() {
                public void run() {
                    int counter = 0;
                    while (counter < 10) {
                        try {
                            CloseableHttpResponse execute = HttpClientBuilder.create().build().execute(httpPost);
                            assertEquals(200, execute.getStatusLine().getStatusCode());
                        } catch (Exception e) {
                            fail("Exception when running HTTP Request"+ e.getCause());
                        } 
                        counter++;
                    }
                }
            };

            long start = System.currentTimeMillis();
            List<Thread> ts = new ArrayList<Thread>();
            for (int i = 0; i < 200; i++) { 
                Thread t = new Thread(r);
                t.start();
                ts.add(t);
            }
            for (Thread t: ts) {
                t.join();
            }
            System.out.println(" == ");
            System.out.println(" == Execution time, REST API:" + (System.currentTimeMillis() - start));
            System.out.println(" == ");

        } catch (Exception e) {
            Assert.fail("Could not connect to localhost server");
        }

        
        
    }

}
