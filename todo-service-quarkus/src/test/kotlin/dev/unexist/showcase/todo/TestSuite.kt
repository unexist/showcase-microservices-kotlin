/**
 * @package Showcase-Microservices-Kotlin
 *
 * @file Test Suite
 * @copyright 2021-present Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/
 
package dev.unexist.showcase.todo;

import dev.unexist.showcase.todo.application.TodoResourceFixture
import dev.unexist.showcase.todo.domain.TodoCucumberFixture
import io.github.oshai.kotlinlogging.KotlinLogging
import io.quarkus.bootstrap.app.QuarkusBootstrap
import io.quarkus.bootstrap.app.RunningQuarkusApplication
import io.quarkus.bootstrap.model.PathsCollection
import io.quarkus.test.common.PathTestHelper
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.runners.Suite
import java.net.BindException
import java.nio.file.Path
import java.nio.file.Paths

@RunWith(Suite::class)
@Suite.SuiteClasses(
        TodoResourceFixture::class,
        TodoCucumberFixture::class
)
class TestSuite {
    companion object {
        private val logger = KotlinLogging.logger {}
        private lateinit var application: RunningQuarkusApplication

        @JvmStatic
        @BeforeClass
        fun setUp() {
            startApplication()
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            application.close()
        }

        private fun startApplication() {
            try {
                val rootBuilder: PathsCollection.Builder = PathsCollection.builder()

                var testClassLocation: Path = PathTestHelper.getTestClassesLocation(
                        TodoCucumberFixture::class.java)

                /* Load step definitions */
                rootBuilder.add(testClassLocation)

                /* Load application classes */
                val appClassLocation: Path = PathTestHelper.getAppClassLocationForTestLocation(
                        testClassLocation.toString())
                rootBuilder.add(appClassLocation)

                application = QuarkusBootstrap.builder()
                        .setIsolateDeployment(false)
                        .setMode(QuarkusBootstrap.Mode.TEST)
                        .setProjectRoot(Paths.get("").normalize().toAbsolutePath())
                        .setApplicationRoot(rootBuilder.build())
                        .build()
                        .bootstrap()
                        .createAugmentor()
                        .createInitialRuntimeApplication()
                        .run()
            } catch (e: BindException) {
                /* If Quarkus is already running - fine */
                logger.error(e) { "Address already in use - which is fine!" }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}
