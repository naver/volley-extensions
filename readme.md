# Introduction to Volley Extensions

This library provides assistance in developing Android applications using [Volley](https://android.googlesource.com/platform/frameworks/volley/).

It provides useful classes such as various requests, disk caches, memory caches, and custom views.

Volley Extensions has been divided into following pluggable sub-projects.

 - [Volleyer](https://github.com/naver/volley-extensions/tree/master/volleyer) : Volleyer provides a very simple and much improved interface for developers to work with while using Volley. Also includes branch new features previously not in Volley.
 - [Volley requests](https://github.com/naver/volley-extensions/tree/master/volley-requests) : Contains Request classes that parses json and xml using Jackson library and Simple xml library.
 - [Volley caches](https://github.com/naver/volley-extensions/tree/master/volley-caches) : Contains improved DiskBasedCache and capable AUIL Cache for use in Volley.
 - [Volley custom views](https://github.com/naver/volley-extensions/tree/master/volley-views) : Added user interaction capabilities to NetworkImageView.

Each sub-project includes guides and samples for installation/development.

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
