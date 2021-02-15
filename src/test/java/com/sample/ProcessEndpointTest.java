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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sample.service.StraightThroughService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;

public class ProcessEndpointTest {

    @Test()
    public void testProcessEndpoint() throws Exception {

    HttpResponse response = HttpClientBuilder.create().build().execute( new HttpGet( "http://localhost:8080/jbpm-test-1.0.0-SNAPSHOT/rest/process"));
    Assert.assertNotNull(response);

        Runnable r = new Runnable() {
                public void run() {
                    int counter = 0;
                    while (counter < 10) {
                        try {
							HttpClientBuilder.create().build().execute( new HttpGet( "http://localhost:8080/jbpm-test-1.0.0-SNAPSHOT/rest/process"));
						} catch (Exception e) {
							e.printStackTrace();
						} 
                        counter++;
                    }
                }

            };

            long start = System.currentTimeMillis();
            List<Thread> ts = new ArrayList<Thread>();
            for (int i = 0; i < 300; i++) { 
                Thread t = new Thread(r);
                t.start();
                ts.add(t);
            }
            for (Thread t: ts) {
                t.join();
            }

            System.out.println(" == Execution time, REST API:" + (System.currentTimeMillis() - start));
        }

}
    
