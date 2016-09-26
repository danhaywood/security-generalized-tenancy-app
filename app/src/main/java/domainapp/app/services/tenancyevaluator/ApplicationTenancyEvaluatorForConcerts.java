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
package domainapp.app.services.tenancyevaluator;

import domainapp.dom.simple.Concert;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.isisaddons.module.security.dom.role.ApplicationRole;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyEvaluator;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import java.util.Objects;
import java.util.Optional;

@DomainService(nature = NatureOfService.DOMAIN)
public class ApplicationTenancyEvaluatorForConcerts implements ApplicationTenancyEvaluator {

    @Override
    public boolean handles(Class<?> cls) {
        return Concert.class.isAssignableFrom(cls);
    }

    @Override
    public String hides(Object domainObject, ApplicationUser applicationUser) {
        if (!(domainObject instanceof Concert)) {
            return null;
        }
        final Concert concert = (Concert) domainObject;

        final Optional<ApplicationRole> roleIfAny =
                applicationUser.getRoles()
                        .stream()
                        .filter(role -> Objects.equals(role.getName(), concert.getName()))
                        .findAny();

        return roleIfAny.isPresent()? null: "Requires role " + concert.getName();
    }

    @Override
    public String disables(Object domainObject, ApplicationUser applicationUser) {
        return null;
    }
}
