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
import domainapp.fixture.dom.simple.ConcertCreate;
import domainapp.fixture.dom.simple.ConcertsTearDown;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.isisaddons.module.security.dom.permission.ApplicationPermissionMode;
import org.isisaddons.module.security.dom.permission.ApplicationPermissionRule;
import org.isisaddons.module.security.dom.user.AccountType;
import org.isisaddons.module.security.seed.scripts.AbstractRoleAndPermissionsFixtureScript;
import org.isisaddons.module.security.seed.scripts.AbstractUserAndRolesFixtureScript;

public class DemoFixture extends FixtureScript {

    public DemoFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    @Override
    protected void execute(final ExecutionContext ec) {

        ec.executeChild(this, new ConcertsTearDown());

        ec.executeChild(this, new ConcertCreate() {{setName("easter2017");}});
        ec.executeChild(this, new ConcertCreate() {{setName("summer2017");}});
        ec.executeChild(this, new ConcertCreate() {{setName("christmas2017");}});

        ec.executeChild(this, new RoleAndPermissionsFixtureScript("easter2017", "Easter 2017 Concert"));
        ec.executeChild(this, new RoleAndPermissionsFixtureScript("summer2017", "Summer 2017 Concert"));
        ec.executeChild(this, new RoleAndPermissionsFixtureScript("christmas2017", "Christmas 2017 Concert"));

        ec.executeChild(this, new AbstractUserAndRolesFixtureScript(
                "bill", "pass", AccountType.LOCAL, Lists.newArrayList("easter2017","christmas2017","isis-module-security-regular-user")){});
        ec.executeChild(this, new AbstractUserAndRolesFixtureScript(
                "joe", "pass", AccountType.LOCAL, Lists.newArrayList("easter2017", "summer2017","isis-module-security-regular-user")){});
    }

    private static class RoleAndPermissionsFixtureScript extends AbstractRoleAndPermissionsFixtureScript {
        public RoleAndPermissionsFixtureScript(String roleName, String roleDescriptionIfAny) {
            super(roleName, roleDescriptionIfAny);
        }

        @Override
        protected void execute(ExecutionContext ec) {
            newPackagePermissions(ApplicationPermissionRule.ALLOW, ApplicationPermissionMode.CHANGING, "domainapp");
        }
    }
}
