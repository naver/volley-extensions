# Introduction to Volley Extensions

This library provides assistance in developing Android applications using [Volley](https://android.googlesource.com/platform/frameworks/volley/).

It provides useful classes such as various requests, disk caches, memory caches, and custom views.

# Features and usage
## Request classes supporting JSON type of the response
### [JacksonRequest](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/request/JacksonRequest.java)
- JSON parsing with [Jackson 1.x](http://jackson.codehaus.org/) library

		Request<Person> request = new JacksonRequest<Person>(url, Person.class, listener, errorListener);
		requestQueue.add(request);

### [Jackson2Request](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/request/Jackson2Request.java)
- JSON parsing with [Jackson 2.x](http://wiki.fasterxml.com/JacksonHome) library

		Request<Person> request = new Jackson2Request<Person>(url, Person.class, listener, errorListener);
		requestQueue.add(request);

## Request classes supporting XML type of the response
### [SimpleXmlRequest](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/request/SimpleXmlRequest.java)
- XML parsing with [Simple XML](http://simple.sourceforge.net/) library

		Request<Person> request = new SimpleXmlRequest<Person>(url, Person.class, listener, errorListener);
		requestQueue.add(request);
		
## Disk caches

### [Improved DiskBasedCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/disk/DiskBasedCache.java)
Improved `DiskBasedCache` is a version of which has been fixed the performance issue on `initialize()` method. Click [here](http://stackoverflow.com/questions/20916478/performance-issue-with-volleys-diskbasedcache) if you want to know the issue in detail.

The issue has not been resolved in official Volley yet. so you need to use this version instead of original `DiskBasedCache`.

This version is originally located in a [AOSP review](https://android-review.googlesource.com/#/c/63630/) and the author of it is Anders Aagaard.

### Android Universal Image Loader disk caches
AUIL disk caches wrap `disc caches` of [Android Universal Image Loader](https://github.com/nostra13/Android-Universal-Image-Loader) library, and are adapted for `Cache` that `RequestQueue` uses.

You can use the cache as below,

		requestQueue = new RequestQueue(new UniversalUnlimitedDiscCache(cacheDir)
										/* Disc cache's instance here */, 
										new BasicNetwork(new HurlStack()));

#### [UniversalFileCountLimitedDiscCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/disc/impl/UniversalFileCountLimitedDiscCache.java)
- Disc cache limited by file count. If file count in cache directory exceeds specified limit then file with the most oldest last usage date will be deleted.
- An adapter class of [FileCountLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/FileCountLimitedDiscCache.java)

#### [UniversalLimitedAgeDiscCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/disc/impl/UniversalLimitedAgeDiscCache.java)
- Cache which deletes files which were loaded more than defined time. Cache size is unlimited.
- An adapter class of [LimitedAgeDiscCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/LimitedAgeDiscCache.java)

#### [UniversalTotalSizeLimitedDiscCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/disc/impl/UniversalTotalSizeLimitedDiscCache.java)
- Cache limited by total cache size. If cache size exceeds specified limit then file with the most oldest last usage date will be deleted.
- An adapter class of [TotalSizeLimitedDiscCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/TotalSizeLimitedDiscCache.java)

#### [UniversalUnlimitedDiscCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/disc/impl/UniversalUnlimitedDiscCache.java)
- The fastest cache, doesn't limit cache size.
- An adapter class of [UnlimitedDiscCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/UnlimitedDiscCache.java)

## Memory caches

### Android Universal Image Loader image caches
AUIL image caches wrap `memory caches` of [Android Universal Image Loader](https://github.com/nostra13/Android-Universal-Image-Loader) library which store bitmaps, and are adapted for `ImageCache` that `ImageLoader` uses.

You can use the cache as below,

		imageLoader = new ImageLoader(requestQueue, new UniversalLruMemoryCache(cacheSize)
													/* ImageCache's instance here */);

#### [UniversalFifoLimitedMemoryCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalFifoLimitedMemoryCache.java)
- Size of all stored bitmaps will not to exceed size limit. When cache reaches limit size then cache clearing is processed by FIFO principle.
- An adapter class of [FIFOLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/FIFOLimitedMemoryCache.java)

#### [UniversalFuzzyKeyMemoryCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalFuzzyKeyMemoryCache.java)
- Some different keys are considered as equals using `java.util.Comparator`, And when you try to put some value into cache by key so entries with "equals" keys will be removed from cache before. 
(_NOTE : Normally you don't need to use this class._)
- An adapter class of [FuzzyKeyMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/FuzzyKeyMemoryCache.java)

#### [UniversalLargestLimitedMemoryCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalLargestLimitedMemoryCache.java)
- The largest bitmap is deleted when cache size limit is exceeded.
- An adapter class of [LargestLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LargestLimitedMemoryCache.java)

#### [UniversalLimitedAgeMemoryCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalLimitedAgeMemoryCache.java)
- Cached object is deleted when its age exceeds defined value.
- An adapter class of [LimitedAgeMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LimitedAgeMemoryCache.java)

#### [UniversalLruLimitedMemoryCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalLruLimitedMemoryCache.java)
- Least recently used bitmap is deleted when cache size limit is exceeded.
- An adapter class of [LRULimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LRULimitedMemoryCache.java)

#### [UniversalLruMemoryCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalLruMemoryCache.java)
- Least recently used bitmap is deleted when cache size limit is exceeded.
- An adapter class of [LruMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LruMemoryCache.java)

#### [UniversalUsingFreqLimitedMemoryCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalUsingFreqLimitedMemoryCache.java)
- Least frequently used bitmap is deleted when cache size limit is exceeded.
- An adapter class of [UsingFreqLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/UsingFreqLimitedMemoryCache.java)

#### [UniversalWeakMemoryCache](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalWeakMemoryCache.java)
- Cache using _only weak_ references
- An adapter class of [WeakMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/WeakMemoryCache.java)

_(NOTE : All of these descriptions of caches are extracted from README.md of Android Universal ImageLoader.))_

## Custom Views

### Zoomable NetworkImageViews
Zoomable NetworkImageView is a sub type of NetworkImageView and is available to zoom-in/out an image.

You can use the view as below, 

1. Choose one of types of ZoomableNetworkImageView and add it into layout xml.
``` xml
<com.navercorp.volleyextensions.view.TwoLevelDoubleTapZoomNetworkImageView
	    android:id="@+id/zoom_networkimageview"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    />
```

2. (In Activity,) Call setImageUrl of NetworkImageView.
``` java
zoomableImageView = (ZoomableNetworkImageView) findViewById(R.id.zoom_networkimageview);
zoomableImageView.setImageUrl(imageUrl, 
							  imageLoader /* Volley's image loader in here */);
```

There are two sub types of ZoomableNetworkImageView as you can see below. 

These types have each own UI interactions.

#### [TwoLevelDoubleTapZoomNetworkImageView](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/view/TwoLevelDoubleTapZoomNetworkImageView.java)
- Zoom-in/out by double tapping or pinch gesture.
- The maximum level that users can zoom to is 2.

#### [MultiLevelSingleTapZoomNetworkImageView](https://github.com/nhnopensource/volley-extensions/blob/master/volley-extensions/src/main/java/com/navercorp/volleyextensions/view/MultiLevelSingleTapZoomNetworkImageView.java)
- Zoom-in by single tapping or pinch-out gesture.
- Zoom-out by double tapping or pinch-in gesture.
- The maximum level that users can zoom to is 6.

# Install

### How to setup for maven or gradle users
#### 1. Add the dependency below,
##### Maven
``` xml
<dependency>
	<groupId>com.navercorp.volleyextensions</groupId>
	<artifactId>volley-extensions</artifactId>
	<version>1.0.0</version>
</dependency>
```

##### Gradle
```
dependencies {
	compile 'com.nhncorp.volleyextensions:volley-extensions:1.0.+'
}
```

#### 2. Add the appropriate dependency as per the classes you are using. 
For example, if you are using `JacksonRequest`, you need to add the dependency of [Jackson 1.x](http://jackson.codehaus.org/) library . If you are also using image cache, add the dependency of [Android Universal ImageLoader](https://github.com/nostra13/Android-Universal-Image-Loader) library as well. See the following settings below.

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

- Android Universal Image Loader (when using AUIL disk caches or image caches)
	
		<dependency>
			<groupId>com.nostra13.universalimageloader</groupId>
			<artifactId>universal-image-loader</artifactId>
			<version>1.8.6</version>
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

- Android Universal Image Loader (when using AUIL disk caches or image caches)

		dependencies {
			compile 'com.nostra13.universalimageloader:universal-image-loader:1.8.+'
		}

### How to add jar directly
1. Download the jar package of volley-extensions.
	- [volley-extensions-1.0.0.jar](http://github.com/nhnopensource/volley-extensions/wiki/downloads/1.0.0/volley-extensions-1.0.0.jar)
2. Import the jar package into your project.
3. Download and import Jackson, Simple XML, or Universal Image Loader as needed.

# Sample Application
Sample application is being provided as APK file (the source is in the repository):
 * [Download Here!](http://github.com/nhnopensource/volley-extensions/wiki/downloads/1.0.0/sample-demos-1.0.0.apk)

# Volley mirror dependency
This library depends on [Volley mirror project](https://github.com/mcxiaoke/android-volley). The mirror project keeps an up-to-date version of [Volley](https://android.googlesource.com/platform/frameworks/volley/) and automatically uploads to Maven Central. 

The library will continue to keep this dependency until Volley provides an official package in Maven Central.

# License

	Copyright (C) 2014 Naver Business Platform Corp.
 	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.