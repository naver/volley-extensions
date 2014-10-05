# Volley extensions : Requests

This library provides assistance in developing Android applications using [Volley](https://android.googlesource.com/platform/frameworks/volley/).

It provides various useful requests.

# Features and usage
## Request classes supporting JSON type of the response
### [JacksonRequest](https://github.com/naver/volley-extensions/blob/master/volley-requests/src/main/java/com/navercorp/volleyextensions/request/JacksonRequest.java)
- JSON parsing with [Jackson 1.x](http://jackson.codehaus.org/) library

		Request<Person> request = new JacksonRequest<Person>(url, Person.class, listener, errorListener);
		requestQueue.add(request);

### [Jackson2Request](https://github.com/naver/volley-extensions/blob/master/volley-requests/src/main/java/com/navercorp/volleyextensions/request/Jackson2Request.java)
- JSON parsing with [Jackson 2.x](http://wiki.fasterxml.com/JacksonHome) library

		Request<Person> request = new Jackson2Request<Person>(url, Person.class, listener, errorListener);
		requestQueue.add(request);

## Request classes supporting XML type of the response
### [SimpleXmlRequest](https://github.com/naver/volley-extensions/blob/master/volley-requests/src/main/java/com/navercorp/volleyextensions/request/SimpleXmlRequest.java)
- XML parsing with [Simple XML](http://simple.sourceforge.net/) library

		Request<Person> request = new SimpleXmlRequest<Person>(url, Person.class, listener, errorListener);
		requestQueue.add(request);

# Install

### How to setup for maven or gradle users
#### 1. Add the dependency below,
##### Maven
``` xml
<dependency>
	<groupId>com.navercorp.volleyextensions</groupId>
	<artifactId>volley-requests</artifactId>
	<version>2.0.0</version>
</dependency>
```

##### Gradle
```
dependencies {
	compile 'com.navercorp.volleyextensions:volley-requests:2.0.+'
}
```

#### 2. Add the appropriate dependency as per the classes you are using. 
For example, if you are using `JacksonRequest`, you need to add the dependency of [Jackson 1.x](http://jackson.codehaus.org/) library. See the following settings below.

_(NOTE : The dependencies below were set to be `optional` as default in pom.xml of the library. Because I assumed that each developer will use different parts of it.)_

##### Maven

- Jackson 1.x (when using JacksonRequest)
	
		<dependency>
			<groupId>org.codehaus.jackson</groupId>
			<artifactId>jackson-mapper-asl</artifactId>
			<version>1.9.12</version>
		</dependency>

- Jackson 2.x (when using Jackson2Request)
	
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.2.1</version>
		</dependency>

- Simple XML (when using SimpleXmlRequest)
	
		<dependency>
			<groupId>org.simpleframework</groupId>
			<artifactId>simple-xml</artifactId>
			<version>2.7</version>
			<exclusions>
				<!-- StAX is not available on Android -->
				<exclusion>
					<artifactId>stax</artifactId>
					<groupId>stax</groupId>
				</exclusion>
				<exclusion>
					<artifactId>stax-api</artifactId>
					<groupId>stax</groupId>
				</exclusion>
				<!-- Provided by Android -->
				<exclusion>
					<artifactId>xpp3</artifactId>
					<groupId>xpp3</groupId>
				</exclusion>
			</exclusions>
		</dependency>

##### Gradle

- Jackson 1.x (when using JacksonRequest)

		dependencies {
			compile 'org.codehaus.jackson:jackson-mapper-asl:1.9.+'
			}

- Jackson 2.x (when using Jackson2Request)

		dependencies {
			compile 'com.fasterxml.jackson.core:jackson-databind:2.2.+'
		}

- Simple XML (when using SimpleXmlRequest)

		dependencies {
			compile('org.simpleframework:simple-xml:2.7.+') {
				exclude module: 'stax'
				exclude module: 'stax-api'
				exclude module: 'xpp3'
			}
		}

### How to add jar directly
1. Download the jar package of volley-extensions.
	- [volley-requests-2.0.0.jar](http://github.com/naver/volley-extensions/wiki/downloads/2.0.0/volley-requests-2.0.0.jar)
2. Import the jar package into your project.
3. Download and import Jackson, Simple XML as needed.

# Sample Application
Sample application is being provided as APK file (the source is in the repository):
 * [Download Here!](http://github.com/naver/volley-extensions/wiki/downloads/2.0.0/sample-demos-2.0.0.apk)

# Volley mirror dependency
This library depends on [Volley mirror project](https://github.com/mcxiaoke/android-volley). The mirror project keeps an up-to-date version of [Volley](https://android.googlesource.com/platform/frameworks/volley/) and automatically uploads to Maven Central. 

The library will continue to keep this dependency until Volley provides an official package in Maven Central.

# License

	Copyright (C) 2014 Naver Corp.
 	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.