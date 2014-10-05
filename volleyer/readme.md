# What is Volleyer?

 - Volleyer is a tool for Volley. Volleyer makes Volley be a modern style client.
 - Volleyer makes you to use Volley more easier and concise.
 - Volleyer supports an additional features such as multipart feature.

__Detailed features can be found below sections!__

# Volleyer building flow
It's good to know how the Volleyer builds a request and execute it.
But it doesn't matter even if you skip this section and jump to usage section.

Volleyer is a sort of builder with method-chain style.
There are several steps for building a request.

![](https://raw.githubusercontent.com/wiki/naver/volley-extensions/volleyer-flow.png)

(1) Call `volleyer()`.
``` java
	volleyer(requestQueue)
```

(2) Choose a http method among GET, or POST, PUT, DELETE method.
``` java
	volleyer(requestQueue)
			.get(URL)
```

(3) Construct Request settings such as headers or body, multipart.
``` java
	volleyer(requestQueue)
			.get(URL)
            .addHeader("someHeader", "value")
            .withBody("body")
```

(4) Set a target class(which is usually is a model class.) that response is converted to.
``` java
	volleyer(requestQueue)
			.get(URL)
            .addHeader("someHeader", "value")
            .withBody("body")
            .withTargetClass(User.class)
```

(5) Construct Response settings such as parser or listener or an error listener.
``` java
	volleyer(requestQueue)
			.get(URL)
            .addHeader("someHeader", "value")
            .withBody("body")
            .withTargetClass(User.class)
            .withNetworkResponseParser(parser)
            .withListener(listener)
            .withErrorListener(errorListener)
```

(6) Call `execute()`.
``` java
	volleyer(requestQueue)
			.get(URL)
            .addHeader("someHeader", "value")
            .withBody("body")
            .withTargetClass(User.class)
            .withNetworkResponseParser(parser)
            .withListener(listener)
            .withErrorListener(errorListener)
            .execute();
```

Then the steps are completely done. If done, a request will get on flight to the server.

There seems to be so many steps for building it, but some of steps can be skipped.
Various scenarios of these description are explained at the next section.

# Learn usage by following examples!
## Basic example
``` java
	volleyer(requestQueue)
			.get("http://google.com")
			.execute();
```

This is the basic usage of volleyer. If you run this code, Volleyer makes a request for google.com and send it to requestQueue.Understandably, RequestQueue should be already started before running this code.

> * NOTE :
> When creating a RequestQueue, I recommend you to create it by using [DefaultRequestQueueFactory](https://github.com/naver/volley-extensions/blob/master/volleyer/src/main/java/com/navercorp/volleyextensions/volleyer/factory/DefaultRequestQueueFactory.java) like below.
>
>		public class MyApplication extends Application {
>			@Override
>		    public void onCreate() {
>    			super.onCreate();
>		        RequestQueue requestQueue = DefaultRequestQueueFactory.create(this);
>        		requestQueue.start();
>		        ...
>		    }
>		}

Does the compiler not find where `volleyer()` symbol is? You have to specify `import static` as below in the source where `Volleyer()` is being used.

		import static com.navercorp.volleyextensions.volleyer.Volleyer.*;

## Omitting RequestQueue
If you want requestQueue to be ommited in `volleyer(requestQueue)`, you should enter `settings()` and change some settings as below.

		volleyer(rq).settings().setAsDefault().done();


After this, you can use `volleyer()` without requestQueue.

		volleyer()
				.get(url)
		        .execute();

* NOTE : If you use `volleyer()` without this setting, Volleyer will throw an error.

## Adding a request header
If you want to add a request header, call `addHeader()` like below,

``` java
	volleyer()
			.get(url)
			.addHeader("User-Agent", "Lynx/2.8.7dev.9 libwww-FM/2.14")
			.execute();
```

## Adding body or multipart
POST method and PUT method are available for adding body or multipart.

Call `withBody()` if you want add body.
``` java
	volleyer()
			.post(url)
			.withBody("body is here.")
			.execute();
```

You can call `withStringPart()` for a string part, or call `withFilePart()` for a file part.

``` java
	volleyer()
			.post(url)
			.addStringPart("string part")
			.addFilePart(new File("./file.jpg"))
			.execute();
```

* NOTE : If any part is added, body which was added by calling `withBody()` is ignored.

### Important!
For supporting a multipart feature, [MultipartHttpStack](https://github.com/naver/volley-extensions/blob/master/volleyer/src/main/java/com/navercorp/volleyextensions/volleyer/multipart/stack/MultipartHttpStack.java) should be used.

If you create RequestQueue by using [DefaultRequestQueueFactory](https://github.com/naver/volley-extensions/blob/master/volleyer/src/main/java/com/navercorp/volleyextensions/volleyer/factory/DefaultRequestQueueFactory.java), the RequestQueue instance includes MultipartHttpStack. But if not, you have to wrap a HttpStack as below.

``` java
	HttpStack myHttpStack = (create your own HttpStack);

	// Wrap a multipart http stack.
	MultipartHttpStack multipartStack = new DefaultMultipartHttpStack();
	HttpStack completedStack = new MultipartHttpStackWrapper(myHttpStack, multipartStack);

	// Create a RequestQueue instance.
	RequestQueue requestQueue = new RequestQueue(diskCache , completedStack);
```

## Adding a listener and ErrorListener
You can use an existing `Request.Listener`. To use it, call `withListener()` as below.
``` java
	volleyer()
			.get(url)
			.withListener(new Listener<String>() {
				@Override
				public void onResponse(String response) {
					...
				}
			})
			.execute();
```

You can use `ErrorListener` class too, call `withErrorListener()` as below.

``` java
	volleyer()
			.get(url)
			.withListener(new Listener<String>() {
				@Override
				public void onResponse(String response) {
					...
				}
			})
			.withErrorListener(new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					...
				}
			})
			.execute();
```

## Adding a json parser, or a xml parser
If content of a response is a json type or xml, this can be converted to a model class.
Volleyer checks content type of a response, it automatically converts it if the type is xml or json.

For supporting this. You should import Jackson 1.x library or Jackson 2.x for json, or import Simple XML library for Xml(Check "install section" if you know how to import libraries.).

After import the library, Call `withTargetClass()` and specify a target class as a parameter.

But, a target class should be convertible for the library. For example, [ShoppingRssFeed](https://github.com/naver/volley-extensions/blob/master/samples/demos/src/main/java/com/navercorp/volleyextensions/sample/demos/amazon/model/ShoppingRssFeed.java) class is made for which is converted by Simple XML library.

``` java
	volleyer()
			.get(url)
			.withTargetClass(Person.class)
			.withListener(new Listener<Person>() {
				@Override
				public void onResponse(Person response) {
					...
				}
			})
			.execute();
```

What if response has not a specific content type? Volleyer must not find the proper parser. In this situtation, specify the parser directly by calling `withNetworkResponseParser()` as below. `NetworkResponseParser` is a class for converting response to a target class(Volleyer hides this class, so it can convert it automatically).

``` java
	volleyer()
			.get(url)
			.withTargetClass(Person.class)
			.withNetworkResponseParser(new JacksonNetworkResponseParser())
			.withListener(new Listener<Person>() {
				@Override
				public void onResponse(Person response) {
					...
				}
			})
			.execute();
```

There are various `NetworkResponseParser`s like below.

type| NetworkResponseParser
-|-
json|JacksonNetworkResponseParser (for jackson 1.x)
|Jackson2NetworkResponseParser (for jackson 2.x)
xml|SimpleXmlNetworkResponseParser

## Customizing the configuration
You can customize default settings for each RequestQueue of Volleyer.
Components that you can customize are listed below.

Class|Description
-----|-----------
RequestCreator| Request An interface for creating a Request instance.
RequestExecutor| Request An interface for executing a Request instance.
NetworkResponseParser| A parser as a default If a parser is not set by `withNetworkResponseParser()`.
ErrorListener| An error listener as a default If an error listener is not set by `withErrorListener()`.

All of components are interfaces and default implementations of those are in  [DefaultVolleyerConfigurationFactory](https://github.com/naver/volley-extensions/blob/master/volleyer/src/main/java/com/navercorp/volleyextensions/volleyer/factory/DefaultVolleyerConfigurationFactory.java).
If you want to implement new implementations, create a VolleyerConfiguration instance and insert implementations into the configuration like below.

``` java

VolleyerConfiguration configuration = new VolleyerConfiguration(requestCreator,
																requestExecutor,
                                                                networkResponseParser,
                                                                errorListener);
volleyer(requestQueue).settings()
					.setConfiguration(configuration);
					.done();
```

## Creating an IntegratedNetworkResponseParser
[IntegratedNetworkResponseParser](https://github.com/naver/volley-extensions/blob/master/volleyer/src/main/java/com/navercorp/volleyextensions/volleyer/response/parser/IntegratedNetworkResponseParser.java) has a special role for parsers. This class does not parse anything, but it can contain other parsers.
The class checks the content type of a response, and delegate the permission to parse the response to the proper parser from which it contains.

The usage is as below.
``` java
builder = new IntegratedNetworkResponseParser.Builder();
parser = builder
			.addParser(ContentType.CONTENT_TYPE_APPLICATION_JSON,
						new JacksonNetworkResponseParser())
			.addParser(ContentType.createContentType("image/jpeg"),
						new MyCustomNetworkResponseParser())
			.build();

```
After IntegratedNetworkResponseParser instance is created, this can be used as a paremeter for `withNetworkResponseParser()` or as a component for VolleyerConfiguration class.

# Install

### How to setup for maven or gradle users
#### 1. Add the dependency below,
##### Maven
``` xml
<dependency>
	<groupId>com.navercorp.volleyextensions</groupId>
	<artifactId>volleyer</artifactId>
	<version>2.0.0</version>
</dependency>
```

##### Gradle
```
dependencies {
	compile 'com.navercorp.volleyextensions:volleyer:2.0.+'
}
```

#### 2. Add the appropriate dependency as per the classes you are using. 
For example, if you are using a json parser(for example, JacksonNetworkResponseParser or Jackson2NetworkResponseParser), you need to add the dependency of [Jackson 1.x](http://jackson.codehaus.org/) library the dependency of or [Jackson 2.x](http://jackson.codehaus.org/) library. See the following settings below.

_(NOTE : The dependencies below were set to be `optional` as default in pom.xml of the library. Because I assumed that each developer will use different parts of it.)_

##### Maven

- Jackson 1.x (when using JacksonNetworkResponseParser)
		
		<dependency>
			<groupId>org.codehaus.jackson</groupId>
			<artifactId>jackson-mapper-asl</artifactId>
			<version>1.9.12</version>
		</dependency>

- Jackson 2.x (when using Jackson2NetworkResponseParser)
		
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.2.2</version>
		</dependency>

- Simple XML (when using SimpleXmlNetworkResponseParser)
		
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

- Jackson 1.x (when using JacksonNetworkResponseParser)

		dependencies {
			compile 'org.codehaus.jackson:jackson-mapper-asl:1.9.+'
			}

- Jackson 2.x (when using Jackson2NetworkResponseParser)

		dependencies {
			compile 'com.fasterxml.jackson.core:jackson-databind:2.2.+'
		}

- Simple XML (when using SimpleXmlNetworkResponseParser)

		dependencies {
			compile('org.simpleframework:simple-xml:2.7.+') {
				exclude module: 'stax'
				exclude module: 'stax-api'
				exclude module: 'xpp3'
			}
		}

### How to add jar directly
1. Download the jar package of volley-extensions.
	- [volleyer-2.0.0.jar](http://github.com/nhnopensource/volley-extensions/wiki/downloads/2.0.0/volleyer-2.0.0.jar)
2. Import the jar package into your project.
3. Download and import Jackson, Simple XML as needed.

# Sample Application
Sample application is being provided as APK file ([the source](https://github.com/naver/volley-extensions/blob/master/samples/sample-volleyer-twitter/) is in the repository):
 * [Download Here!](http://github.com/nhnopensource/volley-extensions/wiki/downloads/2.0.0/sample-volleyer-twitter-2.0.0.apk)

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