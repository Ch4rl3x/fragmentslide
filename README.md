<table align="center"><tr><td align="center" width="9999">

# fragmentslide

Master: ![Android CI](https://github.com/Ch4rl3x/fragmentslide/workflows/Android%20CI/badge.svg?branch=master)
</td></tr></table>


[![CircleCI](https://circleci.com/github/Ch4rl3x/fragmentslide.svg?style=svg)](https://circleci.com/github/Ch4rl3x/fragmentslide)


Usage
-----

In order to use the library, there are 2 different options:

**1. Gradle dependency** (recommended)

  -  Add the following to your project level `build.gradle`:

```gradle
allprojects {
	repositories {
		maven { url "https://jitpack.io" }
	}
}
```
  -  Add this to your app `build.gradle`:

```gradle
dependencies {
	compile 'com.github.Ch4rl3x:fragmentslide:1.0.0'
}
```

**2. Maven**
- Add the following to your `pom.xml`:
 ```xml
<repository>
       	<id>jitpack.io</id>
	    <url>https://jitpack.io</url>
</repository>

<dependency>
	    <groupId>com.github.Ch4rl3x</groupId>
	    <artifactId>fragmentslide</artifactId>
	    <version>1.0.0</version>
</dependency>
```

License
=======
Copyright 2016 Ch4rl3x

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
