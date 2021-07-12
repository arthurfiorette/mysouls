/*
 * Copyright 2007-present the original author or authors. Licensed under the
 * Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Properties;

public class MavenWrapperDownloader {

  private static final String WRAPPER_VERSION = "0.5.6";
  /**
   * Default URL to download the maven-wrapper.jar from, if no 'downloadUrl' is
   * provided.
   */
  private static final String DEFAULT_DOWNLOAD_URL = "https://repo.maven.apache.org/maven2/io/takari/maven-wrapper/"
      + MavenWrapperDownloader.WRAPPER_VERSION + "/maven-wrapper-"
      + MavenWrapperDownloader.WRAPPER_VERSION + ".jar";

  /**
   * Path to the maven-wrapper.properties file, which might contain a
   * downloadUrl property to use instead of the default one.
   */
  private static final String MAVEN_WRAPPER_PROPERTIES_PATH = ".mvn/wrapper/maven-wrapper.properties";

  /**
   * Path where the maven-wrapper.jar will be saved to.
   */
  private static final String MAVEN_WRAPPER_JAR_PATH = ".mvn/wrapper/maven-wrapper.jar";

  /**
   * Name of the property which should be used to override the default download
   * url for the wrapper.
   */
  private static final String PROPERTY_NAME_WRAPPER_URL = "wrapperUrl";

  public static void main(final String args[]) {
    System.out.println("- Downloader started");
    final File baseDirectory = new File(args[0]);
    System.out.println("- Using base directory: " + baseDirectory.getAbsolutePath());

    // If the maven-wrapper.properties exists, read it and check if it contains
    // a custom
    // wrapperUrl parameter.
    final File mavenWrapperPropertyFile = new File(baseDirectory,
        MavenWrapperDownloader.MAVEN_WRAPPER_PROPERTIES_PATH);
    String url = MavenWrapperDownloader.DEFAULT_DOWNLOAD_URL;
    if (mavenWrapperPropertyFile.exists()) {
      FileInputStream mavenWrapperPropertyFileInputStream = null;
      try {
        mavenWrapperPropertyFileInputStream = new FileInputStream(mavenWrapperPropertyFile);
        final Properties mavenWrapperProperties = new Properties();
        mavenWrapperProperties.load(mavenWrapperPropertyFileInputStream);
        url = mavenWrapperProperties.getProperty(MavenWrapperDownloader.PROPERTY_NAME_WRAPPER_URL,
            url);
      } catch (final IOException e) {
        System.out.println(
            "- ERROR loading '" + MavenWrapperDownloader.MAVEN_WRAPPER_PROPERTIES_PATH + "'");
      } finally {
        try {
          if (mavenWrapperPropertyFileInputStream != null) {
            mavenWrapperPropertyFileInputStream.close();
          }
        } catch (final IOException e) {
          // Ignore ...
        }
      }
    }
    System.out.println("- Downloading from: " + url);

    final File outputFile = new File(baseDirectory.getAbsolutePath(),
        MavenWrapperDownloader.MAVEN_WRAPPER_JAR_PATH);
    if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs()) {
      System.out.println("- ERROR creating output directory '"
          + outputFile.getParentFile().getAbsolutePath() + "'");
    }
    System.out.println("- Downloading to: " + outputFile.getAbsolutePath());
    try {
      MavenWrapperDownloader.downloadFileFromURL(url, outputFile);
      System.out.println("Done");
      System.exit(0);
    } catch (final Throwable e) {
      System.out.println("- Error downloading");
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void downloadFileFromURL(final String urlString, final File destination)
      throws Exception {
    if ((System.getenv("MVNW_USERNAME") != null) && (System.getenv("MVNW_PASSWORD") != null)) {
      final String username = System.getenv("MVNW_USERNAME");
      final char[] password = System.getenv("MVNW_PASSWORD").toCharArray();
      Authenticator.setDefault(new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      });
    }
    final URL website = new URL(urlString);
    ReadableByteChannel rbc;
    rbc = Channels.newChannel(website.openStream());
    final FileOutputStream fos = new FileOutputStream(destination);
    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    fos.close();
    rbc.close();
  }
}
