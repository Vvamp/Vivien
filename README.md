<h1 align="center">Vivien</h1>
<p align="center"><i>A custom discord bot.</i></p>
<div align="center">
<a href="https://github.com/Vvamp/Vivien/releases"><img alt="GitHub release badge" src="https://img.shields.io/github/v/release/Vvamp/Vivien?include_prereleases"></a>
  <a href="https://github.com/Vvamp/Vivien/stargazers"><img src="https://img.shields.io/github/stars/Vvamp/Vivien" alt="Stars Badge"/></a>
<a href="https://github.com/Vvamp/Vivien/network/members"><img src="https://img.shields.io/github/forks/Vvamp/Vivien" alt="Forks Badge"/></a>
<a href="https://github.com/Vvamp/Vivien/pulls"><img src="https://img.shields.io/github/issues-pr/Vvamp/Vivien" alt="Pull Requests Badge"/></a>
<a href="https://github.com/Vvamp/Vivien/issues"><img src="https://img.shields.io/github/issues/Vvamp/Vivien" alt="Issues Badge"/></a>
<a href="https://github.com/Vvamp/Vivien/blob/master/LICENSE"><img alt="GitHub License Badge" src="https://img.shields.io/github/license/Vvamp/Vivien"></a>
<a href="https://github.com/Vvamp/Vivien/actions/workflows/gradle.yml"><img alt="GitHub Workflow Status Badge" src="https://img.shields.io/github/workflow/status/Vvamp/Vivien/gradle"></a>
</div>
<br>
<p align="center"><i>Loved the project? Please visit my <a href="https://vincentvansetten.com">Website</a></i></p>
<br>

## About

Vivien is an all-in-one bot made for a community server.  
While not developed with the intention to have this bot be shared, I have decided to make it open-source and everyone is free to use it.
However, I will not make any guarantees about the support of this project, or the usability of it.
Anyone is, however, encouraged to contribute. Personally, I will still do my best do make this a great bot.

## Features

- Automatically changing status
- Reaction role messages

## Installation

### Download the jar file

1. Download the jar file
2. Rename the `secrets.env.example` to `.env`
3. Edit `.env` and replace "YOUR_BOT_TOKEN" with your discord bot's client code.
4. Run the jar file `java -jar Vivien.jar`

### Build it Yourself

1. Clone this repo
2. Install gradle, if you haven't already
3. Run `gradlew build` to build the project
4. To run, simply type `gradle run`, or make a jar yourself with `gradle fatJar`

## Commands

| Command | Usage          | Description                     |
| ------- | -------------- | ------------------------------- |
| About   | !about, /about | Shows information about the bot |

## License

This project is licensed under the [MIT License](https://github.com/Vvamp/Vivien/blob/master/LICENSE).

## Contribute

I am still quite new to the Discord4J framework and java in general.  
Any contributions are welcome!

## Disclaimer

This project is still in its very early phase and was not designed to be used on any other server than the one intended.  
You are allowed to use it and modify it, under the project's [license](#License), but no guarantees are made.

## Acknowledgements

- This project uses the [Discord4J Framework](https://discord4j.com/)
