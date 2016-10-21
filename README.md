Usage
-----

In order to use the library, there are 4 different options:

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
