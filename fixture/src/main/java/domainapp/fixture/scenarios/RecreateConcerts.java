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

package domainapp.fixture.scenarios;

import com.google.common.collect.Lists;
import domainapp.dom.simple.Concert;
import domainapp.fixture.dom.simple.ConcertCreate;
import domainapp.fixture.dom.simple.ConcertsTearDown;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecreateConcerts extends FixtureScript {

    public final List<String> NAMES = Collections.unmodifiableList(Arrays.asList(
            "easter2017", "summer2017", "christmas2017"));

    public RecreateConcerts() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    //region > concerts (output)
    private final List<Concert> concerts = Lists.newArrayList();

    /**
     * The simpleobjects created by this fixture (output).
     */
    @Programmatic
    public List<Concert> getConcerts() {
        return concerts;
    }
    //endregion

    @Override
    protected void execute(final ExecutionContext ec) {

        // defaults
        final int number = defaultParam("number", ec, 3);

        // validate
        if(number < 0 || number > NAMES.size()) {
            throw new IllegalArgumentException(String.format("number must be in range [0,%d)", NAMES.size()));
        }

        //
        // execute
        //
        ec.executeChild(this, new ConcertsTearDown());

        for (int i = 0; i < number; i++) {
            final ConcertCreate fs = new ConcertCreate().setName(NAMES.get(i));
            ec.executeChild(this, fs.getName(), fs);
            concerts.add(fs.getConcert());
        }
    }
}
