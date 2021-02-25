[![Spigot][spigot-shield]][spigot-url] [![Release][release-shield]][release-url] [![Forks][forks-shield]][forks-url] [![Stargazers][stars-shield]][stars-url] [![Issues][issues-shield]][issues-url] [![License][license-shield]][license-url] 

# My Souls

There are several other soul plugins for spigot, however, I couldn't find any that would please my taste, so I ended up creating this open source project so surprising that it will be the last one you will be looking for on the internet.

Here's why:

* Limited number of souls per player, making the **death** of a player something to think about and not spend it for nothing.
* Use of APIs, such as **PlaceholderAPI**, **bStats** and **SinkAPI** *(coming soon)*.
* Fully configurable via YAML, to make it **unique** for each server.
* **Language files**, so that all countries understand what the plugin says
* **Open source** project, which means that the code always has updates.
* Own **Event API** for implementations in other plugins.

Of course, there aren't perfect plugins, since each need tends to be different. So I will be adding more and more features to this project. You may also suggest improvements by forking this project and creating a pull request or opening an issue

## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project.
2. Create your Feature Branch.
3. Commit your Changes.
4. Push to the Branch.
5. Open a Pull Request.


**Edit:**<br>
*You can see and add things to TODO.md <br>
Because of my poor English, the names of my commits are in PT-BR, sorry. 😢*

## Plugin Info
 
<details>
 <summary><b>Placeholders</b></summary> 
 
 ```yaml
Below is the list of placeholders that the plugin offers the PlaceholderAPI extension to be used in any other plugin.:

 - %mysouls_soulscount%: returns the player's amount of souls.
 - %mysouls_soulscount_<player>%: returns the number of souls he has from a player.
 - %mysouls_playercount%: returns the number of unique players of their souls.
 - %mysouls_soulsratio%: returns the ratio from soulscount and playercount.
```
</details>

<details>
 <summary><b>Lang.yml</b></summary> 
 
  ```yaml
commands:
  unknown-argument: "&4Oops! &7Unknown argument."
  menu-command: "menu"

messages:
    page: "&7Page"
    credits: "&dCredits:"

    forward: "&aForward"
    backward: "&cBackward"

    soul-added: "&aYay! &7Soul added."
    souls-added: "&aYay! &7Souls added."
    soul-removed: "&eDone! &7Soul removed."
    coins-removed: "&eDone! &7Withdrawn coins."
    soul-64-limit: "&4Oops! &7You can only have 64 souls per player."
    dont-have-soul: "&4Oops! &7You don't have this player's soul."
    dont-have-souls: "&4Oops! &7Insufficient coins."

    death-message: "&4Oops! &7You died and one of your souls went to: &f{player}&7."
    death-message-fail: "&4Ah! &7You died but did not lose souls because you had none."
    kill-message: "&aYay! &7You killed the player: &f{player}&7 and won a soul."
    kill-message-fail: "&4Ah! &7The player you killed doesn't have enough souls to transfer you."

    soul-chat-message: "\n&eWrite in chat, the &fPlayer Name&e to remove or &f*&e to any soul.\n "
    coin-chat-message: "\n&eWrite in the chat the amount of the soul to be withdrawn in coins:\n "

    not-a-number: "&4Oops! &7{text} isn't a number."
    cannot-use: "&4Oops! &7You can't put the coin on the floor."
    inventory-full: "&4Oops! &7Your inventory is full."
    player-not-found: "&4Oops! &7Player not found."
    inventory-database-closed: "&4Oops! &7Closed inventory due to database shutdown."

menus:
  ranking:
    name: "&6Ranking"
    lore:
      - "&7Soul's ranking:"
      - ""
      - "&cComing soon..."

  your-wallet:
    name: "&6Your Wallet."
    lore:
      - "&eThis inventory represents"
      - "&eyour souls wallet."
      - ""
      - "&eSouls: &f{souls}"
      - "&ePlayers: &f{players}"
      - "&eAvarage: &f{average}"
      - "&eMore souls from: &f{more-souls}" 

  withdraw-souls:
    name: "&7Withdraw soul"
    lore:
      - "&fClick here to collect"
      - "&fa soul in a soul head"

  withdraw-coins:
    name: "&7Withdraw coins"
    lore:
      - "&fClick here to collect"
      - "&fa soul in a coin head"

  inventory-soul:
    name: "&e{player}"
    lore:
      -  "&7 * &fPlayer's soul &7* "

itens:
  soul:
    name: "&6{player}'s soul"
    lore:
      - "&fThis item is a soul that can be"
      - "&fcollected again, but it can also"
      - "&fbe used as a block and a "
      - "&fcommercial item."
      - ""
      - "&fWithdrawer: &e{wallet}"
      - ""
      - "&cBe careful! When placing the soul"
      - "&con the floor, its value will be lost."

  coin:
    name: "&6Soul Coin"
    lore:
      - "&eThis coin was a soul and now"
      - "&ecan be only used as tradable item"
  ```
</details>

<details>
 <summary><b>Config.yml</b></summary> 
 
```yaml
  souls:
  initial-souls: 2
  
itens:
  coin-head-url: "http://textures.minecraft.net/texture/77b9dfd281deaef2628ad5840d45bcda436d6626847587f3ac76498a51c861"
  
  soul-head-url: "http://textures.minecraft.net/texture/35b116dc769d6d5726f12a24f3f186f839427321e82f4138775a4c40367a49"
  
  trophy-head-url: "http://textures.minecraft.net/texture/e34a592a79397a8df3997c43091694fc2fb76c883a76cce89f0227e5c9f1dfe"
```
</details>

## License
Licensed under the **GNU General Public License v3.0**. See `License` for more information.


## Contact
See my contact information on my [GitHub Profile Page](https://github.com/ArthurFiorette).


## Status

[![bstats]][bstats-url]

<!-- Links -->
<!-- Shields -->
[forks-shield]: https://img.shields.io/github/forks/Hazork/MySouls?style=flat-square
[forks-url]: hhttps://github.com/Hazork/MySouls/network/members
[stars-shield]: https://img.shields.io/github/stars/Hazork/MySouls?style=flat-square
[stars-url]: https://github.com/Hazork/MySouls/stargazers
[issues-shield]: https://img.shields.io/github/issues/Hazork/MySouls?style=flat-square
[issues-url]: https://github.com/Hazork/MySouls/issues
[license-shield]: https://img.shields.io/github/license/Hazork/MySouls?style=flat-square
[license-url]: https://github.com/Hazork/MySouls/blob/main/LICENSE
[release-shield]: https://img.shields.io/github/v/release/hazork/mysouls?style=flat-square
[release-url]: https://github.com/Hazork/MySouls/releases
[spigot-shield]: http://img.shields.io/badge/Spigot-%20-yellow?style=flat-square
[spigot-url]: https://www.spigotmc.org/resources/my-souls-1-8-1-8-9.86749/

<!-- Urls -->
[bstats]: https://bstats.org/signatures/bukkit/MySouls.svg
[bstats-url]: https://bstats.org/plugin/bukkit/MySouls/9601
[twitter-url]: https://twitter.com/Hazork_
[steam-url]: https://steamcommunity.com/profiles/76561198850668121
