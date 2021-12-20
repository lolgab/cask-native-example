# Scala Native Cask

Example repository of [Cask](https://github.com/com-lihaoyi/cask) running on Scala Native.

## Getting started

You need to install NGINX Unit:

```bash
brew install nginx/unit/unit
```

Then in a separate shell run it:

```bash
unitd --no-daemon  --log /dev/stdout
```

When it's running you can create and deploy the Scala Native app with:

```bash
./mill app.native.deployApp
```

Then you can check it is working with:

```bash
$ curl http://localhost:8081
Hello World!⏎
```

You can also run the JVM version of cask with:

```bash
./mill app.jvm.runBackground
```

Then you can check it is working with:

```bash
$ curl http://localhost:8080 # notice the different port
Hello World!⏎
```

To build the native version with optimizations enabled:

```bash
SCALANATIVE_MODE="release-full" SCALANATIVE_LTO="thin" ./mill -i app.native.deployApp
```
