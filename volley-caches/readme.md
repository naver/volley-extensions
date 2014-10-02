# Volley extensions : Caches

This library provides assistance in developing Android applications using [Volley](https://android.googlesource.com/platform/frameworks/volley/).

It provides various useful caches.

# Features and usage
## Disk caches

### [Improved DiskBasedCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/disk/DiskBasedCache.java)
Improved `DiskBasedCache` is a version of which has been fixed the performance issue on `initialize()` method. Click [here](http://stackoverflow.com/questions/20916478/performance-issue-with-volleys-diskbasedcache) if you want to know the issue in detail.

The issue has not been resolved in official Volley yet. so you need to use this version instead of original `DiskBasedCache`.

This version is originally located in a [AOSP review](https://android-review.googlesource.com/#/c/63630/) and the author of it is Anders Aagaard.

### Android Universal Image Loader disk caches
AUIL disk caches wrap `disc caches` of [Android Universal Image Loader](https://github.com/nostra13/Android-Universal-Image-Loader) library, and are adapted for `Cache` that `RequestQueue` uses.

You can use the cache as below,

		requestQueue = new RequestQueue(new UniversalUnlimitedDiscCache(cacheDir)
										/* Disc cache's instance here */, 
										new BasicNetwork(new HurlStack()));

#### [UniversalFileCountLimitedDiscCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/disc/impl/UniversalFileCountLimitedDiscCache.java)
- Disc cache limited by file count. If file count in cache directory exceeds specified limit then file with the most oldest last usage date will be deleted.
- An adapter class of [FileCountLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/FileCountLimitedDiscCache.java)

#### [UniversalLimitedAgeDiscCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/disc/impl/UniversalLimitedAgeDiscCache.java)
- Cache which deletes files which were loaded more than defined time. Cache size is unlimited.
- An adapter class of [LimitedAgeDiscCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/LimitedAgeDiscCache.java)

#### [UniversalTotalSizeLimitedDiscCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/disc/impl/UniversalTotalSizeLimitedDiscCache.java)
- Cache limited by total cache size. If cache size exceeds specified limit then file with the most oldest last usage date will be deleted.
- An adapter class of [TotalSizeLimitedDiscCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/TotalSizeLimitedDiscCache.java)

#### [UniversalUnlimitedDiscCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/disc/impl/UniversalUnlimitedDiscCache.java)
- The fastest cache, doesn't limit cache size.
- An adapter class of [UnlimitedDiscCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/UnlimitedDiscCache.java)

## Memory caches

### Android Universal Image Loader image caches
AUIL image caches wrap `memory caches` of [Android Universal Image Loader](https://github.com/nostra13/Android-Universal-Image-Loader) library which store bitmaps, and are adapted for `ImageCache` that `ImageLoader` uses.

You can use the cache as below,

		imageLoader = new ImageLoader(requestQueue, new UniversalLruMemoryCache(cacheSize)
													/* ImageCache's instance here */);

#### [UniversalFifoLimitedMemoryCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalFifoLimitedMemoryCache.java)
- Size of all stored bitmaps will not to exceed size limit. When cache reaches limit size then cache clearing is processed by FIFO principle.
- An adapter class of [FIFOLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/FIFOLimitedMemoryCache.java)

#### [UniversalFuzzyKeyMemoryCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalFuzzyKeyMemoryCache.java)
- Some different keys are considered as equals using `java.util.Comparator`, And when you try to put some value into cache by key so entries with "equals" keys will be removed from cache before. 
(_NOTE : Normally you don't need to use this class._)
- An adapter class of [FuzzyKeyMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/FuzzyKeyMemoryCache.java)

#### [UniversalLargestLimitedMemoryCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalLargestLimitedMemoryCache.java)
- The largest bitmap is deleted when cache size limit is exceeded.
- An adapter class of [LargestLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LargestLimitedMemoryCache.java)

#### [UniversalLimitedAgeMemoryCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalLimitedAgeMemoryCache.java)
- Cached object is deleted when its age exceeds defined value.
- An adapter class of [LimitedAgeMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LimitedAgeMemoryCache.java)

#### [UniversalLruLimitedMemoryCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalLruLimitedMemoryCache.java)
- Least recently used bitmap is deleted when cache size limit is exceeded.
- An adapter class of [LRULimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LRULimitedMemoryCache.java)

#### [UniversalLruMemoryCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalLruMemoryCache.java)
- Least recently used bitmap is deleted when cache size limit is exceeded.
- An adapter class of [LruMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LruMemoryCache.java)

#### [UniversalUsingFreqLimitedMemoryCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalUsingFreqLimitedMemoryCache.java)
- Least frequently used bitmap is deleted when cache size limit is exceeded.
- An adapter class of [UsingFreqLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/UsingFreqLimitedMemoryCache.java)

#### [UniversalWeakMemoryCache](https://github.com/naver/volley-extensions/blob/master/volley-caches/src/main/java/com/navercorp/volleyextensions/cache/universalimageloader/memory/impl/UniversalWeakMemoryCache.java)
- Cache using _only weak_ references
- An adapter class of [WeakMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/WeakMemoryCache.java)

_(NOTE : All of these descriptions of caches are extracted from README.md of Android Universal ImageLoader.))_


# Install

### How to setup for maven or gradle users
#### 1. Add the dependency below,
##### Maven
``` xml
<dependency>
	<groupId>com.navercorp.volleyextensions</groupId>
	<artifactId>volley-caches</artifactId>
	<version>2.0.0</version>
</dependency>
```

##### Gradle
```
dependencies {
	compile 'com.navercorp.volleyextensions:volley-caches:2.0.+'
}
```

#### 2. Add the appropriate dependency as per the classes you are using. 
For example, if you are also using image cache, add the dependency of [Android Universal ImageLoader](https://github.com/nostra13/Android-Universal-Image-Loader) library as well. See the following settings below.

_(NOTE : The dependencies below were set to be `optional` as default in pom.xml of the library. Because I assumed that each developer will use different parts of it.)_

##### Maven

- Android Universal Image Loader (when using AUIL disk caches or image caches)
	
		<dependency>
			<groupId>com.nostra13.universalimageloader</groupId>
			<artifactId>universal-image-loader</artifactId>
			<version>1.8.6</version>
		</dependency>

##### Gradle

- Android Universal Image Loader (when using AUIL disk caches or image caches)

		dependencies {
			compile 'com.nostra13.universalimageloader:universal-image-loader:1.8.+'
		}

### How to add jar directly
1. Download the jar package of volley-extensions.
	- [volley-caches-2.0.0.jar](http://github.com/naver/volley-extensions/wiki/downloads/2.0.0/volley-caches-2.0.0.jar)
2. Import the jar package into your project.
3. Download and import Universal Image Loader as needed.

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