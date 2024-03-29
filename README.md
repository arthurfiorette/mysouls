<br />
<div align="center">
  <pre>
  <br />
  <h1>👻
My Souls</h1>
  <br />
  </pre>
  <br />
  <br />
  <code
    ><a href="https://github.com/ArthurFiorette/mysouls/network/members"
      ><img
        src="https://img.shields.io/github/forks/ArthurFiorette/mysouls?logo=github&label=Forks"
        target="_blank"
        alt="Forks" /></a
  ></code>
  <code
    ><a href="https://github.com/ArthurFiorette/mysouls/issues"
      ><img
        src="https://img.shields.io/github/issues/ArthurFiorette/mysouls?logo=github&label=Issues"
        target="_blank"
        alt="Issues" /></a
  ></code>
  <code
    ><a href="https://github.com/ArthurFiorette/mysouls/stargazers"
      ><img
        src="https://img.shields.io/github/stars/ArthurFiorette/mysouls?logo=github&label=Stars"
        target="_blank"
        alt="Stars" /></a
  ></code>
  <code
    ><a href="https://github.com/ArthurFiorette/mysouls/blob/main/LICENSE"
      ><img
        src="https://img.shields.io/github/license/ArthurFiorette/mysouls?logo=githu&label=License"
        target="_blank"
        alt="License" /></a
  ></code>
</div>

#

<br />
<br />

#### `My Souls` is a plugin to manage a complete new currency about you player lives!

<br />
<!-- TODO: Get some screenshots -->
<pre><img src="https://wallpapercave.com/wp/wp7672085.jpg" /></pre>

<br />
<br />

### Installing

**You can get the latest version jar in the latest
[Github](https://github.com/ArthurFiorette/mysouls/releases) release.**

Just download the generated jar and put it into the plugins folder,as we does not require
any dependency.

> If you want the latest development build, go to
> [actions](https://github.com/ArthurFiorette/mysouls/actions/workflows/maven.yml?query=branch%3Amain),
> click in the latest workflow run with a green mark. At the artifacts session, download
> the **build-result** zip and extract it, remember that it a development version, and may
> result in bugs and fails, that you can report
> [here](https://github.com/ArthurFiorette/mysouls/issues).

<br />

### Placeholder API

This plugin has it's own placeholders to you use with the rest of your server. Here are
the accurate list of them:

 <!-- I used java because it has a nice color scheme :) -->

```java
// The soul amount of the player
%mysouls_soulscount%
```

```java
// The soul amount of a specific player.
%mysouls_soulscount_<player>%
```

```java
// The amount of unique players souls this player has.
%mysouls_playercount%
```

```java
// The ratio between souls count and player count.
%mysouls_soulsratio%
```

<br />

### Configuration files

#### [Config.yml](/src/main/resources/config.yml)

The general configuration file, where you can change textures, numbers and etc.

#### [Lang.yml](/src/main/resources/lang.yml)

The translation file, where all the messages are included.

> There are some [templates](/resources/lang-templates/) in many languages, you can
> copy-it to your plugin.

<br />

### Common Problems

#### All colored text has a strange character on the front

![example](/docs/assets/cp-1.png 'Title')

To resolve it, make sure that the lang.yml and the server are using the same enconding.
All of this files are using UTF-8, and you should too.

To force the server to also use UTF8, add **`-Dfile.encoding=UTF-8`** when starting it.
Example:

```sh
# start.bat
java -Dfile.encoding=UTF-8 -jar spigot.jar
```

See more: [Spigot startup parameters](https://www.spigotmc.org/wiki/start-up-parameters/)

<br />

### License

Licensed under the **MIT**. See [`LICENSE`](LICENSE) for more informations.

<br />

### Contact

See my contact information on my [github profile](https://github.com/ArthurFiorette) or
open a new issue.

<br />
