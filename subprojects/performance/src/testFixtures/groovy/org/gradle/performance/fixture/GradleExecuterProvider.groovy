/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.performance.fixture

import org.gradle.integtests.fixtures.executer.GradleExecuter
import org.gradle.test.fixtures.file.TestDirectoryProvider

class GradleExecuterProvider {
    private final TestDirectoryProvider testDirectoryProvider

    GradleExecuterProvider(TestDirectoryProvider testDirectoryProvider) {
        this.testDirectoryProvider = testDirectoryProvider
    }

    GradleExecuter executer(GradleInvocationSpec buildSpec) {
        def executer = buildSpec.gradleDistribution.executer(testDirectoryProvider).
                requireGradleHome().
                requireIsolatedDaemons().
                withDeprecationChecksDisabled().
                withStackTraceChecksDisabled().
                withArgument('-u').
                inDirectory(buildSpec.workingDirectory).
                withTasks(buildSpec.tasksToRun)

        if (buildSpec.useDaemon) {
            executer.withGradleOpts("-Dorg.gradle.jvmargs=" + buildSpec.gradleOpts.join(" "))
        } else {
            executer.withGradleOpts(buildSpec.gradleOpts as String[])
        }

        buildSpec.args.each { executer.withArgument(it) }

        if (buildSpec.useDaemon) {
            executer.withArgument('--daemon')
        }
        executer
    }
}
