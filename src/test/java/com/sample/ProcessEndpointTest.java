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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;

public class ProcessEndpointTest {

    private HttpPost httpPost;

	@Test()
    public void testProcessEndpoint() throws Exception {
        try {
            httpPost = new HttpPost( "http://localhost:8080/jbpmsample/rest/processes/sample-process");
            HttpClientBuilder.create().build().execute(httpPost); 
            
            Runnable r = new Runnable() {
                public void run() {
                    int counter = 0;
                    while (counter < 10) {
                        try {
                            HttpClientBuilder.create().build().execute(httpPost);
                        } catch (Exception e) {
                            e.printStackTrace();
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
    