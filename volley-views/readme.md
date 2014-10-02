# Volley extensions : Custom views

This library provides assistance in developing Android applications using [Volley](https://android.googlesource.com/platform/frameworks/volley/).

It provides various useful custom views.

# Features and usage
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

#### [TwoLevelDoubleTapZoomNetworkImageView](https://github.com/naver/volley-extensions/blob/master/volley-views/src/main/java/com/navercorp/volleyextensions/view/TwoLevelDoubleTapZoomNetworkImageView.java)
- Zoom-in/out by double tapping or pinch gesture.
- The maximum level that users can zoom to is 2.

#### [MultiLevelSingleTapZoomNetworkImageView](https://github.com/naver/volley-extensions/blob/master/volley-views/src/main/java/com/navercorp/volleyextensions/view/MultiLevelSingleTapZoomNetworkImageView.java)
- Zoom-in by single tapping or pinch-out gesture.
- Zoom-out by double tapping or pinch-in gesture.
- The maximum level that users can zoom to is 6.

# Install

### How to setup for maven or gradle users
##### Maven
``` xml
<dependency>
	<groupId>com.navercorp.volleyextensions</groupId>
	<artifactId>volley-views</artifactId>
	<version>2.0.0</version>
</dependency>
```

##### Gradle
```
dependencies {
	compile 'com.navercorp.volleyextensions:volley-views:2.0.+'
}
```

### How to add jar directly
1. Download the jar package of volley-extensions.
	- [volley-views-2.0.0.jar](http://github.com/naver/volley-extensions/wiki/downloads/2.0.0/volley-views-2.0.0.jar)
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