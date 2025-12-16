# McFormatter
## About the mod
McFormatter is a lightweight Minecraft chat formatter plugin for Fabric servers.
It aims to allow users to use markdown-style formatting in Minecraft chat messages.

## Current Features
- Bold text
  - Surround bolded text with two asterixes, \**like this\**
- Italicised text
  - Surround text to be italicised with a single asterix on each side, \*like this\*.
- Hyperlinks
  - Hyperlinks can created by surrounding the text of the link with square brackets, then surrounding the URL with parentheses, \[like this\]\(http://example.com/\)
- Basic links
  - If you want some text to be interpreted as a link, surround it with angle brackets
  - i.e. \<http://example.com/\>
- Escaping
  - Players can escape formatting characters by using a backslash (\\) before the character they would like to escape.

## Planned Features
- Emojis
  - In future, players will be able to use Unicode emojis in a similar way to how emojis are used in applications like Discord
  - i.e. \:smile\: will be able to be used to display a smiling emoji
- Underlining
  - Future updates will allow players to underline text by surrounding the text with two underscores on each side.
- Permissions
  - I realise that sending links, and masking links, can be a large risk to a server's security. This is why, in future, I plan to add ways to disable/enable types of formatting for different players. This will likely require some permissions library.
