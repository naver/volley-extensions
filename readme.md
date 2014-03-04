# 기능
## Jackson json 지원
### [JacksonRequest](code#/VolleyExtensions/src/main/java/com/nhncorp/volleyextensions/request/JacksonRequest.java)
- [Jackson 1.x](http://jackson.codehaus.org/)를 이용한 Json 파싱

		Request<Person> request = new JacksonRequest<Person>(url, Person.class, listener, errorListener);
		requestQueue.add(request);

### [Jackson2Request](code#/VolleyExtensions/src/main/java/com/nhncorp/volleyextensions/request/Jackson2Request.java)
- [Jackson 2.x](http://wiki.fasterxml.com/JacksonHome)를 이용한 Json 파싱

		Request<Person> request = new Jackson2Request<Person>(url, Person.class, listener, errorListener);
		requestQueue.add(request);

## [Simple XML](http://simple.sourceforge.net/) 지원
### [SimpleXmlRequest](code#/VolleyExtensions/src/main/java/com/nhncorp/volleyextensions/request/SimpleXmlRequest.java)
- [Simple XML](http://simple.sourceforge.net/)을 활용한 XML 파싱

		Request<Person> request = new SimpleXmlRequest<Person>(url, Person.class, listener, errorListener);
		requestQueue.add(request);
		
## [Android Universal ImageLoader](https://github.com/nostra13/Android-Universal-Image-Loader) 지원

### DiskCache
#### UniversalFileCountLimitedDiscCache
- 캐쉬 파일의 갯수를 제한하고 FIFO 규칙으로 삭제하는 캐쉬.
- [FileCountLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/FileCountLimitedDiscCache.java)의 Adaptor.

#### UniversalLimitedAgeDiscCache
- 지정된 시간동안만 생존하는 캐쉬.
- [LimitedAgeDiscCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/LimitedAgeDiscCache.java)의 Adaptor.

#### UniversalTotalSizeLimitedDiscCache
- 제한된 용량을 넘어서면 가장 오래전에 사용된 파일을 삭제하는 캐쉬.
- [TotalSizeLimitedDiscCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/TotalSizeLimitedDiscCache.java)의 Adaptor.

#### UniversalUnlimitedDiscCache
- 용량 제한이 없는 캐쉬. 
- [UnlimitedDiscCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/disc/impl/UnlimitedDiscCache.java)의 Adaptor.

### MemoryCache

#### UniversalFifoLimitedMemoryCache
- 제한된 용량을 넘어서면 FIFO방식으로 이미지를 삭제하는 캐쉬.
- [FIFOLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/FIFOLimitedMemoryCache.java)의 Adaptor.

#### UniversalFuzzyKeyMemoryCache
- 파일의 명명규칙으로 중복된 이미지를 인식하여 삭제하는 캐쉬.
- [FuzzyKeyMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/FuzzyKeyMemoryCache.java)의 Adaptor.

#### UniversalLargestLimitedMemoryCache
- 제한된 용량을 넘어서면 가장 큰 이미지를 삭제하는 캐쉬.
- [LargestLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LargestLimitedMemoryCache.java)의 Adaptor.

#### UniversalLimitedAgeMemoryCache
- 지정된 시간동안만 생존하는 캐쉬.
- [LimitedAgeMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LimitedAgeMemoryCache.java) 의 Adaptor.

#### UniversalLruLimitedMemoryCache
- 제한된 용량을 넘어서면 LRU로직으로 이미지를 삭제하는 캐쉬
- [LRULimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LRULimitedMemoryCache.java) 의 Adaptor.

#### UniversalLruMemoryCache
- 제한된 갯수를 넘어서면 LRU방식으로 이미지를 삭제하는 캐쉬.
- [LruMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/LruMemoryCache.java)의 Adaptor.

#### UniversalUsingFreqLimitedMemoryCache
- 제한된 용량을 넘어서면 가장 자주 사용되지 않은 이미지를 삭제하는 캐쉬.
- [UsingFreqLimitedMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/UsingFreqLimitedMemoryCache.java)의 Adaptor

#### UniversalWeakMemoryCache
- WeakReference만 이용하는 캐쉬.
- [WeakMemoryCache](https://github.com/nostra13/Android-Universal-Image-Loader/blob/master/library/src/com/nostra13/universalimageloader/cache/memory/impl/WeakMemoryCache.java)의 Adaptor.

# 사용방법
### Maven 혹은 Gradle을 통한 의존성 관리
1. volley 라이브러리를 Depdendency에 추가한다.
	- <http://devcafe.nhncorp.com//mappscafe/intro/entry/Volley> 참고 
	- 아직 Volley의 공식 버전이 부여되지 않았기 때문에 현재 Volley-extensions에서는 Volley에 대한 의존성을 'optional'로 두고, 최종앱에서 명시적으로 Depdendency를 관리하도록 하였다.
2. volley-extensions 라이브러리의 저장소를 추가한다.

		<repository>
				<id>multiline-release-repo</id>
				<url>http://hive.nhncorp.com/weblab/volley-extensions/rawcode/Download/</url>
				<snapshots>
					<enabled>false</enabled>
				</snapshots>
		</repository>
		
3. volley-extensions의 외존성을 추가한다.

		<dependency>
			<groupId>com.nhncorp.volleyextensions</groupId>
			<artifactId>volley-extensions</artifactId>
			<version>${volley-ext.version}</version>
		</dependency>
		
4. Jackson, SimpleXML, Android UniversalImageLoader 등 중 사용하려는 라이브러리의 Depdendency를 추가한다.  volley-extensions에서는 사용자가 이를 라이브러리를 선택적으로 사용한다고 가정을 하고, dependency를 optional로 설정했다. 따라서 사용하려는 라이브러리는 앱에서 명시적으로 추가해야 한다.

	- Jackson 1.x
	
			<dependency>
				<groupId>org.codehaus.jackson</groupId>
				<artifactId>jackson-mapper-asl</artifactId>
				<version>1.9.12</version>
			</dependency>
	- Jackson 2.x
	
			<dependency>
				<groupId>com.fasterxml.jackson.core</groupId>
				<artifactId>jackson-databind</artifactId>
				<version>2.2.1</version>
			</dependency>
	- Simple XML
	
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
	- Android Universal Image Loader
	
			<dependency>
					<groupId>com.nostra13.universalimageloader</groupId>
					<artifactId>universal-image-loader</artifactId>
					<version>1.8.5</version>
			</dependency>


### 직접 jar 추가
1. Volley의 jar를 직접 생성하거나 다운로드하고 프로젝트에 추가한다.
	- <http://devcafe.nhncorp.com//mappscafe/intro/entry/Volley> 참고 
2. Volley-extensions의 jar를 다운로드한다. 
	- [volley-extensions-0.7.13.jar](http://hive.nhncorp.com/weblab/volley-extensions/rawcode/Download/com/nhncorp/volleyextensions/volley-extensions/0.7.13/volley-extensions-0.7.13.jar)
3. 프로젝트에 추가한다.
4. Jackson, Simple, UniversalImageLoader 등에서 사용하려는 라이브러리의 jar파일을 추가한다.

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