/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.integtests.tests.modules.simple;

import domainapp.dom.simple.Concert;
import domainapp.dom.simple.ConcertRepository;
import domainapp.fixture.scenarios.DemoFixture;
import domainapp.integtests.tests.DomainAppIntegTest;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.sudo.SudoService;
import org.apache.isis.applib.services.xactn.TransactionService;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Concert_IntegTest extends DomainAppIntegTest {

    @Inject
    FixtureScripts fixtureScripts;
    @Inject
    TransactionService transactionService;
    @Inject
    SudoService sudoService;
    @Inject
    ConcertRepository concertRepository;

    Concert concert;

    @Before
    public void setUp() throws Exception {
        // given
        DemoFixture fs = new DemoFixture();
        fixtureScripts.runFixtureScript(fs, null);
        transactionService.nextTransaction();


    }

    @Test
    public void raw() throws Exception {

        // when
        final List<Concert> concerts = concertRepository.listAll();

        // then
        assertThat(concerts.size()).isEqualTo(3);
    }

    @Test
    public void user_with_access_to_subset() throws Exception {

        sudoService.sudo("joe", () -> {

            // when
            final List<Concert> concerts = wrap(concertRepository).listAll();

            // when
            assertThat(concerts.size()).isEqualTo(2);
        });
    }

    @Test
    public void unknown_user() throws Exception {

        sudoService.sudo("unknown", () -> {

            // when
            final List<Concert> concerts = wrap(concertRepository).listAll();

            // when
            assertThat(concerts.size()).isEqualTo(0);
        });

    }




}